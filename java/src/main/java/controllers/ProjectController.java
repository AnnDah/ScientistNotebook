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
import java.util.*;

/**
 * Class to handle CRUD operations on projects to database.
 *
 * @author Annika Magnusson
 * @version 1.0 - 20/04/15
 */
public class ProjectController {
    private DatabaseConnector db;
    private Mapper<Project> mapper;

    public ProjectController() {
        db = new DatabaseConnector();
        db.connectDefault();

        mapper = new MappingManager(db.getSession()).mapper(Project.class);

    }

    public JSONObject create(String projectInfo) throws CreationException {
        //Create an unique identifier
        UUID id = UUID.randomUUID();
        Project project = null;

        try{
            // Parse the string into a JSONObject
            JSONObject jObj = (JSONObject) new JSONParser().parse(projectInfo);

            // Set values to the variables
            String field  = (String) jObj.get("field");
            String description = (String) jObj.get("description");
            String createdBy = (String) jObj.get("createdBy");
            String name = (String) jObj.get("name");
            String status = (String) jObj.get("status");
            String owner = (String) jObj.get("owner");
            String strPrivate = (String) jObj.get("isPrivate");
            boolean isPrivate = false;
            if(strPrivate.equals("true")) {
                isPrivate = true;
            }

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

            // Get the date of creation
            Long created = new Date().getTime();

            // Create the project
            project = new Project(id, field, tags, description, projectRoles, createdBy, name,
                    status, isPrivate, created, fundedBy, members, employers, funds, departments, owner);

            // Save the project to database
            mapper.save(project);

            // Create a mapper for DataTags
            Mapper<ProjectTags> tagMapper = new MappingManager(db.getSession()).mapper(ProjectTags.class);
            // Create a new DataTag
            ProjectTags tag = new ProjectTags(id, tags, name, status, description, isPrivate, created);
            // Save the DataTag
            tagMapper.save(tag);
        } catch (ParseException e) {
            throw new CreationException("Invalid input data");
        } finally {
            db.close();
        }

        return project.toJson();
    }

    /**
     * Gets a specific project and returns it as a JSONObject
     * @param id    id of the project to get
     * @return  a JSONObject of the project
     * @throws GetException
     */
    public JSONObject get(String id)throws GetException {
        Project project = getProject(id);
        db.close();
        return project.toJson();
    }

