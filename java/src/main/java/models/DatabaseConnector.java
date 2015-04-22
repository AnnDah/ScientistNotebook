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

    private final String dbaddress = "81.170.233.123:9160";
    private final String keyspace = "scinotes";

    public JSONObject GetData(String query){
        cluster = Cluster.builder().addContactPoint(dbaddress).build();
        session = cluster.connect(keyspace);

        session.execute(query);
        cluster.close();
        return null;
    }
}
