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
    public JSONObject createUserJson(User whose){


        UUID id = whose.getId();
        String firstName = whose.getFirstName();
        String lastName = whose.getLastName();
        String email = whose.getEmail();
        String password = "OMITTED!";
        Long date = whose.getMemberSince();
        String organization = whose.getOrganization();
        String department = whose.getDepartment();
        String role = whose.getRole();
        List<String> follows = whose.getFollows();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate= new Date(date);

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

        JSONArray ja = new JSONArray();
        ja.add(userJson);
        JSONObject mainObj = new JSONObject();
        mainObj.put("users", ja);

        return mainObj;

    }

    public JSONObject getUserJson(String id) throws GetException{
        User user = getUser(id);
        return createUserJson(user);
    }

    @SuppressWarnings("unchecked")
    public User getUser(String userId) throws GetException{

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
        try {
            UUID userUuid = UUID.fromString(userId);
            return mapper.get(userUuid);
        } catch (IllegalArgumentException e){
            throw new GetException("User wasn't found in database");
        } finally {
            db.close();
        }

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
                user = createUserJsonTemp(
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

    public void updateUser(String id, String update)throws GetException{
        User user;
        try {
            user = getUser(id);
        } catch (GetException e){
            throw e;
        }
        user.setFirstName("sebastian");

    }

    public void addFollows(){

    }

    @SuppressWarnings("unchecked")
    public JSONObject createUserJsonTemp(UUID id, String firstName, String lastName, String email, String password, Long date,
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
}
