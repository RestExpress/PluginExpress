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
package com.strategicgains.restexpress.plugin.swagger.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;
import org.restexpress.domain.metadata.RouteMetadata;

/**
 * @author toddf
 * @since Nov 21, 2013
 * @see https://github.com/wordnik/swagger-core/wiki/Api-Declaration
 */
public class ApiDeclarations
{
	private String apiVersion;
	private String swaggerVersion;
	private String basePath;
	private String resourcePath;
	private List<String> consumes = new ArrayList<String>();
	private List<String> produces = new ArrayList<String>();
	private List<ApiDeclaration> apis = new ArrayList<ApiDeclaration>();
	private Map<String, ApiModel> models = new HashMap<String, ApiModel>();

	public ApiDeclarations(ApiResources api, RestExpress server, String path)
	{
		this.apiVersion = api.getApiVersion();
		this.swaggerVersion = api.getSwaggerVersion();
		this.basePath = server.getBaseUrl();
		this.resourcePath = path;
	}

	public void addApi(ApiDeclaration api)
	{
		apis.add(api);
	}

	public ApiDeclarations consumes(String contentType)
	{
		if (!consumes.contains(contentType))
		{
			consumes.add(contentType);
		}

		return this;
	}

	public ApiDeclarations produces(String contentType)
	{
		if (!produces.contains(contentType))
		{
			produces.add(contentType);
		}

		return this;
	}

	public Map<String, ApiModel> getModels()
	{
		return models;
	}

//	public ApiDeclaration addOperation(RouteMetadata route, HttpMethod method)
//	{
//		ApiDeclaration apiDeclaration = findApiDeclarationByPath(route.getUri().getPattern());
//
//		if (apiDeclaration == null)
//		{
//			apiDeclaration = new ApiDeclaration(route.getUri().getPattern(), route.getName());
//		}
//
//		ApiOperation operation = new ApiOperation(method.getName(), route);
//		apiDeclaration.addOperation(operation);
//		return apiDeclaration;
//	}

	public ApiDeclaration findApiDeclarationByPath(String path)
	{
		for (ApiDeclaration api : apis)
		{
			if (api.getPath().equals(path))
			{
				return api;
			}
		}

		return null;
	}
}
