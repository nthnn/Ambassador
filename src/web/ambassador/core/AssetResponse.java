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
package web.ambassador.core;

/**
 * The AssetResponse class represents the response returned by the AssetHandler when
 * retrieving an asset (such as an image, CSS, or JavaScript file) from the classpath.
 *
 * This class encapsulates the asset content as a byte array, its MIME type, and
 * a flag indicating whether the asset was successfully found and loaded.
 *
 * <p><b>Usage:</b></p>
 * ```java
 *     AssetResponse response = assetHandler.getAsset("/assets/image.png");
 *     if(response.wasFound()) {
 *         byte[] content = response.getContent();
 *         String mimeType = response.getMimeType();
 *         // Process content and MIME type
 *     }
 * ```
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class AssetResponse {
    private final byte[] content;
    private final String mimeType;
    private final boolean found;

    /**
     * Constructs a new AssetResponse.
     *
     * @param content The content of the asset as a byte array.
     * @param mimeType The MIME type of the asset (e.g., "image/png", "text/css").
     * @param found A boolean flag indicating whether the asset was found and successfully loaded.
     */
    public AssetResponse(byte[] content, String mimeType, boolean found) {
        this.content = content;
        this.mimeType = mimeType;
        this.found = found;
    }

    /**
     * Returns the content of the asset as a byte array.
     *
     * @return A byte array representing the content of the asset. Can be empty or null
     *         if the asset was not found.
     */
    public byte[] getContent() {
        return this.content;
    }

    /**
     * Returns the MIME type of the asset.
     *
     * @return A string representing the MIME type of the asset (e.g., "image/png").
     *         If the asset was not found, this will be null.
     */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * Returns whether the asset was successfully found and loaded.
     *
     * @return A boolean flag indicating if the asset was found and successfully loaded.
     *         If true, the content is valid; if false, the content will be null.
     */
    public boolean wasFound() {
        return this.found;
    }
}
