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
@Table(keyspace = "complex", name = "accounts")
public class userMapping {
    @PartitionKey
    private String email;
    private String name;
    @Column(name = "addr")
    @Frozen
    private String address;

    public userMapping(){

    }

    public userMapping(String name, String email, String address){
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this. address = address;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof userMapping){
            userMapping that = (userMapping) other;
            return Objects.equals(this.name, that.name) &&
                    Objects.equals(this.email, that.email);
        }
        return false;
    }

}
