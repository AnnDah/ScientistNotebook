package exceptions;

/**
 * Exception class for delete exceptions
 * @author Annika Magnusson
 * @version 1.0 - 12/05/15
 */
public class DeletionException extends Exception {
    public DeletionException(String message){
        super(message);
    }
}
