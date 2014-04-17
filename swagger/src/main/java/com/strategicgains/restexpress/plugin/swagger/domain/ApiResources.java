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
import java.util.Collections;
import java.util.List;

/**
 * @author toddf
 * @since Nov 21, 2013
 */
public class ApiResources
{
	private String apiVersion;
	private String swaggerVersion;
	private Info info;
	private Authorizations authorizations;
	private List<ApiResource> apis = new ArrayList<ApiResource>();
	
	public ApiResources(String apiVersion, String swaggerVersion)
	{
		super();
		this.apiVersion = apiVersion;
		this.swaggerVersion = swaggerVersion;
	}

	public String getApiVersion()
	{
		return apiVersion;
	}

	public String getSwaggerVersion()
	{
		return swaggerVersion;
	}

	public List<ApiResource> getApis()
	{
		return Collections.unmodifiableList(apis);
	}

	public void addApi(String path, String description)
	{
		addApi(new ApiResource(path, description));
	}

	public void addApi(ApiResource api)
	{
		apis.add(api);
	}
}
