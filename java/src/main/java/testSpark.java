/**
 * Created by annikamagnusson on 17/04/15.
 */
import spark.*;
public class testSpark {
    public static void main(String[] args) {
        Spark.get(new Route("/") {
            public Object handle(final Request request, final Response response){
                return "Hello World from Spark";
            }
        });
    }
}
