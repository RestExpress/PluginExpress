/*
    Copyright 2011, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.plugin.route;

import java.util.ArrayList;
import java.util.List;

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.Format;
import org.restexpress.RestExpress;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.plugin.AbstractPlugin;
import org.restexpress.route.RouteBuilder;

/**
 * @author toddf
 * @since Jul 20, 2011
 */
public class RoutesMetadataPlugin
extends AbstractPlugin
{
	private RouteMetadataController controller = new RouteMetadataController();
	private List<RouteBuilder> routeBuilders = new ArrayList<RouteBuilder>();

	public RoutesMetadataPlugin()
	{
		super();
	}

	@Override
	public RoutesMetadataPlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);
		RouteBuilder builder;

		builder = server
		    .uri("/routes/metadata.{format}", controller)
		    .action("getAllRoutes", HttpMethod.GET)
		    .name("all.routes.metadata");
		routeBuilders.add(builder);

		builder = server
		    .uri("/routes/{routeName}/metadata.{format}", controller)
		    .action("getSingleRoute", HttpMethod.GET)
		    .name("single.route.metadata");
		routeBuilders.add(builder);

		server.uri("/routes/console.html", controller)
		    .action("getConsole", HttpMethod.GET)
		    .noSerialization()
		    .name("routes.metadata.console")
		    .format(Format.HTML);

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

	public RoutesMetadataPlugin flag(String flagValue)
	{
		for (RouteBuilder routeBuilder : routeBuilders)
		{
			routeBuilder.flag(flagValue);
		}

		return this;
	}

	public RoutesMetadataPlugin parameter(String name, Object value)
	{
		for (RouteBuilder routeBuilder : routeBuilders)
		{
			routeBuilder.parameter(name, value);
		}

		return this;
	}
}
