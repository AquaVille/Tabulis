package sk.aquaville.tabulis.universal.query;

import lombok.RequiredArgsConstructor;
import sk.aquaville.tabulis.abstraction.*;
import sk.aquaville.tabulis.universal.explorer.SQLSchemaExplorer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class SQLQueryBuilder implements QueryBuilder {

    private final String table;
    private final QueryExecutor exec;

    // Separate lists for AND OR conditions and params
    private final List<String> whereAndCs = new ArrayList<>();
    private final List<Object> andParams = new ArrayList<>();
    private final List<String> whereOrCs = new ArrayList<>();
    private final List<Object> orParams = new ArrayList<>();
    private final DatabaseConnection connection;
    private String order = "";
    private String lim = "";

    public SQLQueryBuilder(String table, DatabaseConnection connection) {
        this.table = table;
        this.exec = new SQLQueryExecutor(connection);
        this.connection = connection;
    }

    @Override
    public SQLQueryBuilder where(String col, Object val) {
        whereAndCs.add(col + " = ?");
        andParams.add(val);
        return this;
    }

    @Override
    public SQLQueryBuilder whereOr(String col, Object val) {
        whereOrCs.add(col + " = ?");
        orParams.add(val);
        return this;
    }

    @Override
    public SQLQueryBuilder orderBy(String c) {
        order = c;
        return this;
    }

    @Override
    public SQLQueryBuilder limit(int l) {
        lim = String.valueOf(l);
        return this;
    }

    @Override
    public SQLQueryBuilder related(Map<String, Object> parentRow, String relatedTable) throws SQLException {
        SchemaExplorer s = new SQLSchemaExplorer(connection);
        String pk = s.getPrimaryKey(table);
        String fk = s.getForeignKey(relatedTable, table);
        Object pkVal = parentRow.get(pk);
        return new SQLQueryBuilder(relatedTable, connection).where(fk, pkVal);
    }

    /**
     * Builds the WHERE clause of the query combining all AND, OR conditions.
     *
     * @return a string representing the WHERE clause, or an empty string if no conditions are set
     */
    private String buildWhereClause() {
        List<String> parts = new ArrayList<>();
        if (!whereAndCs.isEmpty()) {
            parts.add("(" + String.join(" AND ", whereAndCs) + ")");
        }
        if (!whereOrCs.isEmpty()) {
            parts.add("(" + String.join(" OR ", whereOrCs) + ")");
        }
        if (parts.isEmpty()) return "";
        return " WHERE " + String.join(" AND ", parts);
    }

    /**
     * Combines all parameters (AND OR) into a single list for use in query execution.
     *
     * @return a list of all parameters to bind to the query
     */
    private List<Object> getAllParams() {
        List<Object> all = new ArrayList<>(andParams);
        all.addAll(orParams);
        return all;
    }

    /**
     * Builds the SELECT statement with the specified columns and WHERE conditions.
     *
     * @param selectCols the columns to select (comma-separated list)
     * @return a string representing the complete SELECT query
     */
    private String buildSelect(String selectCols) {
        StringBuilder sb = new StringBuilder()
                .append("SELECT ").append(selectCols)
                .append(" FROM ").append(table)
                .append(buildWhereClause());
        if (!order.isEmpty()) sb.append(" ORDER BY ").append(order);
        if (!lim.isEmpty()) sb.append(" LIMIT ").append(lim);
        return sb.toString();
    }

    @Override
    public CompletableFuture<Integer> count() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM " + table + buildWhereClause();

        return exec.fetchOne(sql, getAllParams())
                .thenApply(row -> row == null ? 0 : row.getAs("total", Number.class).intValue());
    }

    @Override
    public CompletableFuture<List<Row>> fetchAll(String... columns) throws SQLException {
        String cols = (columns == null || columns.length == 0) ? "*" : String.join(", ", columns);
        return exec.fetchAll(buildSelect(cols), getAllParams());
    }

    @Override
    public CompletableFuture<Row> fetchOne(String... columns) throws SQLException {
        String cols = (columns == null || columns.length == 0) ? "*" : String.join(", ", columns);
        String sql = buildSelect(cols);
        if (!sql.toLowerCase().contains("limit")) {
            sql += " LIMIT 1";
        }
        return exec.fetchOne(sql, getAllParams());
    }

    @Override
    public CompletableFuture<Integer> insert(Map<String, Object> data) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
        StringBuilder ps = new StringBuilder();
        List<Object> p2 = new ArrayList<>();
        data.forEach((k, v) -> {
            if (!p2.isEmpty()) {
                sql.append(", ");
                ps.append(", ");
            }
            sql.append(k);
            ps.append("?");
            p2.add(v);
        });
        sql.append(") VALUES (").append(ps).append(")");
        return exec.execute(sql.toString(), p2);
    }

    @Override
    public CompletableFuture<Integer> update(Map<String, Object> data) throws SQLException {
        if (whereAndCs.isEmpty() && whereOrCs.isEmpty()) throw new RuntimeException("Update without WHERE");
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        List<Object> p2 = new ArrayList<>();
        data.forEach((k, v) -> {
            if (!p2.isEmpty()) sql.append(", ");
            sql.append(k).append(" = ?");
            p2.add(v);
        });
        sql.append(buildWhereClause());
        p2.addAll(getAllParams());
        return exec.execute(sql.toString(), p2);
    }

    @Override
    public CompletableFuture<Integer> delete() throws SQLException {
        if (whereAndCs.isEmpty() && whereOrCs.isEmpty()) throw new RuntimeException("Delete without WHERE");
        String sql = "DELETE FROM " + table + buildWhereClause();
        return exec.execute(sql, getAllParams());
    }

    @Override
    public CompletableFuture<QueryBuilder> thenSync(java.util.function.Consumer<QueryBuilder> action) {
        return CompletableFuture.completedFuture(this).thenApply(builder -> {
            action.accept(builder);
            return builder;
        });
    }
}
