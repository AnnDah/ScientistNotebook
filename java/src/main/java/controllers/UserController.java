package controllers;
import Utility.PasswordUtility;
import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import exceptions.DeletionException;
import exceptions.GetException;
import exceptions.CreationException;
import models.DatabaseConnector;
import models.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by annikamagnusson on 20/04/15.
 *
 */
public class UserController {

    public UUID createUser(String strUser) throws CreationException {
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        UUID id = UUID.randomUUID();

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

            Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
            User user = new User(id, firstName, lastName, email, password, memberSince  , organization, department, role);
            mapper.save(user);
        }  catch (org.json.simple.parser.ParseException e){
            throw new CreationException("Invalid input data");
        } catch (java.security.NoSuchAlgorithmException e){
            throw new CreationException("Failed to hash password");
        } catch (java.io.IOException e){
            throw new CreationException("Failed to hash password");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return id;
    }

    public void deleteUser(String userId) throws DeletionException{

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
        try {
            UUID id = UUID.fromString(userId);
            User whose = mapper.get(id);

            mapper.delete(whose);
        } catch (IllegalArgumentException e){
            throw new DeletionException("User wasn't found in database");
        }

        db.close();
    }

    @SuppressWarnings("unchecked")
    public JSONObject createUserJson(UUID id, String firstName, String lastName, String email, String password, Long date,
                                     String organization, String department, String role, List<String> follows){
        // Parse date to UTC
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate= new Date(date);
        System.out.println(formatter.format(utcDate));

        JSONObject userJson = new JSONObject();
        userJson.put("id", id);
        userJson.put("firstName", firstName);
        userJson.put("lastName", lastName);
        userJson.put("email", email);
        userJson.put("password", password);
        userJson.put("memberSince", utcDate);
        userJson.put("organization", organization);
        userJson.put("department", department);
        userJson.put("role", role);
        userJson.put("follows", follows);

        /**
         Only needs to be implemented when we need to get multiple users
         JSONArray ja = new JSONArray();
         ja.add(jObj);
         JSONObject mainObj = new JSONObject();
         mainObj.put("users", ja);
         System.out.println(mainObj);
         */

        return userJson;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getUser(String userId) throws GetException{

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        User whose;

        Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
        try {
            UUID userUuid = UUID.fromString(userId);
            whose = mapper.get(userUuid);
        } catch (IllegalArgumentException e){
            throw new GetException("User wasn't found in database");
        }

        JSONObject user = createUserJson(
                whose.getId(),
                whose.getFirstName(),
                whose.getLastName(),
                whose.getEmail(),
                "OMITTED!",
                whose.getMemberSince(),
                whose.getOrganization(),
                whose.getDepartment(),
                whose.getRole(),
                whose.getFollows());

        JSONArray ja = new JSONArray();
        ja.add(user);
        JSONObject mainObj = new JSONObject();
        mainObj.put("users", ja);

        db.close();

        return mainObj;
    }

    public JSONObject getUserLogin(String inputEmail) throws GetException {
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        Statement statement = new SimpleStatement(String.format(
                "SELECT * FROM scinote.users WHERE email = '%s' ALLOW FILTERING;", inputEmail.trim()));

        JSONObject user = null;

        try{
            ResultSet results = db.getSession().execute(statement);
            Row row = results.one();
            if (row != null){
                user = createUserJson(
                        row.getUUID("id"),
                        row.getString("first_name"),
                        row.getString("last_name"),
                        row.getString("email"),
                        row.getString("password"),
                        row.getLong("member_since"),
                        row.getString("organization"),
                        row.getString("department"),
                        row.getString("role"),
                        row.getList("follows", String.class));
            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e){
            throw new GetException("User wasn't found in database");
        }

        db.close();

        return user;
    }
}
