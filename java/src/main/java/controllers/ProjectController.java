package controllers;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import models.DatabaseConnector;
import models.Project;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by annikamagnusson on 20/04/15.
 * Class to handle CRUD operations on projects to database.
 */
public class ProjectController {

    public void createProject(String projectInfo) throws Exception{
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        //Create an unique identifier
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        JSONObject jObj = null;
        try{
            jObj = (JSONObject) new JSONParser().parse(projectInfo);
        } catch (Exception e){
            System.out.println(e);
        }
        try{
            String field  = (String) jObj.get("field");
            String sharedLevel = (String) jObj.get("sharedLevel");
            String projectAbstract = (String) jObj.get("projectAbstract");
            String author = (String) jObj.get("author");
            String name = (String) jObj.get("name");
            String status = (String) jObj.get("status");

            JSONArray tagsArray = (JSONArray) jObj.get("tags");
            List<String> tags = new ArrayList<String>();
            for(int i=0; i < tagsArray.size(); i++){
                tags.add(tagsArray.get(i).toString());
            }

            JSONArray participantsArray = (JSONArray) jObj.get("participants");
            List<String> participants = new ArrayList<String>();
            for(int i=0; i < participantsArray.size(); i++){
                participants.add(participantsArray.get(i).toString());
            }

            JSONArray rolesArray = (JSONArray) jObj.get("projectRoles");
            List<String> projectRoles = new ArrayList<String>();
            for(int i=0; i < rolesArray.size(); i++){
                projectRoles.add(rolesArray.get(i).toString());
            }

            JSONArray followersArray = (JSONArray) jObj.get("followers");
            List<String> followers = new ArrayList<String>();
            for(int i=0; i < followersArray.size(); i++){
                followers.add(followersArray.get(i).toString());
            }

            Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);
            Project project = new Project(id, field, tags, sharedLevel, projectAbstract, participants,projectRoles, author, name, followers, status);
            mapper.save(project);
        } catch (Exception e){
            System.out.println(e);
        }
        db.close();
    }

    public JSONObject getProject(String projectId){
        if (projectId == null){
            System.out.println("No request parameter was provided");
            return null;
        }
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        Project project;

        Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);
        project = mapper.get(projectId);
        if (project == null) {
            System.out.println("Project wasn't found in database");
            db.close();
            return null;
        }

        String id = project.getId();
        String field = project.getField();
        List<String> tags = project.getTags();
        String sharedLevel = project.getSharedLevel();
        String projectAbstract = project.getProjectAbstract();
        List<String> participants = project.getParticipants();
        List<String> projectRoles = project.getProjectRoles();
        String author = project.getAuthor();
        String name = project.getName();
        List<String> followers = project.getFollowers();
        String status = project.getStatus();

        JSONObject projectJson = createProjectJson(id, field, tags, sharedLevel, projectAbstract, participants,
                projectRoles, author, name, followers, status);
        db.close();
        return projectJson;
    }

    public int deleteProject(String projectId){
        if (projectId == null){
            System.out.println("No request parameter was provided");
            return 400;
        }
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);
            Project toDelete = mapper.get(projectId);
            if (toDelete == null){
                System.out.println("Project wasn't found in database");
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

    public JSONObject createProjectJson(String id, String field, List<String> tags, String sharedLevel, String projectAbstract,
                                        List<String> participants, List<String> projectRoles, String author, String name,
                                        List<String> followers, String status){
        JSONObject projectJson = new JSONObject();
        projectJson.put("id", id);
        projectJson.put("field", field);
        projectJson.put("sharedLevel", sharedLevel);
        projectJson.put("projectAbstract", projectAbstract);
        projectJson.put("author", author);
        projectJson.put("name", name);
        projectJson.put("status", status);
        projectJson.put("tags", tags);
        projectJson.put("participants", participants);
        projectJson.put("projectRoles", projectRoles);
        projectJson.put("followers", followers);

        return projectJson;
    }
}
