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
 * The {@code DefaultErrorController} is a default implementation of the {@code ErrorController} interface.
 * It is responsible for handling HTTP errors and providing appropriate error messages and responses.
 * This implementation maps standard HTTP status codes to their corresponding messages and
 * returns an HTML representation of the error using a DOM structure.
 * <br/>
 * When an error occurs in the web application, this controller generates a response that
 * includes the HTTP status code and a message that describes the error. The response is
 * typically displayed to the user in the browser or logged for debugging purposes.
 * <br/>
 * The controller maps error codes (such as 404 for "Not Found" or 500 for "Internal Server Error")
 * to their respective messages. If a status code is unrecognized, it returns a generic error message.
 *
 * <p><b>Usage Example:</b></p>
 * ```java
 *     DefaultErrorController errorController = new DefaultErrorController();
 *     Dom errorPage = errorController.handleError(404, request, response);
 *     // Render or return the generated error page
 * ```
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class DefaultErrorController implements ErrorController {
    /**
     * Handles HTTP errors by generating an HTML error page with the status code and
     * a corresponding error message.
     *
     * This method takes the HTTP status code, request, and response objects,
     * maps the status code to a standard message, and returns a DOM representation
     * of the error page.
     *
     * The method uses a switch statement to map the provided status code to a
     * corresponding message. If the status code is not explicitly listed, it defaults
     * to a generic "Error" message followed by the status code.
     *
     * @param statusCode The HTTP status code representing the error (e.g., 404 for "Not Found").
     * @param request The HTTP request that caused the error. This can be used to inspect the request context.
     * @param response The HTTP response that will be sent to the client. This can be used to set response headers.
     * @return A {@code Dom} object representing the error page, which includes the status code and message.
     */
    @Override
    public Dom handleError(int statusCode, Request request, Response response) {
        String message = switch(statusCode) {
            case 100 -> "Continue";
            case 101 -> "Switching Protocols";
            case 102 -> "Processing";
            case 103 -> "Early Hints";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 203 -> "Non-Authoritative Information";
            case 204 -> "No Content";
            case 205 -> "Reset Content";
            case 206 -> "Partial Content";
            case 207 -> "Multi-Status";
            case 208 -> "Already Reported";
            case 226 -> "IM Used";
            case 300 -> "Multiple Choices";
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 303 -> "See Other";
            case 304 -> "Not Modified";
            case 305 -> "Use Proxy";
            case 306 -> "Unused";
            case 307 -> "Temporary Redirect";
            case 308 -> "Permanent Redirect";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 402 -> "Payment Required";
            case 403 -> "Forbidden";
            case 404 -> "Page Not Found";
            case 405 -> "Method Not Allowed";
            case 406 -> "Not Acceptable";
            case 407 -> "Proxy Authentication Required";
            case 408 -> "Request Timeout";
            case 409 -> "Conflict";
            case 410 -> "Gone";
            case 411 -> "Length Required";
            case 412 -> "Precondition Failed";
            case 413 -> "Content Too Large";
            case 414 -> "URI Too Long";
            case 415 -> "Unsupported Media Type";
            case 416 -> "Range Not Satisfiable";
            case 417 -> "Expectation Failed";
            case 418 -> "I'm A Teapot";
            case 421 -> "Misdirected Request";
            case 422 -> "Unprocessable Content";
            case 423 -> "Locked";
            case 424 -> "Failed Dependency";
            case 425 -> "Too Early";
            case 426 -> "Upgrade Required";
            case 428 -> "Precondition Required";
            case 429 -> "Too Many Requests";
            case 431 -> "Request Header Fields Too Large";
            case 451 -> "Unavailable For Legal Reasons";
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            case 505 -> "HTTP Version Not Supported";
            case 506 -> "Variant Also Negotiates";
            case 507 -> "Insufficient Storage";
            case 508 -> "Loop Detected";
            case 510 -> "Not Extended";
            case 511 -> "Network Authentication Required";
            default -> "Error " + statusCode;
        };

        return Dom.div(
            Dom.h1(String.valueOf(statusCode)),
            Dom.p(message)
        );
    }
}
