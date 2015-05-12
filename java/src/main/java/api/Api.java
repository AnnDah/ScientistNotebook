package api;
import controllers.*;
import org.json.simple.JSONObject;
import spark.*;
import models.DatabaseConnector;
import java.util.UUID;

/**
 * Created by annikamagnusson on 17/04/15.
 *
 */

public class Api {
    public static void main(String[] args){
        Spark.setPort(9090);

        //Start: Database connection test
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        db.close();
        //End

        // Routes for da
        Spark.get(new Route("/data/:id  ") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                JSONObject json = new DataController().getData(request.params(":id"));
                if (json == null) {
                    response.status(404);
                }
                return json;
            }
        });

        Spark.post(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {

                return new DataController().createData(request.body());
            }
        });

        Spark.delete(new Route("/data/:id") {
            @Override
            public Object handle(Request request, Response response) {
                int status = new DataController().deleteData(request.params(":id"));
                response.status(status);
                return response;
            }
        });

        Spark.put(new Route("/data/:id") {
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

                return new UserController().createUser(body);
            }
        });

        Spark.get(new Route("/users/:id") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                JSONObject j = new UserController().getUser(request.params(":id"));
                if (j == null) {
                    response.status(404);
                }
                return j;
            }
        });

        Spark.delete(new Route("/users/:id") {
            @Override
            public Object handle(Request request, Response response) {
                int status = new UserController().deleteUser(request.params(":id"));
                response.status(status);
                return response;
            }
        });

        // Routes for projects
        Spark.post(new Route("/projects") {
            @Override
            public Object handle(Request request, Response response) {
                String body = request.body();
                UUID id = null;
                try {
                    id = new ProjectController().createProject(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return id;
            }
        });

        Spark.get(new Route("/projects/:id") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                JSONObject j = new ProjectController().getProject(request.params(":id"));
                if (j == null) {
                    response.status(404);
                }
                return j;
            }
        });

        Spark.delete(new Route("/projects/:id") {
            @Override
            public Object handle(Request request, Response response) {
                int status = new ProjectController().deleteProject(request.params(":id"));
                response.status(status);
                return response;
            }
        });

        // Routes for organizations
        Spark.post(new Route("/organizations") {
            @Override
            public Object handle(Request request, Response response) {
                return new OrganizationController().createOrganization(request.body());
            }
        });

        Spark.get(new Route("/organizations/:id") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                JSONObject j = new OrganizationController().getOrganization(request.params(":id"));
                if (j == null) {
                    response.status(404);
                }
                return j;
            }
        });

        Spark.delete(new Route("/organizations/:id") {
            @Override
            public Object handle(Request request, Response response) {
                int status = new OrganizationController().deleteOrganization(request.params(":id"));
                response.status(status);
                return response;
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


    }
}
