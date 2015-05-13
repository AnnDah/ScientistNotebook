package controllers;
import Utility.PasswordUtility;
import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import exceptions.DeletionException;
import exceptions.GetException;
import exceptions.UserCreationException;
import models.DatabaseConnector;
import models.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by annikamagnusson on 20/04/15.
 *
 */
public class UserController {

    public UUID createUser(String strUser) throws UserCreationException {
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
            throw new UserCreationException("Invalid input data");
        } catch (java.security.NoSuchAlgorithmException e){
            throw new UserCreationException("Failed to hash password");
        } catch (java.io.IOException e){
            throw new UserCreationException("Failed to hash password");
        }


        db.close();
        return id;
    }

    public void deleteUser(String userId) throws DeletionException{

        UUID id = UUID.fromString(userId);

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
        try {
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

        UUID id = whose.getId();
        String firstName = whose.getFirstName();
        String lastName = whose.getLastName();
        String mail = whose.getEmail();
        Long memberSince = whose.getMemberSince();
        String organization = whose.getOrganization();
        String department = whose.getDepartment();
        String role = whose.getRole();
        List<String> follows = whose.getFollows();

        JSONObject user = createUserJson(id, firstName, lastName, mail, "OMITTED!", memberSince, organization, department, role, follows);

        db.close();

        return user;
    }

    public JSONObject getUserLogin(String inputEmail) throws GetException {
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        Statement statement = new SimpleStatement(String.format("SELECT * FROM scinote.users WHERE email = '%s' ALLOW FILTERING;", inputEmail.trim()));

        JSONObject user = null;

        try{
            ResultSet results = db.getSession().execute(statement);
            Row row = results.one();
            if (row != null){
                UUID id = row.getUUID("id");
                String firstName = row.getString("first_name");
                String lastName = row.getString("last_name");
                String mail = row.getString("email");
                String password = row.getString("password");
                Long memberSince = row.getLong("member_since");
                String organization = row.getString("organization");
                String department = row.getString("department");
                String role = row.getString("role");
                List<String> follows = row.getList("follows", String.class);

                System.out.println(String.format("User: %s %s is logging in...", firstName, lastName));
                user = createUserJson(id, firstName, lastName, mail, password, memberSince, organization, department, role, follows);
            }
        } catch (com.datastax.driver.core.exceptions.InvalidQueryException e){
            throw new GetException("User wasn't found in database");
        }

        db.close();

        return user;
    }
}
