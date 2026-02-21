package sk.aquaville.tabulis.sqlite;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import sk.aquaville.tabulis.abstraction.DatabaseConnection;
import sk.aquaville.tabulis.universal.executor.DatabaseExecutor;
import sk.aquaville.tabulis.universal.query.SQLQueryBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class SQLiteConnection implements DatabaseConnection {

    private final HikariDataSource dataSource;

    public SQLiteConnection(SQLiteCredentials credentials) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + credentials.databaseFile());
        config.setPoolName("SQLiteConnectionPool");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        this.dataSource = new HikariDataSource(config);
    }


    @Override
    public Connection getRaw() throws SQLException {
        var c = dataSource.getConnection();
        // Should optimize task time... i guess...
        try (Statement s = c.createStatement()) {
            s.execute("PRAGMA journal_mode=WAL");
            s.execute("PRAGMA synchronous=NORMAL");
            s.execute("PRAGMA busy_timeout=5000");
        }
        return c;
    }

    @Override
    public void close() throws InterruptedException {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }

        DatabaseExecutor.shutdown(10L);
    }

    @Override
    public SQLQueryBuilder table(String table) { return new SQLQueryBuilder(table, this); }
}
