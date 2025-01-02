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

import java.nio.charset.StandardCharsets;

/**
 * Represents an empty view content, typically used as a placeholder for views that do not need to render any content.
 *
 * The {@code EmptyView} class implements the {@code ViewContent} interface and provides a simple implementation
 * for rendering empty content, such as when a view is not supposed to display any visible data to the user.
 * The class returns an empty string when rendered and has a MIME type of {@code "text/plain"}, which can be useful
 * in scenarios where a response is required, but no content needs to be displayed.
 *
 * This class employs a caching mechanism to store the result of the {@code toString()} call as a byte array
 * for subsequent requests, improving performance by avoiding redundant string-to-byte array conversions.
 *
 * @see web.ambassador.view.ViewContent
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class EmptyView implements ViewContent {
    /** Cached content to avoid redundant rendering. */
    private byte[] cache = null;

    /**
     * Constructs a new {@code EmptyView} instance.
     *
     * This constructor initializes the {@code EmptyView} without any content. It is designed to represent
     * a view with no visible data.
     */
    public EmptyView() { }

    /**
     * Renders the empty content as a byte array.
     *
     * If the content has already been cached, it returns the cached byte array. Otherwise, it calls the {@code toString}
     * method to generate the content (which is an empty string), converts it into a byte array using UTF-8 encoding,
     * stores it in the cache, and then returns it.
     *
     * @return A byte array representing the empty content (an empty string), ready to be sent as part of the HTTP response body.
     */
    @Override
    public byte[] render() {
        if(this.cache == null)
            this.cache = this.toString()
                .getBytes(StandardCharsets.UTF_8);

        return this.cache;
    }

    /**
     * Returns the MIME type of the empty content.
     *
     * Since this view represents empty content, the MIME type returned is {@code "text/plain"}. This allows the
     * client (such as a browser) to recognize that the content type is plain text, even though the content is empty.
     *
     * @return A string representing the MIME type of the empty content, which is {@code "text/plain"}.
     */
    @Override
    public String mimeType() {
        return "text/plain";
    }

    /**
     * Returns a string representation of the empty content.
     *
     * This method always returns an empty string, as the {@code EmptyView} class is designed to represent
     * a view with no content to display.
     *
     * @return An empty string.
     */
    @Override
    public String toString() {
        return "";
    }
}
