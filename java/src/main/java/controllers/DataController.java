package controllers;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import exceptions.CreationException;
import exceptions.DeletionException;
import exceptions.GetException;
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

    public UUID createData(String strData) throws CreationException{
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
            String strLevel = (String) jObj.get("level");
            String dataType = (String) jObj.get("dataType");
            String project = (String) jObj.get("project");
            String name = (String) jObj.get("name");
            String description = (String) jObj.get("description");

            JSONArray tagsArray = (JSONArray) jObj.get("tags");
            List<String> tags = new ArrayList<String>();
            for (Object aTagsArray : tagsArray) {
                tags.add(aTagsArray.toString());
            }

            int level = Integer.parseInt(strLevel);

            Mapper<Data> mapper = new MappingManager(db.getSession()).mapper(Data.class);
            Data data = new Data(content, created, author, level, tags, id, dataType, project, name, description);
            mapper.save(data);
        } catch (org.json.simple.parser.ParseException e){
            throw new CreationException("Invalid input data");
        }
        db.close();

        return id;
    }

    public JSONObject getData(String strId) throws GetException{

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        Data data;

        Mapper<Data> mapper = new MappingManager(db.getSession()).mapper(Data.class);
        try {
            UUID dataId = UUID.fromString(strId);
            data = mapper.get(dataId);
        } catch (IllegalArgumentException e){
            throw new GetException("Data wasn't found in database");
        }

        //content, created, author, level, tags, id, dataType, project, name, description, revision_history
        String author = data.getAuthor();
        String content = data.getContent();
        String created = data.getCreated();
        int level = data.getLevel();
        UUID id = data.getId();
        List<String> tags = data.getTags();
        String dataType = data.getDataType();
        String project = data.getProject();
        String name = data.getName();
        String description = data.getDescription();
        List<String> revisionHistory = data.getRevisionHistory();

        JSONObject dataJson = createDataJson(content, created, author, level, tags, id, dataType, project, name,
                description, revisionHistory);

        db.close();
        return dataJson;

    }

    @SuppressWarnings("unchecked")
    public JSONObject createDataJson(String content, String created, String author, int level, List<String> tags,
                                     UUID id, String dataType, String project, String name, String description,
                                     List<String> revisionHistory){
        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("content", content);
            dataJson.put("created", created);
            dataJson.put("author", author);
            dataJson.put("visibility", level);
            dataJson.put("tags", tags);
            dataJson.put("id", id);
            dataJson.put("dataType", dataType);
            dataJson.put("project", project);
            dataJson.put("name", name);
            dataJson.put("description", description);
            dataJson.put("revisionHistory", revisionHistory);
        } catch (Exception e){
            System.out.println(e);
        }
        return dataJson;
    }

    public void deleteData(String id) throws DeletionException{
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<Data> mapper = new MappingManager(db.getSession()).mapper(Data.class);
            Data toDelete = mapper.get(id);
            mapper.delete(toDelete);

        } catch (IllegalArgumentException e){
            throw new DeletionException("Data wasn't found in database");
        }
        db.close();
    }
}
