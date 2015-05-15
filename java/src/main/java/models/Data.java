package models;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by annikamagnusson on 20/04/15.
 *
 */
@Table(keyspace = "scinote", name = "data")
public class Data {
    @PartitionKey
    private UUID id;
    private String content;
    private Long created;
    private String author;
    private String project;
    private String name;
    private String description;

    @Column(name="data_type")
    private String dataType;
    private int level;
    private List<String> tags = new ArrayList<String>();
    @Column(name="revision_history")
    private List<String> revisionHistory = new ArrayList<String>();



    public Data(){

    }

    public Data(String content, Long created, String author, int level, List<String> tags, UUID id, String dataType,
                String project, String name, String description){
        this.content = content;
        this.created = created;
        this.author = author;
        this.level = level;
        this.tags = tags;
        this.id = id;
        this.dataType = dataType;
        this.project = project;
        this.name = name;
        this.setDescription(description);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public  String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getProject(){
        return this.project;
    }

    public void setProject(String project){
        this.project = project;
    }

    public List<String> getRevisionHistory(){
        return this.revisionHistory;
    }

    public void setRevisionHistory(List<String> revisionHistory){
        this.revisionHistory = revisionHistory;
    }

    public String getDataType(){
        return this.dataType;
    }

    public void setDataType(String dataType){
        this.dataType = dataType;
    }

    public UUID getId() { return this.id; }

    public void setId(UUID id){
        this.id = id;
    }

    public String getContent() { return this.content; }

    public void setContent(String content){
        this.content = content;
    }

    public String getAuthor() { return this.author; }

    public void setAuthor(String author){
        this.author = author;
    }

    public int getLevel() { return this.level; }

    public void setLevel(int level){
        this.level = level;
    }

    public Long getCreated(){
        return this.created;
    }

    public void setCreated(Long created){
        this.created = created;
    }

    public List<String> getTags(){
        return this.tags;
    }

    public void setTags(List<String> tags){
        this.tags = tags;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Data){
            Data that = (Data) other;
            return Objects.equals(this.author, that.author) &&
                    Objects.equals(this.content, that.content) &&
                    Objects.equals(this.created, that.created) &&
                    Objects.equals(this.id, that.id) &&
                    Objects.equals(this.tags, that.tags) &&
                    Objects.equals(this.level, that.level) &&
                    Objects.equals(this.dataType, that.dataType) &&
                    Objects.equals(this.revisionHistory, that.revisionHistory) &&
                    Objects.equals(this.project, that.project) &&
                    Objects.equals(this.name, that.name) &&
                    Objects.equals(this.getDescription(), that.getDescription());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, created, author, level, tags, dataType, revisionHistory, project, name,
                getDescription());
    }

}
