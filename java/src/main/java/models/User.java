package models;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class User {
    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private String password;

    public User(String firstName, String lastName, String id, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // Getters for class variables
    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }
}
