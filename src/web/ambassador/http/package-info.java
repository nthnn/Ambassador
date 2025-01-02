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
 * Provides classes and utilities for handling HTTP-related operations within the Ambassador web framework.
 *
 * This package includes classes for managing HTTP requests, responses, and file uploads, as well as enums
 * for defining HTTP status codes. It facilitates the handling of common HTTP tasks such as setting headers,
 * status codes, cookies, and managing the content and metadata of uploaded files.
 *
 * The classes in this package are designed to assist in building web applications and services, providing
 * a high-level abstraction over HTTP communication. Whether working with raw HTTP responses, handling multipart
 * file uploads, or interacting with HTTP status codes, this package provides the necessary tools to perform
 * these operations efficiently.
 *
 * <h2>Classes Overview:</h2>
 * <ul>
 *     <li>{@link web.ambassador.http.Request} - Represents an HTTP request, encapsulating various elements
 *         such as the request path, query parameters, POST data, headers, cookies, and uploaded files.</li>
 *     <li>{@link web.ambassador.http.Response} - Represents an HTTP response, allowing you to configure the
 *         response status, headers, body, and cookies.</li>
 *     <li>{@link web.ambassador.http.UploadedFile} - Represents a file uploaded via an HTTP request,
 *         containing metadata such as the field name, filename, content type, and the file content.</li>
 * </ul>
 *
 * This package is part of the larger Ambassador framework, which provides a set of tools for building web
 * applications and handling HTTP communication. It offers a flexible way to handle the complexities of HTTP
 * interactions in a modular and easy-to-understand manner.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 */
package web.ambassador.http;
