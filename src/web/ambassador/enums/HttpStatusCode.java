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
package web.ambassador.enums;

/**
 * Enum representing HTTP status codes used in the Ambassador framework.
 *
 * This enum defines the standard HTTP response status codes as specified
 * by the Internet Engineering Task Force (IETF). Each status code corresponds
 * to a specific meaning, providing a way to indicate the result of an HTTP request.
 *
 * <p><b>Status Code Categories:</b></p>
 * <ul>
 *     <li><b>1xx (Informational):</b> Request received, continuing process</li>
 *     <li><b>2xx (Success):</b> Request successfully received, understood, and accepted</li>
 *     <li><b>3xx (Redirection):</b> Further action must be taken to complete the request</li>
 *     <li><b>4xx (Client Error):</b> Request contains bad syntax or cannot be fulfilled</li>
 *     <li><b>5xx (Server Error):</b> Server failed to fulfill a valid request</li>
 * </ul>
 *
 * This enum is useful for constructing HTTP responses, error handling, and ensuring
 * that appropriate status codes are returned to clients based on the outcome of requests.
 *
 * <p><b>Example Usage:</b></p>
 * ```java
 *     HttpStatusCode status = HttpStatusCode.NOT_FOUND;
 *     System.out.println("Error: " + status.getStatusCode());  // Prints 404
 * ```
 *
 * @author  <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since   1.0
 */
public enum HttpStatusCode {
    /**
     * 100 Continue - The server has received the request headers and the client should proceed to send the request body.
     */
    CONTINUE(100),

    /**
     * 101 Switching Protocols - The server is complying with a request to switch protocols.
     */
    SWITCHING_PROTOCOLS(101),

    /**
     * 102 Processing - The server has received and is processing the request, but no response is available yet.
     */
    PROCESSING(102),

    /**
     * 103 Early Hints - The server is sending preliminary headers before the final response.
     */
    EARLY_HINTS(103),

    /**
     * 200 OK - The request was successful, and the server responded with the requested data.
     */
    OK(200),

    /**
     * 201 Created - The request was successful, and a new resource was created as a result.
     */
    CREATED(201),

    /**
     * 202 Accepted - The request has been accepted for processing, but the processing is not complete.
     */
    ACCEPTED(202),

    /**
     * 203 Non-Authoritative Information - The server successfully processed the request, but the returned data may be from a cached source.
     */
    NON_AUTHORITATIVE_INFO(203),

    /**
     * 204 No Content - The server successfully processed the request, but there is no content to return.
     */
    NO_CONTENT(204),

    /**
     * 205 Reset Content - The server successfully processed the request, but requires the client to reset the view.
     */
    RESET_CONTENT(205),

    /**
     * 206 Partial Content - The server successfully processed part of the request.
     */
    PARTIAL_CONTENT(206),

    /**
     * 207 Multi-Status - The response provides status information for multiple resources.
     */
    MULTI_STATUS(207),

    /**
     * 208 Already Reported - The members of a DAV binding have already been enumerated in a previous part of the response.
     */
    ALREADY_REPORTED(208),

    /**
     * 226 IM Used - The server has fulfilled a request for the resource and the response is a representation of the result of one or more instance-manipulations.
     */
    IM_USED(226),

    /**
     * 300 Multiple Choices - The request has more than one possible response.
     */
    MULTIPLE_CHOICES(300),

    /**
     * 301 Moved Permanently - The requested resource has been permanently moved to a new location.
     */
    MOVED_PERMANENTLY(301),

    /**
     * 302 Found - The resource resides temporarily under a different URI.
     */
    FOUND(302),

    /**
     * 303 See Other - The response can be found under a different URI using the GET method.
     */
    SEE_OTHER(303),

    /**
     * 304 Not Modified - The resource has not been modified since the last request.
     */
    NOT_MODIFIED(304),

    /**
     * 305 Use Proxy - The requested resource must be accessed through a proxy.
     */
    USE_PROXY(305),

    /**
     * 307 Temporary Redirect - The resource resides temporarily under a different URI, and the client should repeat the request at that URI.
     */
    TEMPORARY_REDIRECT(307),

    /**
     * 308 Permanent Redirect - The resource has been permanently moved to a different URI.
     */
    PERMANENT_REDIRECT(308),

    /**
     * 400 Bad Request - The server could not understand the request due to invalid syntax.
     */
    BAD_REQUEST(400),

    /**
     * 401 Unauthorized - The client must authenticate itself to get the requested response.
     */
    UNAUTHORIZED(401),

    /**
     * 402 Payment Required - Reserved for future use.
     */
    PAYMENT_REQUIRED(402),

    /**
     * 403 Forbidden - The client does not have permission to access the requested resource.
     */
    FORBIDDEN(403),

    /**
     * 404 Not Found - The server could not find the requested resource.
     */
    NOT_FOUND(404),

    /**
     * 405 Method Not Allowed - The HTTP method used is not allowed for the requested resource.
     */
    METHOD_NOT_ALLOWED(405),

    /**
     * 406 Not Acceptable - The resource is not available in a format acceptable to the client.
     */
    NOT_ACCEPTABLE(406),

