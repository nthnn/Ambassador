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
package web.ambassador.annotations;

import web.ambassador.enums.MethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define the HTTP method type for a handler method in the Ambassador framework.
 *
 * This annotation is used to specify the HTTP method (GET, POST, PUT, DELETE, etc.)
 * that a particular method in a controller should respond to. By default, if no value
 * is provided, the method will handle HTTP GET requests.
 *
 * <p><b>Example Usage:</b></p>
 * ```java
 * @Controller(path = "/users")
 * public class UserController implements Component{
 *     @Method(MethodType.GET)
 *     public ViewContent index(Request request, Response response, DatabaseManager dbManager) {
 *         // Logic to fetch user list
 *     }
 *
 *     @Method(MethodType.POST)
 *     public ViewContent createUser(Request request, Response response, DatabaseManager dbManager) {
 *         // Logic to create a new user
 *     }
 * }
 * ```
 *
 * This annotation works in conjunction with the {@code Controller} annotation to define
 * RESTful endpoints. It helps map methods to specific HTTP methods, allowing the framework
 * to route requests appropriately.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 * @see Controller
 * @see web.ambassador.enums.MethodType
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Method {
    /**
     * The HTTP method type that the annotated method should handle.
     * By default, the method will handle GET requests.
     *
     * @return the HTTP method type (e.g., GET, POST, PUT)
     */
    MethodType value() default MethodType.GET;
}
