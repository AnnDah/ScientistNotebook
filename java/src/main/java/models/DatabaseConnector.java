package models;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DefaultRetryPolicy;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class DatabaseConnector {
    private Cluster cluster;
    private Session session;
    private boolean connected;

    private final String dbaddress = "52.28.87.178";
    private final int dbport = 9042;


    public DatabaseConnector(){
        connected = false;
    }

    public void connectDefault(){
        connect(dbaddress, dbport);
    }

    public void connect(final String node, final int port){
        if(!connected){
            try{
                cluster = Cluster.builder().addContactPoint(node).withRetryPolicy(DefaultRetryPolicy.INSTANCE).withPort(port).build();
                final Metadata metadata = cluster.getMetadata();

                System.out.println(String.format("Connected to cluster: %s", metadata.getClusterName()));
                for (final Host host : metadata.getAllHosts())
                {
                    System.out.println(String.format("Host: %s", host.getAddress()));
                }
                session = cluster.connect();
                connected = true;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public Session getSession()
    {
        return this.session;
    }

    public void close()
    {
        if (connected){
            try{
                final Metadata metadata = cluster.getMetadata();
                System.out.println(String.format("Closing connection: %s", metadata.getClusterName()));
                cluster.close();
                connected = false;
            }catch (Exception e){
                System.out.println(e);
            }
        }

    }

    public void execute(String query){
        try{
            connectDefault();
            if (connected){
                final Metadata metadata = cluster.getMetadata();
                session.execute(query);
                System.out.println(String.format("Executed query: %s\nCluster: %s", query, metadata.getClusterName()));
                close();
            }
        } catch (Exception e){
            System.out.println(e);
        }

    }
}
