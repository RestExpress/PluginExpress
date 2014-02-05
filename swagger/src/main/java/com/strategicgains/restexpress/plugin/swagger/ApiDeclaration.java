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
import java.util.Arrays;
import java.util.List;

import org.restexpress.domain.metadata.RouteMetadata;

/**
 * @author toddf
 * @since Nov 21, 2013
 * @see https://github.com/wordnik/swagger-core/wiki/Api-Declaration
 */
public class ApiDeclaration
{
	private static final List<String> VALID_METHODS = new ArrayList<String>(Arrays.asList(new String[] {"GET", "PUT","POST","DELETE"}));

	private String path;
	private String description;
	private List<ApiOperation> operations = new ArrayList<ApiOperation>();

	public ApiDeclaration(RouteMetadata route)
    {
		super();
		this.path = route.getUri().getPattern();
		this.description = route.getName();
		
		for (String method : route.getMethods())
		{
			if (VALID_METHODS.contains(method))
			{
				operations.add(new ApiOperation(method, route));
			}
		}
    }
}
