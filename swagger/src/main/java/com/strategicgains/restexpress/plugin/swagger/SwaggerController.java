/*
    Copyright 2013, Strategic Gains, Inc.

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.exception.NotFoundException;
import org.restexpress.route.Route;
import org.restexpress.route.RouteBuilder;
import org.restexpress.util.Callback;

import com.strategicgains.restexpress.plugin.swagger.domain.ApiDeclarations;
import com.strategicgains.restexpress.plugin.swagger.domain.ApiOperation;
import com.strategicgains.restexpress.plugin.swagger.domain.ApiResources;

/**
 * @author toddf
 * @since Nov 21, 2013
 */
public class SwaggerController
implements Callback<RouteBuilder>
{
	public static final List<String> VALID_METHODS = new ArrayList<String>(
    Arrays.asList(new String[]
    {
        "GET", "PUT", "POST", "DELETE", "HEAD"
    }));

	private RestExpress server;
	private ApiResources resources;
	private Map<String, ApiDeclarations> apisByPath = new HashMap<String, ApiDeclarations>();
	private String swaggerRoot;

	public SwaggerController(RestExpress server, String apiVersion, String swaggerVersion)
	{
		super();
		this.resources = new ApiResources(apiVersion, swaggerVersion);
		this.server = server;
	}

	public void initialize(String urlPath, RestExpress server)
    {
		swaggerRoot = getPathSegment(urlPath);
		server.iterateRouteBuilders(this);
    }

	public ApiResources readAll(Request request, Response response)
	{
		return resources;
	}

	public ApiDeclarations read(Request request, Response response)
	{
		String path = request.getHeader("path");
		ApiDeclarations api = apisByPath.get("/" + path);

		if (api == null) throw new NotFoundException(path);

		return api;
	}

	/**
	 * Returns the first part of the URL path. Either the leading slash to the first period ('.')
	 * or the first slash to the second slash.
	 * 
	 * @param pattern a URL pattern.
	 * @return
	 */
	private String getPathSegment(String pattern)
	{
		int slash = pattern.indexOf('/', 1);
		int dot = pattern.indexOf('.', 1);
		String path;

		if (slash > 0)
		{
			path = pattern.substring(0, slash);
		}
		else if (dot > 0)
		{
			path = pattern.substring(0, dot);
		}
		else
		{
			path = pattern;
		}

		return path;
	}

	@Override
    public void process(RouteBuilder routeBuilder)
    {
		for (Route route : routeBuilder.build())
		{
			if (!VALID_METHODS.contains(route.getMethod().getName())) continue;

			String path = getPathSegment(route.getPattern());

			// Don't report the Swagger routes...
			if (swaggerRoot.equals(path)) continue;

			ApiDeclarations apis = apisByPath.get(path);

			if (apis == null) // new path to document
			{
				apis = new ApiDeclarations(resources, server, path);
				apisByPath.put(path, apis);
				// TODO: pull the description from the route metadata (not
				// currently available).
				resources.addApi(path, null);
			}

			ApiOperation operation = apis.addOperation(route);
			apis.addModels(operation, route);
		}
    }
}
