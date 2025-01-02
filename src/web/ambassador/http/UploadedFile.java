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
package web.ambassador.http;

/**
 * The {@code UploadedFile} record represents a file that has been uploaded
 * through an HTTP request. It contains information about the file such as
 * the field name, filename, content type, and the actual file content.
 *
 * This class is typically used to handle multipart file uploads in web applications.
 * The fields provide metadata about the uploaded file as well as the raw file data.
 *
 * @param fieldName The name of the form field that was used to upload the file. This is typically the name attribute of the file input element in the HTML form.
 * @param filename The name of the uploaded file, including its extension (e.g., "document.txt").
 * @param contentType The content type (MIME type) of the uploaded file (e.g., "image/png", "application/pdf").
 * @param content The raw content of the uploaded file as a byte array.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @since 1.0
 * @version 1.0
 */
public record UploadedFile(
    String fieldName,
    String filename,
    String contentType,
    byte[] content
) { }
