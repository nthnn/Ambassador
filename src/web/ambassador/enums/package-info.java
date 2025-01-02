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
 * Contains enumerations used within the Ambassador framework to represent various HTTP status codes
 * and request method types.
 *
 * <p>This package includes two main enums:</p>
 * <ul>
 *     <li>{@link web.ambassador.enums.HttpStatusCode} - Defines common HTTP status codes, providing
 *         a standardized way to represent the responses from a server.</li>
 *     <li>{@link web.ambassador.enums.MethodType} - Defines the HTTP request methods, such as GET, POST, PUT, etc.,
 *         that determine the type of interaction the client wishes to perform with the server.</li>
 * </ul>
 *
 * <p><b>Usage:</b></p>
 * ```java
 *     HttpStatusCode status = HttpStatusCode.OK;
 *     MethodType method = MethodType.GET;
 * ```
 *
 * These enums help in creating a more readable and type-safe HTTP-based interaction in the Ambassador framework.
 *
 * This package is part of the larger Ambassador framework, which provides a set of tools for building web
 * applications and handling HTTP communication. It offers a flexible way to handle the complexities of HTTP
 * interactions in a modular and easy-to-understand manner.
 *
 * @since 1.0
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 */
package web.ambassador.enums;
