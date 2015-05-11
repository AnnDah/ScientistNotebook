package controllers;

import models.DatabaseConnector;
import org.json.simple.JSONObject;
import controllers.UserController;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class LoginController {

    public JSONObject Login(String email, String password){
        UserController uc = new UserController();

        JSONObject user = null;
        user = uc.getUser(email, true);

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
        error.put("error", "Wrong email");
        return error;
    }
}
