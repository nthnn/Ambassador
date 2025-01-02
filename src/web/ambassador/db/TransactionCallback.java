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
import java.sql.SQLException;

/**
 * A functional interface for executing database operations within a transaction.
 *
 * This interface is used in scenarios where database operations need to be executed within a transactional context.
 * Implementations of this interface define the logic for the database operation to be performed as part of a
 * transaction, ensuring that all operations are either committed or rolled back as a single unit.
 *
 * @param <T> The type of the result that is returned after executing the transaction.
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 * @see web.ambassador.db.DatabaseManager
 * @see web.ambassador.db.QueryBuilder
 * @see web.ambassador.db.RowMapper
 */
public interface TransactionCallback<T> {
    /**
     * Executes the transaction operation within the given {@code Connection}.
     *
     * This method is called within a transactional context where the provided {@code Connection} is part of an
     * ongoing transaction. The implementation should perform the necessary database operations using this connection
     * and return the result. The transaction will be committed if the operation is successful, or rolled back in case
     * of an exception.
     *
     * @param connection The {@code Connection} object representing the current transaction context.
     * @return The result of the transaction operation, typically a result such as the number of rows affected or
     *         any object produced by the operation.
     * @throws SQLException If an error occurs during the transaction operation, such as a database access error.
     */
    T execute(Connection connection) throws SQLException;
}
