package controllers;

import com.datastax.driver.core.SimpleStatement;
import models.DatabaseConnector;

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
            db.getSession().execute(new SimpleStatement("SHOW KEYSPACES;"));
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }

    public void createTable(){
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            db.getSession().execute(new SimpleStatement("CREATE TABLE scinote.Users (" +
                    "block_id uuid, " +
                    "email text, " +
                    "firstName text, " +
                    "lastName text, " +
                    "password text," +
                    "PRIMARY KEY (block_id, email));"));
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }

    public void getTables(){
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            db.getSession().execute(new SimpleStatement("SHOW TABLES;"));
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }
}
