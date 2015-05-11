package controllers;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import models.DatabaseConnector;
import models.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by annikamagnusson on 20/04/15.
 *
 */
public class UserController {

    public void createUser(String strUser){
        System.out.println(strUser);
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();



        try{
            JSONObject jObj = (JSONObject) new JSONParser().parse(strUser);
            String firstName = (String) jObj.get("firstName");
            String lastName = (String) jObj.get("lastName");
            String email = (String) jObj.get("email");
            String password = (String) jObj.get("password");
            Long date = new Date().getTime();
            String organization = (String) jObj.get("organization");
            String department = (String) jObj.get("department");
            String role = (String) jObj.get("role");

            Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
            User user = new User(firstName, lastName, email, password, date, organization, department, role);
            mapper.save(user);
            System.out.printf("First Name: %s\nLast Name: %s", user.getFirstName(), user.getLastName());

            //Set millis to UTC time
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date utcDate= new Date(date);
            System.out.println(formatter.format(utcDate));
        } catch (Exception e){
            System.out.println(e);
        }
        db.close();
    }

    public int deleteUser(String email){
        if (email == null){
            System.out.println("No request parameter was provided");
            return 400;
        }
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        try {
            Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
            User whose = mapper.get(email);
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

    public JSONObject createUserJson(String firstName, String lastName, String email, String password, Long date,
                                     String organization, String department, String role){
        // Parse date to UTC
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate= new Date(date);
        System.out.println(formatter.format(utcDate));

        JSONObject userJson = new JSONObject();
        userJson.put("firstName", firstName);
        userJson.put("lastName", lastName);
        userJson.put("email", email);
        userJson.put("password", password);
        userJson.put("date", utcDate);
        userJson.put("organization", organization);
        userJson.put("department", department);
        userJson.put("role", role);

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

    public JSONObject getUser(String email){
        return getUser(email, false);
    }

    public JSONObject getUser(String email, boolean forLogin){
        if (email == null){
            System.out.println("No request parameter was provided");
            return null;
        }
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        User whose;

        Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
        whose = mapper.get(email);
        if (whose == null) {
            System.out.println("User wasn't found in database");
            db.close();
            return null;
        }


        String firstName = whose.getFirstName();
        String lastName = whose.getLastName();
        String mail = whose.getEmail();
        String password = whose.getPassword();
        Long date = whose.getDate();
        String organization = whose.getOrganization();
        String department = whose.getDepartment();
        String role = whose.getRole();

        JSONObject user = createUserJson(firstName, lastName, mail, password, date, organization, department, role);
        db.close();
        if(!forLogin){
            user.put("password", "OMITTED!");
        }
        return user;
    }

    public JSONObject testJson(){
        JSONObject userJson = new JSONObject();

        List<String> mylist = new ArrayList<String>();

        for (int i = 0; i < 3; i++){
            mylist.add("hej");
        }

        userJson.put("firstName", "Annika");
        userJson.put("lastName", "Magnusson");
        userJson.put("email", "annika@mail.com");
        userJson.put("list", mylist);

        System.out.println(userJson.toString());

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
