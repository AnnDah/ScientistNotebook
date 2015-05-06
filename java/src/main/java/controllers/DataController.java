package controllers;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import models.DatabaseConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import models.Data;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class DataController {

    public void createData(String strData){
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        //Create an unique identifier
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        String id = uuid.toString();
        System.out.println(id);

        JSONObject jObj = null;
        try{
            jObj = (JSONObject) new JSONParser().parse(strData);
        } catch (Exception e){
            System.out.println(e);
        }
        try{
            String content  = (String) jObj.get("content");
            String created = (String) jObj.get("created");
            String author = (String) jObj.get("author");
            String visibility = (String) jObj.get("visibilty");
            JSONArray jArray = (JSONArray) jObj.get("tags");
            List<String> tags = new ArrayList<String>();
            for(int i=0; i < jArray.size(); i++){
                tags.add(jArray.get(i).toString());
            }

            Mapper<Data> mapper = new MappingManager(db.getSession()).mapper(Data.class);
            Data data = new Data(created, content, author, visibility, tags, id);
            mapper.save(data);
        } catch (Exception e){
            System.out.println(e);
        }
        db.close();
    }

    public JSONObject getData(String dataId){
        if (dataId == null){
            System.out.println("No request parameter was provided");
            return null;
        }
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        Data data;

        Mapper<Data> mapper = new MappingManager(db.getSession()).mapper(Data.class);
        data = mapper.get(dataId);
        if (data == null) {
            System.out.println("Data wasn't found in database");
            db.close();
            return null;
        }

        String author = data.getAuthor();
        String content = data.getContent();
        String created = data.getCreated();
        String visibility = data.getVisibility();
        String id = data.getId();
        List<String> tags = data.getTags();

        JSONObject dataJson = createDataJson(content, created, author, visibility, tags, id);
        db.close();
        return dataJson;

    }

    public JSONObject createDataJson(String content, String created, String author, String visibility, List<String> tags, String id){
        JSONObject dataJson = new JSONObject();
        dataJson.put("content", content);
        dataJson.put("created", created);
        dataJson.put("author", author);
        dataJson.put("visibility", visibility);
        dataJson.put("tags", tags);
        dataJson.put("id", id);
        return dataJson;
    }

    public int deleteData(String id){
        if (id == null){
            System.out.println("No request parameter was provided");
            return 400;
        }
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<Data> mapper = new MappingManager(db.getSession()).mapper(Data.class);
            Data toDelete = mapper.get(id);
            if (toDelete == null){
                System.out.println("Data wasn't found in database");
                db.close();
                return 404;
            }
            mapper.delete(toDelete);

        } catch (Exception e){
            System.out.println(e);
        }
        db.close();
        return 200;
    }
}
