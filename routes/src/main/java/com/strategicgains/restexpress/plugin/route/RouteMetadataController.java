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

import java.util.HashMap;
import java.util.Map;

import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.exception.NotFoundException;

/**
 * @author toddf
 * @since Jan 31, 2011
 */
public class RouteMetadataController
{
	private ServerMetadata metadata = null;
	private Map<String, RouteMetadata> routeMetadata = new HashMap<String, RouteMetadata>();

	public RouteMetadataController()
	{
		super();
	}

	public void setMetadata(ServerMetadata data)
	{
		this.metadata = data;

		routeMetadata.clear();
		for (RouteMetadata routeInfo : data.getRoutes())
		{
			// cache the named routes.
			if (routeInfo.getName() != null
			    && !routeInfo.getName().trim().isEmpty())
			{
				routeMetadata.put(routeInfo.getName().toLowerCase(), routeInfo);
			}
		}
	}

	public ServerMetadata getAllRoutes(Request request, Response response)
	{
		return metadata;
	}

	public ServerMetadata getSingleRoute(Request request, Response response)
	{
		String routeName = request.getHeader("routeName", "Route name must be provided");
		RouteMetadata routeInfo = routeMetadata.get(routeName.toLowerCase());
		
		if (routeInfo == null)
		{
			throw new NotFoundException("Route name not found: " + routeName);
		}

		ServerMetadata results = metadata.copyRootData();
		results.addRoute(routeInfo);
		return results;
	}

	public String getConsole(Request request, Response response)
	{
		response.setContentType(ContentType.HTML);
		return "<html><head></head><body><h1>Coming soon...</h1><p>Watch <a href=\"http://github.com/RestExpress/RestExpress\">RestExpress on GitHub.com</a> for more.</p></body></html>";
	}
}
