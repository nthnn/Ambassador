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

import web.ambassador.enums.MethodType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an HTTP request, encapsulating various elements such as the request path, query parameters,
 * POST data, headers, cookies, and uploaded files. It supports parsing of multipart form data, query strings,
 * POST data, and cookies from the HTTP request.
 *
 * <p>This class provides utility methods for handling different types of request data and parsing mechanisms,
 * such as multipart/form-data for file uploads and URL decoding for query parameters and POST data.
 * The request data can be accessed and manipulated using a variety of getter and setter methods.
 *
 * <p>It also supports the handling of HTTP headers, cookies, and uploaded files through specific methods
 * like {@link #getUploadedFiles()}, {@link #getCookie(String)}, and methods for parsing multipart file uploads.
 * Additionally, it provides convenience methods for handling path parameters and query parameters.
 *
 * <p>The class can be extended to accommodate additional features, such as custom request handling or
 * additional parsing functionality as needed.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @see web.ambassador.enums.MethodType
 * @see web.ambassador.http.UploadedFile
 * @since 1.0
 * @version 1.0
 */
public class Request {
    private String path;
    private byte[] cache;
    private MethodType method;

    private final Map<String, String> queryParams;
    private final Map<String, String> postData;
    private final Map<String, String> pathParams;
    private final Map<String, String> headers;
    private final Map<String, String> cookies;

    private InputStream body;
    private List<UploadedFile> uploadedFiles;

    /**
     * Constructs a new HTTP Request with empty parameter maps and an empty uploaded files list.
     */
    public Request() {
        this.queryParams = new HashMap<>();
        this.postData = new HashMap<>();
        this.pathParams = new HashMap<>();
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
        this.uploadedFiles = new ArrayList<>();
    }

    /**
     * Retrieves the boundary string from the "Content-Type" header if it exists.
     * This is typically used for multipart/form-data requests to separate different sections of the body.
     *
     * @return the boundary string, or null if not found.
     */
    private String getBoundaryFromContentType() {
        String contentType = this.headers.get("Content-type");
        if(contentType == null)
            return null;

        Matcher matcher = Pattern.compile("boundary=(.*)")
            .matcher(contentType);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Finds the end of the file data in a multipart request, given the boundary.
     *
     * @param data the byte array representing the entire request body.
     * @param startIndex the starting index for the search.
     * @param boundaryBytes the byte array representing the boundary.
     * @return the index where the file data ends.
     */
    private int findEndOfFileData(
        byte[] data,
        int startIndex,
        byte[] boundaryBytes
    ) {
        if(startIndex + boundaryBytes.length > data.length)
            return data.length;

        for(int i = startIndex; i <= data.length - boundaryBytes.length; i++)
            if(Arrays.equals(
                Arrays.copyOfRange(data, i, i + boundaryBytes.length),
                boundaryBytes
            )) return i;

        return data.length;
    }

    /**
     * Finds the end of a field's value in a multipart form-data request.
     *
     * @param data the byte array representing the entire request body.
     * @param startIndex the starting index for the search.
     * @return the index where the field value ends.
     */
    private int findEndOfFieldValue(byte[] data, int startIndex) {
        if(startIndex >= data.length)
            return data.length;

        for(int i = startIndex; i < data.length - 1; i++)
            if(data[i] == '\r' && data[i + 1] == '\n')
                return i;

        return data.length;
    }

    /**
     * Finds the end of the headers section in a multipart form-data request.
     *
     * @param data the byte array representing the entire request body.
     * @param startIndex the starting index for the search.
     * @return the index where the headers section ends.
     */
    private int findEndOfHeaders(byte[] data, int startIndex) {
        if(startIndex >= data.length - 3)
            return data.length;

        for(int i = startIndex; i < data.length - 3; i++)
            if(data[i] == '\r' && data[i + 1] == '\n' &&
                data[i + 2] == '\r' && data[i + 3] == '\n')
                return i + 4;

        return data.length;
    }

    /**
     * Extracts the field name from a "Content-Disposition" header.
     *
     * @param disposition the "Content-Disposition" header value.
     * @return the field name, or null if not found.
     */
    private String getFieldName(String disposition) {
        if(disposition == null)
            return null;

        Matcher matcher = Pattern.compile("name=\"([^\"]+)\"")
            .matcher(disposition);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Extracts the file name from a "Content-Disposition" header.
     *
     * @param disposition the "Content-Disposition" header value.
     * @return the file name, or null if not found.
     */
    private String getFileName(String disposition) {
        if(disposition == null)
            return null;

        Matcher matcher = Pattern.compile("filename=\"([^\"]+)\"")
            .matcher(disposition);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Parses a string containing headers and returns a map of header names to their values.
     *
     * @param headersContent the string representing the raw header content.
     * @return a map of header names to their values.
     */
    private Map<String, String> parseHeaders(String headersContent) {
        if(headersContent == null || headersContent.isEmpty())
            return Collections.emptyMap();

        Map<String, String> headers = new HashMap<>();
        String[] headerLines = headersContent.split("\r\n");

        for(String line : headerLines) {
            int separatorIndex = line.indexOf(": ");
            if(separatorIndex > 0) {
                String key = line.substring(0, separatorIndex).trim();
                String value = line.substring(separatorIndex + 2).trim();
                headers.put(key, value);
            }
        }

        return headers;
    }

    /**
     * Reads the request body content into a byte array.
     *
     * @return a byte array containing the body content.
     * @throws IOException if an I/O error occurs.
     */
    private byte[] readBodyContent() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int bytesRead;

        while((bytesRead = body.read(data)) != -1)
            buffer.write(data, 0, bytesRead);

        return buffer.toByteArray();
    }

    /**
     * Determines whether the given data at the specified index matches the boundary.
     *
     * @param data the byte array representing the entire request body.
     * @param index the index where the boundary match should be checked.
     * @param boundary the boundary to match against.
     * @return true if the boundary matches, false otherwise.
     */
    private boolean isBoundaryMatch(byte[] data, int index, byte[] boundary) {
        return Arrays.equals(
            Arrays.copyOfRange(data, index, index + boundary.length),
            boundary
        );
    }

    /**
     * Determines if the end of the boundary has been reached in the multipart data.
     *
     * @param data the byte array representing the entire request body.
     * @param index the current index.
     * @return true if the end boundary is detected, false otherwise.
     */
    private boolean isEndBoundary(byte[] data, int index) {
        return (index + 1 < data.length) &&
            (data[index] == '-') &&
            (data[index + 1] == '-');
    }

    /**
     * Processes a file upload in a multipart form-data request.
     *
     * @param data the byte array representing the entire request body.
     * @param startIndex the starting index for the file data.
     * @param boundary the boundary separating different sections in the body.
     * @param disposition the "Content-Disposition" header for the file upload.
     * @param headers the headers for the file section.
     * @param files the list to store the uploaded files.
     * @return the new index after processing the file upload.
     */
    private int processFileUpload(
        byte[] data,
        int startIndex,
        byte[] boundary,
        String disposition,
        Map<String, String> headers,
        List<UploadedFile> files
    ) {
        String filename = this.getFileName(disposition);
        String fieldName = this.getFieldName(disposition);
        String contentType = headers.get("Content-Type");

        int endIndex = this.findEndOfFileData(data, startIndex, boundary);
        byte[] fileContent = Arrays.copyOfRange(data, startIndex, endIndex);

        if(filename != null && !filename.isEmpty())
            files.add(new UploadedFile(
                fieldName,
                filename,
                contentType,
                fileContent
            ));

        return endIndex;
    }

    /**
     * Processes a form field in a multipart form-data request.
     *
     * @param data the byte array representing the entire request body.
     * @param startIndex the starting index for the form field.
     * @param fieldName the name of the field.
     * @return the new index after processing the form field.
     */
    private int processFormField(
        byte[] data,
        int startIndex,
        String fieldName
    ) {
        int endIndex = findEndOfFieldValue(data, startIndex);
        if(fieldName != null) {
            String fieldValue = new String(
                data,
                startIndex,
                endIndex - startIndex,
                StandardCharsets.UTF_8
            ).trim();
            postData.put(fieldName, fieldValue);
        }

        return endIndex + 2;
    }

    /**
     * Processes a section of multipart data, which may include a form field or file upload.
     *
     * @param data the byte array representing the entire request body.
     * @param startIndex the starting index for the section.
     * @param boundary the boundary separating different sections in the body.
     * @param files the list to store the uploaded files.
     * @return the new index after processing the section.
     */
    private int processMultipartSection(
        byte[] data,
        int startIndex,
        byte[] boundary,
        List<UploadedFile> files
    ) {
        int headerEnd = findEndOfHeaders(data, startIndex);
        String headersContent = new String(
            data,
            startIndex,
            headerEnd - startIndex,
            StandardCharsets.UTF_8
        );

        Map<String, String> headers = parseHeaders(headersContent);
        String disposition = headers.get("Content-Disposition");

        if(disposition == null)
            return headerEnd;

        String fieldName = getFieldName(disposition);
        if(disposition.contains("filename"))
            return processFileUpload(
                data,
                headerEnd,
                boundary,
                disposition,
                headers,
                files
            );
        else return processFormField(data, headerEnd, fieldName);
    }

    /**
     * Parses multipart/form-data from the request body, extracting uploaded files and form fields.
     *
     * @throws IOException if an I/O error occurs during reading the body.
     */
    public void parseMultipartData() throws IOException {
        String boundary = this.getBoundaryFromContentType();
        if(boundary == null || body == null)
            return;

        List<UploadedFile> files = new ArrayList<>();
        byte[] boundaryBytes = ("--" + boundary).getBytes(StandardCharsets.UTF_8);
        byte[] requestData = this.readBodyContent();

        if(this.cache == null)
            this.cache = requestData;

        int currentIndex = 0;
        while(currentIndex < requestData.length - boundaryBytes.length) {
            if(!this.isBoundaryMatch(requestData, currentIndex, boundaryBytes)) {
                currentIndex++;
                continue;
            }

            currentIndex += boundaryBytes.length;
            if(this.isEndBoundary(requestData, currentIndex))
                break;

            currentIndex = this.processMultipartSection(
                requestData,
                currentIndex,
                boundaryBytes,
                files
            );
        }

        for(UploadedFile file : files)
            if(file.filename() == null || file.content() != null) {
                String[] data = new String(file.content()).split("=");
                if(data.length != 2)
                    continue;

                postData.put(data[0], data[1]);
            }

        this.uploadedFiles = files;
    }

    /**
     * Parses the query string from a URL, extracting key-value pairs.
     *
     * @param query the query string from the URL.
     */
    public void parseQueryString(String query) {
        if(query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");

            for(String pair : pairs) {
                String[] keyValue = pair.split("=");

                if(keyValue.length == 2)
                    this.queryParams.put(
                        URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                    );
            }
        }
    }

    /**
     * Parses POST data from the request body, extracting key-value pairs.
     *
     * @throws IOException if an I/O error occurs during reading the body.
     */
    public void parsePostData() throws IOException {
        byte[] bodyData = {};
        if(this.cache == null)
            this.readBodyContent();
        else bodyData = this.cache;

        String postDataString = new String(bodyData, StandardCharsets.UTF_8).trim();
        if(!postDataString.isEmpty()) {
            String[] pairs = postDataString.split("&");

            for(String pair : pairs) {
                String[] keyValue = pair.split("=", 2);

                if(keyValue.length == 2)
                    this.postData.put(
                        URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                    );
            }
        }
    }

    /**
     * Parses cookies from the "Cookie" header in the request.
     */
    public void parseCookies() {
        String cookieHeader = this.headers.get("Cookie");
        if(cookieHeader != null && !cookieHeader.isEmpty()) {
            String[] cookiePairs = cookieHeader.split(";");

            for(String cookiePair : cookiePairs) {
                String[] cookie = cookiePair.trim().split("=");

                if(cookie.length == 2)
                    cookies.put(cookie[0].trim(), cookie[1].trim());
            }
        }
    }

    /**
     * Retrieves all the path parameters.
     *
     * @return the key and value of the path parameters, or empty if no path parameters.
     */
    public Map<String, String> getPathParams() {
        return this.pathParams;
    }

    /**
     * Retrieves the value of a path parameter.
     *
     * @param name the name of the path parameter.
     * @return the value of the path parameter, or null if not found.
     */
    public String getPathParam(String name) {
        return this.pathParams.get(name);
    }

    /**
     * Retrieves the request path.
     *
     * @return the path of the request origin.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Sets the path of the request.
     *
     * @param path the request path.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Retrieves the HTTP method of the request (e.g., GET, POST).
     *
     * @return the HTTP method.
     */
    public MethodType getMethod() {
        return this.method;
    }

    /**
     * Sets the HTTP method for the request (e.g., GET, POST).
     *
     * @param method the HTTP method.
     */
    public void setMethod(MethodType method) {
        this.method = method;
    }

    /**
     * Retrieves the value of a path parameter.
     *
     * @param name the name of the path parameter.
     * @return the value of the path parameter, or null if not found.
     */
    public String getParam(String name) {
        return pathParams.get(name);
    }

    /**
     * Retrieves the value of a query parameter.
     *
     * @param name the name of the query parameter.
     * @return the value of the query parameter, or null if not found.
     */
    public String getQueryParam(String name) {
        return this.queryParams.get(name);
    }

    /**
     * Retrieves all query parameters as a map of name-value pairs.
     *
     * @return a map of query parameters.
     */
    public Map<String, String> getQueryParams() {
        return this.queryParams;
    }

    /**
     * Retrieves the value of a POST parameter.
     *
     * @param name the name of the POST parameter.
     * @return the value of the POST parameter, or null if not found.
     */
    public String getPostParam(String name) {
        return this.postData.get(name);
    }

    /**
     * Retrieves all POST parameters as a map of name-value pairs.
     *
     * @return a map of POST parameters.
     */
    public Map<String, String> getPostParams() {
        return this.postData;
    }

    /**
     * Retrieves all headers as a map of header names to values.
     *
     * @return a map of headers.
     */
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    /**
     * Sets the body content of the request.
     *
     * @param body the input stream representing the request body.
     */
    public void setBody(InputStream body) {
        this.body = body;
    }

    /**
     * Retrieves the body content of the request as an input stream.
     *
     * @return the input stream representing the body content.
     */
    public InputStream getBody() {
        return this.body;
    }

    /**
     * Retrieves the value of a cookie by its name.
     *
     * @param name the name of the cookie.
     * @return the value of the cookie, or null if not found.
     */
    public String getCookie(String name) {
        return this.cookies.get(name);
    }

    /**
     * Sets a cookie in the request.
     *
     * @param name the name of the cookie.
     * @param value the value of the cookie.
     */
    public void setCookie(String name, String value) {
        this.cookies.put(name, value);
    }

    /**
     * Check if the request contains a cookie with the specified name.
     *
     * @param name name of the cookie to be checked.
     * @return true if the name exists on the list of cookie from the request.
     */
    public boolean hasCookie(String name) {
        return this.cookies.containsKey(name);
    }

    /**
     * Retrieves the list of uploaded files from the request.
     *
     * @return a list of uploaded files.
     */
    public List<UploadedFile> getUploadedFiles() {
        return this.uploadedFiles;
    }
}
