package sk.aquaville.tabulis.abstraction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents an abstraction over a database connection.
 *
 * <p>
 * Implementations provide access to the underlying database, allow safe connection handling,
 * and simplify query operations via a {@link QueryBuilder}.
 * </p>
 *
 * <p>
 * This interface decouples database operations from specific JDBC or connection implementations,
 * enabling easier testing and swapping of database backends.
 * </p>
 */
public interface DatabaseConnection {

    /**
     * Returns the raw, native connection object to the database.
     *
     * @return the underlying {@link Connection} instance
     * @throws SQLException if the connection is not established or is invalid
     */
    Connection getRaw() throws SQLException;

    /**
     * Closes the active database connection safely.
     * <p>
     * Implementations should release any resources associated with the connection,
     * ensuring that subsequent calls to {@link #getRaw()} or {@link #table(String)} fail gracefully.
     * </p>
     */
    void close() throws InterruptedException;

    /**
     * Initializes a {@link QueryBuilder} for the specified database table.
     *
     * @param table the name of the table for which queries will be built
     * @return a new instance of {@link QueryBuilder} bound to the specified table
     */
    QueryBuilder table(String table);

}
