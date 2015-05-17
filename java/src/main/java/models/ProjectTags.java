package models;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by annikamagnusson on 16/05/15.
 *
 */
@Table(keyspace = "scinote", name = "project_tags")
public class ProjectTags {
    @PartitionKey
    private UUID id;
    private List<String> tags = new ArrayList<String>();
    private String name;
    private String status;
    private String description;
    @Column(name="is_private")
    private boolean isPrivate;
    private Long created;

    public ProjectTags(){

    }

    public ProjectTags(UUID id, List<String> tags, String name, String status, String description, boolean isPrivate,
                       Long created){
        this.setId(id);
        this.setTags(tags);
        this.setName(name);
        this.setStatus(status);
        this.setDescription(description);
        this.setIsPrivate(isPrivate);
        this.setCreated(created);


    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof ProjectTags){
            ProjectTags that = (ProjectTags) other;
            return Objects.equals(this.id, that.id) &&
                    Objects.equals(this.tags, that.tags) &&
                    Objects.equals(this.name, that.name) &&
                    Objects.equals(this.status, that.status) &&
                    Objects.equals(this.description, that.description) &&
                    Objects.equals(this.isPrivate, that.isPrivate) &&
                    Objects.equals(this.created, that.created);

        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tags, name, status, description, isPrivate, created);
    }
}
