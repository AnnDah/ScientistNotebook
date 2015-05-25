package api;
import controllers.*;
import exceptions.DeletionException;
import exceptions.GetException;
import exceptions.CreationException;
import exceptions.UpdateException;
import org.json.simple.JSONObject;
import spark.*;
import models.DatabaseConnector;

/**
 * API for application.
 *
 * @author Annika Magnusson
 * @version 1.0, 17/04/15
 */

public class Api {
    public static void main(String[] args){
        Spark.setPort(9090);

        //Start: Database connection test
        DatabaseConnector db = new DatabaseConnector();
        db.connectDefault();
        db.close();
        //End

        /**
         * Get a specific data.
         * Data id in request params.
         */
        Spark.get(new Route("/data/:id  ") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                try {
                    return new DataController().getDataJson(request.params(":id"));
                } catch (GetException e) {
                    response.status(404);
                }
                return 0;
            }
        });

        /**
         * Create a new data.
         * Data information is sent request body in JSON format.
         */
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

        /**
         * Delete a specific data.
         * Data id in request params.
         */
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

        /**
         * Update a specific data.
         * Data id in request params.
         * Updates are sent in request body in JSON format.
         */
        Spark.put(new Route("/data/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new DataController().updateData(request.params(":id"), request.body());
                } catch (UpdateException e) {
                    response.status(400);
                }
                return response;
            }
        });

        /**
         * Search data using tags.
         * Tags are sent in request query.
         */
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

        /**
         * Get all data published by a specific user.
         * User id in request params.
         */
        Spark.get(new Route("/data/user/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new DataController().getDataUser(request.params(":id"));
                } catch (GetException e) {
                    response.status(400);
                }
                return response;
            }
        });

        /**
         * Get all data that belongs to a specific project.
         * Project id in request params.
         */
        Spark.get(new Route("/data/project/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new DataController().getDataProject(request.params(":id"));
                } catch (GetException e) {
                    response.status(400);
                }
                return response;
            }
        });

        /**
         * Create a new user.
         * User information is sent in request body in JSON format.
         */
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

        /**
         * Get a specific user.
         * User id in request params.
         */
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

        /**
         * Update a specific user.
         * User id in request params.
         * Updates are sent in request body in JSON format.
         */
        Spark.put(new Route("/users/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new UserController().updateUser(request.params(":id"), request.body());
                } catch (UpdateException e) {
                    response.status(400);
                }
                return response;
            }
        });

        /**
         * Add a user to data list of followers and a project in users list of follows.
         * User id and project id is sent in request params.
         */
        Spark.put(new Route("/users/:userId/:projectId") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    new ProjectController().addFollower(request.params(":projectId"), request.params(":userId"));
                    new UserController().addFollows(request.params(":userId"), request.params(":projectId"));
                } catch (UpdateException e) {
                    response.status(400);
                }
                return response;
            }
        });

        /**
         * Delete a specific user.
         * User id in request params.
         */
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

        /**
         * Create a new project.
         * Project information is sent in request body in JSON format.
         */
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

        /**
         * Get a specific project.
         * Project id in request params.
         */
        Spark.get(new Route("/projects/:id") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                try {
                    return new ProjectController().getProjectJson(request.params(":id"));
                } catch (GetException e) {
                    response.status(400);
                }
                return 0;
            }
        });

        /**
         * Search projects using tags.
         * Tags in request query.
         */
        Spark.get(new Route("/projects") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new ProjectController().searchProjectTags(request.queryParams("tags"));
                } catch (GetException e) {
                    response.status(400);
                }

                return 0;
            }
        });

        /**
         * Update a specific project.
         * Project id in request params.
         */
        Spark.put(new Route("/projects/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new ProjectController().updateProject(request.params(":id"), request.body());
                } catch (UpdateException e) {
                    response.status(400);
                }
                return response;
            }
        });

        /**
         * Delete a specific project.
         * Project id in requrst params.
         */
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

        /**
         * Create a new organization.
         * Organization information are sent in request body in JSON format.
         */
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

        /**
         * Get a specific organization.
         * Organization id in request params.
         */
        Spark.get(new Route("/organizations/:id") {
            @Override
            public Object handle(Request request, Response response) {
                response.header("Content-Type", "Application/JSON");
                try {
                    return new OrganizationController().getOrganizationJson(request.params(":id"));
                } catch (GetException e) {
                    response.status(404);
                }
                return 0;
            }
        });

        /**
         * Update a specific organization.
         * Organization id in request params.
         */
        Spark.put(new Route("/organizations/:id") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return new OrganizationController().updateOrganization(request.params(":id"), request.body());
                } catch (UpdateException e) {
                    response.status(400);
                }
                return response;
            }
        });

        /**
         * Delete a specific organization.
         * Organization id in request params.
         */
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

        /**
         * Login for user.
         * User email and password are sent in request body.
         */
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

        /**
         * Used to test api calls containing JSON in request body.
         * Should be removed before project delivery.
         */
        Spark.post(new Route("/test") {
            @Override
            public Object handle(Request request, Response response) {
                System.out.println("Json in body: " + request.body());
                return request.body();
            }
        });

    }
}
