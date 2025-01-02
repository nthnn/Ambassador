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
package web.ambassador.util;

/**
 * Exception thrown to indicate an error during JSON parsing.
 *
 * This exception extends {@code RuntimeException}, allowing it to be thrown
 * without requiring explicit handling or declaration. It is typically used
 * when parsing JSON strings into Java objects fails due to syntax errors,
 * unexpected tokens, or invalid JSON structure.
 *
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class JsonParseException extends RuntimeException {
    /**
     * Constructs a new {@code JsonParseException} with the specified
     * detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public JsonParseException(String message) {
        super(message);
    }
}
