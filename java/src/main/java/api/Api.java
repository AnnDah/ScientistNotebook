package api;
import controllers.*;
import exceptions.DeletionException;
import exceptions.GetException;
import exceptions.CreationException;
import org.json.simple.JSONObject;
import spark.*;
import models.DatabaseConnector;

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
                try {
                    return new DataController().getData(request.params(":id"));
                } catch (GetException e) {
                    response.status(404);
                }
                return 0;
            }
        });

        Spark.post(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new DataController().createData(request.body());
                } catch (CreationException e) {
                    response.status(400);
                }

                return 0;
            }
        });

        Spark.delete(new Route("/data/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    new DataController().deleteData(request.params(":id"));
                } catch (DeletionException e) {
                    response.status(400);
                }
                return response;
            }
        });

        Spark.put(new Route("/data/:id") {
            @Override
            public Object handle(Request request, Response response) {
                return "put file";
            }
        });

        Spark.get(new Route("/data") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new DataController().searchDataTags(request.queryParams("tags"));
                } catch (GetException e) {
                    response.status(400);
                }

                return 0;
            }
        });

        // Routes for users
        Spark.post(new Route("/users") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new UserController().createUser(request.body());
                } catch (CreationException e) {
                    response.status(400);
                }
                return 0;
            }
        });

        Spark.get(new Route("/users/:id") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                try {
                    return new UserController().getUserJson(request.params(":id"));
                } catch (GetException e) {
                    response.status(400);
                }

                return 0;
            }
        });

        Spark.put(new Route("/users/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try{
                    new UserController().updateUser(request.params(":id"), request.body());
                }catch (GetException e){
                    response.status(400);
                }
                return null;
            }
        });

        Spark.delete(new Route("/users/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    new UserController().deleteUser(request.params(":id"));
                } catch (DeletionException e) {
                    response.status(400);
                }
                return response;
            }
        });

        // Routes for projects
        Spark.post(new Route("/projects") {
            @Override
            public Object handle(Request request, Response response) {
                String body = request.body();
                try {
                    return new ProjectController().createProject(body);
                } catch (CreationException e) {
                    response.status(400);
                }
                return 0;
            }
        });

        Spark.get(new Route("/projects/:id") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                try {
                    return new ProjectController().getProject(request.params(":id"));
                } catch (GetException e) {
                    response.status(400);
                }
                return 0;
            }
        });

        Spark.get(new Route("/projects") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new ProjectController().searchProjectTags(request.queryParams("tags"));
                }catch (GetException e){
                    response.status(400);
                }

                return 0;
            }
        });

        Spark.delete(new Route("/projects/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    new ProjectController().deleteProject(request.params(":id"));
                } catch (DeletionException e) {
                    response.status(400);
                }
                return 0;
            }
        });

        // Routes for organizations
        Spark.post(new Route("/organizations") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new OrganizationController().createOrganization(request.body());
                } catch (CreationException e) {
                    response.status(400);
                }
                return 0;
            }
        });

        Spark.get(new Route("/organizations/:id") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                try {
                    return new OrganizationController().getOrganization(request.params(":id"));
                } catch (GetException e) {
                    response.status(404);
                }
                return 0;
            }
        });

        Spark.delete(new Route("/organizations/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    new OrganizationController().deleteOrganization(request.params(":id"));
                } catch (DeletionException e) {
                    response.status(400);
                }
                return 0;
            }
        });

        // Routes for login
        Spark.post(new Route("/login") {
            @Override
            public Object handle(Request request, Response response) {
                String body = request.body();
                JSONObject user = null;
                try {
                    user = new LoginController().login(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return user;
            }
        });

        //Route for testing
        Spark.post(new Route("test") {
            @Override
            public Object handle(Request request, Response response) {
                System.out.println("Json in body: " + request.body());
                return request.body();
            }
        });

        Spark.post(new Route("/search") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new DataController().searchDataTags(request.body());
                }catch (GetException e){
                    response.status(400);
                }

                return 0;
            }
        });


    }
}
