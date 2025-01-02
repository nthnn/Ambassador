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
 * Provides annotations for defining RESTful routes, HTTP method mappings, and controller behavior
 * in the Ambassador framework.
 *
 * This package contains the annotations used to define the structure of a web controller in
 * the Ambassador framework. These annotations are used to map HTTP requests to specific methods
 * and controllers, allowing the framework to handle the routing and processing of requests
 * based on the specified paths and HTTP methods.
 *
 * <p><b>Key Annotations:</b></p>
 * <ul>
 *     <li>{@link web.ambassador.annotations.Controller}: Defines a controller class and its associated base path.</li>
 *     <li>{@link web.ambassador.annotations.Method}: Specifies the HTTP method (GET, POST, etc.) that a method in a controller will handle.</li>
 * </ul>
 *
 * <p><b>Example:</b></p>
 * ```java
 * @Controller(path = "/users")
 * public class UserController {
 *     @Method(MethodType.GET)
 *     public ViewContent index(Request request, Response response, DatabaseManager dbManager) {
 *         // Fetch and return the user data
 *     }
 *
 *     @Method(MethodType.POST)
 *     public Response createUser(Request request, Response response, DatabaseManager dbManager) {
 *         // Create a new user
 *     }
 * }
 * ```
 *
 * These annotations help to declaratively define web API routes and the associated methods
 * for processing the requests. The Ambassador framework processes these annotations at runtime
 * to route HTTP requests to the correct controller methods based on the configured paths and
 * HTTP methods.
 * <br/>
 * This package is part of the larger Ambassador framework, which provides a set of tools for building web
 * applications and handling HTTP communication. It offers a flexible way to handle the complexities of HTTP
 * interactions in a modular and easy-to-understand manner.
 *
 * @author  <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @see web.ambassador.annotations.Controller
 * @see web.ambassador.annotations.Method
 */
package web.ambassador.annotations;
