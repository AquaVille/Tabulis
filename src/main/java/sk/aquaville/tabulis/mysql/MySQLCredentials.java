package sk.aquaville.tabulis.mysql;

import com.github.bsideup.jabel.Desugar;
import sk.aquaville.tabulis.abstraction.DatabaseCredentials;

/**
 * Holds all necessary credentials and connection settings for a MySQL database.
 * Used by {@link MySQLConnection} to configure and establish a connection pool.
 *
 * @param host     The hostname or IP address of the MySQL server.
 * @param port     The port number of the MySQL server (default is usually "3306").
 * @param user     The username for authenticating with the database.
 * @param password The password for authenticating with the database.
 * @param database The name of the database to connect to.
 * @param ssl      Whether to use SSL/TLS for the connection.
 */
@Desugar
public record MySQLCredentials(
        String host,
        String port,
        String user,
        String password,
        String database,
        Boolean ssl
) implements DatabaseCredentials {
}
