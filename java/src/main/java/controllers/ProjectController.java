package controllers;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import models.DatabaseConnector;
import models.Project;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by annikamagnusson on 20/04/15.
 * Class to handle CRUD operations on projects to database.
 */
public class ProjectController {

    public UUID createProject(String projectInfo) throws Exception{
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        //Create an unique identifier
        UUID id = null;

        JSONObject jObj = null;
        try{
            jObj = (JSONObject) new JSONParser().parse(projectInfo);

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

            id = UUID.randomUUID();

            JSONArray tagsArray = (JSONArray) jObj.get("tags");
            List<String> tags = new ArrayList<String>();
            for(int i=0; i < tagsArray.size(); i++){
                tags.add(tagsArray.get(i).toString());
            }

            JSONArray rolesArray = (JSONArray) jObj.get("projectRoles");
            List<String> projectRoles = new ArrayList<String>();
            for(int i=0; i < rolesArray.size(); i++){
                projectRoles.add(rolesArray.get(i).toString());
            }

            JSONArray fundedArray = (JSONArray) jObj.get("fundedBy");
            List<String> fundedBy = new ArrayList<String>();
            for(int i=0; i < fundedArray.size(); i++){
                fundedBy.add(fundedArray.get(i).toString());
            }

            JSONArray membersArray = (JSONArray) jObj.get("members");
            List<String> members = new ArrayList<String>();
            for(int i=0; i < membersArray.size(); i++){
                members.add(membersArray.get(i).toString());
            }

            JSONArray employersArray = (JSONArray) jObj.get("employers");
            List<String> employers = new ArrayList<String>();
            for(int i=0; i < employersArray.size(); i++){
                employers.add(employersArray.get(i).toString());
            }

            JSONArray fundsArray = (JSONArray) jObj.get("funds");
            List<String> funds = new ArrayList<String>();
            for(int i=0; i < fundsArray.size(); i++){
                funds.add(fundsArray.get(i).toString());
            }

            JSONArray departmentsArray = (JSONArray) jObj.get("departments");
            List<String> departments = new ArrayList<String>();
            for(int i=0; i < departmentsArray.size(); i++){
                departments.add(departmentsArray.get(i).toString());
            }

            Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);
            Project project = new Project(id, field, tags, projectAbstract, projectRoles, createdBy, name,
                    status, isPrivate, created, fundedBy, members, employers, funds, departments, owner);
            mapper.save(project);
        } catch (Exception e){
            throw e;
        }
        db.close();

        return id;
    }

    public JSONObject getProject(String projectId){
        if (projectId == null){
            System.out.println("No request parameter was provided");
            return null;
        }
        UUID projectUuid = UUID.fromString(projectId);
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        Project project;

        Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);
        project = mapper.get(projectUuid);
        if (project == null) {
            System.out.println("Project wasn't found in database");
            db.close();
            return null;
        }


        UUID id = project.getId();
        String field = project.getField();
        List<String> tags = project.getTags();
        String projectAbstract = project.getProjectAbstract();
        List<String> projectRoles = project.getProjectRoles();
        String createdBy = project.getCreatedBy();
        String name = project.getName();
        String status = project.getStatus();
        boolean isPrivate = project.getIsPrivate();
        Long created = project.getCreated();
        List<String> fundedBy = project.getFundedBy();
        List<String> members = project.getMembers();
        List<String> employers = project.getEmployers();
        List<String> funds = project.getFunds();
        List<String> departments = project.getDepartments();
        List<String> followers = project.getFollowers();
        String owner = project.getOwner();

        JSONObject projectJson = createProjectJson(id, field, tags, projectAbstract, projectRoles, createdBy,
                name, status, isPrivate, created, fundedBy, members, employers, funds, departments, owner, followers);
        db.close();
        return projectJson;
    }

    public int deleteProject(String projectId){
        if (projectId == null){
            System.out.println("No request parameter was provided");
            return 400;
        }
        UUID id = UUID.fromString(projectId);
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<Project> mapper = new MappingManager(db.getSession()).mapper(Project.class);
            Project toDelete = mapper.get(id);
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
