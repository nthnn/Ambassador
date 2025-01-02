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

/**
 * Represents content that can be rendered as part of a web response.
 *
 * The {@code ViewContent} interface defines the contract for all types
 * of content that can be returned as part of an HTTP response. Implementing
 * classes are responsible for providing the content to be sent back to the
 * client, as well as the MIME type of the content to correctly identify the
 * format of the response.
 *
 * Typically, {@code ViewContent} implementations might include HTML pages,
 * JSON responses, or other types of media such as images or text files.
 * The interface provides methods to render the content in its raw form and
 * to specify the MIME type to ensure that the response is handled correctly
 * by the client (e.g., browser, mobile device, etc.).
 *
 * @see web.ambassador.view.Component
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 */
public interface ViewContent {
    /**
     * Renders the content to be returned in the HTTP response.
     *
     * This method is responsible for generating the actual raw content of
     * the response, which could be in various formats (e.g., HTML, JSON, etc.).
     * The content is returned as a byte array, which allows for flexibility
     * in how the content is represented (e.g., binary data, encoded text, etc.).
     *
     * @return A byte array representing the rendered content, ready to be sent
     *         as the HTTP response body.
     */
    byte[] render();

    /**
     * Returns the MIME type of the content.
     *
     * The MIME type specifies the format of the response content and helps
     * the client interpret the data correctly. For example, {@code "text/html"}
     * indicates that the response content is an HTML page, while {@code "application/json"}
     * indicates that the content is in JSON format.
     *
     * @return A string representing the MIME type of the content (e.g., "text/html", "application/json").
     */
    String mimeType();
}
