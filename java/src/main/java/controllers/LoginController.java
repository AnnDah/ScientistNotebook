package controllers;

import utility.PasswordUtility;
import exceptions.GetException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Class to handle login of user
 *
 * @author Sebastian Nirfelt
 * @version 1.0 - 20/04/15
 */
public class LoginController {

    @SuppressWarnings("unchecked")
    public JSONObject login(String strLogin) throws GetException{
        UserController uc = new UserController();

        try{
            JSONObject jObj = (JSONObject) new JSONParser().parse(strLogin);
            String email = (String) jObj.get("email");
            String password = (String) jObj.get("password");
            JSONObject user = uc.getUserLogin(email);

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
