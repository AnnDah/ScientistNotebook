package models;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Objects;

/**
 * Created by annikamagnusson on 20/04/15.
 */
@Table(keyspace = "scinote", name = "projects")
public class Project {
    @PartitionKey
    private String id;
    private String field;
    private List<String> tags;
    private String sharedLevel;
    private String projectAbstract;
    private List<String> participants;
    private List<String> projectRoles;
    private String author;
    private String name;
    private List<String> followers;
    private String status;

    public Project(){

    }

    public Project(String id, String field, List<String> tags, String sharedLevel, String projectAbstract, List<String> participants,
                   List<String> projectRoles, String author, String name, List<String> followers, String status){
        this.id = id;
        this.field = field;
        this.tags = tags;
        this.sharedLevel = sharedLevel;
        this.projectAbstract = projectAbstract;
        this.participants = participants;
        this.projectRoles = projectRoles;
        this.author = author;
        this.name = name;
        this.followers = followers;
        this.status = status;

    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getField(){
        return this.field;
    }

    public void setField(String field){
        this.field = field;
    }

    public List<String> getTags(){
        return this.tags;
    }

    public void setTags(List<String> tags){
        this.tags = tags;
    }

    public String getSharedLevel(){
        return this.sharedLevel;
    }

    public void setSharedLevel(String sharedLevel){
        this.sharedLevel = sharedLevel;
    }

    public String getProjectAbstract(){
        return this.projectAbstract;
    }

    public void setProjectAbstract(String projectAbstract){
        this.projectAbstract = projectAbstract;
    }

    public List<String> getParticipants(){
        return  this.participants;
    }

    public void setParticipants(List<String> participants){
        this.participants = participants;
    }

    public List<String> getProjectRoles(){
        return  this.projectRoles;
    }

    public void setProjectRoles(List<String> projectRoles){
        this.projectRoles = projectRoles;
    }

    public String getAuthor(){
        return this.author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<String> getFollowers(){
        return this.followers;
    }

    public void setFollowers(List<String> followers){
        this.followers = followers;
    }

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Project){
            Project that = (Project) other;
            return Objects.equals(this.id, that.id) &&
                    Objects.equals(this.field, that.field) &&
                    Objects.equals(this.sharedLevel, that.sharedLevel) &&
                    Objects.equals(this.projectAbstract, that.projectAbstract) &&
                    Objects.equals(this.participants, that.participants) &&
                    Objects.equals(this.projectRoles, that.projectRoles) &&
                    Objects.equals(this.author, that.author) &&
                    Objects.equals(this.name, that.name) &&
                    Objects.equals(this.followers, that.followers) &&
                    Objects.equals(this.status, that.status) &&
                    Objects.equals(this.tags, that.tags);
        }
        return false;
    }
}
