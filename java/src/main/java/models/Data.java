package models;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class Data {
    private String id;
    private String date;
    private String content;
    private String author;
    private String visibility;
    private List<String> tags = new ArrayList<String>();

    public Data(String date, String content, String author, String visibility, List<String> tags){
        this.date = date;
        this.content = content;
        this.author = author;
        this.visibility = visibility;
        this.tags = tags;
    }

    public String getId() { return this.id; }

    public String getContent() { return this.content; }

    public String getAuthor() { return this.author; }

    public String getVisibility() { return this.visibility; }

    public String getDate(){
        return this.date;
    }

    public List<String> getTags(){
        return this.tags;
    }
}
