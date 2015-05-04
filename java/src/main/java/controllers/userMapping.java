package controllers;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Objects;

/**
 * Created by annikamagnusson on 04/05/15.
 * Test class for mapping cassandra objects
 */
@Table(keyspace = "account", name = "users")
public class userMapping {
    @PartitionKey
    private String email;
    @Column(name = "addr")
    @Frozen
    private String firstName;
    private String lastName;
    private String password;

    public userMapping(){

    }

    public userMapping(String FirstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof userMapping){
            userMapping that = (userMapping) other;
            return Objects.equals(this.firstName, that.firstName) &&
                    Objects.equals(this.lastName, that.lastName) &&
                    Objects.equals(this.email, that.email);
        }
        return false;
    }

}
