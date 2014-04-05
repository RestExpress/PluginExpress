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

import org.restexpress.domain.metadata.UriMetadata;
import org.restexpress.route.Route;

/**
 * @author toddf
 * @since Nov 21, 2013
 */
public class ApiOperation
extends DataType
{
	private String method;
	private String nickname = "";
	private List<ApiOperationParameters> parameters;
	private String summary = "";
	private String notes;
	private String[] errorResponses;

	public ApiOperation(Route route)
	{
		// TODO: use Swagger annotation on controller method, if present.

		this.method = route.getMethod().getName();
		String name = route.getName();
		this.nickname = method + (name == null ? "" : " " + name);

		if (route.getUrlParameters() == null) return;

		for (String param : route.getUrlParameters())
		{
			addParameter(new ApiOperationParameters("path", param, "string", !param.equals("format")));
		}

		// TODO: determine body/input parameters.
	}

	public void addParameter(ApiOperationParameters param)
	{
		if (parameters == null)
		{
			parameters = new ArrayList<ApiOperationParameters>();
		}

		parameters.add(param);
	}
}
