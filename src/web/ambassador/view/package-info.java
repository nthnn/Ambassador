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
 * Provides classes and interfaces for rendering dynamic web content and structuring
 * server-side views in the Ambassador framework. This package is designed to generate HTML, JSON,
 * and other types of web content directly from Java, enabling developers to build views
 * programmatically without relying on external templating engines.
 *
 * The core abstraction in this package is the {@code ViewContent} interface,
 * which represents any renderable content, including HTML, JSON, plain text, and empty responses.
 * Classes implementing this interface can produce byte arrays for efficient transmission over HTTP,
 * and specify MIME types to define how clients interpret the response.
 *
 * <p><b>Key Features</b></p>
 * <ul>
 *     <li>Unified Content Model – All content types implement {@code ViewContent}, providing
 *     a consistent way to handle various response formats (HTML, JSON, etc.).</li>
 *     <li>Dynamic HTML Generation – The {@code Dom} class models HTML
 *     elements as Java objects, supporting dynamic DOM construction with fluent APIs.</li>
 *     <li>JSON Rendering – The {@code JsonContent} class wraps JSON strings
 *     or maps, allowing seamless serialization and rendering of JSON responses.</li>
 *     <li>Asset Rendering – The {@code AssetContent} class serves static assets
 *     from byte arrays, enabling efficient caching and response handling for images, scripts, and CSS files.</li>
 *     <li>Fallback View – {@code EmptyView} provide minimal or empty responses, useful for
 *     status pages or placeholder content.</li>
 * </ul>
 *
 * <p><b>Example Usage</b></p>
 * <p>Constructing a basic HTML page using the {@code Dom} class:</p>
 *
 * ```java
 * Dom page = Dom.createPage(
 *     Dom.div(
 *         Dom.h1("Welcome to Ambassador"),
 *         Dom.p("Generate HTML using Java!")
 *     )
 * );
 *
 * String htmlOutput = page.toString();
 * byte[] bytes = page.render();
 * ```
 *
 * <p>Rendering a JSON response using {@code JsonContent}:</p>
 * ```java
 * Map<String, Object> data = new HashMap&lt;&gt;();
 * data.put("name", "John Doe");
 * data.put("age", 30);
 *
 * JsonContent jsonResponse = JsonContent.fromMap(data);
 * byte[] jsonBytes = jsonResponse.render();
 * ```
 *
 * <h2>Design Goals</h2>
 * <p>The {@code web.ambassador.view} package is designed with the following goals:</p>
 * <ul>
 *     <li>Flexibility – Support diverse content types (HTML, JSON, plain text) for a variety
 *     of web applications.</li>
 *     <li>Performance – Caching mechanisms within content classes minimize redundant rendering,
 *     enhancing response times.</li>
 *     <li>Simplicity – A fluent API and intuitive class design reduce boilerplate code for
 *     web view creation.</li>
 *     <li>Maintainability – Clear separation of responsibilities between HTML rendering, JSON serialization,
 *     and asset management.</li>
 * </ul>
 *
 * <h2>Integration</h2>
 * <p>The {@code Component} interface allows for the integration of view content with HTTP request handling,
 * enabling the seamless generation of views based on user input and database queries.</p>
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @see web.ambassador.view.ViewContent
 * @see web.ambassador.view.Component
 * @see web.ambassador.view.AssetContent
 * @see web.ambassador.view.JsonContent
 * @see web.ambassador.view.PlainText
 * @see web.ambassador.view.EmptyView
 * @see web.ambassador.view.Dom
 */
package web.ambassador.view;