    /**
     * 407 Proxy Authentication Required - The client must authenticate itself with the proxy.
     */
    PROXY_AUTH_REQUIRED(407),

    /**
     * 408 Request Timeout - The server timed out waiting for the client to send a request.
     */
    REQUEST_TIMEOUT(408),

    /**
     * 409 Conflict - The request could not be processed because of a conflict with the current state of the resource.
     */
    CONFLICT(409),

    /**
     * 410 Gone - The resource is no longer available and has been permanently removed.
     */
    GONE(410),

    /**
     * 411 Length Required - The server requires a valid Content-Length header in the request.
     */
    LENGTH_REQUIRED(411),

    /**
     * 412 Precondition Failed - One or more conditions given in the request header fields were not met.
     */
    PRECONDITION_FAILED(412),

    /**
     * 413 Payload Too Large - The request payload is larger than the server is willing or able to process.
     */
    CONTENT_TOO_LARGE(413),

    /**
     * 414 URI Too Long - The URI provided was too long for the server to process.
     */
    URI_TOO_LONG(414),

    /**
     * 415 Unsupported Media Type - The media type of the request's payload is not supported by the server.
     */
    UNSUPPORTED_MEDIA_TYPE(415),

    /**
     * 416 Range Not Satisfiable - The range specified by the client is not satisfiable.
     */
    RANGE_NOT_SATISFIABLE(416),

    /**
     * 417 Expectation Failed - The server cannot meet the expectations specified in the Expect header.
     */
    EXPECTATION_FAILED(417),

    /**
     * 418 I'm a Teapot - The server refuses to brew coffee because it is a teapot (an April Fools' joke).
     */
    IM_A_TEAPOT(418),

    /**
     * 421 Misdirected Request - The request was directed at a server that is not able to produce a response.
     */
    MISDIRECTED_REQUEST(421),

    /**
     * 422 Unprocessable Entity - The server understands the request, but it cannot process it due to semantic errors.
     */
    UNPROCESSABLE_CONTENT(422),

    /**
     * 423 Locked - The resource that is being accessed is locked.
     */
    LOCKED(423),

    /**
     * 424 Failed Dependency - The request failed due to failure of a previous request.
     */
    FAILED_DEPENDENCY(424),

    /**
     * 425 Too Early - The server is unwilling to risk processing a request that might be replayed.
     */
    TOO_EARLY(425),

    /**
     * 426 Upgrade Required - The client should switch to a different protocol.
     */
    UPGRADE_REQUIRED(426),

    /**
     * 428 Precondition Required - The origin server requires the request to be conditional.
     */
    PRECONDITION_REQUIRED(428),

    /**
     * 429 Too Many Requests - The user has sent too many requests in a given amount of time.
     */
    TOO_MANY_REQUESTS(429),

    /**
     * 431 Request Header Fields Too Large - The request's header fields are too large.
     */
    REQUEST_HEADER_FIELDS_TOO_LARGE(431),

    /**
     * 451 Unavailable For Legal Reasons - The resource is unavailable due to legal reasons.
     */
    UNAVAILABLE_FOR_LEGAL_REASONS(451),

    /**
     * 500 Internal Server Error - The server encountered an unexpected condition that prevented it from fulfilling the request.
     */
    INTERNAL_SERVER_ERROR(500),

    /**
     * 501 Not Implemented - The server does not support the functionality required to fulfill the request.
     */
    NOT_IMPLEMENTED(501),

    /**
     * 502 Bad Gateway - The server received an invalid response from the upstream server.
     */
    BAD_GATEWAY(502),

    /**
     * 503 Service Unavailable - The server is currently unavailable (due to overload or maintenance).
     */
    SERVICE_UNAVAILABLE(503),

    /**
     * 504 Gateway Timeout - The server did not receive a timely response from the upstream server.
     */
    GATEWAY_TIMEOUT(504),

    /**
     * 505 HTTP Version Not Supported - The server does not support the HTTP protocol version used in the request.
     */
    HTTP_VERSION_NOT_SUPPORTED(505),

    /**
     * 506 Variant Also Negotiates - The server has an internal configuration error and is unable to negotiate the response.
     */
    VARIANT_ALSO_NEGOTIATE(506),

    /**
     * 507 Insufficient Storage - The server is unable to store the representation needed to complete the request.
     */
    INSUFFICIENT_STORAGE(507),

    /**
     * 508 Loop Detected - The server detected an infinite loop while processing a request.
     */
    LOOP_DETECTED(508),

    /**
     * 510 Not Extended - The policy for the request has not been met.
     */
    NOT_EXTENDED(510),

    /**
     * 511 Network Authentication Required - The client needs to authenticate to gain network access.
     */
    NETWORK_AUTH_REQUIRED(511);

    private final int statusCode;

    /**
     * Constructs an HttpStatusCode enum with the specified status code.
     *
     * @param statusCode the HTTP status code value
     */
    HttpStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Returns the integer value of the HTTP status code.
     *
     * @return the HTTP status code
     */
    public int getStatusCode() {
        return this.statusCode;
    }
}
