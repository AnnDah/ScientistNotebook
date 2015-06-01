package models;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DefaultRetryPolicy;

/**
 * Handles the connection to the database.
 *
 * @author Sebastian Nirfelt
 * @version 1.0 - 20/04/15
 *
 */
public class DatabaseConnector {
    private Cluster cluster;
    private Session session;
    private boolean connected;


    public DatabaseConnector(){
        connected = false;
    }

    /**
     * Sets the database address and port.
     */
    public void connectDefault(){
        String dbAddress = "52.28.87.178";
        int dbPort = 9042;
        connect(dbAddress, dbPort);
    }

    /**
     * MAkes the conection to the database.
     * @param node
     * @param port
     */
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
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the current session.
     * @return the current session.
     */
    public Session getSession()
    {
        return this.session;
    }

    /**
     * Closes the database connection.
     */
    public void close()
    {
        if (connected){
            try{
                final Metadata metadata = cluster.getMetadata();
                System.out.println(String.format("Closing connection: %s", metadata.getClusterName()));
                cluster.close();
                connected = false;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * 
     * @param query
     */
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
            e.printStackTrace();
        }

    }
}
