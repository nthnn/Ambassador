/*
 * Copyright (c) 2024 - Nathanne Isip
 * This file is part of Ambassador.
 *
 * Ambassador is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Ambassador is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ambassador. If not, see <https://www.gnu.org/licenses/>.
 */
package web.ambassador.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Manages database connections, providing connection pooling and transaction handling.
 *
 * The {@code DatabaseManager} class maintains a pool of database connections and provides methods
 * for obtaining, releasing, and executing queries with transactions. It manages a connection pool
 * that helps improve performance by reusing database connections.
 * <br/>
 * This class supports executing database queries with automatic transaction management, as well
 * as a shutdown process that cleans up resources.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 * @see web.ambassador.db.QueryBuilder
 */
public class DatabaseManager {
    private final String url;
    private final String username;
    private final String password;
    private final int maxPoolSize;
    private final int timeoutSeconds;
    private final BlockingQueue<Connection> connectionPool;
    private final Set<Connection> usedConnections;
    private volatile boolean isShuttingDown;

    /**
     * Constructs a {@code DatabaseManager} with the specified database connection details and default pool size and timeout.
     *
     * @param url The URL of the database.
     * @param username The username for the database connection.
     * @param password The password for the database connection.
     */
    public DatabaseManager(String url, String username, String password) {
        this(url, username, password, 10, 30);
    }

    /**
     * Constructs a {@code DatabaseManager} with the specified database connection details, pool size, and timeout.
     *
     * @param url The URL of the database.
     * @param username The username for the database connection.
     * @param password The password for the database connection.
     * @param poolSize The maximum size of the connection pool.
     * @param timeoutSeconds The maximum time (in seconds) to wait for a connection from the pool.
     */
    public DatabaseManager(String url, String username, String password, int poolSize, int timeoutSeconds) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxPoolSize = poolSize;
        this.timeoutSeconds = timeoutSeconds;
        this.connectionPool = new ArrayBlockingQueue<>(poolSize);
        this.usedConnections = Collections.synchronizedSet(new HashSet<>());
        this.isShuttingDown = false;

        this.initializePool();
    }

    /**
     * Initializes the connection pool by creating the initial set of database connections.
     *
     * This method is invoked during the construction of the {@code DatabaseManager} and creates the
     * specified number of connections that will be used by the pool.
     *
     * @throws DatabaseException If there is an error establishing connections to the database.
     */
    private void initializePool() {
        try {
            for(int i = 0; i < maxPoolSize; i++)
                this.connectionPool.offer(createConnection());
        }
        catch(SQLException e) {
            throw new DatabaseException("Failed to initialize connection pool", e);
        }
    }

    /**
     * Creates a new database connection using the specified connection details.
     *
     * @return A new {@code Connection} to the database.
     * @throws SQLException If an error occurs while creating the connection.
     */
    private Connection createConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(
            this.url,
            this.username,
            this.password
        );

        conn.setAutoCommit(true);
        return conn;
    }

    /**
     * Retrieves a {@code Connection} from the connection pool. Blocks until a connection is available or the timeout is reached.
     *
     * @return A {@code Connection} from the pool.
     * @throws DatabaseException If there is an error retrieving the connection from the pool or if the timeout is reached.
     */
    public Connection getConnection() throws DatabaseException {
        try {
            Connection connection = this.connectionPool.poll(
                this.timeoutSeconds,
                TimeUnit.SECONDS
            );

            if(connection == null)
                throw new DatabaseException("Timeout waiting for database connection");

            if(!connection.isValid(1))
                connection = createConnection();

            this.usedConnections.add(connection);
            return connection;
        }
        catch(SQLException | InterruptedException e) {
            throw new DatabaseException("Failed to get database connection", e);
        }
    }

    /**
     * Releases a {@code Connection} back into the pool if the system is not shutting down.
     * If the system is shutting down, the connection is closed immediately.
     *
     * @param connection The connection to be released.
     */
    public void releaseConnection(Connection connection) {
        if(connection != null && this.usedConnections.remove(connection)) {
            if(!this.isShuttingDown)
                this.connectionPool.offer(connection);
            else closeConnection(connection);
        }
    }

    /**
     * Closes the given {@code Connection}.
     *
     * @param connection The connection to be closed.
     */
    private void closeConnection(Connection connection) {
        try {
            connection.close();
        }
        catch(SQLException _) { }
    }

    /**
     * Shuts down the {@code DatabaseManager}, closing all connections and clearing the connection pool.
     */
    public void shutdown() {
        this.isShuttingDown = true;

        for(Connection conn : this.usedConnections)
            this.closeConnection(conn);
        this.usedConnections.clear();

        Connection conn;
        while((conn = this.connectionPool.poll()) != null)
            this.closeConnection(conn);
    }

    /**
     * Executes a database operation within a transaction.
     *
     * The method ensures that the database operation is executed within a transaction, which will be committed
     * upon success, or rolled back in case of an exception.
     *
     * @param callback The callback that performs the database operation.
     * @param <T> The return type of the callback.
     * @return The result of the database operation.
     * @throws DatabaseException If there is an error executing the transaction or rolling back the transaction.
     */
    public <T> T withTransaction(TransactionCallback<T> callback) throws DatabaseException {
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            T result = callback.execute(connection);
            connection.commit();

            return result;
        }
        catch(SQLException e) {
            try {
                connection.rollback();
            }
            catch(SQLException rollbackEx) {
                throw new DatabaseException("Failed to rollback transaction", rollbackEx);
            }

            throw new DatabaseException("Transaction failed", e);
        }
        finally {
            if(connection != null) {
                try {
                    connection.setAutoCommit(true);
                }
                catch (SQLException _) {}

                this.releaseConnection(connection);
            }
        }
    }

    /**
     * Executes a query using the specified {@code QueryBuilder} and maps the results to a list of objects using the specified {@code RowMapper}.
     *
     * @param queryBuilder The {@code QueryBuilder} that constructs the query to be executed.
     * @param mapper The {@code RowMapper} that maps each row of the result set to an object.
     * @param <T> The type of the objects in the result list.
     * @return A list of objects mapped from the result set.
     * @throws DatabaseException If there is an error executing the query or mapping the results.
     */
    public <T> List<T> query(QueryBuilder queryBuilder, RowMapper<T> mapper) throws DatabaseException {
        return this.withTransaction(connection -> {
            List<T> results = new ArrayList<>();
            try(PreparedStatement stmt = queryBuilder.prepare(connection);
                ResultSet rs = stmt.executeQuery()) {
                while(rs.next())
                    results.add(mapper.mapRow(rs));
            }

            return results;
        });
    }

    /**
     * Executes a query that updates the database (e.g., INSERT, UPDATE, DELETE) using the specified {@code QueryBuilder}.
     *
     * @param queryBuilder The {@code QueryBuilder} that constructs the update query.
     * @return The number of rows affected by the query.
     * @throws DatabaseException If there is an error executing the update query.
     */
    public int execute(QueryBuilder queryBuilder) throws DatabaseException {
        return this.withTransaction(connection -> {
            try(PreparedStatement stmt = queryBuilder.prepare(connection)) {
                return stmt.executeUpdate();
            }
        });
    }
}
