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
 * Core components and infrastructure for the Ambassador web server framework.
 *
 * The `web.ambassador.core` package provides the essential classes and mechanisms to build and manage
 * a lightweight, high-performance HTTP server. This package serves as the backbone for the Ambassador
 * web framework, facilitating server configuration, request routing, error handling, and lifecycle
 * management.
 *
 * <h2>Overview</h2>
 *
 * This package defines the central Kernel class, which orchestrates the initialization and management
 * of the HTTP server. It integrates components such as routing, database management, and custom error
 * handling to deliver a comprehensive and extensible web server environment.
 *
 * The core responsibilities of this package include:
 * <ul>
 *     <li>Bootstrapping and configuring the HTTP server.</li>
 *     <li>Managing request routing through dynamic controller registration.</li>
 *     <li>Handling errors and providing customizable error controllers.</li>
 *     <li>Integrating with the database layer through DatabaseManager.</li>
 *     <li>Ensuring graceful shutdown and resource management using the Cleaner API.</li>
 * </ul>
 *
 * <h2>Key Classes</h2>
 *
 * <h3>Kernel</h3>
 * <ul>
 *     <li>The main entry point for configuring and starting the HTTP server.</li>
 *     <li>Responsible for initializing thread pools, binding server ports, and managing the request lifecycle.</li>
 *     <li>Implements {@code java.lang.AutoCloseable} to ensure proper resource cleanup.</li>
 *     <li>Supports custom shutdown hooks, error controllers, and dynamic route registration.</li>
 * </ul>
 *
 * <h3>Router</h3>
 * <ul>
 *     <li>Facilitates the registration and mapping of controller classes to specific HTTP routes.</li>
 *     <li>Handles dispatching of requests to the appropriate controller methods.</li>
 *     <li>Central to the Ambassador framework's ability to handle RESTful APIs dynamically.</li>
 * </ul>
 *
 * <h3>KernelConfig</h3>
 * <ul>
 *     <li>Provides a convenient configuration object for initializing the Kernel with custom settings.</li>
 *     <li>Allows easy specification of port numbers, thread pools, database connections, and backlog sizes.</li>
 * </ul>
 *
 * <h3>DatabaseManager</h3>
 * <ul>
 *     <li>Handles database connectivity and connection pooling.</li>
 *     <li>Seamlessly integrates with Kernel to provide persistent storage support.</li>
 *     <li>Manages lifecycle operations, including connection establishment and graceful shutdown.</li>
 * </ul>
 *
 * <h3>ErrorController</h3>
 * <ul>
 *     <li>Represents a base class for handling error responses for different HTTP status codes.</li>
 *     <li>Developers can extend this class to create custom error pages or JSON error responses.</li>
 * </ul>
 *
 * <h3>DefaultErrorController</h3>
 * <ul>
 *     <li>A fallback error controller used when no specific error handler is registered.</li>
 *     <li>Provides basic HTTP error responses for common error scenarios.</li>
 * </ul>
 *
 * <p><b>Usage</b></p>
 * The following example demonstrates how to create and start a simple HTTP server using the Kernel:
 *
 * ```java
 * Kernel kernel = new Kernel(8080);
 * kernel.registerController(MyController.class);
 * kernel.setupShutdownHook(
 *     () -> System.out.println("Server stopped successfully."),
 *     () -> System.err.println("Server shutdown failed.")
 * );
 * kernel.start();
 * ```
 *
 * <h2>Shutdown and Resource Management</h2>
 *
 * The Kernel class ensures that resources are released correctly during shutdown by implementing the
 * {@code java.lang.AutoCloseable} interface. This allows the Kernel to be used in try-with-resources
 * blocks to guarantee safe shutdown even in the presence of exceptions.
 *
 * <p><b>Example:</b></p>
 * ```java
 * try (Kernel kernel = new Kernel(8080)) {
 *     kernel.start();
 * }
 * // Server shuts down automatically
 * ```
 *
 * <h2>Error Handling</h2>
 * Developers can register custom error handlers to manage specific HTTP status codes.
 * This enables greater flexibility in defining server responses for various error conditions.
 *
 * <p><b>Example:</b></p>
 * ```java
 * kernel.setErrorController(404, new NotFoundErrorController());
 * kernel.setErrorController(500, new InternalServerErrorController());
 * ```
 *
 * <h2>Extensibility</h2>
 *
 * The package is designed to be extensible by allowing developers to create and register their own
 * controllers, error handlers, and database configurations. This makes it suitable for building
 * large-scale web applications with custom requirements.
 *
 * <h2>Concurrency and Scalability</h2>
 *
 * The Kernel utilizes a thread pool for concurrent request processing. By default, it scales with
 * the number of available processors, ensuring efficient handling of multiple requests simultaneously.
 * This behavior can be adjusted using the {@link web.ambassador.core.Kernel#setMaxThreads(int)} method.
 *
 * <p><b>Example:</b></p>
 * ```java
 * kernel.setMaxThreads(100);
 * ```
 *
 * <h2>Notes</h2>
 *
 * <ul>
 *     <li>This package does not handle HTTPS directly. SSL/TLS support must be implemented separately.</li>
 *     <li>This package assumes a basic understanding of HTTP and RESTful API development.</li>
 * </ul>
 * <br/>
 * This package is part of the larger Ambassador framework, which provides a set of tools for building web
 * applications and handling HTTP communication. It offers a flexible way to handle the complexities of HTTP
 * interactions in a modular and easy-to-understand manner.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
package web.ambassador.core;
