package models;

import org.json.simple.JSONObject;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class DatabaseConnector {
    //Cluster cluster;
    //Session session;

    private String dbaddress = "";
    private String keyspace = "";

    // Connect to the cluster and keyspace "demo"
    //cluster = Cluster.builder().addContactPoint(dbaddress).build();
    //session = cluster.connect(keyspace);

    public JSONObject GetData(String query){
        return null;
    }
}
