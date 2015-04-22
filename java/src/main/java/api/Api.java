package api;
import controllers.UserController;

/**
 * Created by annikamagnusson on 17/04/15.
 */
import models.User;
import org.json.simple.JSONObject;
import spark.*;
public class Api {
    public static void main(String[] args){
        Spark.setPort(9090);

        // Routes for files
        Spark.get(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {
                return "get file";
            }
        });

        Spark.post(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {
                return "post file";
            }
        });

        Spark.delete(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {
                return "delete file";
            }
        });

        Spark.put(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {
                return "put file";
            }
        });

        // Routes for users
        Spark.post(new Route("/users") {
            @Override
            public Object handle(Request request, Response response) {
                String body = request.body();
                System.out.println(body);
                new UserController().createUser(body);
                return "User created";
            }
        });

        Spark.get(new Route("/users") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                return new UserController().CreateJson();
            }
        });

        Spark.delete(new Route("/users") {
            @Override
            public Object handle(Request request, Response response) {
                return "Delete user";
            }
        });

        // Routes for groups
        Spark.post(new Route("/groups") {
            @Override
            public Object handle(Request request, Response response) {
                return null;
            }
        });

        Spark.get(new Route("/groups") {
            @Override
            public Object handle(Request request, Response response) {
                return null;
            }
        });

        Spark.delete(new Route("/groups") {
            @Override
            public Object handle(Request request, Response response) {
                return null;
            }
        });

        // Routes for login
        Spark.get(new Route("/login") {
            @Override
            public Object handle(Request request, Response response) {
                return null;
            }
        });


    }
}