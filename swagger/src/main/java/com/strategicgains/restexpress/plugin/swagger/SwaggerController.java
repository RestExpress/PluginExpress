/*
    Copyright 2013, Strategic Gains, Inc.
    Copyright 2017, pulsIT UG

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package com.strategicgains.restexpress.plugin.swagger;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.route.Route;
import org.restexpress.route.RouteBuilder;
import org.restexpress.util.Callback;

import com.strategicgains.restexpress.plugin.swagger.wrapper.OpenApi;
import com.strategicgains.restexpress.plugin.swagger.wrapper.Operation;
import com.strategicgains.restexpress.plugin.swagger.wrapper.PathItem;

public class SwaggerController implements Callback<RouteBuilder> {
	public static final List<String> VALID_METHODS = new ArrayList<String>(Arrays.asList(new String[] { "GET", "PUT", "POST", "DELETE", "HEAD", "PATCH" }));

	// Determines if the route will show in swagger output if it is not annotated
	// if set to true then route must be explicitly annotated with ApiOperation to
	// show in swagger
	// if false then all routes will show in swagger unless ApiOperation.hidden is
	// set to true
	// (backward compatiblility means this should be false unless explicitly set)
	private boolean shouldShowAnnotatedOnly = false;

	protected RestExpress server;
	private OpenApi openApi;
	private String basePath;
	private String swaggerRoot;
	
	private String filterByTag = "";
	private List<RouteBuilder> routes = new ArrayList<RouteBuilder>();

	public SwaggerController(RestExpress server, OpenApi openApi, String basePath, String swaggerVersion, boolean shouldShowAnnotatedOnly) {
		this(server, swaggerVersion, openApi, basePath);
		this.shouldShowAnnotatedOnly = shouldShowAnnotatedOnly;
	}

	public SwaggerController(RestExpress server, String swaggerVersion, OpenApi openApi, String basePath) {
		super();
		this.openApi = openApi;
		this.basePath = basePath;
		this.server = server;
	}

	public void initialize(String urlPath, RestExpress server) {
		swaggerRoot = getPathSegment(urlPath);
		server.iterateRouteBuilders(this);
	}

	public OpenApi readAll(Request request, Response response) {
		try {
			String filter = URLDecoder.decode(request.getQueryStringMap().getOrDefault("byTag", ""), "UTF-8");
			if(!filterByTag.equals(filter)) {
				filterByTag = filter;
				openApi.getPaths().clear();	
				for(RouteBuilder r : routes) {
					process(r);				
				}
			}
		} catch (Exception e) { }
		
		return openApi;
	}

	/**
	 * Returns the first part of the URL path. Either the leading slash to the first
	 * period ('.') or the first slash to the second slash.
	 * 
	 * @param pattern a URL pattern.
	 * @return
	 */
	private String getPathSegment(String pattern) {
		int slash = pattern.indexOf('/', 1);
		int dot = pattern.indexOf('.', 1);
		String path;

		if (slash > 0) {
			path = pattern.substring(0, slash);
		} else if (dot > 0) {
			path = pattern.substring(0, dot);
		} else {
			path = pattern;
		}

		return path;
	}

	@Override
	public void process(RouteBuilder routeBuilder) {
		if(!routes.contains(routeBuilder)) {
			routes.add(routeBuilder);
		}
		for (Route route : routeBuilder.build()) {
			if (!VALID_METHODS.contains(route.getMethod().name()))
				continue;

			 String routePath = route.getPattern();
			 String path = "";
			 if(routePath.startsWith(basePath)){
				routePath = routePath.substring(basePath.length());
			 	path = getPathSegment(routePath);
			 } else {
				 path = getPathSegment(routePath);
			 }
			 path = routePath;

			// Don't report the Swagger routes...
			if (swaggerRoot.equals(path))
				continue;

			// Don't report the / route. It will not be resolved.
			if ("/".equals(path))
				continue;

			if (isRouteHidden(route))
				continue;

			
			Operation operation;
			Method m = route.getAction();
			
			if (m.isAnnotationPresent(io.swagger.v3.oas.annotations.Operation.class)) {
				operation = new Operation(m.getAnnotation(io.swagger.v3.oas.annotations.Operation.class));
			} else {
				operation = new Operation();
			}
			
			operation.setOperationId(route.getController().getClass().getName() + "::" + route.getAction().getName());
			if(filterByTag != null && filterByTag.length() > 0 && operation != null && operation.getTags() != null && (operation.getTags().stream().filter(Pattern.compile(filterByTag).asPredicate()).findFirst().orElse(null) == null)) {
				continue;
			}
			
			PathItem p = openApi.getPaths().get(path);
			if(p != null) {
				p.add(route.getMethod().name(), operation);
			} else {
				p = new PathItem();
				p.add(route.getMethod().name(), operation);
				openApi.getPaths().put(path, p);
			}
		}
	}

	private boolean isRouteHidden(Route route) {
		Method method = route.getAction();
		if (method.isAnnotationPresent(io.swagger.v3.oas.annotations.Operation.class)) {
			return (method.isAnnotationPresent(io.swagger.v3.oas.annotations.Hidden.class));
		}

		return shouldShowAnnotatedOnly;
	}
}
