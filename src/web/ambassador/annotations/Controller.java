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

import web.ambassador.http.Request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a class as a web controller in the Ambassador framework.
 *
 * This annotation is used to define classes that handle HTTP requests. The path specified
 * in the annotation determines the base URI for all request mappings within the controller.
 * By default, the base path is set to "/", meaning it will handle requests at the root level.
 *
 * **Usage Example:**
 * ```java
 * @Controller(path = "/api")
 * public class ApiController implements Component {
 *     // Methods to handle requests
 * }
 * ```
 *
 * Classes annotated with {@code @Controller} can define methods to handle specific HTTP routes,
 * making it easier to structure web applications by grouping related endpoints.
 *
 * Path parameters can also be defined between curly braces ('{' and '}'), this path parameter can
 * later on be retrieved via the {@code web.ambassador.http.Request.getPathParams()}. For example:
 *
 * ```java
 * @Controller(path = "/api/{action}/{name}")
 * public class ApiController implements Component {
 *     @Method(MethodType.GET)
 *     public ViewContent index(Request request, Response response, DatabaseManager dbManager) {
 *         String action = request.getPathParam("action");
 *         String name = request.getPathParam("name");
 *         // ...
 *     }
 * }
 * ```
 *
 * @author  <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
    /**
     * Specifies the base path for this controller.
     *
     * @return the base URI path for the controller (default is "/")
     */
    String path() default "/";
}
