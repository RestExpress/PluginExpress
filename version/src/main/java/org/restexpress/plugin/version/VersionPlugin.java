/*
    Copyright 2014, Strategic Gains, Inc.

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
package org.restexpress.plugin.version;

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.plugin.RoutePlugin;
import org.restexpress.route.RouteBuilder;

/**
 * Provides a '/version' route URL in your API to provide the version.
 * 
 * @author toddf
 * @since Oct 17, 2014
 */
public class VersionPlugin
extends RoutePlugin
{
	private final String version;
	private String path = "/version";

	public VersionPlugin(String version)
	{
		this.version = version;
	}

	public VersionPlugin path(String path)
	{
		this.path = path;
		return this;
	}

	public String read(Request request, Response response)
	{
		return version;
	}

	@Override
	public VersionPlugin register(RestExpress restExpress)
	{
		if (isRegistered()) return this;

		super.register(restExpress);

		RouteBuilder rb = restExpress.uri(path, this)
		    .method(HttpMethod.GET)
		    .name("PluginExpress.version");

		applyFlags(rb);
		applyParameters(rb);

		return this;
	}
}
