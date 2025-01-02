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

import web.ambassador.util.JsonUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Represents JSON content that can be rendered as part of an HTTP response.
 *
 * <p>The {@code JsonContent} class implements the {@code ViewContent} interface and is designed to handle
 * JSON data in the form of a string. It provides methods for rendering JSON content and returning the appropriate
 * MIME type, {@code "application/json"}. The class caches the JSON content after the first rendering to optimize
 * performance by avoiding redundant serialization.</p>
 *
 * <p>There are convenience static methods provided to easily convert a {@code Map} or {@code List} into JSON
 * content, using the {@code JsonUtil.toJson(Object)} utility class for serialization.</p>
 *
 * @see web.ambassador.view.ViewContent
 * @see web.ambassador.util.JsonUtil
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 */
public class JsonContent implements ViewContent {
    /** Cached byte array to avoid redundant rendering. */
    private byte[] cache = null;

    /** The actual JSON content as a string */
    private final String jsonContent;

    /**
     * Constructs a new {@code JsonContent} instance with the specified JSON content.
     *
     * @param jsonContent The JSON content to be rendered, as a string.
     */
    public JsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    /**
     * Creates a new {@code JsonContent} instance from a {@code Map} object by serializing it into JSON format.
     *
     * @param map A {@code Map} containing key-value pairs to be serialized into JSON.
     * @return A new {@code JsonContent} instance containing the JSON string representation of the map.
     */
    public static JsonContent fromMap(Map<String, Object> map) {
        return new JsonContent(JsonUtil.toJson(map));
    }

    /**
     * Creates a new {@code JsonContent} instance from a {@code List} of strings by serializing it into JSON format.
     *
     * @param list A {@code List} of strings to be serialized into JSON.
     * @return A new {@code JsonContent} instance containing the JSON string representation of the list.
     */
    public static JsonContent fromList(List<String> list) {
        return new JsonContent(JsonUtil.toJson(list));
    }

    /**
     * Renders the JSON content as a byte array.
     *
     * If the content has already been cached, it returns the cached byte array. Otherwise, it converts the JSON
     * string into a byte array using UTF-8 encoding, stores it in the cache, and then returns it.
     *
     * @return A byte array representing the JSON content, ready to be sent as part of the HTTP response body.
     */
    @Override
    public byte[] render() {
        if(this.cache == null)
            this.cache = this.toString()
                .getBytes(StandardCharsets.UTF_8);

        return this.cache;
    }

    /**
     * Returns the MIME type for the JSON content.
     *
     * The MIME type returned by this method is {@code "application/json"}, indicating that the content
     * is JSON-formatted.
     *
     * @return A string representing the MIME type for JSON content, which is {@code "application/json"}.
     */
    @Override
    public String mimeType() {
        return "application/json";
    }

    /**
     * Returns the string representation of the JSON content.
     *
     * This method returns the original JSON string passed to the constructor. It is used to retrieve the
     * content in its raw form before it is converted into a byte array for rendering.
     *
     * @return The JSON content as a string.
     */
    @Override
    public String toString() {
        return this.jsonContent;
    }
}
