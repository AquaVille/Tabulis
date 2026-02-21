package sk.aquaville.tabulis.abstraction;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Provides low-level asynchronous methods for executing raw SQL queries.
 * <p>
 * This interface is designed for direct database interaction with full control
 * over SQL statements. It supports the following operations:
 * </p>
 *
 * <ul>
 *     <li>SELECT queries for fetching multiple or single rows</li>
 *     <li>INSERT, UPDATE, and DELETE statements for modifying data</li>
 *     <li>Parameter binding for prepared statements to prevent SQL injection</li>
 * </ul>
 *
 * <p>
 * All methods are asynchronous and return {@link CompletableFuture} instances
 * to allow non-blocking execution and integration with reactive workflows.
 * Implementations are responsible for mapping rows to {@link Row} objects.
 * </p>
 */
public interface QueryExecutor {

    /**
     * Executes a SELECT query and returns all matching rows.
     *
     * @param sql    The SQL query to execute.
     * @param params Parameters to bind to the query.
     * @return CompletableFuture resolving to a list of {@link Row} objects representing the results.
     */
    CompletableFuture<List<Row>> fetchAll(String sql, List<Object> params) throws CompletionException;

    /**
     * Executes a SELECT query and returns the first matching row.
     *
     * @param sql    The SQL query to execute.
     * @param params Parameters to bind to the query.
     * @return CompletableFuture resolving to a {@link Row} object representing the first result, or null if no rows are found.
     */
    CompletableFuture<Row> fetchOne(String sql, List<Object> params) throws CompletionException;

    /**
     * Executes an INSERT, UPDATE, or DELETE statement.
     *
     * @param sql    The SQL statement to execute.
     * @param params Parameters to bind to the statement.
     * @return CompletableFuture resolving to the number of rows affected by the statement.
     */
    CompletableFuture<Integer> execute(String sql, List<Object> params) throws CompletionException;
}
