package controllers;
import models.User;
import java.io.StringWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 * Created by annikamagnusson on 20/04/15.
 */
public class UserController {

    public void createUser(String firstName, String lastName, String id, String email, String password){
        User user;
        user = new User(firstName, lastName, id, email, password);
        //System.out.printf("First Name: %s\nLast Name: %s",user.getFirstName(), user.getLastName());
    }

    public void CreateJson(){
        JSONObject jObj = new JSONObject();
        jObj.put("firstName", "Annika");
        jObj.put("lastName", "Magnusson");

        JSONArray ja = new JSONArray();
        ja.add(jObj);
        JSONObject mainObj = new JSONObject();
        mainObj.put("users", ja);
        System.out.println(mainObj);
    }
}
