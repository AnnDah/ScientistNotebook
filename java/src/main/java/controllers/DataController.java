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
}
