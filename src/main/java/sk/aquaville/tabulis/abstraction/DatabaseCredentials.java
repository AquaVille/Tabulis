package sk.aquaville.tabulis.abstraction;

/**
 * Represents a container for credentials required to authenticate with a database.
 *
 * <p>
 * Implementations typically provide information such as username, password,
 * host, port, database name, or other authentication-related properties.
 * </p>
 *
 * <p>
 * This interface allows decoupling database connection logic from the actual credential storage,
 * enabling flexibility for secure handling, environment-based configuration, or credential rotation.
 * </p>
 */
public interface DatabaseCredentials {
}
