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

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.exception.NotFoundException;
import org.restexpress.route.Route;
import org.restexpress.route.RouteBuilder;
import org.restexpress.util.Callback;

import java.util.HashMap;
import java.util.Map;

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
		final String swaggerPath = getPath(urlPath);

        server.iterateRouteBuilders(new Callback<RouteBuilder>()
        {
            @Override
            public void process(RouteBuilder routeBuilder)
            {
                for (Route route : routeBuilder.build ())
                {
                    RouteMetadata routeMetadata = routeBuilder.asMetadata();
                    String path = getPath(route.getPattern());
                    if (!swaggerPath.equals(path))
                    {
                        ApiDeclarations apis = apisByPath.get(path);
                        if (apis == null)
                        {
                            apis = new ApiDeclarations(resources, server, path);
                            apisByPath.put(path, apis);
                            resources.addApi(path, null);
                        }
                        // TODO: use some annotation to indicate the model for the request body

                        ApiDeclaration apiDeclaration = new ApiDeclaration(routeMetadata.getUri().getPattern(), routeMetadata.getName());

                        ApiOperation operation = new ApiOperation(route.getMethod().getName(), routeMetadata);
                        apiDeclaration.addOperation(operation);
                        ModelResolver resolver = new ModelResolver(apis.getModels());
                        SchemaNode returnType = resolver.resolve(route.getAction().getReturnType());
                        if (returnType.getRef() != null) {
                            operation.type(returnType.getRef());
                        } else {
                            operation.type(returnType.getType());
                        }
                        apis.addApi(apiDeclaration);
                    }
                }
            }
        });
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
