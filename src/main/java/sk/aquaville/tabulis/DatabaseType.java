package sk.aquaville.tabulis;

import lombok.Getter;
import sk.aquaville.tabulis.abstraction.DatabaseConnection;
import sk.aquaville.tabulis.abstraction.DatabaseCredentials;
import sk.aquaville.tabulis.mysql.MySQLConnection;
import sk.aquaville.tabulis.mysql.MySQLCredentials;
import sk.aquaville.tabulis.sqlite.SQLiteConnection;
import sk.aquaville.tabulis.sqlite.SQLiteCredentials;

/**
 * Enum representing the supported types of databases in the system.
 *
 * <p>
 * Each enum value corresponds to a specific database engine (e.g., MySQL, SQLite) and holds
 * the respective connection and credentials classes used to interact with that database.
 * </p>
 *
 * <p>
 * This enum is used to create and manage database connections in a flexible and extensible way.
 * Connection is initialized via {@link DatabaseProvider} builder class
 * </p>
 */
@Getter
public enum DatabaseType {

    /**
     * Represents the MySQL database engine.
     *
     * <p>
     * This constant holds the connection class {@link MySQLConnection} and the credentials class {@link MySQLCredentials}.
     * The MySQL database requires specific configurations like host, username, password, and database name for authentication.
     * </p>
     */
    MYSQL(MySQLConnection.class, MySQLCredentials.class),

    /**
     * Represents the SQLite database engine.
     *
     * <p>
     * This constant holds the connection class {@link SQLiteConnection} and the credentials class {@link SQLiteCredentials}.
     * SQLite is a file-based database, and the credentials typically consist of the file path to the SQLite database file.
     * </p>
     */
    SQLITE(SQLiteConnection.class, SQLiteCredentials.class);

    private final Class<? extends DatabaseConnection> connectionType;
    private final Class<? extends DatabaseCredentials> credentialsType;

    /**
     * Constructor for initializing the database type with its corresponding connection and credentials classes.
     *
     * @param connectionClass the class representing the database connection for this type
     * @param databaseCredentialsClass the class representing the credentials required to connect to the database
     */
    DatabaseType(Class<? extends DatabaseConnection> connectionClass, Class<? extends DatabaseCredentials> databaseCredentialsClass) {
        this.connectionType = connectionClass;
        this.credentialsType = databaseCredentialsClass;
    }

}
