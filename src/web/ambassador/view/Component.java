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
package web.ambassador.view;

import web.ambassador.db.DatabaseManager;
import web.ambassador.http.Request;
import web.ambassador.http.Response;

/**
 * Represents a web component responsible for handling HTTP requests
 * and generating a corresponding view content.
 *
 * <p>The {@code Component} interface defines the contract for all components
 * within the web application that generate view content in response to HTTP
 * requests. It serves as an abstraction for various types of views that might
 * be rendered, such as HTML pages, JSON responses, or other types of content.
 * Implementing classes are required to provide their own logic for handling
 * incoming requests and generating the appropriate content.</p>
 *
 * <p>Components are typically used to define the structure and content of the
 * response based on the incoming {@code Request}, allowing the application to
 * respond dynamically to user interactions. The {@code DatabaseManager} is also
 * provided to allow access to database operations within the context of handling
 * the request.</p>
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @see web.ambassador.http.Request
 * @see web.ambassador.http.Response
 * @see web.ambassador.db.DatabaseManager
 * @since 1.0
 * @version 1.0
 */
public interface Component {
    /**
     * Handles an HTTP request, generates the appropriate response content,
     * and interacts with the database if needed.
     *
     * This method is invoked when an HTTP request is made to a specific
     * route or endpoint that corresponds to the component. It processes the
     * incoming request, potentially interacts with the database through the
     * {@code DatabaseManager}, and generates a {@code ViewContent} object that
     * represents the content to be returned in the HTTP response.
     *
     * @param request The incoming HTTP request containing parameters, headers,
     *                and other relevant data.
     * @param response The HTTP response object used to set the response status,
     *                 headers, and content.
     * @param dbManager A {@code DatabaseManager} instance for accessing the
     *                  application's database.
     * @return A {@code ViewContent} object that contains the content to be
     *         returned in the HTTP response, such as HTML, JSON, etc.
     */
    ViewContent index(
        Request request,
        Response response,
        DatabaseManager dbManager
    );
}
