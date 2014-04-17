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
import java.util.List;

import org.restexpress.route.Route;

/**
 * @author toddf
 * @since Nov 21, 2013
 * @see https://github.com/wordnik/swagger-core/wiki/Api-Declaration
 */
public class ApiDeclaration
{
	private String path;
	private String description;
	private List<String> consumes;
	private List<String> produces;
	private Authorizations authorizations;
	private List<ApiOperation> operations = new ArrayList<ApiOperation>();

	public ApiDeclaration(Route route)
	{
		super();
		this.path = route.getPattern();
		this.description = route.getName();
	}

	public ApiDeclaration(String path, String description)
	{
		this.path = path;
		this.description = description;
	}

	public ApiDeclaration operation(ApiOperation operation)
	{
		operations.add(operation);
		return this;
	}

	public ApiDeclaration authorization(String key, Authorization authn)
	{
		if (authorizations == null)
		{
			authorizations = new Authorizations();
		}

		authorizations.put(key, authn);
		return this;
	}

	public ApiDeclaration consumes(String contentType)
	{
		if (consumes == null)
		{
			consumes = new ArrayList<String>();
		}

		if (!consumes.contains(contentType))
		{
			consumes.add(contentType);
		}

		return this;
	}

	public ApiDeclaration produces(String contentType)
	{
		if (produces == null)
		{
			produces = new ArrayList<String>();
		}

		if (!produces.contains(contentType))
		{
			produces.add(contentType);
		}

		return this;
	}

	public String getPath()
	{
		return path;
	}

	public String getDescription()
	{
		return description;
	}
}
