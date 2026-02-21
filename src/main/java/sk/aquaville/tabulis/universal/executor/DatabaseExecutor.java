package sk.aquaville.tabulis.universal.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Manages a shared thread pool for asynchronous database operations.
 *
 * <p>
 * Provides a fixed-size pool of daemon threads to execute non-blocking
 * database queries and updates, helping to offload work from the main thread.
 * </p>
 *
 * <p>
 * The pool size defaults to half of the available CPU cores, with a minimum of 2 threads.
 * </p>
 *
 * <p>
 * Use {@link #shutdown(long)} to gracefully terminate the pool during application shutdown.
 * </p>
 */
public final class DatabaseExecutor {

    /**
     * Default number of database worker threads.
     */
    private static final int DEFAULT_DB_THREADS = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);

    /**
     * Shared thread pool for executing database tasks.
     * Threads are named "db-worker" and run as daemon threads.
     */
    public static final ExecutorService DB_POOL = Executors.newFixedThreadPool(DEFAULT_DB_THREADS, r -> {
        Thread t = new Thread(r, "db-worker");
        t.setDaemon(true);
        return t;
    });

    /**
     * Gracefully shuts down the database thread pool.
     *
     * <p>
     * Waits up to {@code timeoutSeconds} for running tasks to complete,
     * then forces shutdown if the timeout is exceeded.
     * </p>
     *
     * @param timeoutSeconds maximum time to wait for termination, in seconds
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public static void shutdown(long timeoutSeconds) throws InterruptedException {
        DB_POOL.shutdown();
        if (!DB_POOL.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
            DB_POOL.shutdownNow();
        }
    }
}
