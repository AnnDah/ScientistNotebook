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
import models.DatabaseConnector;
import models.Project;
import models.ProjectTags;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by annikamagnusson on 20/04/15.
 * Class to handle CRUD operations on projects to database.
 */
public class ProjectController {
    private DatabaseConnector db;
    private Mapper<Project> mapper;

    public ProjectController(){
        db = new DatabaseConnector();
        db.connectDefault();

        mapper = new MappingManager(db.getSession()).mapper(Project.class);

    }

    public JSONObject createProject(String projectInfo) throws CreationException{
        //Create an unique identifier
        UUID id = UUID.randomUUID();
        Project project = null;

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

            project = new Project(id, field, tags, description, projectRoles, createdBy, name,
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

        return createProjectJson(project);
    }

    public JSONObject getProjectJson(String id)throws GetException{
        Project project = getProject(id);
        db.close();
        return createProjectJson(project);
    }
    @SuppressWarnings("unchecked")
    private Project getProject(String projectId) throws GetException {
        try {
            UUID projectUuid = UUID.fromString(projectId);
            return mapper.get(projectUuid);
        } catch (IllegalArgumentException e) {
            throw new GetException("Project wasn't found in database");
        }
    }

    public void deleteProject(String projectId) throws DeletionException{
        try {
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
    private JSONObject createProjectJson(Project whose){
        UUID id = whose.getId();
        String field = whose.getField();
        List<String> tags = whose.getTags();
        String description = whose.getDescription();
        List<String> projectRoles = whose.getProjectRoles();
        String createdBy = whose.getCreatedBy();
        String name = whose.getName();
        String status = whose.getStatus();
        boolean isPrivate = whose.getIsPrivate();
        Long created = whose.getCreated();
        List<String> fundedBy = whose.getFundedBy();
        List<String> members = whose.getMembers();
        List<String> employers = whose.getEmployers();
        List<String> funds = whose.getFunds();
        List<String> departments = whose.getDepartments();
        String owner = whose.getOwner();
        List<String> followers = whose.getFollowers();

        Date utcCreated= getUtcDate(created);

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
        projectJson.put("created", utcCreated);
        projectJson.put("fundedBy", fundedBy);
        projectJson.put("members", members);
        projectJson.put("employers", employers);
        projectJson.put("funds", funds);
        projectJson.put("departments", departments);
        projectJson.put("owner", owner);
        projectJson.put("followers", followers);

        JSONArray ja = new JSONArray();
        ja.add(projectJson);

        JSONObject mainObj = new JSONObject();
        mainObj.put("projects", ja);

        return mainObj;
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
    private JSONObject createSearchJson(UUID id, String name, String author, String description,
                                       Long created, boolean isPrivate){
        Date utcCreated = getUtcDate(created);

        JSONObject projectJson = new JSONObject();
        projectJson.put("id", id);
        projectJson.put("name", name);
        projectJson.put("author", author);
        projectJson.put("description", description);
        projectJson.put("created", utcCreated);
        projectJson.put("isPrivate", isPrivate);

        return projectJson;

    }

    public void addFollower(String projectId, String userId) throws UpdateException{
        try {
            UUID id = UUID.fromString(projectId);
            Project project = mapper.get(id);
            if(project.getIsPrivate() == true){
                System.out.println("Project is private and can't be followed");
                throw new UpdateException("Project can't be followed");
            }
            List<String> followers = project.getFollowers();
            followers.add(userId);
            project.setFollowers(followers);
            mapper.save(project);
        } catch (IllegalArgumentException e){
            throw new UpdateException("Invalid input data");
        } finally {
            db.close();
        }
    }

    public JSONObject updateProject(String id, String update) throws UpdateException{
        String field;
        String description;
        String status;
        String owner;
        List<String> tags;
        List<String> projectRoles;
        List<String> fundedBy;
        List<String> members;
        List<String> employers;
        List<String> funds;
        List<String> departments;
        List<String> followers;
        boolean isPrivate;
        String createdBy;
        String name;

        try {
            JSONObject jObj = (JSONObject) new JSONParser().parse(update);

            field = (String) jObj.get("field");
            description = (String) jObj.get("description");
            status = (String) jObj.get("status");
            owner = (String) jObj.get("owner");
            createdBy = (String) jObj.get("createdBy");
            name = (String) jObj.get("name");
            String strPrivate = (String) jObj.get("isPrivate");

            isPrivate = false;
            if("true".equals(strPrivate)) isPrivate = true;


            JSONArray tagsArray = (JSONArray) jObj.get("tags");
            tags = new ArrayList<String>();
            for (Object aTagsArray : tagsArray) {
                tags.add(aTagsArray.toString());
            }

            JSONArray rolesArray = (JSONArray) jObj.get("projectRoles");
            projectRoles = new ArrayList<String>();
            for (Object aRolesArray : rolesArray) {
                projectRoles.add(aRolesArray.toString());
            }

            JSONArray fundedArray = (JSONArray) jObj.get("fundedBy");
            fundedBy = new ArrayList<String>();
            for (Object aFundedArray : fundedArray) {
                fundedBy.add(aFundedArray.toString());
            }

            JSONArray membersArray = (JSONArray) jObj.get("members");
            members = new ArrayList<String>();
            for (Object aMembersArray : membersArray) {
                members.add(aMembersArray.toString());
            }

            JSONArray employersArray = (JSONArray) jObj.get("employers");
            employers = new ArrayList<String>();
            for (Object anEmployersArray : employersArray) {
                employers.add(anEmployersArray.toString());
            }

            JSONArray fundsArray = (JSONArray) jObj.get("funds");
            funds = new ArrayList<String>();
            for (Object aFundsArray : fundsArray) {
                funds.add(aFundsArray.toString());
            }

            JSONArray departmentsArray = (JSONArray) jObj.get("departments");
            departments = new ArrayList<String>();
            for (Object aDepartmentsArray : departmentsArray) {
                departments.add(aDepartmentsArray.toString());
            }

            JSONArray followersArray = (JSONArray) jObj.get("followers");
            followers = new ArrayList<String>();
            for (Object aFollowersArray : followersArray) {
                followers.add(aFollowersArray.toString());
            }

        }  catch (org.json.simple.parser.ParseException e){
            throw new UpdateException("Invalid input data");
        } catch (IllegalArgumentException e){
            throw new UpdateException("Invalid input data");
        }

        try {
            Project project = getProject(id);

            project.setField(field);
            project.setDescription(description);
            project.setStatus(status);
            project.setOwner(owner);
            project.setTags(tags);
            project.setProjectRoles(projectRoles);
            project.setFundedBy(fundedBy);
            project.setMembers(members);
            project.setEmployers(employers);
            project.setFunds(funds);
            project.setDepartments(departments);
            project.setFollowers(followers);
            project.setIsPrivate(isPrivate);
            project.setCreatedBy(createdBy);
            project.setName(name);

            mapper.save(project);

            try {
                UUID projectId = UUID.fromString(id);

                Mapper<ProjectTags> tagMapper = new MappingManager(db.getSession()).mapper(ProjectTags.class);
                ProjectTags projectTags = tagMapper.get(projectId);

                projectTags.setName(name);
                projectTags.setTags(tags);
                projectTags.setStatus(status);
                projectTags.setDescription(description);
                projectTags.setIsPrivate(isPrivate);

                tagMapper.save(projectTags);
            } catch (IllegalArgumentException e){
                throw new GetException("Project wasn't found in database");
            }


            return createProjectJson(project);
        } catch (IllegalArgumentException e){
            throw new UpdateException("Project wasn't found in database");
        } catch (NullPointerException e){
            throw new UpdateException("Invalid input data");
        } catch (GetException e){
            throw new UpdateException("Invalid input data");
        }finally {
            db.close();
        }
    }

    private Date getUtcDate(Long date){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new Date(date);
    }
}
