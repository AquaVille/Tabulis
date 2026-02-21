package sk.aquaville.tabulis.abstraction.exception;

import sk.aquaville.tabulis.abstraction.DatabaseConnection;

/**
 * Exception thrown when a database connection cannot be established
 * or an error occurs while interacting with the database connection.
 *
 * <p>
 * Typically used to signal critical failures in obtaining or using a
 * {@link DatabaseConnection}, such as connectivity issues or misconfigured credentials.
 * </p>
 */
public class DatabaseConnectionException extends RuntimeException {

    /**
     * Constructs a new DatabaseConnectionException with the specified detail message.
     *
     * @param message Detailed message explaining the reason for the exception.
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
