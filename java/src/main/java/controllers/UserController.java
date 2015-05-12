package controllers;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
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

    public UUID createUser(String strUser){
        System.out.println(strUser);
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        UUID id = null;


        try{
            JSONObject jObj = (JSONObject) new JSONParser().parse(strUser);
            String firstName = (String) jObj.get("firstName");
            String lastName = (String) jObj.get("lastName");
            String email = (String) jObj.get("email");
            String password = (String) jObj.get("password");
            Long memberSince = new Date().getTime();
            String organization = (String) jObj.get("organization");
            String department = (String) jObj.get("department");
            String role = (String) jObj.get("role");

            id = UUID.randomUUID();

            Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
            User user = new User(id, firstName, lastName, email, password, memberSince  , organization, department, role);
            mapper.save(user);
            System.out.printf("First Name: %s\nLast Name: %s", user.getFirstName(), user.getLastName());

        } catch (Exception e){
            System.out.println(e);
        }
        db.close();
        return id;
    }

    public int deleteUser(String userId){
        if (userId == null){
            System.out.println("No request parameter was provided");
            return 400;
        }

        UUID id = UUID.fromString(userId);

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
            User whose = mapper.get(id);
            if (whose == null){
                System.out.println("User wasn't found in database");
                db.close();
                return 404;
            }
            mapper.delete(whose);

        } catch (Exception e){
            System.out.println(e);
        }
        db.close();
        return 200;
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

    public JSONObject getUser(String id){
        return getUser(id, false);
    }

    @SuppressWarnings("unchecked")
    public JSONObject getUser(String userId, boolean forLogin){
        if (userId == null){
            System.out.println("No request parameter was provided");
            return null;
        }

        UUID userUuid = UUID.fromString(userId);

        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        User whose;

        Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
        whose = mapper.get(userUuid);
        if (whose == null) {
            System.out.println("User wasn't found in database");
            db.close();
            return null;
        }

        UUID id =whose.getId();
        String firstName = whose.getFirstName();
        String lastName = whose.getLastName();
        String mail = whose.getEmail();
        String password = whose.getPassword();
        Long memberSince = whose.getMemberSince();
        String organization = whose.getOrganization();
        String department = whose.getDepartment();
        String role = whose.getRole();
        List<String> follows = whose.getFollows();

        JSONObject user = createUserJson(id, firstName, lastName, mail, password, memberSince, organization, department, role, follows);
        db.close();
        if(!forLogin){
            user.put("password", "OMITTED!");
        }
        return user;
    }

}
