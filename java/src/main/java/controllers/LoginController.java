package controllers;

import models.DatabaseConnector;
import org.json.simple.JSONObject;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class LoginController {

    public JSONObject Login(String email, String password){
        DatabaseConnector db = new DatabaseConnector();

        JSONObject user = db.GetData("");
        if(user != null) {
            if (user.get("password") == password) {
                user.put("password", "OMITTED!");
                return user;
            }
            JSONObject error = new JSONObject();
            error.put("error", "Wrong password");
            return error;
        }
        JSONObject error = new JSONObject();
        error.put("error", "Wrong username");
        return error;;
    }
}
