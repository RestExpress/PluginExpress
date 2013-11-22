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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.domain.metadata.RouteMetadata;
import com.strategicgains.restexpress.domain.metadata.ServerMetadata;
import com.strategicgains.restexpress.exception.NotFoundException;

/**
 * @author toddf
 * @since Nov 21, 2013
 */
public class SwaggerController
{
	// private static final String RESOURCE_REGEX = "(/.*)(/|\\.)?";
	// private static final Pattern RESOURCE_PATTERN =
	// Pattern.compile(RESOURCE_REGEX);

	private RestExpress server;
	private ApiResources resources;
	private Map<String, ApiDeclarations> apisByPath = new HashMap<String, ApiDeclarations>();

	public SwaggerController(RestExpress server, String apiVersion,
	    String swaggerVersion)
	{
		super();
		this.resources = new ApiResources(apiVersion, swaggerVersion);
		this.server = server;
	}

	public void initialize(String urlPath, ServerMetadata data)
	{
		String swaggerPath = getPath(urlPath);

		for (RouteMetadata route : data.getRoutes())
		{
			String path = getPath(route.getUri().getPattern());

			if (swaggerPath.equals(path)) continue;

			ApiDeclarations apis = apisByPath.get(path);

			if (apis == null) // new path to document
			{
				apis = new ApiDeclarations(resources, server, path);
				apisByPath.put(path, apis);
				// TODO: pull the description from the route metadata (not
				// currently available).
				resources.addApi(path, null);
			}

			apis.addApi(new ApiDeclaration(route));
			// apis.addModels();
		}
	}

	private String getPath(String pattern)
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
}
