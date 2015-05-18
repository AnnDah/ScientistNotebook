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
import models.DatabaseConnector;
import models.Project;
import models.ProjectTags;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by annikamagnusson on 20/04/15.
 * Class to handle CRUD operations on projects to database.
 */
public class ProjectController {

    public UUID createProject(String projectInfo) throws CreationException{
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        //Create an unique identifier
        UUID id = UUID.randomUUID();

        try{
            JSONObject jObj = (JSONObject) new JSONParser().parse(projectInfo);

            String field  = (String) jObj.get("field");
            String description = (String) jObj.get("description");
            String createdBy = (String) jObj.get("createdBy");
            String name = (String) jObj.get("name");
            String status = (String) jObj.get("status");
            String owner = (String) jObj.get("owner");
            String strPrivate = (String) jObj.get("isPrivate");
            boolean isPrivate = false;
            if("true".equals(strPrivate)) isPrivate = true;

            Long created = new Date().getTime();

            JSONArray tagsArray = (JSONArray) jObj.get("tags");
            List<String> tags = new ArrayList<String>();
            for (Object aTagsArray : tagsArray) {
                tags.add(aTagsArray.toString());
            }

            JSONArray rolesArray = (JSONArray) jObj.get("projectRoles");
            List<String> projectRoles = new ArrayList<String>();
            for (Object aRolesArray : rolesArray) {
                projectRoles.add(aRolesArray.toString());
            }

            JSONArray fundedArray = (JSONArray) jObj.get("fundedBy");
            List<String> fundedBy = new ArrayList<String>();
            for (Object aFundedArray : fundedArray) {
                fundedBy.add(aFundedArray.toString());
            }

            JSONArray membersArray = (JSONArray) jObj.get("members");
            List<String> members = new ArrayList<String>();
            for (Object aMembersArray : membersArray) {
                members.add(aMembersArray.toString());
            }

            JSONArray employersArray = (JSONArray) jObj.get("employers");
            List<String> employers = new ArrayList<String>();
            for (Object anEmployersArray : employersArray) {
                employers.add(anEmployersArray.toString());
            }

            JSONArray fundsArray = (JSONArray) jObj.get("funds");
            List<String> funds = new ArrayList<String>();
            for (Object aFundsArray : fundsArray) {
                funds.add(aFundsArray.toString());
            }

            JSONArray departmentsArray = (JSONArray) jObj.get("departments");
            List<String> departments = new ArrayList<String>();
            for (Object aDepartmentsArray : departmentsArray) {
                departments.add(aDepartmentsArray.toString());
            }

            Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);
            Project project = new Project(id, field, tags, description, projectRoles, createdBy, name,
                    status, isPrivate, created, fundedBy, members, employers, funds, departments, owner);
            mapper.save(project);

            Mapper<ProjectTags> tagMapper = new MappingManager(db.getSession()).mapper(ProjectTags.class);
            ProjectTags tag = new ProjectTags(id, tags, name, status, description, isPrivate, created);
            tagMapper.save(tag);
        } catch (ParseException e){
            throw new CreationException("Invalid input data");
        } finally {
            db.close();
        }

        return id;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getProject(String projectId) throws GetException{
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        Project whose;

        Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);

        try {
            UUID projectUuid = UUID.fromString(projectId);
            whose = mapper.get(projectUuid);
        } catch (IllegalArgumentException e){
            throw new GetException("Project wasn't found in database");
        } finally {
            db.close();
        }

        JSONObject project = createProjectJson(
                whose.getId(),
                whose.getField(),
                whose.getTags(),
                whose.getDescription(),
                whose.getProjectRoles(),
                whose.getCreatedBy(),
                whose.getName(),
                whose.getStatus(),
                whose.getIsPrivate(),
                whose.getCreated(),
                whose.getFundedBy(),
                whose.getMembers(),
                whose.getEmployers(),
                whose.getFunds(),
                whose.getDepartments(),
                whose.getOwner(),
                whose.getFollowers());

        JSONArray ja = new JSONArray();
        ja.add(project);
        JSONObject mainObj = new JSONObject();
        mainObj.put("projects", ja);

        return mainObj;
    }

    public void deleteProject(String projectId) throws DeletionException{
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);
            Mapper<ProjectTags> tagMapper = new MappingManager(db.getSession()).mapper(ProjectTags.class);

            UUID id = UUID.fromString(projectId);

            Project toDelete = mapper.get(id);
            ProjectTags tagDelete = tagMapper.get(id);

            mapper.delete(toDelete);
            tagMapper.delete(tagDelete);

        } catch (IllegalArgumentException e){
            throw new DeletionException("Project wasn't found in database");
        } finally {
            db.close();
        }
    }

    @SuppressWarnings("unchecked")
    public JSONObject createProjectJson(UUID id, String field, List<String> tags, String description,
                                        List<String> projectRoles, String createdBy, String name, String status,
                                        boolean isPrivate, Long created, List<String> fundedBy, List<String> members,
                                        List<String> employers, List<String> funds, List<String> departments,
                                        String owner, List<String> followers){
        JSONObject projectJson = new JSONObject();
        projectJson.put("id", id);
        projectJson.put("field", field);
        projectJson.put("description", description);
        projectJson.put("createdBy", createdBy);
        projectJson.put("name", name);
        projectJson.put("status", status);
        projectJson.put("tags", tags);
        projectJson.put("projectRoles", projectRoles);
        projectJson.put("isPrivate", isPrivate);
        projectJson.put("created", created);
        projectJson.put("fundedBy", fundedBy);
        projectJson.put("members", members);
        projectJson.put("employers", employers);
        projectJson.put("funds", funds);
        projectJson.put("departments", departments);
        projectJson.put("owner", owner);
        projectJson.put("followers", followers);

        return projectJson;
    }

    @SuppressWarnings("unchecked")
    public JSONObject searchProjectTags(String tags) throws GetException{
        System.out.println(tags);

        String query = "SELECT * FROM scinote.project_tags WHERE";
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

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        try{
            ResultSet results = db.getSession().execute(statement);
            for(Row row : results) {
                if (row != null) {
                    JSONObject project = createSearchJson(
                            row.getUUID("id"),
                            row.getString("name"),
                            row.getString("status"),
                            row.getString("description"),
                            row.getLong("created"),
                            row.getBool("is_private"));
                    ja.add(project);
                }
            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e){
            throw new GetException("Invalid input data");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        JSONObject mainObj = new JSONObject();
        mainObj.put("projects", ja);

        return mainObj;

    }

    @SuppressWarnings("unchecked")
    public JSONObject createSearchJson(UUID id, String name, String author, String description,
                                       Long created, boolean isPrivate){
        JSONObject projectJson = new JSONObject();
        projectJson.put("id", id);
        projectJson.put("name", name);
        projectJson.put("author", author);
        projectJson.put("description", description);
        projectJson.put("created", created);
        projectJson.put("isPrivate", isPrivate);

        return projectJson;

    }

    public void addFollower(){

    }
}
