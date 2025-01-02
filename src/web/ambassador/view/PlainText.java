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
 * Represents plain text content that can be rendered as part of an HTTP response.
 *
 * The {@code PlainText} class implements the {@code ViewContent} interface and is responsible
 * for managing plain text responses. It stores text content as a string and provides methods
 * to render the content as a byte array in UTF-8 encoding. The class caches the rendered byte
 * array to optimize performance by avoiding redundant conversions.
 *
 * This class is suitable for returning simple text-based responses such as status messages,
 * notifications, or plain textual data that does not require complex serialization or formatting.
 *
 * @see web.ambassador.view.ViewContent
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 */
public class PlainText implements ViewContent {
    /** Cached byte array to avoid redundant rendering. */
    private byte[] cache = null;

    /** The actual plain text content as a string. */
    private final String textContent;

    /**
     * Constructs a new {@code PlainText} instance with the specified text content.
     *
     * @param textContent The plain text content to be rendered.
     */
    public PlainText(String textContent) {
        this.textContent = textContent;
    }

    /**
     * Renders the plain text content as a byte array in UTF-8 encoding.
     *
     * If the content has already been cached, it returns the cached byte array. Otherwise,
     * it converts the text string into a byte array using UTF-8 encoding, stores it in the cache,
     * and then returns it.
     *
     * @return A byte array representing the plain text content.
     */
    @Override
    public byte[] render() {
        if(this.cache == null)
            this.cache = this.toString()
                .getBytes(StandardCharsets.UTF_8);

        return this.cache;
    }

    /**
     * Returns the MIME type for plain text content.
     *
     * The MIME type returned by this method is {@code "text/plain"}, indicating that the
     * response is plain text without any formatting or markup.
     *
     * @return A string representing the MIME type for plain text, which is {@code "text/plain"}.
     */
    @Override
    public String mimeType() {
        return "text/plain";
    }

    /**
     * Returns the string representation of the plain text content.
     *
     * This method returns the raw text string passed to the constructor, allowing the content
     * to be retrieved in its original form.
     *
     * @return The plain text content as a string.
     */
    @Override
    public String toString() {
        return this.textContent;
    }
}
