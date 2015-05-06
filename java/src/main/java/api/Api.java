package api;
import controllers.*;
import models.DatabaseConnector;
import models.User;
import org.json.simple.JSONObject;
import spark.*;

/**
 * Created by annikamagnusson on 17/04/15.
 */

public class Api {
    public static void main(String[] args){
        Spark.setPort(9090);

        //Start: Database connection test
        //DatabaseConnector db = new DatabaseConnector();
        //db.connectDefault();
        //db.close();
        //End

        // Routes for data
        Spark.get(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                JSONObject json =  new DataController().getData(request.queryParams("id"));
                if (json == null) {
                    response.status(404);
                }
                return json;
            }
        });

        Spark.post(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {
                new DataController().createData(request.body());
                return "data posted";
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
                JSONObject j = new UserController().getUser(request.queryParams("email"));
                if (j == null) {
                    response.status(404);
                }
                return j;
            }
        });

        Spark.delete(new Route("/users") {
            @Override
            public Object handle(Request request, Response response) {
                int status = new UserController().deleteUser(request.queryParams("email"));
                response.status(status);
                return response;
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

                String email = request.queryParams("email");
                String password = request.queryParams("password");
                return new LoginController().Login(email, password);
            }
        });

        // Routes for admin
        Spark.get(new Route("/tables") {
            @Override
            public Object handle(Request request, Response response) {

                return null;
            }
        });

        Spark.post(new Route("/tables") {
            @Override
            public Object handle(Request request, Response response) {
                new DatabaseController().createTable(request.body());
                return "Table created";
            }
        });

        Spark.get(new Route("/keyspace") {
            @Override
            public Object handle(Request request, Response response) {
                new DatabaseController().getKeyspaces();
                return "yey";
            }
        });

        Spark.post(new Route("/keyspace") {
            @Override
            public Object handle(Request request, Response response) {
                new DatabaseController().createKeyspace(request.body());
                return null;
            }
        });



    }
}
