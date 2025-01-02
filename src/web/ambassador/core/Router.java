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

import web.ambassador.annotations.Controller;
import web.ambassador.db.DatabaseManager;
import web.ambassador.http.Request;
import web.ambassador.http.Response;
import web.ambassador.view.AssetContent;
import web.ambassador.view.Dom;
import web.ambassador.view.ViewContent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The {@code Router} class handles the registration of controller classes and
 * maps incoming HTTP requests to the appropriate route handlers. It acts as
 * the entry point for processing web requests in the application.
 * <br/>
 * The {@code Router} supports dynamic registration of routes through controller
 * annotations, enabling flexible and scalable request handling.
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *     <li>Register controller classes and associate routes dynamically.</li>
 *     <li>Route incoming requests to the correct handler based on path and method.</li>
 *     <li>Handle static asset requests (e.g., CSS, JS, images) through an {@code AssetHandler}.</li>
 *     <li>Return appropriate HTTP responses (404, 405) for unmatched or invalid requests.</li>
 * </ul>
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class Router {
    private final List<Route> routes;
    private final AssetHandler assetHandler;

    /**
     * Constructs a new {@code Router} instance with an empty list of routes
     * and an asset handler for serving static files.
     */
    public Router() {
        this.routes = new ArrayList<>();
        this.assetHandler = new AssetHandler();
    }

    /**
     * Registers a controller class and extracts route mappings from its
     * annotated methods.
     *
     * This method scans the provided controller class for {@code @Method} annotations
     * and registers the corresponding routes dynamically. The base path is extracted
     * from the {@code @Controller} annotation.
     *
     * @param controllerClass The controller class to register.
     */
    public void registerController(Class<?> controllerClass) {
        Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
        if(controllerAnnotation == null)
            return;

        String basePath = controllerAnnotation.path();
        Route route = new Route(basePath);

        for(Method method : controllerClass.getMethods()) {
            web.ambassador.annotations.Method methodAnnotation =
                method.getAnnotation(web.ambassador.annotations.Method.class);

            if(methodAnnotation != null)
                route.addHandler(methodAnnotation.value(),
                    new RouteHandler(controllerClass, method));
        }

        this.routes.add(route);
    }

    /**
     * Handles incoming HTTP requests by matching the request path to a registered route.
     *
     * If the request path starts with {@code /assets/}, the request is forwarded
     * to the {@code AssetHandler} to retrieve static assets.
     * <br/>
     * For other paths, the router attempts to find a matching route and invokes
     * the associated route handler. If no match is found, a 404 response is returned.
     * If the method is not allowed, a 405 response is generated.
     *
     * @param request   The incoming HTTP request.
     * @param response  The HTTP response to populate.
     * @param dbManager The database manager to provide access to data.
     * @return A {@code ViewContent} representing the response body (HTML, JSON, etc.).
     */
    public ViewContent handleRequest(Request request, Response response, DatabaseManager dbManager) {
        String path = request.getPath();

        if(path.startsWith("/assets/")) {
            AssetResponse assetResponse = assetHandler.getAsset(path);
            if(assetResponse.wasFound()) {
                response.getHeaders().put(
                    "Content-Type",
                    assetResponse.getMimeType()
                );
                return new AssetContent(assetResponse);
            }

            response.setStatusCode(404);
            return Dom.h1("404 Asset Not Found");
        }

        for(Route route : routes)
            if(route.matches(path)) {
                Map<String, String> pathParams = route.extractParams(path);
                request.getPathParams().putAll(pathParams);

                RouteHandler handler = route.getHandler(request.getMethod());
                if(handler != null)
                    return handler.handle(request, response, dbManager);

                response.setStatusCode(405);
                return Dom.h1("405 Method Not Allowed");
            }

        response.setStatusCode(404);
        return Dom.h1("404 Not Found");
    }
}
