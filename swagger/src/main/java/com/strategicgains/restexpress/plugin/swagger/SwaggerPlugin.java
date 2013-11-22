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

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.plugin.AbstractPlugin;

/**
 * @author toddf
 * @since Nov 21, 2013
 */
public class SwaggerPlugin
extends AbstractPlugin
{
	private SwaggerController controller;
	private String urlPath;
	private String apiVersion;
	private String swaggerVersion = "1.2";

	public SwaggerPlugin()
	{
		this("/api-docs");
	}

	public SwaggerPlugin(String urlPath)
	{
		super();
		this.urlPath = urlPath;
	}

	public SwaggerPlugin apiVersion(String version)
	{
		this.apiVersion = version;
		return this;
	}

	public SwaggerPlugin swaggerVersion(String version)
	{
		this.swaggerVersion = version;
		return this;
	}

	@Override
	public SwaggerPlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);
		controller = new SwaggerController(server, apiVersion, swaggerVersion);

		server.uri(urlPath, controller)
			.action("readAll", HttpMethod.GET)
			.name("swagger.resources")
		    .format(Format.JSON);

		server.uri(urlPath + "/{path}", controller)
			.method(HttpMethod.GET)
			.name("swagger.apis")
			.format(Format.JSON);
		
		// TODO: add flags and parameters to route builders here...

		return this;
	}

	@Override
	public void bind(RestExpress server)
	{
		controller.initialize(urlPath, server.getRouteMetadata());
	}

	// RouteBuilder route augmentation delegates.

	public SwaggerPlugin flag(String flagValue)
	{
//		for (RouteBuilder routeBuilder : routeBuilders)
//		{
//			routeBuilder.flag(flagValue);
//		}

		return this;
	}

	public SwaggerPlugin parameter(String name, Object value)
	{
//		for (RouteBuilder routeBuilder : routeBuilders)
//		{
//			routeBuilder.parameter(name, value);
//		}

		return this;
	}
}
