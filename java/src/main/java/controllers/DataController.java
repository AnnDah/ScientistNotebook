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
 *
 */
public class DataController {

    public void createData(String strData){
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        //Create an unique identifier
        UUID id = UUID.randomUUID();

        JSONObject jObj;
        try{
            jObj = (JSONObject) new JSONParser().parse(strData);

            String content  = (String) jObj.get("content");
            String created = (String) jObj.get("created");
            String author = (String) jObj.get("author");
            String level = (String) jObj.get("visibilty");
            String dataType = (String) jObj.get("dataType");
            String project = (String) jObj.get("project");
            String name = (String) jObj.get("name");
            String description = (String) jObj.get("description");

            JSONArray revisionArray = (JSONArray) jObj.get("tags");
            List<String> revisionHistory = new ArrayList<String>();
            for(int i=0; i < revisionArray.size(); i++){
                revisionHistory.add(revisionArray.get(i).toString());
            }

            JSONArray tagsArray = (JSONArray) jObj.get("tags");
            List<String> tags = new ArrayList<String>();
            for(int i=0; i < tagsArray.size(); i++){
                tags.add(tagsArray.get(i).toString());
            }

            Mapper<Data> mapper = new MappingManager(db.getSession()).mapper(Data.class);
            Data data = new Data(content, created, author, level, tags, id, dataType, revisionHistory, project, name, description);
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
        String level = data.getLevel();
        UUID id = data.getId();
        List<String> tags = data.getTags();

        JSONObject dataJson = createDataJson(content, created, author, level, tags, id);
        db.close();
        return dataJson;

    }

    public JSONObject createDataJson(String content, String created, String author, String visibility, List<String> tags, UUID id){
        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("content", content);
            dataJson.put("created", created);
            dataJson.put("author", author);
            dataJson.put("visibility", visibility);
            dataJson.put("tags", tags);
            dataJson.put("id", id);
        } catch (Exception e){
            System.out.println(e);
        }
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
