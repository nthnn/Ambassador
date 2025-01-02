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

import web.ambassador.http.Request;
import web.ambassador.http.Response;
import web.ambassador.view.Dom;

/**
 * The {@code ErrorController} interface defines the contract for handling HTTP errors within the web framework.
 *
 * Any class implementing this interface is expected to provide logic for handling various types of HTTP error status codes,
 * generating an appropriate response in the form of an HTML page or other response types. This controller is invoked when
 * an error occurs in the web application, such as when a requested resource is not found or the server encounters an
 * issue.
 *
 * The primary function of this interface is to define a method for handling error scenarios, taking in the HTTP status code,
 * the incoming request, and the response object. Implementations of this interface should return a representation of the error,
 * often in the form of a DOM structure containing the error code and message, or a more complex error page.
 *
 * <p><b>Usage Example:</b></p>
 * ```java
 * public class MyCustomErrorController implements ErrorController {
 *     @Override
 *     public Dom handleError(int statusCode, Request request, Response response) {
 *         // Handle error and return a custom error page.
 *         return Dom.div(
 *             Dom.h1("Custom Error"),
 *             Dom.p("An error occurred")
 *         );
 *     }
 * }
 * ```
 *
 * This interface enables the customization of error responses based on the HTTP status code, request, and response,
 * allowing developers to implement specific error-handling behavior.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public interface ErrorController {
    /**
     * Handles an HTTP error by processing the given status code and providing an appropriate error response.
     *
     * Implementations of this method should handle the provided status code and return an appropriate
     * response, often including an HTML representation of the error page. The {@code statusCode} will correspond to
     * the HTTP error code (e.g., 404 for "Not Found" or 500 for "Internal Server Error"). The method should ensure
     * that the response includes a meaningful message or explanation for the error.
     *
     * The implementation can customize the error response based on the {@code request} and {@code response}
     * objects. For instance, developers may want to inspect the request headers or query parameters to customize
     * the error page dynamically based on the user's request context.
     *
     * @param statusCode The HTTP status code that represents the error (e.g., 404, 500, etc.).
     * @param request The HTTP request object containing the request details. This can be used to inspect request data.
     * @param response The HTTP response object that will be sent to the client. This can be used to set response headers or content.
     * @return A {@code Dom} object representing the error page or response, often containing the status code and error message.
     */
    Dom handleError(
        int statusCode,
        Request request,
        Response response
    );
}
