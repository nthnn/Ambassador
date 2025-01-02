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
package web.ambassador.enums;

/**
 * Enum representing the various HTTP methods supported by the Ambassador framework.
 * <p>
 * This enum defines the standard HTTP methods used to indicate the desired action
 * to be performed for a given resource. It is commonly used in request handling
 * to specify the type of operation being performed.
 * </p>
 *
 * <p><b>Supported Methods:</b></p>
 * <ul>
 *     <li>{@code GET} - Retrieve information from the server (read-only)</li>
 *     <li>{@code POST} - Submit data to be processed (create or update)</li>
 *     <li>{@code PUT} - Update an existing resource</li>
 *     <li>{@code DELETE} - Remove a resource</li>
 *     <li>{@code PATCH} - Partially update a resource</li>
 *     <li>{@code CONNECT} - Establish a tunnel to the server</li>
 *     <li>{@code HEAD} - Retrieve metadata of a resource (headers only)</li>
 *     <li>{@code OPTIONS} - Describe communication options for the target resource</li>
 *     <li>{@code TRACE} - Perform a message loop-back test along the path to the resource</li>
 * </ul>
 *
 * This enum is useful for routing, request processing, and building RESTful APIs,
 * ensuring that the correct HTTP method is used for the desired operation.
 *
 * <p><b>Example Usage:</b></p>
 * ```java
 *     @Method(MethodType.GET)
 *     public ViewContent index(Request request, Response response, DatabaseManager dbManager) {
 *         // Your index's code here...
 *         return null;
 *     }
 * ```
 *
 * @author  <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since   1.0
 */
public enum MethodType {
    /**
     * Requests a representation of the specified resource.
     * This method is used to retrieve data from the server.
     */
    GET,

    /**
     * Submits data to the specified resource.
     * Often used to create a new resource or to submit data that requires processing.
     */
    POST,

    /**
     * Replaces all current representations of the specified resource with the provided data.
     * Used to update or create a resource at the specified URL.
     */
    PUT,

    /**
     * Deletes the specified resource.
     * This method is used to remove a resource from the server.
     */
    DELETE,

    /**
     * Partially modifies the resource.
     * Used to update or modify a resource with a subset of data.
     */
    PATCH,

    /**
     * Establishes a tunnel to the server identified by the target resource.
     * Typically used for establishing a connection to a web server using a proxy.
     */
    CONNECT,

    /**
     * Requests the headers of the specified resource, but without the body content.
     * Often used to gather metadata about a resource, such as the length of the content.
     */
    HEAD,

    /**
     * Describes the communication options for the target resource.
     * Used to query the available methods and capabilities supported by the server for a specific resource.
     */
    OPTIONS,

    /**
     * Performs a diagnostic trace of the path a request takes to the target resource.
     * This method is useful for debugging and tracing the request-response cycle.
     */
    TRACE
}
