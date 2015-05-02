package models;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DefaultRetryPolicy;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class DatabaseConnector {
    private Cluster cluster;
    private Session session;

    private final String dbaddress = "52.17.214.10";
    private final int dbport = 9042;

    //"CREATE KEYSPACE scinote WITH replication = {'class':'SimpleStrategy','replication_factor':1}";

    public void connectDefault(){
        connect(dbaddress, dbport);
    }

    public void connect(final String node, final int port){

        cluster = Cluster.builder().addContactPoint(node).withRetryPolicy(DefaultRetryPolicy.INSTANCE).withPort(port).build();
        final Metadata metadata = cluster.getMetadata();

        System.out.println(String.format("Connected to cluster: %s\n", metadata.getClusterName()));
        for (final Host host : metadata.getAllHosts())
        {
            System.out.println(String.format("Host: %s\n", host.getAddress()));
        }
        session = cluster.connect();
    }

    public Session getSession()
    {
        return this.session;
    }

    public void close()
    {
        final Metadata metadata = cluster.getMetadata();
        System.out.println(String.format("Closing connection: %s\n", metadata.getClusterName()));
        cluster.close();
    }
}
