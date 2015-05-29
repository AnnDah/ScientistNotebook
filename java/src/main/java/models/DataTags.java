package models;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import org.json.simple.JSONObject;

/**
 * Model for DataTags
 *
 * @author Annika Magnusson
 * @version 1.0, 15/05/15
 */
@Table(keyspace = "scinote", name = "data_tags")
public class DataTags {
    @PartitionKey
    private UUID id;
    private List<String> tags = new ArrayList<String>();
    private String name;
    private String author;
    private String description;
    private Long created;

    public DataTags() {

    }

    public DataTags(List<String> tags, UUID id, String name, String author, String description, Long created) {
        this.setTags(tags);
        this.setId(id);
        this.setName(name);
        this.setAuthor(author);
        this.setDescription(description);
        this.setCreated(created);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }


    /**
     * Determines whether two objects are equal
     * @param other the object to compare with
     * @return true if they are equal, else false
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof DataTags) {
            DataTags that = (DataTags) other;
            return Objects.equals(this.tags, that.tags) &&
                    Objects.equals(this.id, that.id) &&
                    Objects.equals(this.name, that.name) &&
                    Objects.equals(this.author, that.author) &&
                    Objects.equals(this.description, that.description) &&
                    Objects.equals(this.created, that.created);
        }
        return false;
    }

    /**
     * Generates a hash value that can be used to uniquely identify a particular Object.
     * @return a hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(tags, id, name, author, description, created);
    }

    /**
     * Creates a JSONObject of the DataTags object
     * @return a JSONObject of the DataTags object
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {

        JSONObject dataJson = new JSONObject();
        dataJson.put("id", this.getId().toString());
        dataJson.put("name", this.getName());
        dataJson.put("author", this.getAuthor());
        dataJson.put("description", this.getDescription());
        dataJson.put("created", getUtcDate(this.getCreated()).toString());
        dataJson.put("tags", this.getTags());

        return dataJson;

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
