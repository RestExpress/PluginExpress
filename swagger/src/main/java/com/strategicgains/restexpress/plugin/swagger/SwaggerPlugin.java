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
import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.domain.metadata.RouteMetadata;
import com.strategicgains.restexpress.domain.metadata.ServerMetadata;
import com.strategicgains.restexpress.plugin.AbstractPlugin;
import com.strategicgains.restexpress.route.RouteBuilder;

/**
 * @author toddf
 * @since Nov 21, 2013
 */
public class SwaggerPlugin
extends AbstractPlugin
{
	private SwaggerController controller = new SwaggerController();
	private List<RouteBuilder> routeBuilders = new ArrayList<RouteBuilder>();
	private String urlPath;

	public SwaggerPlugin()
	{
		this("/routes/swagger");
	}

	public SwaggerPlugin(String urlPath)
	{
		super();
		this.urlPath = urlPath;
	}

	@Override
	public SwaggerPlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);
		RouteBuilder builder;

		builder = server
		    .uri(urlPath, controller)
		    .method(HttpMethod.GET)
		    .name("swagger.metadata")
		    .format(Format.JSON);
		routeBuilders.add(builder);

		server.alias("service", ServerMetadata.class);
		server.alias("route", RouteMetadata.class);
		return this;
	}

	@Override
	public void bind(RestExpress server)
	{
		controller.setMetadata(server.getRouteMetadata());
	}

	// RouteBuilder route augmentation delegates.

	public SwaggerPlugin flag(String flagValue)
	{
		for (RouteBuilder routeBuilder : routeBuilders)
		{
			routeBuilder.flag(flagValue);
		}

		return this;
	}

	public SwaggerPlugin parameter(String name, Object value)
	{
		for (RouteBuilder routeBuilder : routeBuilders)
		{
			routeBuilder.parameter(name, value);
		}

		return this;
	}
}
