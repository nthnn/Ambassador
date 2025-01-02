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
package web.ambassador.http;

import web.ambassador.enums.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Response} class represents an HTTP response that contains headers,
 * a status code, and a body. It is used for constructing HTTP responses in a
 * web application or server environment.
 *
 * The class allows chaining of methods to fluently configure the status code,
 * headers, and body of the response. It also provides methods for setting and
 * deleting cookies within the response headers.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class Response {
    /** A map of headers associated with the HTTP response. */
    private final Map<String, String> headers;

    /** The HTTP status code of the response. */
    private int statusCode;

    /** The body of the response. */
    private String body;

    /**
     * Constructs a new {@code Response} object with a default status code of 200 (OK).
     * Initializes the headers as an empty map.
     */
    public Response() {
        this.headers = new HashMap<>();
        this.statusCode = 200;
    }

    /**
     * Sets the status code of the response.
     *
     * This method allows setting the HTTP status code as an integer.
     *
     * @param statusCode the HTTP status code to be set (e.g., 200, 404, 500).
     * @return the current {@code Response} object, allowing for method chaining.
     */
    public Response withStatus(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * Sets the status code of the response using a predefined {@code HttpStatusCode}.
     *
     * This method allows setting the HTTP status code using an {@code HttpStatusCode}
     * enum value, which provides named status codes for better readability and maintainability.
     *
     * @param statusCode the HTTP status code from the {@code HttpStatusCode} enum.
     * @return the current {@code Response} object, allowing for method chaining.
     */
    public Response withStatus(HttpStatusCode statusCode) {
        this.statusCode = statusCode.getStatusCode();
        return this;
    }

    /**
     * Adds a header to the response.
     *
     * This method allows setting arbitrary headers as key-value pairs.
     * If the header already exists, its value will be overwritten.
     *
     * @param name  the name of the header (e.g., "Content-Type").
     * @param value the value of the header (e.g., "application/json").
     * @return the current {@code Response} object, allowing for method chaining.
     */
    public Response withHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * Sets the body content of the response.
     *
     * This method allows setting the body of the response as a string.
     * The body typically contains the data to be sent in the response body.
     *
     * @param body the body content of the response (e.g., HTML, JSON, etc.).
     * @return the current {@code Response} object, allowing for method chaining.
     */
    public Response withBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Retrieves the status code of the response.
     *
     * @return the HTTP status code of the response (e.g., 200, 404, 500).
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * Sets the status code of the response.
     *
     * This method allows setting the HTTP status code directly as an integer.
     *
     * @param statusCode the HTTP status code to be set (e.g., 200, 404, 500).
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Sets the status code of the response using a predefined {@code HttpStatusCode}.
     * <p>
     * This method allows setting the HTTP status code using an {@code HttpStatusCode}
     * enum value, which provides named status codes for better readability and maintainability.
     *
     * @param statusCode the HTTP status code from the {@code HttpStatusCode} enum.
     */
    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode.getStatusCode();
    }

    /**
     * Retrieves the headers of the response.
     *
     * This method returns a map of the headers currently set in the response.
     *
     * @return a {@code Map} containing the headers, where the keys are header names
     *         and the values are header values.
     */
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    /**
     * Retrieves the body of the response.
     * This method returns the body content as a string.
     *
     * @return the body of the response.
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Sets the body content of the response.
     * This method allows directly modifying the body content of the response.
     *
     * @param body the body content of the response (e.g., HTML, JSON, etc.).
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Adds a cookie to the response headers.
     * This method sets a cookie with a specific name and value in the response headers.
     *
     * @param name  the name of the cookie (e.g., "sessionId").
     * @param value the value of the cookie (e.g., "12345").
     */
    public void setCookie(String name, String value) {
        String cookie = name + "=" + value;
        this.headers.put("Set-Cookie", cookie);
    }

    /**
     * Deletes a cookie by setting its expiration date in the past.
     *
     * This method removes the specified cookie from the response by setting
     * its "expires" attribute to a past date, causing the cookie to be deleted.
     *
     * @param name the name of the cookie to be deleted (e.g., "sessionId").
     */
    public void deleteCookie(String name) {
        String cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        this.headers.put("Set-Cookie", cookie);
    }
}
