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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for dynamically constructing SQL queries with support for the most common SQL clauses.
 *
 * This class provides a fluent API to help developers build SQL queries by appending various parts of the query
 * such as SELECT, FROM, WHERE, AND, OR, ORDER BY, LIMIT, and OFFSET. Additionally, it allows for adding parameters
 * to the query for safe execution using a {@code PreparedStatement}. The class handles the correct formatting of
 * SQL syntax and supports dynamic query building in a flexible manner.
 *
 * <p><b>Example usage:</b></p>
 * ```java
 * QueryBuilder queryBuilder = new QueryBuilder();
 * PreparedStatement query = queryBuilder.select("name", "age")
 *     .from("users")
 *     .where("age > ?")
 *     .orderBy("name")
 *     .limit(10)
 *     .offset(0)
 *     .addParameter(25)
 *     .prepare(connection);
 * ```
 *
 * This class is useful for constructing queries dynamically based on different conditions, and it abstracts away
 * the manual creation and formatting of SQL queries.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 * @see web.ambassador.db.DatabaseManager
 * @see web.ambassador.db.RowMapper
 * @see web.ambassador.db.TransactionCallback
 */
public class QueryBuilder {
    private final StringBuilder query;
    private final List<Object> parameters;
    private boolean isSelect;

    /**
     * Constructs a new empty {@code QueryBuilder} instance.
     *
     * Initializes an empty query string and an empty list of parameters. This constructor is used to start
     * building a SQL query using the fluent API.
     */
    public QueryBuilder() {
        this.query = new StringBuilder();
        this.parameters = new ArrayList<>();
    }

    /**
     * Adds a SELECT clause to the SQL query.
     *
     * If no columns are specified, "*" is used to select all columns. Otherwise, the provided column names
     * are included in the SELECT clause, separated by commas.
     *
     * @param columns The columns to select. If empty, all columns ("*") are selected.
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder select(String... columns) {
        this.query.append("SELECT ");
        this.query.append(columns.length > 0 ? String.join(", ", columns) : "*");
        this.isSelect = true;

        return this;
    }

    /**
     * Adds a FROM clause to the SQL query to specify the table to query data from.
     *
     * @param table The name of the table to select data from.
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder from(String table) {
        this.query.append(" FROM ").append(table);
        return this;
    }

    /**
     * Adds a WHERE clause to the SQL query with the specified condition.
     *
     * @param condition The condition for the WHERE clause (e.g., "age > 25").
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder where(String condition) {
        this.query.append(" WHERE ").append(condition);
        return this;
    }

    /**
     * Adds an AND condition to the WHERE clause of the SQL query.
     *
     * @param condition The condition to be appended with the AND keyword (e.g., "salary < 50000").
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder and(String condition) {
        this.query.append(" AND ").append(condition);
        return this;
    }

    /**
     * Adds an OR condition to the WHERE clause of the SQL query.
     *
     * @param condition The condition to be appended with the OR keyword (e.g., "city = 'New York'").
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder or(String condition) {
        this.query.append(" OR ").append(condition);
        return this;
    }

    /**
     * Adds an ORDER BY clause to the SQL query to specify sorting columns.
     *
     * @param columns The columns to order by, separated by commas.
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder orderBy(String... columns) {
        this.query.append(" ORDER BY ").append(String.join(", ", columns));
        return this;
    }

    /**
     * Adds a LIMIT clause to the SQL query to limit the number of rows returned.
     *
     * @param limit The maximum number of rows to return.
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder limit(int limit) {
        this.query.append(" LIMIT ").append(limit);
        return this;
    }

    /**
     * Adds an OFFSET clause to the SQL query to skip a number of rows before returning results.
     *
     * @param offset The number of rows to skip.
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder offset(int offset) {
        this.query.append(" OFFSET ").append(offset);
        return this;
    }

    /**
     * Adds a parameter to the query. This method is used to safely bind values to the SQL query
     * (e.g., to avoid SQL injection attacks).
     *
     * @param value The value of the parameter to be added to the query.
     * @return The current {@code QueryBuilder} instance to allow method chaining.
     */
    public QueryBuilder addParameter(Object value) {
        this.parameters.add(value);
        return this;
    }

    /**
     * Prepares a {@code PreparedStatement} for execution using the current query and parameters.
     *
     * This method takes a {@code Connection} and creates a {@code PreparedStatement} with the built query string
     * and binds all the parameters that have been added using {@link #addParameter(Object)}.
     *
     * @param connection The {@code Connection} to use for preparing the statement.
     * @return A {@code PreparedStatement} ready for execution.
     * @throws SQLException If there is an error while preparing the statement or setting the parameters.
     */
    public PreparedStatement prepare(Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query.toString());
        for(int i = 0; i < this.parameters.size(); i++)
            stmt.setObject(i + 1, this.parameters.get(i));

        return stmt;
    }
}
