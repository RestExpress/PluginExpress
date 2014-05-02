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

import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;

import com.strategicgains.restexpress.plugin.swagger.annotations.ApiModelRequest;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * @author toddf
 * @since Nov 22, 2013
 */
public class DummyController
{
	public DummyModel read(Request request, Response response)
	{
        return null;
	}

	public List<DummyModel> readAll(Request request, Response response)
	{
		return null;
	}

    @ApiModelRequest(model=DummyModel.class, required=true)
	public void create(Request request, Response response)
	{
		response.setResponseCreated();
	}

	public void update(Request request, Response response)
	{
	}

	public void delete(Request request, Response response)
	{
		response.setResponseNoContent();
	}

	public void health(Request request, Response response)
	{
	}

	public void options(Request request, Response response)
	{
	}

	public Another readAnother(Request request, Response response)
	{
		return null;
	}
	
		@ApiOperation(value = "Read with Annotations.", 
			notes = "More detailed description here.")
	public Another readWithApiOperationAnnotation(Request request, Response response) 
	{
		return null;
	}

	@ApiResponses({
		@ApiResponse(code = 204, message = "Successful update"),
		@ApiResponse(code = 404, message = "Item not found"),
		@ApiResponse(code = 400, message = "Item id incorrect format")})
	public void updateWithApiResponse(Request request, Response response) 
	{
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "expand", required = false, value = "(Optional) Return item and all its children.", paramType = "query", dataType = "String", allowableValues = "all, some, none"),
		@ApiImplicitParam(name = "title", required = true, value = "(Required) Title of the item.", paramType = "body", dataType = "String", allowableValues = "Any string"),
	})
	public void createWithApiImplicitParams(Request request, Response response) 
	{
	}
	
	@ApiParam(name = "title", required = true, value = "(Required) Title of the item.", defaultValue = "Title placeholder", allowableValues = "Any String")
	public void createWithApiParam(Request request, Response response) 
	{
	}

}
