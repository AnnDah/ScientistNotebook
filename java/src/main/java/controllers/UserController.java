package controllers;

import utility.PasswordUtility;
import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import exceptions.DeletionException;
import exceptions.GetException;
import exceptions.CreationException;
import exceptions.UpdateException;
import models.DatabaseConnector;
import models.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Exchanger;


/**
 * Class to handle CRUD operations on users to database.
 *
 * @author Annika Magnusson
 * @version 1.0 - 20/04/15
 */
public class UserController {
    private Mapper<User> mapper;
    private DatabaseConnector db;

    public UserController() {
        db = new DatabaseConnector();
        db.connectDefault();
        mapper = new MappingManager(db.getSession()).mapper(User.class);
    }

    /**
     * Creates a new User and saves it to database.
     * @param strUser   User information to be saved in database.
     * @return JSONObject of the User that was created
     * @throws CreationException
     */
    public JSONObject create(String strUser) throws CreationException {
        // Create a random UUID
        UUID id = UUID.randomUUID();
        User user = null;

        try {
            // Parse the parameter string to a JSONObject
            JSONObject jObj = (JSONObject) new JSONParser().parse(strUser);

            // Set values to the variables
            String firstName = (String) jObj.get("firstName");
            String lastName = (String) jObj.get("lastName");
            String email = (String) jObj.get("email");
            String password = PasswordUtility.generateHash((String) jObj.get("password"));
            String organization = (String) jObj.get("organization");
            String department = (String) jObj.get("department");
            String role = (String) jObj.get("role");

            // Get the date of registration
            Long memberSince = new Date().getTime();

            // Create a new user
            user = new User(id, firstName, lastName, email, password, memberSince , organization, department, role);
            // Save user in database
            mapper.save(user);


        }  catch (org.json.simple.parser.ParseException e) {
            throw new CreationException("Invalid input data");
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new CreationException("Failed to hash password");
        } catch (java.io.IOException e) {
            throw new CreationException("Failed to hash password");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        // Check if user is null
        if(user == null) {
            throw new CreationException("User couldn't be created");
        }
        // return a JSONObject of the user
        return user.toJson(false);
    }

    /**
     * Deletes a user object from database.
     * @param userId    id of the user to be deleted.
     * @throws DeletionException
     */
    public void delete(String userId) throws DeletionException {
        try {
            mapper.delete(UUID.fromString(userId));
        } catch (IllegalArgumentException e) {
            throw new DeletionException("User wasn't found in database");
        }

        db.close();
    }

    /**
     * Get a specific user and return it as a JSONObject.
     * @param id    id of the user to get.
     * @return  a JSONObject of the user.
     * @throws GetException
     */
    public JSONObject get(String id) throws GetException {
        User user = getUser(id);
        db.close();
        return user.toJson(false);
    }

    /**
     *  Get a specific user from database.
     * @param userId    id of the user to get.
     * @return  the user object
     * @throws GetException
     */
    @SuppressWarnings("unchecked")
    private User getUser(String userId) throws GetException {
        try {
            return mapper.get(UUID.fromString(userId));
        } catch (IllegalArgumentException e) {
            throw new GetException("User wasn't found in database");
        }

    }

    /**
     * Get a specific user through user email
     * @param inputEmail    email of the user to get
     * @return  a JSONObject of the user
     * @throws GetException
     */
    public JSONObject getUserLogin(String inputEmail) throws GetException {
        // Create the statement to be sent to database
        Statement statement = new SimpleStatement(String.format(
                "SELECT * FROM scinote.users WHERE email = '%s' ALLOW FILTERING;", inputEmail.trim()));

        try {
            // Get the result from database
            ResultSet results = db.getSession().execute(statement);
            // Get the first result
            Row row = results.one();
            // Check if the result is empty
            if (row != null) {
                // Get the user from the result
                User user = mapper.get(row.getUUID("id"));
                return user.toJson(true);
            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e) {
            throw new GetException("User wasn't found in database");
        } finally {
            db.close();
        }
        throw new GetException("User wasn't found in database");
    }

    /**
     * Updates a specific user
     * @param id    id of the user to be updated
     * @param update    update information
     * @return  a JSONObject of the updated user
     * @throws UpdateException
     */
    public JSONObject update(String id, String update) throws UpdateException {
        String firstName;
        String lastName;
        String email;
        String password;
        String organization;
        String department;
        String role;
        List<String> follows;

        try {
            // Parse the string to a JSONObject
            JSONObject jObj = (JSONObject) new JSONParser().parse(update);

            // Set values to the variables
            firstName = (String) jObj.get("firstName");
            lastName = (String) jObj.get("lastName");
            email = (String) jObj.get("email");
            password = PasswordUtility.generateHash((String) jObj.get("password"));
            organization = (String) jObj.get("organization");
            department = (String) jObj.get("department");
            role = (String) jObj.get("role");

            JSONArray followArray = (JSONArray) jObj.get("follows");
            follows = new ArrayList<String>();
            for (Object aFollowsArray : followArray) {
                follows.add(aFollowsArray.toString());
            }

        }  catch (org.json.simple.parser.ParseException e) {
            throw new UpdateException("Invalid input data");
        } catch (IllegalArgumentException e) {
            throw new UpdateException("Invalid input data");
        } catch (NoSuchAlgorithmException e) {
            throw new UpdateException("Unable to hash password");
        } catch (IOException e) {
            throw new UpdateException("Invalid input data");
        }

        try {
            // Get the user from database
            User user = getUser(id);

            // Set new values to user fields
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setOrganization(organization);
            user.setDepartment(department);
            user.setRole(role);
            user.setFollows(follows);

            // Save user to database
            mapper.save(user);

            // Return the updated user as a JSONObject
            return user.toJson(false);
        } catch (IllegalArgumentException e) {
            throw new UpdateException("User wasn't found in database");
        } catch (NullPointerException e) {
            throw new UpdateException("Invalid input data");
        } catch (GetException e) {
            throw new UpdateException("Invalid input data");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return null;

    }

    /**
     * Adds a project to the list of projects the user follows.
     * @param userId    id of the user
     * @param projectId id of the project
     * @throws UpdateException
     */
    public void addFollows(String userId, String projectId)throws UpdateException {
        try {
            // Get the user
            User user = mapper.get(UUID.fromString(userId));
            // Get the list of followed projects
            List<String> follows = user.getFollows();
            // Add the project to the list
            follows.add(projectId);
            // Save the updated list in the user object
            user.setFollows(follows);
            // Save the user to database
            mapper.save(user);
        } catch (IllegalArgumentException e) {
            throw new UpdateException("Invalid input data");
        } finally {
            db.close();
        }

    }

}
