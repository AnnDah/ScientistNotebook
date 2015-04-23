package controllers;
import models.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;




/**
 * Created by annikamagnusson on 20/04/15.
 */
public class UserController {

    public void createUser(String strUser){
        JSONObject jObj = null;
        try{
            jObj = (JSONObject) new JSONParser().parse(strUser);
        } catch (Exception e){
            System.out.println(e);
        }
        try{
        String firstName = (String) jObj.get("firstName");
        String lastName = (String) jObj.get("lastName");
        String email = (String) jObj.get("email");
        String password = (String) jObj.get("password");

        User user;
        user = new User(firstName, lastName, email, password);
        System.out.printf("First Name: %s\nLast Name: %s",user.getFirstName(), user.getLastName());
        } catch (Exception e){
            
        }
    }

    public JSONObject createUserJson(String firstName, String lastName, String email){
        JSONObject userObj = new JSONObject();
        userObj.put("firstName", firstName);
        userObj.put("lastName", lastName);
        userObj.put("email", email);

        //Only needs to be implementet when we need to get multiple users
        //JSONArray ja = new JSONArray();
        //ja.add(jObj);
        //JSONObject mainObj = new JSONObject();
        //mainObj.put("users", ja);
        //System.out.println(mainObj);
        return userObj;
    }

    public JSONObject getUser(String userId){
        String firstName = "Annika";
        String lastName = "Magnusson";
        String email = "email@email.com";

        JSONObject user = createUserJson(firstName, lastName, email);
        return user;
    }
}
