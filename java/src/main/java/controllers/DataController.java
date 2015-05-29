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
import java.util.regex.PatternSyntaxException;

import models.Data;

/**
 * Class to handle CRUD operations on data to database.
 *
 * @author Annika Magnusson
 * @version 1.0, 20/04/15
 */
public class DataController {
    private DatabaseConnector db;
    private Mapper<Data> mapper;

    public DataController(){
        db = new DatabaseConnector();
        db.connectDefault();

        mapper = new MappingManager(db.getSession()).mapper(Data.class);

    }

    /**
     * Creates a new object of Data and saves it in the database
     * @param strData   String of data information in json format to be saved
     * @return a JSONObject of the saved Data
     * @throws CreationException
     */
    @SuppressWarnings("unchecked")
    public JSONObject create(String strData) throws CreationException {
        //Create an unique identifier
        UUID id = UUID.randomUUID();

        Data data = null;

        JSONObject jObj;

        try{
            // Parse the parameter string
            jObj = (JSONObject) new JSONParser().parse(strData);

            // Fill the variables
            String content  = (String) jObj.get("content");
            Long created = new Date().getTime();
            Long lastUpdate = new Date().getTime();
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

            // Creates a new object of Data
            data = new Data(
                    content, created, author, level, tags, id, dataType, project, name, description, lastUpdate);
            // Save the Data object in the database
            mapper.save(data);

            // Creates a mapper for DataTags and saves a Data object in data_tags table
            Mapper<DataTags> tagMapper = new MappingManager(db.getSession()).mapper(DataTags.class);
            DataTags tag = new DataTags(tags, id, name, author, description, created);
            tagMapper.save(tag);
        } catch (org.json.simple.parser.ParseException e){
            throw new CreationException("Invalid input data");
        } finally {
            db.close();
        }

        return data.toJson();
    }

    /**
     * Get a specific data object
     * @param id    id of the data to get
     * @return a JSONObject of the data
     * @throws GetException
     */
    public JSONObject get(String id)throws GetException {
        Data data = getData(id);
        db.close();
        return data.toJson();
    }

    /**
     * Get a specific data object from the database
     * @param id    id of the data to get
     * @return  a Data object
     * @throws GetException
     */
    @SuppressWarnings("unchecked")
    private Data getData(String id) throws GetException {
        try {
            return mapper.get(UUID.fromString(id));
        } catch (IllegalArgumentException e){
            throw new GetException("Data wasn't found in database");
        }

    }

