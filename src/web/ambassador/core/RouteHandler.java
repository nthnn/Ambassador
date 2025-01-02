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
package web.ambassador.core;

import web.ambassador.db.DatabaseManager;
import web.ambassador.http.Request;
import web.ambassador.http.Response;
import web.ambassador.view.Dom;
import web.ambassador.view.ViewContent;

import java.lang.reflect.Method;

/**
 * The {@code RouteHandler} class is responsible for invoking controller methods
 * that handle HTTP requests, providing a bridge between incoming requests and
 * the corresponding business logic in a web application.
 *
 * It leverages reflection to instantiate controller classes and call handler methods
 * dynamically. This design allows for flexible and decoupled routing and controller
 * handling.
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *     <li>Instantiate the appropriate controller.</li>
 *     <li>Invoke the handler method with request, response, and database context.</li>
 *     <li>Return a {@code ViewContent} object representing the rendered view.</li>
 *     <li>Handle errors gracefully by returning a 500 Internal Server Error response.</li>
 * </ul>
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class RouteHandler {
    private final Class<?> controllerClass;
    private final Method handlerMethod;

    /**
     * Constructs a {@code RouteHandler} with a target controller and handler method.
     *
     * @param controllerClass The class of the controller that contains the handler method.
     * @param handlerMethod   The specific method to handle the request.
     */
    public RouteHandler(Class<?> controllerClass, Method handlerMethod) {
        this.controllerClass = controllerClass;
        this.handlerMethod = handlerMethod;
    }

    /**
     * Handles an incoming HTTP request by invoking the associated controller method.
     *
     * This method performs the following steps:
     * <ul>
     *     <li>Instantiates the controller using reflection.</li>
     *     <li>Invokes the handler method with {@code Request}, {@code Response},
     *     and {@code DatabaseManager} as arguments.</li>
     *     <li>Returns the resulting {@code ViewContent} object representing the
     *     rendered HTML view or API response.</li>
     * </ul>
     *
     * If an exception occurs during method invocation, it sets the response status
     * to 500 and returns an error page with the exception message.
     *
     * @param request   The HTTP request object.
     * @param response  The HTTP response object.
     * @param dbManager The database manager providing data access.
     * @return A {@code ViewContent} representing the result of the request
     *         (either the actual view or an error page).
     */
    public ViewContent handle(Request request, Response response, DatabaseManager dbManager) {
        try {
            Object controller = controllerClass.getDeclaredConstructor()
                .newInstance();

            return (ViewContent) handlerMethod.invoke(
                controller,
                request,
                response,
                dbManager
            );
        }
        catch(Exception e) {
            response.setStatusCode(500);
            return Dom.div(
                Dom.h1("500 Internal Server Error"),
                Dom.p("<p>" + e.getMessage() + "</p>")
            );
        }
    }
}
