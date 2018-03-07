/*
    Copyright 2018, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.plugin.openapi;

import java.util.Map.Entry;

import org.restexpress.RestExpress;
import org.restexpress.plugin.RoutePlugin;
import org.restexpress.route.RouteBuilder;

import com.strategicgains.restexpress.plugin.openapi.domain.ContactObject;
import com.strategicgains.restexpress.plugin.openapi.domain.InfoObject;
import com.strategicgains.restexpress.plugin.openapi.domain.LicenseObject;

import io.netty.handler.codec.http.HttpMethod;

/**
 * @author toddf
 * @since Mar 6, 2018
 */
public class OpenApiPlugin
extends RoutePlugin
{
	private static final String OPENAPI_VERSION = "3.0.0";

	private OpenApiController controller;
	private String urlPath;
	private String openapi = OPENAPI_VERSION;
	private InfoObject info = new InfoObject();
	private boolean showAnnotatedOnly = false;

	public OpenApiPlugin()
	{
		this("/openapi.json");
	}

	public OpenApiPlugin(String urlPath)
	{
		super();
		this.urlPath = urlPath;
	}

	public OpenApiPlugin version(String version)
	{
		info.setVersion(version);
		return this;
	}

	public OpenApiPlugin description(String description)
	{
		info.setDescription(description);
		return this;
	}

	public OpenApiPlugin termsOfService(String terms)
	{
		info.setTermsOfService(terms);
		return this;
	}

	public OpenApiPlugin contact(String name, String url, String email)
	{
		info.setContact(new ContactObject(name, url, email));
		return this;
	}

	public OpenApiPlugin license(String name, String url)
	{
		info.setLicense(new LicenseObject(name, url));
		return this;
	}

	public OpenApiPlugin openapiVersion(String version)
	{
		this.openapi = version;
		return this;
	}

	@Override
	public OpenApiPlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);
		controller = new OpenApiController(server, openapi, info, showAnnotatedOnly());

		RouteBuilder resources = server
			.uri(urlPath, controller)
		    .method(HttpMethod.GET)
		    .name("openapi.json");

		for (String flag : flags())
		{
			resources.flag(flag);
		}

		for (Entry<String, Object> entry : parameters().entrySet())
		{
			resources.parameter(entry.getKey(), entry.getValue());
		}

		return this;
	}

	@Override
	public void bind(RestExpress server)
	{
		controller.initialize(urlPath, server);
	}

	public boolean showAnnotatedOnly()
	{
		return showAnnotatedOnly;
	}

	/**
	 * When set to true the swagger documentation is not visible unless an
	 * ApiOperation annotation exists for the controller method. This allows
	 * control over which apis are advertised and which are not visible to the
	 * public
	 * 
	 * @param defaultToHidden
	 * @return returns this (in order to chain commands)
	 */
	public OpenApiPlugin showAnnotatedOnly(boolean defaultToHidden)
	{
		this.showAnnotatedOnly = defaultToHidden;
		return this;
	}
}
