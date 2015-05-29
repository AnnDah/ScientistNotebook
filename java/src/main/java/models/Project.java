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
 * Model of Project
 *
 * @author Annika Magnusson
 * @version 1.0 - 20/04/15
 */
@Table(keyspace = "scinote", name = "projects")
public class Project {
    @PartitionKey
    private UUID id;
    private String field;
    private String description;
    @Column(name="created_by")
    private String createdBy;
    private String name;
    private String status;
    private String owner;
    private List<String> tags = new ArrayList<String>();
    @Column(name="project_roles")
    private List<String> projectRoles = new ArrayList<String>();
    @Column(name="funded_by")
    private List<String> fundedBy = new ArrayList<String>();
    private List<String> members = new ArrayList<String>();
    private List<String> employers = new ArrayList<String>();
    private List<String> funds = new ArrayList<String>();
    private List<String> departments = new ArrayList<String>();
    private List<String> followers = new ArrayList<String>();
    @Column(name="is_private")
    private boolean isPrivate;
    private Long created;

    public Project() {

    }

    public Project(UUID id, String field, List<String> tags, String description, List<String> projectRoles,
                   String createdBy, String name, String status, boolean isPrivate, Long created, List<String> fundedBy,
                   List<String> members, List<String> employers, List<String> funds, List<String> departments,
                   String owner) {
        this.id = id;
        this.field = field;
        this.tags = tags;
        this.description = description;
        this.projectRoles = projectRoles;
        this.createdBy = createdBy;
        this.name = name;
        this.status = status;
        this.isPrivate = isPrivate;
        this.created = created;
        this.fundedBy = fundedBy;
        this.members = members;
        this.employers = employers;
        this.funds = funds;
        this.departments = departments;
        this.owner = owner;

    }

    public List<String> getFollowers() {
        return this.followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getFunds() {
        return this.funds;
    }

    public void setFunds(List<String> funds) {
        this.funds = funds;
    }

    public List<String> getDepartments() {
        return this.departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

    public List<String> getEmployers() {
        return this.employers;
    }

    public void setEmployers(List<String> employers) {
        this.employers = employers;
    }

    public List<String> getMembers() {
        return this.members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<String> getFundedBy() {
        return this.fundedBy;
    }

    public void setFundedBy(List<String> fundedBy) {
        this.fundedBy = fundedBy;
    }

    public Long getCreated() {
        return this.created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public boolean getIsPrivate() {
        return  this.isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getProjectRoles() {
        return  this.projectRoles;
    }

    public void setProjectRoles(List<String> projectRoles) {
        this.projectRoles = projectRoles;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Determines whether two objects are equal
     * @param other the object to compare with
     * @return true if they are equal, else false
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof Project) {
            Project that = (Project) other;
            return Objects.equals(this.id, that.id) &&
                    Objects.equals(this.field, that.field) &&
                    Objects.equals(this.description, that.description) &&
                    Objects.equals(this.projectRoles, that.projectRoles) &&
                    Objects.equals(this.createdBy, that.createdBy) &&
                    Objects.equals(this.name, that.name) &&
                    Objects.equals(this.status, that.status) &&
                    Objects.equals(this.tags, that.tags) &&
                    Objects.equals(this.isPrivate, that.isPrivate) &&
                    Objects.equals(this.created, that.created) &&
                    Objects.equals(this.fundedBy, that.fundedBy) &&
                    Objects.equals(this.members, that.members) &&
                    Objects.equals(this.employers, that.employers) &&
                    Objects.equals(this.funds, that.funds) &&
                    Objects.equals(this.departments, that.departments) &&
                    Objects.equals(this.owner, that.owner) &&
                    Objects.equals(this.followers, that.followers);
        }
        return false;
    }

    /**
     * Generates a hash value that can be used to uniquely identify a particular Object.
     * @return a hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, field, description, projectRoles, createdBy, name, status, tags,
                isPrivate, created, fundedBy, members, employers, funds, departments, owner, followers);
    }

    /**
     * Creates a JSONObject of the Project object
     * @return a JSONObject of the Project object
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject projectJson = new JSONObject();
        projectJson.put("id", this.getId().toString());
        projectJson.put("field", this.getField());
        projectJson.put("description", this.getDescription());
        projectJson.put("createdBy", this.getCreatedBy());
        projectJson.put("name", this.getName());
        projectJson.put("status", this.getStatus());
        projectJson.put("tags", this.getTags());
        projectJson.put("projectRoles", this.getProjectRoles());
        projectJson.put("isPrivate", Boolean.toString(this.getIsPrivate()));
        projectJson.put("created", getUtcDate(this.getCreated()).toString());
        projectJson.put("fundedBy", this.getFundedBy());
        projectJson.put("members", this.getMembers());
        projectJson.put("employers", this.getEmployers());
        projectJson.put("funds", this.getFunds());
        projectJson.put("departments", this.getDepartments());
        projectJson.put("owner", this.getOwner());
        projectJson.put("followers", this.getFollowers());

        JSONArray ja = new JSONArray();
        ja.add(projectJson);

        JSONObject mainObj = new JSONObject();
        mainObj.put("projects", ja);

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
