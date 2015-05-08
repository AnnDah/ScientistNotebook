package controllers;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.sun.tools.javac.util.Convert;
import models.DatabaseConnector;
import models.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by annikamagnusson on 20/04/15.
 */
public class UserController {

    public void createUser(String strUser){
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();

        JSONObject jObj = null;
        String firstName;
        String lastName;
        String email;
        String password;

        try{
            jObj = (JSONObject) new JSONParser().parse(strUser);
        } catch (Exception e){
            System.out.println(e);
        }
        try{
            firstName = (String) jObj.get("firstName");
            lastName = (String) jObj.get("lastName");
            email = (String) jObj.get("email");
            password = (String) jObj.get("password");

            Mapper<User> mapper = new MappingManager(db.getSession()).mapper(User.class);
            User user = new User(firstName, lastName, email, password);
            mapper.save(user);
            System.out.printf("First Name: %s\nLast Name: %s", user.getFirstName(), user.getLastName());
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

    public JSONObject createUserJson(String firstName, String lastName, String email){
        JSONObject userJson = new JSONObject();
        userJson.put("firstName", firstName);
        userJson.put("lastName", lastName);
        userJson.put("email", email);

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

        JSONObject user = createUserJson(firstName, lastName, mail);
        db.close();
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
