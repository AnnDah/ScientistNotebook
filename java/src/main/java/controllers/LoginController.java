package controllers;

import models.DatabaseConnector;
import org.json.simple.JSONObject;
import controllers.UserController;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class LoginController {

    public JSONObject Login(String email, String password){
        DatabaseConnector db = new DatabaseConnector();

        //JSONObject user = db.GetData("");
        JSONObject user = new JSONObject();
        user.put("firstName", "Kristoffer");
        user.put("lastName", "Olsson");
        user.put("email", "email@mail.com");
        user.put("password", "test");
        //String pw = user.get("password").toString();

        if(user != null) {
            if (user.get("password").toString().equals(password)) {
                user.put("password", "OMITTED!");
                return user;
            }
            JSONObject error = new JSONObject();
            error.put("error", "Wrong password");
            return error;
        }
        JSONObject error = new JSONObject();
        error.put("error", "Wrong username");
        return error;
    }
}
