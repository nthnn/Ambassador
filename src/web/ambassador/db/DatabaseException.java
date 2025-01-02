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

/**
 * Custom exception class used for database-related errors.
 *
 * This exception is thrown when there is an issue with database operations, such as
 * connection failures, query execution errors, or transaction issues.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 * @see web.ambassador.db.DatabaseManager
 */
public class DatabaseException extends RuntimeException {
    /**
     * Constructs a new {@code DatabaseException} with the specified detail message.
     *
     * @param message The detail message that explains the reason for the exception.
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code DatabaseException} with the specified detail message and cause.
     *
     * @param message The detail message that explains the reason for the exception.
     * @param cause The cause of the exception (a {@code Throwable} that triggered this exception).
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
