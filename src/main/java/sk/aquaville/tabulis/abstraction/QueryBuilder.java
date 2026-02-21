package sk.aquaville.tabulis.abstraction;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Fluent and asynchronous API for building and executing SQL queries in a database-agnostic manner.
 * This interface supports common relational database operations including:
 *
 * <ul>
 *     <li>SELECT queries with flexible WHERE conditions, ORDER BY clauses, and LIMIT constraints</li>
 *     <li>INSERT, UPDATE, and DELETE operations</li>
 *     <li>Counting rows matching specific conditions</li>
 *     <li>Loading related entities through foreign key relationships</li>
 *     <li>Transaction management (begin, commit, rollback)</li>
 *</ul>
 *
 * <p>
 * All query execution methods return {@link CompletableFuture} instances, allowing non-blocking
 * and asynchronous database interactions. The interface is designed to be thread-safe and easily
 * composable using method chaining.
 * </p>
 *
 * Example usage:
 * {@code
 * queryBuilder.table("users")
 *             .where("status", "active")
 *             .orderBy("created_at DESC")
 *             .limit(10)
 *             .fetchAll("id", "username")
 *             .thenAccept(users -> ...);
 * }
 *
 * <p>
 * Implementations are expected to handle SQL exceptions internally and may provide
 * custom row mapping logic via {@link Row} objects or similar abstractions.
 * </p>
 */
public interface QueryBuilder {
    /**
     * Adds a WHERE clause with the AND logic for specified column and value.
     *
     * @param column Column name for the condition.
     * @param value  Value to compare against.
     * @return this QueryBuilder for method chaining.
     */
    QueryBuilder where(String column, Object value);

    /**
     * Adds a WHERE clause with OR logic for the specified column and value.
     *
     * @param column Column name for the condition.
     * @param value  Value to compare against.
     * @return this QueryBuilder for method chaining.
     */
    QueryBuilder whereOr(String column, Object value);

    /**
     * Adds an ORDER BY clause.
     *
     * @param clause ORDER BY expression (e.g., "created_at DESC").
     * @return this QueryBuilder for method chaining.
     */
    QueryBuilder orderBy(String clause);

    /**
     * Limits the number of results returned.
     *
     * @param limit Maximum number of rows to fetch.
     * @return this QueryBuilder for method chaining.
     */
    QueryBuilder limit(int limit);

    /**
     * Creates a new QueryBuilder for a related table based on a foreign key relationship.
     *
     * @param parentRow    A map representing the parent table's row.
     * @param relatedTable Name of the related table.
     * @return a new QueryBuilder instance targeting the related table.
     */
    QueryBuilder related(Map<String, Object> parentRow, String relatedTable) throws SQLException;

    /**
     * Returns the count of rows matching the current WHERE conditions.
     *
     * @return CompletableFuture resolving to the count of rows.
     */
    CompletableFuture<Integer> count() throws SQLException;

    /**
     * Executes the built SELECT query and maps all rows using the provided mapper.
     * If columns are provided, only those columns are selected.
     * If no columns are provided, selects all columns (*).
     *
     * @param columns Optional list of columns to select.
     * @return CompletableFuture resolving to a list of mapped objects.
     */
    CompletableFuture<List<Row>> fetchAll(String... columns) throws SQLException;

    /**
     * Executes the built SELECT query and maps the first row using the provided mapper.
     * If columns are provided, only those columns are selected.
     * If no columns are provided, selects all columns (*).
     *
     * @param columns Optional list of columns to select.
     * @return CompletableFuture resolving to the first mapped object or null if none.
     */
    CompletableFuture<Row> fetchOne(String... columns) throws SQLException;

    /**
     * Executes an INSERT operation with the specified data.
     *
     * @param data A map of column names to values.
     * @return CompletableFuture resolving to the number of rows inserted.
     */
    CompletableFuture<Integer> insert(Map<String, Object> data) throws SQLException;

    /**
     * Executes an UPDATE operation with the specified data.
     *
     * @param data A map of column names to updated values.
     * @return CompletableFuture resolving to the number of rows updated.
     */
    CompletableFuture<Integer> update(Map<String, Object> data) throws SQLException;

    /**
     * Executes a DELETE operation for rows matching the current WHERE conditions.
     *
     * @return CompletableFuture resolving to the number of rows deleted.
     */
    CompletableFuture<Integer> delete() throws SQLException;

    /**
     * Executes the given action synchronously on the current thread after async operations.
     * This is framework-agnostic and does not require Bukkit or external schedulers.
     *
     * @param action A consumer receiving the current QueryBuilder.
     * @return CompletableFuture resolving to this QueryBuilder after action execution.
     */
    CompletableFuture<QueryBuilder> thenSync(java.util.function.Consumer<QueryBuilder> action);
}
