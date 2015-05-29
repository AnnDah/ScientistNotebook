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


/**
 * Created by annikamagnusson on 20/04/15.
 *
 */
public class UserController {

    private Mapper<User> mapper;
    private DatabaseConnector db;

    public UserController() {
        db = new DatabaseConnector();
        db.connectDefault();
        mapper = new MappingManager(db.getSession()).mapper(User.class);
    }

    public JSONObject create(String strUser) throws CreationException {
        UUID id = UUID.randomUUID();
        User user = null;

        try {
            JSONObject jObj = (JSONObject) new JSONParser().parse(strUser);

            String firstName = (String) jObj.get("firstName");
            String lastName = (String) jObj.get("lastName");
            String email = (String) jObj.get("email");
            String password = PasswordUtility.generateHash((String) jObj.get("password"));
            Long memberSince = new Date().getTime();
            String organization = (String) jObj.get("organization");
            String department = (String) jObj.get("department");
            String role = (String) jObj.get("role");


            user = new User(id, firstName, lastName, email, password, memberSince , organization, department, role);
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

        if(user == null) {
            throw new CreationException("User couldn't be created");
        }
        return user.toJson(false);
    }

    public void delete(String userId) throws DeletionException {
        try {
            UUID id = UUID.fromString(userId);
            User whose = mapper.get(id);

            mapper.delete(whose);
        } catch (IllegalArgumentException e) {
            throw new DeletionException("User wasn't found in database");
        }

        db.close();
    }



    public JSONObject get(String id) throws GetException {
        User user = getUser(id);
        db.close();
        return user.toJson(false);
    }

    @SuppressWarnings("unchecked")
    private User getUser(String userId) throws GetException {
        try {
            UUID userUuid = UUID.fromString(userId);
            return mapper.get(userUuid);
        } catch (IllegalArgumentException e) {
            throw new GetException("User wasn't found in database");
        }

    }

    public JSONObject getUserLogin(String inputEmail) throws GetException {
        Statement statement = new SimpleStatement(String.format(
                "SELECT * FROM scinote.users WHERE email = '%s' ALLOW FILTERING;", inputEmail.trim()));

        try{
            ResultSet results = db.getSession().execute(statement);
            Row row = results.one();
            if (row != null) {
                UUID id = row.getUUID("id");
                User user = mapper.get(id);
                return user.toJson(true);

            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e) {
            throw new GetException("User wasn't found in database");
        } finally {
            db.close();
        }
        throw new GetException("User wasn't found in database");
    }

    public JSONObject update(String id, String update)throws UpdateException {
        String firstName;
        String lastName;
        String email;
        String password;
        String organization;
        String department;
        String role;
        List<String> follows;

        try {
            JSONObject jObj = (JSONObject) new JSONParser().parse(update);

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
            User user = getUser(id);

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setOrganization(organization);
            user.setDepartment(department);
            user.setRole(role);
            user.setFollows(follows);

            mapper.save(user);

            return user.toJson(false);
        } catch (IllegalArgumentException e) {
            throw new UpdateException("User wasn't found in database");
        } catch (NullPointerException e) {
            throw new UpdateException("Invalid input data");
        } catch (GetException e) {
            throw new UpdateException("Invalid input data");
        }finally {
            db.close();
        }

    }

    public void addFollows(String userId, String projectId)throws UpdateException {
        try {
            UUID id = UUID.fromString(userId);
            User user = mapper.get(id);
            List<String> follows = user.getFollows();
            follows.add(projectId);
            user.setFollows(follows);
            mapper.save(user);
        } catch (IllegalArgumentException e) {
            throw new UpdateException("Invalid input data");
        } finally {
            db.close();
        }

    }

}
