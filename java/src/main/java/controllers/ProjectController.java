package controllers;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import exceptions.CreationException;
import exceptions.DeletionException;
import exceptions.GetException;
import models.DatabaseConnector;
import models.Project;
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
            String projectAbstract = (String) jObj.get("projectAbstract");
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
            Project project = new Project(id, field, tags, projectAbstract, projectRoles, createdBy, name,
                    status, isPrivate, created, fundedBy, members, employers, funds, departments, owner);
            mapper.save(project);
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
                whose.getProjectAbstract(),
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
            UUID id = UUID.fromString(projectId);
            Project toDelete = mapper.get(id);
            mapper.delete(toDelete);

        } catch (IllegalArgumentException e){
            throw new DeletionException("Project wasn't found in database");
        } finally {
            db.close();
        }
    }

    @SuppressWarnings("unchecked")
    public JSONObject createProjectJson(UUID id, String field, List<String> tags, String projectAbstract,
                                        List<String> projectRoles, String createdBy, String name, String status,
                                        boolean isPrivate, Long created, List<String> fundedBy, List<String> members,
                                        List<String> employers, List<String> funds, List<String> departments,
                                        String owner, List<String> followers){
        JSONObject projectJson = new JSONObject();
        projectJson.put("id", id);
        projectJson.put("field", field);
        projectJson.put("projectAbstract", projectAbstract);
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
}
