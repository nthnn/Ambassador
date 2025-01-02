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

import web.ambassador.enums.MethodType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Route} class represents an endpoint in a web application,
 * supporting dynamic and static path matching, HTTP method-specific handlers,
 * and parameter extraction from URL paths.
 *
 * This class can handle paths with dynamic segments (e.g., {@code /user/{id}}) or
 * exact paths (e.g., {@code /home}). It allows associating handlers with
 * HTTP methods (GET, POST, etc.) for each route.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class Route {
    private final Map<MethodType, RouteHandler> handlers;
    private PathPattern pattern;
    private String exactPath;

    /**
     * Constructs a {@code Route} instance for the specified path.
     *
     * If the path contains placeholders (e.g., {@code {id}}),
     * a dynamic {@code PathPattern} is created. Otherwise, the route
     * is treated as an exact match.
     *
     * @param path The URL path for the route (e.g., {@code /api/items} or {@code /user/{id}}).
     */
    public Route(String path) {
        this.handlers = new HashMap<>();
        path = path.endsWith("/") ?
            path.substring(0, path.length() - 1) :
            path;

        if(path.contains("{"))
            this.pattern = new PathPattern(path);
        else this.exactPath = path;
    }

    /**
     * Checks if the provided path matches this route.
     *
     * The path is matched against either an exact string or a dynamic pattern
     * depending on how the route was initialized.
     *
     * @param path The URL path to match (e.g., {@code /user/42}).
     * @return {@code true} if the path matches the route, {@code false} otherwise.
     */
    public boolean matches(String path) {
        path = path.endsWith("/") ?
            path.substring(0, path.length() - 1) :
            path;

        return exactPath != null ?
            exactPath.equals(path) :
            pattern.matches(path);
    }

    /**
     * Extracts URL parameters from the provided path if the route has dynamic segments.
     *
     * Returns a map of placeholder names and their corresponding values
     * (e.g., {@code {id} -> 42}).
     *
     * @param path The URL path to extract parameters from (e.g., {@code /user/42}).
     * @return A map containing parameter names and their values, or an empty map
     *         if the path does not have dynamic segments.
     */
    public Map<String, String> extractParams(String path) {
        return pattern != null ?
            pattern.extractParams(path) :
            Collections.emptyMap();
    }

    /**
     * Adds an HTTP method-specific handler to the route.
     *
     * Associates a handler with an HTTP method (e.g., GET, POST). This allows
     * different behavior for the same route based on the method used.
     *
     * @param type    The HTTP method (e.g., {@code MethodType.GET}).
     * @param handler The handler to execute for this method.
     */
    public void addHandler(MethodType type, RouteHandler handler) {
        handlers.put(type, handler);
    }

    /**
     * Retrieves the handler for a specific HTTP method.
     * If no handler is registered for the method, {@code null} is returned.
     *
     * @param type The HTTP method to retrieve the handler for.
     * @return The associated {@code RouteHandler}, or {@code null} if not found.
     */
    public RouteHandler getHandler(MethodType type) {
        return handlers.get(type);
    }
}
