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

import java.util.Map.Entry;

import org.restexpress.RestExpress;
import org.restexpress.plugin.RoutePlugin;
import org.restexpress.route.RouteBuilder;

import com.strategicgains.restexpress.plugin.swagger.wrapper.OpenApi;

import io.netty.handler.codec.http.HttpMethod;

public class SwaggerPlugin extends RoutePlugin {
	private static final String SWAGGER_VERSION = "3.5.0";

	private SwaggerController controller;
	private String urlPath;
	
	private String swaggerVersion = SWAGGER_VERSION;
	private boolean defaultToHidden = false;

	private OpenApi openApi;
	private String basePath = "";

	public SwaggerPlugin(OpenApi openApi) {
		this("/api-docs", openApi);
	}

	public SwaggerPlugin(String urlPath, OpenApi openApi) {
		super();
		this.urlPath = urlPath;
		this.openApi = openApi;
	}

	public SwaggerPlugin swaggerVersion(String version) {
		this.swaggerVersion = version;
		return this;
	}

	@Override
	public SwaggerPlugin register(RestExpress server) {
		if (isRegistered())
			return this;

		super.register(server);
		controller = new SwaggerController(server, openApi, basePath, swaggerVersion, isDefaultToHidden());

		RouteBuilder resources = server.uri(urlPath , controller).action("readAll", HttpMethod.GET).name("swagger.resources");

		for (String flag : flags()) {
			resources.flag(flag);
		}

		for (Entry<String, Object> entry : parameters().entrySet()) {
			resources.parameter(entry.getKey(), entry.getValue());
		}

		return this;
	}

	@Override
	public void bind(RestExpress server) {
		controller.initialize(urlPath, server);
	}

	public boolean isDefaultToHidden() {
		return defaultToHidden;
	}

	/**
	 * When set to true the swagger documentation is not visible unless an
	 * ApiOperation annotation exists for the controller method. This allows control
	 * over which apis are advertised and which are not visible to the public
	 * 
	 * @param defaultToHidden
	 * @return returns this (in order to chain commands)
	 */
	public SwaggerPlugin setDefaultToHidden(boolean defaultToHidden) {
		this.defaultToHidden = defaultToHidden;
		return this;
	}

	public SwaggerPlugin setBasePath(String basePath) {
		this.basePath = basePath;
		return this;
	}
	
}
