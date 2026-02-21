package sk.aquaville.tabulis.universal.query;

import com.github.bsideup.jabel.Desugar;
import sk.aquaville.tabulis.abstraction.DatabaseConnection;
import sk.aquaville.tabulis.abstraction.QueryExecutor;
import sk.aquaville.tabulis.abstraction.Row;
import sk.aquaville.tabulis.universal.executor.DatabaseExecutor;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Desugar
public record SQLQueryExecutor(DatabaseConnection connection) implements QueryExecutor {

    private static <T> Field findField(Class<T> c, String col) {
        for (Field f : c.getDeclaredFields()) {
            if (f.getName().equalsIgnoreCase(col)) return f;
        }
        return null;
    }

    @Override
    public CompletableFuture<List<Row>> fetchAll(String sql, List<Object> params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection conn = connection.getRaw();
                PreparedStatement ps = conn.prepareStatement(sql);

                SQLQueryBinder.bind(ps, params);

                ResultSet rs = ps.executeQuery();
                List<Row> result = new ArrayList<>();
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 1; i <= cols; i++) {
                        map.put(md.getColumnLabel(i), rs.getObject(i));
                    }
                    result.add(new Row(map));
                }

                return result;
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        }, DatabaseExecutor.DB_POOL);
    }

    @Override
    public CompletableFuture<Integer> execute(String sql, List<Object> params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection conn;
                conn = connection.getRaw();
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                SQLQueryBinder.bind(ps, params);
                return ps.executeUpdate();
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        }, DatabaseExecutor.DB_POOL);
    }

    @Override
    public CompletableFuture<Row> fetchOne(String sql, List<Object> params) {
        return fetchAll(sql, params).thenApply(rows -> rows.isEmpty() ? null : rows.get(0));
    }
}