    /**
     * Deletes a data object from the database.
     * @param id    id of the data to be deleted.
     * @throws DeletionException
     */
    public void delete(String id) throws DeletionException {
        try {
            // Create a mapper for DataTags
            Mapper<DataTags> tagMapper = new MappingManager(db.getSession()).mapper(DataTags.class);

            // Deletes the data from data_tags and data tables
            mapper.delete(mapper.get(UUID.fromString(id)));
            tagMapper.delete(tagMapper.get(UUID.fromString(id)));
        } catch (IllegalArgumentException e) {
            throw new DeletionException("Data wasn't found in database");
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    /**
     * Search data_tags table for data that matches the tags.
     * @param tags  tags to be included in the search
     * @return a JSONObject of the data found
     * @throws GetException
     */
    @SuppressWarnings("unchecked")
    public JSONObject searchDataTags(String tags) throws GetException {
        // The beginning of the query
        String query = "SELECT * FROM scinote.data_tags WHERE";
        // Set variable to see number of tags in the search
        int numberOfTags = 1;

        try {
            // Splits the string of tags and goes through each tag and fills the query
            for (String s : tags.split(",")) {
                // If there are only one tag
                if (numberOfTags == 1) {
                    query += (" tags CONTAINS '" + s + "'");
                    numberOfTags++;
                } else {
                    query += (" AND tags CONTAINS '" + s + "'");
                }
            }
        } catch (PatternSyntaxException e) {
            throw new GetException("Invalid input data");
        }

        // Adds the end of the query
        query += " ALLOW FILTERING;";
        // Creates the statement to be sent
        Statement statement = new SimpleStatement(query);

        // Creates a json array
        JSONArray ja = new JSONArray();

        try{
            ResultSet results = db.getSession().execute(statement);

            // Check if there were a result
            if(results.all().size() == 0){
                throw new GetException("No data found");
            }

            // Fills the json array
            for(Row row : results) {
                if (row != null) {
                    DataTags dataTags = new DataTags(row.getList("tags", String.class), row.getUUID("id"),
                            row.getString("name"), row.getString("author"), row.getString("description"),
                            row.getLong("created"));
                    ja.add(dataTags.toJson());
                }
            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e) {
            throw new GetException("Invalid input data");
        } finally {
            db.close();
        }
        // Adds the json array to a json object
        JSONObject mainObj = new JSONObject();
        mainObj.put("data", ja);

        return mainObj;

    }

    /**
     * Update a specific data object. Will update both data and data_tags tables.
     * @param dataId    id of the data to be updated.
     * @param update    string in json format of the update.
     * @return a JSONObject of the data that was updated.
     * @throws UpdateException
     */
    public JSONObject update(String dataId, String update)throws UpdateException {
        String content;
        String author;
        String project;
        String name;
        String description;
        String dataType;
        int level;
        List<String> tags;
        String user;
        Long lastUpdate = new Date().getTime();
        String revisionDescription;

        try {
            // Parse the json string into a JSONObject
            JSONObject jObj = (JSONObject) new JSONParser().parse(update);

            // Get the values from the JSONObject
            content = (String) jObj.get("content");
            author = (String) jObj.get("author");
            project = (String) jObj.get("project");
            name = (String) jObj.get("name");
            description = (String) jObj.get("description");
            dataType = (String) jObj.get("dataType");
            String strLevel = (String) jObj.get("level");
            level = Integer.parseInt(strLevel);
            user = (String) jObj.get("user");
            revisionDescription = (String) jObj.get("revisionDescription");

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
            // Get the data object to be updated
            Data data = getData(dataId);

            // Set the new values to the data object
            data.setContent(content);
            data.setAuthor(author);
            data.setProject(project);
            data.setName(name);
            data.setDescription(description);
            data.setDataType(dataType);
            data.setLevel(level);
            data.setTags(tags);
            data.setLastUpdate(lastUpdate);

            // Get the revision history of the data
            List<String> revision = data.getRevisionHistory();
            // Create a new revision
            String strRevision = String.format(
                    "{\"lastUpdate\":\"%s\", \"updateMadeBy\":\"%s\", \"revisionDescription\":\"%s\"}",
                    lastUpdate, user, revisionDescription);
            // Add the revision to the revision history
            revision.add(strRevision);
            // Save the updated revision history in the data object
            data.setRevisionHistory(revision);

            // Save the data object in the database
            mapper.save(data);

            try {
                UUID idForTags = UUID.fromString(dataId);

                // Create a mapper for DataTags
                Mapper<DataTags> tagMapper = new MappingManager(db.getSession()).mapper(DataTags.class);
                // Get the DataTags object from database
                DataTags dataTags = tagMapper.get(idForTags);

                // Update the DataTags object
                dataTags.setAuthor(author);
                dataTags.setDescription(description);
                dataTags.setName(name);
                dataTags.setTags(tags);

                // Save the DataTags object in the database
                tagMapper.save(dataTags);
            } catch (IllegalArgumentException e) {
                throw new GetException("Data wasn't found in database");
            }

            return data.toJson();
        } catch (IllegalArgumentException e) {
            throw new UpdateException("Data wasn't found in database");
        } catch (NullPointerException e) {
            throw new UpdateException("Invalid input data");
        } catch (GetException e) {
            throw new UpdateException("Invalid input data");
        }finally {
            db.close();
        }
    }

    /**
     * Get all data from a specific user.
     * The function creates a statement and sends it to getDataFromQuery to receive the data.
     * @param userId    the users id
     * @return A JSONObject of all data authored by the user.
     * @throws GetException
     */
    public JSONObject getDataUser(String userId) throws GetException {
        String query = String.format("SELECT * FROM scinote.data WHERE author = '%s' ALLOW FILTERING;", userId);
        return getDataFromQuery(query);
    }

    /**
     * Get all data from a specific project.
     * The function creates a statement and sends it to getDataFromQuery to receive the data.
     * @param projectId the projects id
     * @return aJSONObject of all data associated with the project.
     * @throws GetException
     */
    public JSONObject getDataProject(String projectId) throws GetException {
        String query = String.format("SELECT * FROM scinote.data WHERE project = '%s' ALLOW FILTERING;", projectId);
        return getDataFromQuery(query);
    }

    /**
     * Sends an query to the database and receives the response.
     * @param query     the query to be sent.
     * @return a JSONObject of the data.
     * @throws GetException
     */
    @SuppressWarnings("unchecked")
    private JSONObject getDataFromQuery(String query) throws GetException {
        // Create the statement
        Statement statement = new SimpleStatement(query);
        // Create a json array
        JSONArray ja = new JSONArray();

        try{
            ResultSet results = db.getSession().execute(statement);

            // Check if there were a result
            if(results.all().size() == 0){
                throw new GetException("No data found");
            }

            // Fill the array with the result
            for(Row row : results) {
                if (row != null) {
                    DataTags dataTags = new DataTags
                            (row.getList("tags", String.class), row.getUUID("id"), row.getString("name"),
                                    row.getString("author"), row.getString("description"), row.getLong("created"));
                    ja.add(dataTags.toJson());
                }
            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e) {
            throw new GetException("Invalid input data");
        } finally {
            db.close();
        }

        // Create a json object of the json array
        JSONObject mainObj = new JSONObject();
        mainObj.put("data", ja);

        return mainObj;

    }
}
