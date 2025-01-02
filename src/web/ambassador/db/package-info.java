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

/**
 * Provides classes and interfaces for interacting with a relational database
 * in the Ambassador web framework.
 *
 * This package contains components that facilitate database connectivity,
 * transaction management, query construction, and execution, as well as
 * handling of database-specific exceptions. It abstracts the complexity of
 * direct SQL operations, enabling efficient and safe database interactions
 * within the Ambassador framework.
 *
 * This package simplifies interaction with relational databases by offering
 * reusable components for common database operations, reducing boilerplate
 * code and minimizing the risk of SQL injection. It also supports transaction
 * management, allowing developers to ensure that multiple database operations
 * are treated as a single, atomic unit.
 *
 * The {@code DatabaseManager} serves as the entry point for acquiring database connections,
 * while the {@code QueryBuilder} and {@code RowMapper} facilitate dynamic query generation
 * and mapping of query results to application-specific data structures.
 *
 * The {@code TransactionCallback} interface is useful for managing operations that need
 * to be executed within a transaction, offering a clean API for executing database logic.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 */
package web.ambassador.db;
