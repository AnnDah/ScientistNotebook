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
import exceptions.UpdateException;
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
    private DatabaseConnector db;
    private Mapper<Data> mapper;

    public DataController(){
        db = new DatabaseConnector();
        db.connectDefault();

        mapper = new MappingManager(db.getSession()).mapper(Data.class);

    }

    @SuppressWarnings("unchecked")
    public UUID createData(String strData) throws CreationException{
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

    public JSONObject getDataJson(String strID)throws GetException{
        Data data = getData(strID);
        db.close();
        return createDataJson(data);
    }

    @SuppressWarnings("unchecked")
    public Data getData(String strId) throws GetException{
        try {
            UUID dataId = UUID.fromString(strId);
            return mapper.get(dataId);
        } catch (IllegalArgumentException e){
            throw new GetException("Data wasn't found in database");
        }

    }

    @SuppressWarnings("unchecked")
    public JSONObject createDataJson(Data whose){
        String content = whose.getContent();
        Long created =  whose.getCreated();
        String author = whose.getAuthor();
        int level = whose.getLevel();
        List<String> tags = whose.getTags();
        UUID id = whose.getId();
        String dataType = whose.getDataType();
        String project = whose.getProject();
        String name = whose.getName();
        String description = whose.getDescription();
        List<String> revisionHistory = whose.getRevisionHistory();

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

        JSONArray ja = new JSONArray();
        ja.add(dataJson);

        JSONObject mainObj = new JSONObject();
        mainObj.put("data", ja);

        return mainObj;
    }

    public void deleteData(String id) throws DeletionException{
        UUID dataId = UUID.fromString(id);
        try {
            Mapper<DataTags> tagMapper = new MappingManager(db.getSession()).mapper(DataTags.class);
            Data toDelete = mapper.get(dataId);
            DataTags tagDelete = tagMapper.get(dataId);

            mapper.delete(toDelete);
            tagMapper.delete(tagDelete);


        } catch (IllegalArgumentException e){
            throw new DeletionException("Data wasn't found in database");
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }

    }

    @SuppressWarnings("unchecked")
    public JSONObject searchDataTags(String tags) throws GetException{
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

        JSONArray ja = new JSONArray();

        try{
            ResultSet results = db.getSession().execute(statement);
            for(Row row : results) {
                if (row != null) {
                    JSONObject data = createSearchJson(
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

    public JSONObject updateData(String id, String update)throws UpdateException{
        String content;
        String author;
        String project;
        String name;
        String description;
        String dataType;
        int level;
        List<String> tags;

        try {
            JSONObject jObj = (JSONObject) new JSONParser().parse(update);

            content = (String) jObj.get("content");
            author = (String) jObj.get("author");
            project = (String) jObj.get("project");
            name = (String) jObj.get("name");
            description = (String) jObj.get("description");
            dataType = (String) jObj.get("dataType");
            String strLevel = (String) jObj.get("level");
            level = Integer.parseInt(strLevel);

            JSONArray tagsArray = (JSONArray) jObj.get("tags");
            tags = new ArrayList<String>();
            for (Object aTagsArray : tagsArray) {
                tags.add(aTagsArray.toString());
            }

        }  catch (org.json.simple.parser.ParseException e){
            throw new UpdateException("Invalid input data");
        } catch (IllegalArgumentException e){
            throw new UpdateException("Invalid input data");
        }

        try {
            Data data = getData(id);

            data.setContent(content);
            data.setAuthor(author);
            data.setProject(project);
            data.setName(name);
            data.setDescription(description);
            data.setDataType(dataType);
            data.setLevel(level);
            data.setTags(tags);

            mapper.save(data);

            try {
                UUID dataId = UUID.fromString(id);

                Mapper<DataTags> tagMapper = new MappingManager(db.getSession()).mapper(DataTags.class);
                DataTags dataTags = tagMapper.get(dataId);

                dataTags.setAuthor(author);
                dataTags.setDescription(description);
                dataTags.setName(name);
                dataTags.setTags(tags);

                tagMapper.save(dataTags);
            } catch (IllegalArgumentException e){
                throw new GetException("Data wasn't found in database");
            }


            return createDataJson(data);
        } catch (IllegalArgumentException e){
            throw new UpdateException("Data wasn't found in database");
        } catch (NullPointerException e){
            throw new UpdateException("Invalid input data");
        } catch (GetException e){
            throw new UpdateException("Invalid input data");
        }finally {
            db.close();
        }
    }
}
