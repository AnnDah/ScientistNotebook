package models;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by annikamagnusson on 20/04/15.
 */
@Table(keyspace = "scinote", name = "data")
public class Data {
    @PartitionKey
    private String id;
    private String content;
    private String created;
    private String author;
    private String visibility;
    private List<String> tags = new ArrayList<String>();

    public Data(){

    }

    public Data(String content, String created, String author, String visibility, List<String> tags, String id){
        this.content = content;
        this.created = created;
        this.author = author;
        this.visibility = visibility;
        this.tags = tags;
        this.id = id;
    }

    public String getId() { return this.id; }

    public void setId(String id){
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

    public String getVisibility() { return this.visibility; }

    public void setVisibility(String visibility){
        this.visibility = visibility;
    }

    public String getCreated(){
        return this.created;
    }

    public void setCreated(String created){
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
                    Objects.equals(this.visibility, that.visibility);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, created, author, visibility, tags);
    }
}
