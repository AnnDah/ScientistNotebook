package controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.List;
import java.util.ArrayList;
import models.Data;

/**
 * Created by annikamagnusson on 20/04/15.
 */
public class DataController {
    public void createData(String strData){
        JSONObject jObj = null;
        try{
            jObj = (JSONObject) new JSONParser().parse(strData);
        } catch (Exception e){
            System.out.println(e);
        }
        try{
            String content  = (String) jObj.get("content");
            String date = (String) jObj.get("date");
            String author = (String) jObj.get("author");
            String visibility = (String) jObj.get("visibilty");
            List<String> tags = new ArrayList<String>();

            Data data;
            data = new Data(date, content, author, visibility, tags);
        } catch (Exception e){

        }
    }

    public JSONObject getData(String dataId){
        String content = "Some data";
        String created = "20150910";
        String author = "Annika Magnusson";
        String visibilty = "1";

        JSONObject data = createDataJson(content, created, author, visibilty);
        return data;

    }

    public JSONObject createDataJson(String content, String created, String author, String visibility){
        //Tags should be included here as well
        JSONObject dataJson = new JSONObject();
        dataJson.put("content", content);
        dataJson.put("created", created);
        dataJson.put("author", author);
        dataJson.put("visibility", visibility);
        return dataJson;
    }
}
