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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A functional interface for mapping rows of a {@code ResultSet} to an object of type {@code T}.
 *
 * This interface provides a way to map each row of the {@code ResultSet} to a specific object. It is commonly used
 * in JDBC-based data access layers where each row of a query result is mapped to a Java object. The implementation
 * of this interface can be customized to map the rows according to the specific needs of the application.
 * <br/>
 * The method {@link #mapRow(ResultSet)} will be called for each row in the {@link ResultSet}, and it should
 * return an instance of the object type {@code T} representing the mapped data from the row.
 *
 * @param <T> The type of object that each row should be mapped to.
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 * @see web.ambassador.db.QueryBuilder
 * @see web.ambassador.db.DatabaseManager
 */
public interface RowMapper<T> {
    /**
     * Maps a single row of the {@code ResultSet} to an object of type {@code T}.
     *
     * This method is invoked for each row in the {@code ResultSet}. It is responsible for extracting the necessary
     * data from the {@code ResultSet} and converting it into an object of type {@code T}.
     *
     * @param rs The {@code ResultSet} containing the current row of data to be mapped.
     * @return The object of type {@code T} representing the mapped row data.
     * @throws SQLException If an error occurs while accessing the data in the {@code ResultSet}.
     */
    T mapRow(ResultSet rs) throws SQLException;
}
