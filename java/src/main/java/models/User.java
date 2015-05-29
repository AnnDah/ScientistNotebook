package models;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Column;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Model of User.
 *
 * @author Annika Magnusson
 * @version 1.0 - 20/04/15
 */
@Table(keyspace = "scinote", name = "users")
public class User {
    @PartitionKey
    private UUID id;
    private String email;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    private String password;
    @Column(name="member_since")
    private Long memberSince;
    private String organization;
    private String department;
    private String role;
    private List<String> follows = new ArrayList<String>();

    public User() {

    }

    public User(UUID id, String firstName, String lastName, String email, String password, Long memberSince, String organization,
                String department, String role) {
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

    public List<String> getFollows() {
        return this.follows;
    }

    public void setFollows(List<String> follows) {
        this.follows = follows;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getMemberSince() {
        return this.memberSince;
    }

    public void setMemberSince(Long memberSince) {
        this.memberSince = memberSince;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Determines whether two objects are equal
     * @param other the object to compare with
     * @return true if they are equal, else false
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof User) {
            User that = (User) other;
            return Objects.equals(this.firstName, that.firstName) &&
                    Objects.equals(this.lastName, that.lastName) &&
                    Objects.equals(this.email, that.email) &&
                    Objects.equals(this.password, that.password) &&
                    Objects.equals(this.memberSince, that.memberSince) &&
                    Objects.equals(this.organization, that.organization) &&
                    Objects.equals(this.department, that.department) &&
                    Objects.equals(this.role, that.role) &&
                    Objects.equals(this.id, that.id) &&
                    Objects.equals(this.follows, that.follows);
        }
        return false;
    }

    /**
     * Generates a hash value that can be used to uniquely identify a particular Object.
     * @return a hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, memberSince, organization, department, role, id, follows);
    }

    /**
     * Creates a JSONObject of the User object
     * @return a JSONObject of the User object
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJson(boolean login) {
        JSONObject userJson = new JSONObject();
        userJson.put("id", this.getId().toString());
        userJson.put("firstName", this.getFirstName());
        userJson.put("lastName", this.getLastName());
        userJson.put("email", this.getEmail());
        userJson.put("memberSince", getUtcDate(this.getMemberSince()).toString());
        userJson.put("organization", this.getOrganization());
        userJson.put("department", this.getDepartment());
        userJson.put("role", this.getRole());
        userJson.put("follows", this.getFollows());

        if(login) {
            userJson.put("password", this.getPassword());
            return userJson;

        }

        JSONArray ja = new JSONArray();
        ja.add(userJson);
        JSONObject mainObj = new JSONObject();
        mainObj.put("users", ja);

        return mainObj;

    }

    /**
     * Converts a Unix Timestamp to a Date in UTC
     * @param date a Long of a Unix Timestamp
     * @return the converted Date
     */
    private Date getUtcDate(Long date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new Date(date);
    }
}
