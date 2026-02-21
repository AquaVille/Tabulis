package sk.aquaville.tabulis;

import sk.aquaville.tabulis.abstraction.DatabaseConnection;
import sk.aquaville.tabulis.abstraction.DatabaseCredentials;
import sk.aquaville.tabulis.abstraction.exception.DatabaseConnectionException;
import sk.aquaville.tabulis.util.InstanceInvoker;

/**
 * A utility class responsible for providing a unified connection pool to different database engines.
 *
 * <p>
 * This class uses reflection to dynamically instantiate the appropriate database connection
 * based on the specified database engine type, ensuring compatibility with various database engines.
 * </p>
 */
public final class DatabaseProvider {

    /**
     * Factory method for providing a unified connection to various implemented database engines.
     *
     * <p>
     * This method uses reflection to instantiate a connection pool for the specified database engine type
     * and credentials, returning a suitable {@link DatabaseConnection} instance.
     * </p>
     *
     * @param type the type of the database engine (e.g., MySQL, SQLite, etc.)
     * @param credentials the credentials required to authenticate with the database
     * @return a {@link DatabaseConnection} instance representing the connection pool
     * @throws DatabaseConnectionException if the connection could not be initialized
     */
    public static DatabaseConnection get(DatabaseType type, DatabaseCredentials credentials) throws DatabaseConnectionException {
        DatabaseConnection connection = InstanceInvoker.newInstance((Class<DatabaseConnection>) type.getConnectionType(), credentials);
        if (connection == null) {
            throw new DatabaseConnectionException("Failed to initialize connection to database");
        }
        return connection;
    }
}
