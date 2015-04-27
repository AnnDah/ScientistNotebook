package models;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.json.simple.JSONObject;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class DatabaseConnector {
    private Cluster cluster;
    private Session session;

    private final String dbaddress = "52.17.214.10";
    private final String keyspace = "scinote";
    private final int dbport = 9160;

    public JSONObject GetData(String query){
        cluster = Cluster.builder().addContactPoint(dbaddress).withPort(dbport).build();
        session = cluster.connect(keyspace);

        session.execute(query);
        cluster.close();
        return null;
    }
}
