package models;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class Data {
    private String id;
    private String content;
    private String author;
    private String visibility;

    public Data(String id, String content, String author, String visibility){
        this.id = id;
        this.content = content;
        this.author = author;
        this.visibility = visibility;
    }

    public String GetId() { return this.id; }

    public String GetContent() { return this.content; }

    public String GetAuthor() { return this.author; }

    public String GetVisibility() { return this.visibility; }
}
