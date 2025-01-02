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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import web.ambassador.db.DatabaseManager;
import web.ambassador.enums.MethodType;
import web.ambassador.http.Request;
import web.ambassador.http.Response;
import web.ambassador.view.ViewContent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * The {@code KernelHandler} class is responsible for handling incoming HTTP requests and returning appropriate responses.
 *
 * This class implements the {@code HttpHandler} interface and is part of the server's request handling pipeline. It interacts with the {@code Router},
 * which is used to process the request and determine the correct view/content to return. Additionally, it manages the database connection
 * through the {@code DatabaseManager} and handles error responses using the provided {@code ErrorController} implementations.
 *
 * The {@code KernelHandler} class is designed to handle various HTTP methods (e.g., GET, POST), parse request headers, cookies, and query parameters,
 * and generate corresponding responses. If an error occurs during request processing, it will handle the error gracefully by using the appropriate
 * error controller.
 *
 * In the event of an exception or error, a default error handler is used if no specific error controller is provided for the given HTTP status code.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class KernelHandler implements HttpHandler {
    private Router router = null;
    private DatabaseManager dbManager = null;
    private Map<Integer, ErrorController> errorControllers = null;

    /**
     * Private constructor for creating a {@code KernelHandler} instance.
     */
    protected KernelHandler() { }

    /**
     * Static method to build a new {@code KernelHandler} instance.
     *
     * This method is used to initialize the handler with the required components such as the {@code Router},
     * {@code DatabaseManager}, and error controllers.
     *
     * @param router The router used for handling requests and determining view/content.
     * @param dbManager The database manager used for interacting with the database.
     * @param errorControllers A map of error codes to their respective error controllers.
     * @return A {@code KernelHandler} instance.
     */
    public static KernelHandler build(
        Router router,
        DatabaseManager dbManager,
        Map<Integer, ErrorController> errorControllers
    ) {
        KernelHandler handler = new KernelHandler();
        handler.router = router;
        handler.dbManager = dbManager;
        handler.errorControllers = errorControllers;

        return handler;
    }

    /**
     * Handles an incoming HTTP request by parsing the request details, processing the request using the router,
     * and returning an appropriate response.
     *
     * This method performs the following steps:
     * <ol>
     *   <li>Creates a new {@code Request} and {@code Response} object.</li>
     *   <li>Parses request details such as path, method, headers, cookies, query parameters, and body (for POST requests).</li>
     *   <li>Processes the request using the {@code Router} to determine the appropriate view or content.</li>
     *   <li>If the response status code is not 200, an error response is generated using the appropriate error controller.</li>
     *   <li>Writes the response to the HTTP exchange, including headers and response body.</li>
     * </ol>
     *
     * In case of an exception during request processing, the method will respond with a generic 500 Internal Server Error,
     * using the error controller if defined.
     *
     * @param exchange The {@code HttpExchange} object representing the incoming HTTP request and response.
     * @throws IOException If an I/O error occurs while handling the request or response.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Request request = new Request();
        Response response = new Response();
        InputStream requestBody = null;

        try {
            request.setPath(exchange.getRequestURI().getPath());
            request.setMethod(MethodType.valueOf(exchange.getRequestMethod()));

            exchange.getRequestHeaders().forEach((key, values) ->
                request.getHeaders().put(key, values.getFirst())
            );
            request.parseCookies();

            String query = exchange.getRequestURI().getQuery();
            request.parseQueryString(query);

            if(exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                requestBody = exchange.getRequestBody();
                request.setBody(requestBody);
                request.parseMultipartData();
            }

            byte[] responseBytes;
            ViewContent result = this.router.handleRequest(
                request,
                response,
                this.dbManager
            );

            if(response.getStatusCode() != 200) {
                int statusCode = response.getStatusCode();
                ErrorController controller = this.errorControllers
                    .getOrDefault(statusCode, new DefaultErrorController());
                responseBytes = controller.handleError(statusCode, request, response)
                    .render();
            }
            else responseBytes = result.render();

            request.getHeaders().entrySet().stream()
                .filter(
                    entry ->
                        entry.getKey()
                            .equalsIgnoreCase("Set-Cookie")
                )
                .forEach(
                    cookie ->
                        response.setCookie(cookie.getKey(), cookie.getValue())
                );

            exchange.getResponseHeaders().add("Content-type", result.mimeType());
            for(Map.Entry<String, String> header: response.getHeaders().entrySet())
                exchange.getResponseHeaders().add(header.getKey(), header.getValue());

            exchange.sendResponseHeaders(response.getStatusCode(), responseBytes.length);
            exchange.getResponseBody().write(responseBytes);
        }
        catch(Exception e) {
            int statusCode = response.getStatusCode();
            ErrorController controller = this.errorControllers
                .getOrDefault(statusCode, new DefaultErrorController());
            byte[] errorBytes = controller.handleError(statusCode, request, response)
                .render();

            response.setStatusCode(500);
            exchange.sendResponseHeaders(500, errorBytes.length);
            exchange.getResponseBody().write(errorBytes);
        }
        finally {
            if(requestBody != null)
                try {
                    requestBody.close();
                }
                catch(IOException _) { }

            exchange.getResponseBody().close();
        }
    }
}
