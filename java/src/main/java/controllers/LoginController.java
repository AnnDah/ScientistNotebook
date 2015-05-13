package controllers;

import Utility.PasswordUtility;
import exceptions.GetException;
import org.json.simple.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by annikamagnusson on 20/04/15.
 *
 */
public class LoginController {

    @SuppressWarnings("unchecked")
    public JSONObject Login(String email, String password) throws GetException{
        UserController uc = new UserController();


        JSONObject user;
        user = uc.getUserLogin(email);
        try{
            if(user != null) {
                if (user.get("password").toString().equals(PasswordUtility.generateHash(password))) {
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
        } catch(Exception e){
            JSONObject error = new JSONObject();
            error.put("error", e);
            return error;
        }
    }
}
