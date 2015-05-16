package controllers;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import exceptions.CreationException;
import exceptions.DeletionException;
import exceptions.GetException;
import models.DataTags;
import models.DatabaseConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

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
            Long created = new Date().getTime();
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
            Mapper<DataTags> tagMapper = new MappingManager(db.getSession()).mapper(DataTags.class);
            DataTags tag = new DataTags(tags, id, name, author, description, created);
            tagMapper.save(tag);
        } catch (org.json.simple.parser.ParseException e){
            throw new CreationException("Invalid input data");
        } finally {
            db.close();
        }

        return id;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getData(String strId) throws GetException{

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        Data whose;

        Mapper<Data> mapper = new MappingManager(db.getSession()).mapper(Data.class);
        try {
            UUID dataId = UUID.fromString(strId);
            whose = mapper.get(dataId);
        } catch (IllegalArgumentException e){
            throw new GetException("Data wasn't found in database");
        } finally {
            db.close();
        }

        JSONObject data = createDataJson(
                whose.getContent(),
                whose.getCreated(),
                whose.getAuthor(),
                whose.getLevel(),
                whose.getTags(),
                whose.getId(),
                whose.getDataType(),
                whose.getProject(),
                whose.getName(),
                whose.getDescription(),
                whose.getRevisionHistory());

        JSONArray ja = new JSONArray();
        ja.add(data);
        JSONObject mainObj = new JSONObject();
        mainObj.put("data", ja);

        return mainObj;

    }

    @SuppressWarnings("unchecked")
    public JSONObject createDataJson(String content, Long created, String author, int level, List<String> tags,
                                     UUID id, String dataType, String project, String name, String description,
                                     List<String> revisionHistory){
        JSONObject dataJson = new JSONObject();
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
        } finally {
            db.close();
        }

    }


    /**
    @SuppressWarnings("unchecked")
    public JSONObject searchDataTags1(String tag) throws GetException{
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        Mapper<DataTags> mapper = new MappingManager(db.getSession()).mapper(DataTags.class);
        JSONArray ja = new JSONArray();
        for (String s : tag.split(",")) {
            DataTags whose;
            try{
                whose = mapper.get(s);
            } catch (IllegalArgumentException e){
                throw new GetException("No data with those tags where found in database.");
            } finally {
                db.close();
            }

            JSONObject data = createSearchJson(
                    whose.getTags(),
                    whose.getId(),
                    whose.getName(),
                    whose.getAuthor(),
                    whose.getDescription(),
                    whose.getCreated());
            ja.add(data);
        }

        JSONObject mainObj = new JSONObject();
        mainObj.put("data", ja);

        return mainObj;

    }
     **/

    @SuppressWarnings("unchecked")
    public JSONObject searchDataTags2(String tags) throws GetException{
        System.out.println(tags);

        String query = "SELECT * FROM scinote.data_tags WHERE";
        int numberOfTags = 1;

        try{

            for (String s : tags.split(",")) {
                if (numberOfTags == 1) {
                    query += (" tags CONTAINS '" + s + "'");
                    numberOfTags++;
                } else {
                    query += (" AND tags CONTAINS '" + s + "'");
                }

                System.out.println(query);


            }
        } catch (Exception e){
            e.printStackTrace();
        }

        query += " ALLOW FILTERING;";
        System.out.println(query);
        Statement statement = new SimpleStatement(query);

        JSONObject data = null;
        JSONArray ja = new JSONArray();

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        try{
            ResultSet results = db.getSession().execute(statement);
            for(Row row : results) {
                if (row != null) {
                    data = createSearchJson(
                            row.getUUID("id"),
                            row.getString("name"),
                            row.getString("author"),
                            row.getString("description"),
                            row.getLong("created"));
                    ja.add(data);
                }
            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e){
            throw new GetException("Invalid input data");
        } finally {
            db.close();
        }
        JSONObject mainObj = new JSONObject();
        mainObj.put("data", ja);

        return mainObj;

    }

    @SuppressWarnings("unchecked")
    public JSONObject createSearchJson(UUID id, String name, String author, String description,
                                       Long created){
        JSONObject dataJson = new JSONObject();
        dataJson.put("id", id);
        dataJson.put("name", name);
        dataJson.put("author", author);
        dataJson.put("description", description);
        dataJson.put("created", created);

        return dataJson;

    }
}
