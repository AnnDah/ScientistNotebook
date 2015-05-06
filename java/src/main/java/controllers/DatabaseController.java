package controllers;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.SimpleStatement;
import models.DatabaseConnector;
import org.json.simple.JSONObject;

/**
 * Created by annikamagnusson on 05/05/15.
 */
public class DatabaseController {

    public void createKeyspace(){
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            db.getSession().execute(new SimpleStatement("CREATE KEYSPACE scinote WITH replication = {'class':'SimpleStrategy','replication_factor':1};"));
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }

    public void getKeyspaces() {
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            db.getSession().execute(new SimpleStatement("SELECT * FROM system.schema_keyspaces;"));
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }

    public void createTable(){
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            db.getSession().execute(new SimpleStatement("CREATE TABLE scinote.user (" +
                    "email text, " +
                    "firstName text, " +
                    "lastName text, " +
                    "password text," +
                    "PRIMARY KEY (email));"));
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }

    public void getTables(){
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            db.getSession().execute(new SimpleStatement("DESCRIBE TABLES;"));
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }
}
