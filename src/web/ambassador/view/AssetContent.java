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

import web.ambassador.core.AssetResponse;

/**
 * Represents the content of an asset that can be rendered as part of a web response.
 *
 * The {@code AssetContent} class implements the {@code ViewContent} interface and is used to
 * represent static assets such as images, JavaScript files, CSS files, or other types of media.
 * The class holds an {@code AssetResponse} object, which encapsulates the asset's content and MIME type.
 * It provides methods to render the asset content as a byte array and retrieve the appropriate MIME type
 * for the asset.
 *
 * The class employs a simple caching mechanism to avoid redundant content retrieval. The asset content
 * is fetched only once and stored in memory for subsequent access, improving performance for repeated requests
 * for the same asset.
 *
 * @see web.ambassador.view.ViewContent
 * @see web.ambassador.core.AssetResponse
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 */
public class AssetContent implements ViewContent {
    /** Cached content to avoid redundant fetching. */
    private byte[] cache = null;

    /** The AssetResponse that holds the asset's content and MIME type. */
    private final AssetResponse assetResponse;

    /**
     * Constructs a new {@code AssetContent} instance.
     *
     * This constructor initializes the {@code AssetContent} with the given {@code AssetResponse}
     * which contains the asset's content and MIME type.
     *
     * @param assetResponse The {@code AssetResponse} containing the asset's content and MIME type.
     */
    public AssetContent(AssetResponse assetResponse) {
        this.assetResponse = assetResponse;
    }

    /**
     * Renders the content of the asset.
     *
     * If the content has already been cached, it returns the cached byte array. Otherwise, it fetches
     * the content from the {@code AssetResponse}, stores it in the cache, and then returns it.
     *
     * @return A byte array representing the rendered asset content, ready to be sent as part of the HTTP response body.
     */
    @Override
    public byte[] render() {
        if(this.cache == null)
            this.cache = this.assetResponse.getContent();

        return this.cache;
    }

    /**
     * Returns the MIME type of the asset content.
     *
     * The MIME type indicates the type of content represented by this asset. For example, an image may
     * have the MIME type {@code "image/png"} or {@code "image/jpeg"}, while a CSS file might have
     * the MIME type {@code "text/css"}. This is useful for proper content handling by the client (e.g., a browser).
     *
     * @return A string representing the MIME type of the asset (e.g., "image/png", "text/css").
     */
    @Override
    public String mimeType() {
        return this.assetResponse.getMimeType();
    }

    /**
     * Returns a string representation of the asset content.
     *
     * This method converts the asset content to a string using the {@code render} method. The result
     * may not be meaningful for binary assets (such as images), but is implemented for debugging or logging purposes.
     *
     * @return A string representation of the asset content, or a human-readable form if possible.
     */
    @Override
    public String toString() {
        return new String(this.render());
    }
}
