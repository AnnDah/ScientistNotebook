package controllers;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import models.DatabaseConnector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

/**
 * Created by annikamagnusson on 05/05/15.
 */
public class DatabaseController {

    public void createKeyspace(String keyspaceInfo){
        String keyspace = "";
        try{
            JSONObject jObj = (JSONObject) new JSONParser().parse(keyspaceInfo);
            keyspace = (String) jObj.get("keyspace");
        } catch (Exception e){
            System.out.println(e);
        }

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            db.getSession().execute(new SimpleStatement("CREATE KEYSPACE " + keyspace + " WITH replication = {'class':'SimpleStrategy','replication_factor':1};"));
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }

    public void getKeyspaces() {
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            ResultSet res = db.getSession().execute(new SimpleStatement("SELECT * FROM system.schema_keyspaces;"));
            System.out.println(res.all()); //Send this list in JSON to api
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }

    public void createTable(String tableInfo){
        JSONObject jObj = null;
        try{
            jObj = (JSONObject) new JSONParser().parse(tableInfo);
        } catch (Exception e){
            System.out.println(e);
        }

        String keyspace = (String) jObj.get("keyspace");
        String tableName = (String) jObj.get("tableName");
        String columns = (String) jObj.get("columns");
        String primaryKey = (String) jObj.get("primaryKey");

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        Statement s = new SimpleStatement(
                "CREATE TABLE IF NOT EXISTS " + keyspace + "." + tableName + " (" + columns + ", PRIMARY KEY (" + primaryKey + "));");
        System.out.println(s);
        try {
            db.getSession().execute(s);
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }

    //No working cql statement
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
