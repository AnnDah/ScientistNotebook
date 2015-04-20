package controllers;
import models.User;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class UserController {

    public void createUser(String firstName, String lastName, String id, String email, String password){
        User user;
        user = new User(firstName, lastName, id, email, password);
        System.out.printf("First Name: %s\nLast Name: %s",user.getFirstName(), user.getLastName());
    }
}
