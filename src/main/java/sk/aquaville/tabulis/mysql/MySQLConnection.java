package sk.aquaville.tabulis.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import sk.aquaville.tabulis.abstraction.DatabaseConnection;
import sk.aquaville.tabulis.universal.executor.DatabaseExecutor;
import sk.aquaville.tabulis.universal.query.SQLQueryBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLConnection implements DatabaseConnection {

    private final HikariDataSource dataSource;

    public MySQLConnection(MySQLCredentials credentials)
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + credentials.host() + "/" + credentials.database() + "?useSSL="+ credentials.ssl().toString() +"&serverTimezone=UTC");
        config.setUsername(credentials.user());
        config.setPassword(credentials.password());
        config.setPoolName("MySQLConnectionPool");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);

        // Should be optimal according to MySQL docs
        config.setConnectionTimeout(8000);  // 8s
        config.setIdleTimeout(120000);      // 2 minutes
        config.setMaxLifetime(1800000);     // 30 minutes (MySQL default)
        config.setLeakDetectionThreshold(60000); // 60s

        this.dataSource = new HikariDataSource(config);
    }

    public Connection getRaw() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() throws InterruptedException {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }

        DatabaseExecutor.shutdown(10L);
    }

    public SQLQueryBuilder table(String table) {
        return new SQLQueryBuilder(table, this);
    }
}
