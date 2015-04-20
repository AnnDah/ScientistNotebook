package api;
import controllers.UserController;

/**
 * Created by annikamagnusson on 17/04/15.
 */
import models.User;
import spark.*;
public class Api {
    public static void main(String[] args){
        Spark.setPort(9090);

        Spark.get(new Route("/files") {
            @Override
            public Object handle(Request request, Response response) {
                return "get file";
            }
        });

        Spark.post(new Route("/files") {
            @Override
            public Object handle(Request request, Response response) {
                return "post file";
            }
        });

        Spark.delete(new Route("/files") {
            @Override
            public Object handle(Request request, Response response) {
                return "delete file";
            }
        });

        Spark.put(new Route("/files") {
            @Override
            public Object handle(Request request, Response response) {
                return "put file";
            }
        });

        Spark.post(new Route("/users") {
            @Override
            public Object handle(Request request, Response response) {
                new UserController().createUser("Laban", "Labansson");
                return "User created";
            }
        });


    }
}