    /**
     * Gets a specific project from database
     * @param id    id of the project to get
     * @return  the project object
     * @throws GetException
     */
    @SuppressWarnings("unchecked")
    private Project getProject(String id) throws GetException {
        try {
            return mapper.get(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new GetException("Project wasn't found in database");
        }
    }

    /**
     * Removes a specific project from database
     * @param id    id of the project to remove
     * @throws DeletionException
     */
    public void delete(String id) throws DeletionException {
        try {
            // Create a mapper for DataTags
            Mapper<ProjectTags> tagMapper = new MappingManager(db.getSession()).mapper(ProjectTags.class);

            // Delete the project from projects and project_tags tables in database
            mapper.delete(mapper.get(UUID.fromString(id)));
            tagMapper.delete(tagMapper.get(UUID.fromString(id)));

        } catch (IllegalArgumentException e) {
            throw new DeletionException("Project wasn't found in database");
        } finally {
            db.close();
        }
    }

    /**
     * Search through project_tags table in database for projects matching the tags.
     * @param tags  string of the tags to be added in the search
     * @return  a JSONObject of the projects found in the search
     * @throws GetException
     */
    @SuppressWarnings("unchecked")
    public JSONObject searchProjectTags(String tags) throws GetException {
        // The beginning of the query
        String query = "SELECT * FROM scinote.project_tags WHERE";
        // Set variable to see number of tags in the search
        int numberOfTags = 1;

        try{
            // Splits the string of tags and goes through each tag and fills the query
            for (String s : tags.split(",")) {
                // If there are only one tag
                if (numberOfTags == 1) {
                    query += (" tags CONTAINS '" + s + "'");
                    numberOfTags++;
                } else {
                    query += (" AND tags CONTAINS '" + s + "'");
                }

                System.out.println(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Adds the end of the query
        query += " ALLOW FILTERING;";
        // Creates the statement to be sent
        Statement statement = new SimpleStatement(query);

        // Creates a json array
        JSONArray ja = new JSONArray();

        try{
            ResultSet results = db.getSession().execute(statement);
            for(Row row : results) {
                // Check if there were a result
                if (row != null) {
                    Mapper<ProjectTags> tagMapper = new MappingManager(db.getSession()).mapper(ProjectTags.class);
                    ProjectTags projectTags = tagMapper.get(row.getUUID("id"));

                    // Fills the json array
                    ja.add(projectTags.toJson());
                }
            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e) {
            throw new GetException("Invalid input data");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        // Adds the json array to a json object
        JSONObject mainObj = new JSONObject();
        mainObj.put("projects", ja);

        return mainObj;

    }

    /**
     * Adds a follower to the projects list of followers
     * @param projectId id of the project
     * @param userId    id of the follower to be added to the list
     * @throws UpdateException
     */
    public void addFollower(String projectId, String userId) throws UpdateException {
        try {
            // Get project to add a follower to
            Project project = mapper.get(UUID.fromString(projectId));

            // Check if the project is private, if so no followers are allowed
            if(project.getIsPrivate()) {
                throw new UpdateException("Project can't be followed");
            }
            // Get list of followers from project
            List<String> followers = project.getFollowers();
            // Add the user id to the list
            followers.add(userId);
            // Save the updated list in project
            project.setFollowers(followers);
            // Save the project
            mapper.save(project);
        } catch (IllegalArgumentException e) {
            throw new UpdateException("Invalid input data");
        } finally {
            db.close();
        }
    }

    /**
     * Updates a specific project
     * @param id        id of the project to update
     * @param update    the update information
     * @return          a JSONObject of the updated project
     * @throws UpdateException
     */
    public JSONObject update(String id, String update) throws UpdateException {
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
            // Parse the update information into a JSONObject
            JSONObject jObj = (JSONObject) new JSONParser().parse(update);

            // Set values to the variables
            field = (String) jObj.get("field");
            description = (String) jObj.get("description");
            status = (String) jObj.get("status");
            owner = (String) jObj.get("owner");
            createdBy = (String) jObj.get("createdBy");
            name = (String) jObj.get("name");
            String strPrivate = (String) jObj.get("isPrivate");
            isPrivate = "true".equals(strPrivate);

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

        }  catch (org.json.simple.parser.ParseException e) {
            throw new UpdateException("Invalid input data");
        } catch (IllegalArgumentException e) {
            throw new UpdateException("Invalid input data");
        }

        try {
            // Get the project to be updated
            Project project = getProject(id);

            // Set new values to project fields
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

            // Save the project in database
            mapper.save(project);

            try {
                // Create a mapper for DataTags
                Mapper<ProjectTags> tagMapper = new MappingManager(db.getSession()).mapper(ProjectTags.class);
                // Get ta data tag to update
                ProjectTags projectTags = tagMapper.get(UUID.fromString(id));

                // Set new values to data tag fields
                projectTags.setName(name);
                projectTags.setTags(tags);
                projectTags.setStatus(status);
                projectTags.setDescription(description);
                projectTags.setIsPrivate(isPrivate);

                // Save the data tag
                tagMapper.save(projectTags);
            } catch (IllegalArgumentException e) {
                throw new GetException("Project wasn't found in database");
            }

            // Return a JSONObject of the updated project
            return project.toJson();
        } catch (IllegalArgumentException e) {
            throw new UpdateException("Project wasn't found in database");
        } catch (NullPointerException e) {
            throw new UpdateException("Invalid input data");
        } catch (GetException e) {
            throw new UpdateException("Invalid input data");
        }finally {
            db.close();
        }
    }

}
