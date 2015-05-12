package models;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import java.util.UUID;
import java.util.Date;
import java.util.Objects;

/**
 * Created by annikamagnusson on 20/04/15.
 *
 */
@Table(keyspace = "scinote", name = "users")
public class User {
    @PartitionKey
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Long memberSince;
    private String organization;
    private String department;
    private String role;


    public User(){

    }

    public User(UUID id, String firstName, String lastName, String email, String password, Long memberSince, String organization,
                String department, String role){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.memberSince = memberSince;
        this.organization = organization;
        this.department = department;
        this.role = role;

    }

    public UUID getId(){
        return this.id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
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

    public Long getMemberSince(){
        return this.memberSince;
    }

    public void setMemberSince(Long memberSince){
        this.memberSince = memberSince;
    }

    public String getOrganization(){
        return this.organization;
    }

    public void setOrganization(String organization){
        this.organization = organization;
    }

    public String getDepartment(){
        return this.department;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public String getRole(){
        return this.role;
    }

    public void setRole(String role){
        this.role = role;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof User){
            User that = (User) other;
            return Objects.equals(this.firstName, that.firstName) &&
                    Objects.equals(this.lastName, that.lastName) &&
                    Objects.equals(this.email, that.email) &&
                    Objects.equals(this.password, that.password) &&
                    Objects.equals(this.memberSince, that.memberSince) &&
                    Objects.equals(this.organization, that.organization) &&
                    Objects.equals(this.department, that.department) &&
                    Objects.equals(this.role, that.role) &&
                    Objects.equals(this.id, that.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, memberSince, organization, department, role, id);
    }
}
