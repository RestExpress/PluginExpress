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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
	private List<ApiResponse> responseMessages;
	private Class<?> response;

	public ApiOperation(Route route)
	{
		this.parameters = new ArrayList<ApiOperationParameters>();

		// TODO: use Swagger annotation on controller method, if present.
		// Get the method from the route
		Method m = route.getAction();
		if (m
		    .isAnnotationPresent(com.wordnik.swagger.annotations.ApiOperation.class))
		{
			com.wordnik.swagger.annotations.ApiOperation ao = m
			    .getAnnotation(com.wordnik.swagger.annotations.ApiOperation.class);
			// Both value and notes are used as descriptions in ApiOperation
			// annotation.
			// value is a brief description, notes a more detailed description.
			if (ao.value() != null) summary = ao.value();
			if (ao.notes() != null) notes = ao.notes();
			if (ao.response() != null) {
				response = ao.response();
			}
		}

		this.method = route.getMethod().name();
		String name = route.getName();
		this.nickname = method + (name == null ? "" : " " + name);
		this.nickname = nickname.replaceAll("[^a-zA-Z0-9_]","");

		if (route.getUrlParameters() == null) return;

		for (String param : route.getUrlParameters())
		{
			if (param.equals("format")) continue;

			addParameter(new ApiOperationParameters("path", param, "string", true));
		}

		// TODO: determine body/input parameters.
		// For now depend on the swagger annotation for body/input specific
		// parameters.
		determineBodyInputParameters(m);

		// Check for any swagger responseMessages
		checkForSwaggerResponseAnnotations(m);

	}

	/**
	 * Add operation parameter
	 * 
	 * @param param
	 */
	public void addParameter(ApiOperationParameters param)
	{
		parameters.add(param);
	}

	/**
	 * Add response to the errorResponse list
	 * 
	 * @param response
	 */
	public void addResponse(ApiResponse response)
	{
		if (responseMessages == null)
		{
			responseMessages = new ArrayList<ApiResponse>();
		}

		responseMessages.add(response);
	}

	/**
	 * Process any swagger annotations relating to parameters passed in the
	 * body.
	 * 
	 * Swagger annotations are @ApiParam, @ApiImplicitParam and
	 * @ApiImplicitParams, a collection of @ApiImplicitParam annotations.
	 * 
	 * @param method
	 *            - Controller method
	 */
	public void determineBodyInputParameters(Method method)
	{
		if (method
		    .isAnnotationPresent(com.wordnik.swagger.annotations.ApiParam.class))
		{
			com.wordnik.swagger.annotations.ApiParam ap = method
			    .getAnnotation(com.wordnik.swagger.annotations.ApiParam.class);
			ApiOperationParameters inputParam = new ApiOperationParameters(
			    "body", ap.name(), "string", ap.required());
			if (ap.allowableValues() != null
			    && !ap.allowableValues().equals(""))
			    inputParam.setAllowableValues(ap.allowableValues());
			if (ap.value() != null && !ap.value().equals(""))
			    inputParam.setDescription(ap.value());
			if (ap.defaultValue() != null && !ap.defaultValue().equals(""))
			    inputParam.setDefaultValue(ap.defaultValue());

			addParameter(inputParam);
		}
		if (method
		    .isAnnotationPresent(com.wordnik.swagger.annotations.ApiImplicitParam.class))
		{
			com.wordnik.swagger.annotations.ApiImplicitParam aip = method
			    .getAnnotation(com.wordnik.swagger.annotations.ApiImplicitParam.class);
			ApiOperationParameters inputParam = new ApiOperationParameters(
			    aip.paramType(), aip.name(), aip.dataType(), aip.required());
			if (aip.allowableValues() != null
			    && !aip.allowableValues().equals(""))
			    inputParam.setAllowableValues(aip.allowableValues());
			if (aip.value() != null && !aip.value().equals(""))
			    inputParam.setDescription(aip.value());
			addParameter(inputParam);
		}
		if (method
		    .isAnnotationPresent(com.wordnik.swagger.annotations.ApiImplicitParams.class))
		{
			com.wordnik.swagger.annotations.ApiImplicitParams aip = method
			    .getAnnotation(com.wordnik.swagger.annotations.ApiImplicitParams.class);
			for (com.wordnik.swagger.annotations.ApiImplicitParam ip : aip
			    .value())
			{
				ApiOperationParameters inputParam = new ApiOperationParameters(
				    ip.paramType(), ip.name(), ip.dataType(), ip.required());
				if (ip.allowableValues() != null
				    && !ip.allowableValues().equals(""))
				    inputParam.setAllowableValues(ip.allowableValues());
				if (ip.value() != null && !ip.value().equals(""))
				    inputParam.setDescription(ip.value());
				addParameter(inputParam);
			}
		}
	}

	/**
	 * Process any swagger ApiResponse annotations
	 * 
	 * Swagger response annotations are @ApiResponse and @ApiResponses, a
	 * collection of @ApiResponse annotations.
	 * 
	 * @param m
	 */
	public void checkForSwaggerResponseAnnotations(Method m)
	{
		if (m
		    .isAnnotationPresent(com.wordnik.swagger.annotations.ApiResponse.class))
		{
			com.wordnik.swagger.annotations.ApiResponse ar = m
			    .getAnnotation(com.wordnik.swagger.annotations.ApiResponse.class);
			ApiResponse apiResponse = new ApiResponse(ar.code(), ar.message());
			addResponse(apiResponse);
		}
		else if (m
		    .isAnnotationPresent(com.wordnik.swagger.annotations.ApiResponses.class))
		{
			com.wordnik.swagger.annotations.ApiResponses ar = m
			    .getAnnotation(com.wordnik.swagger.annotations.ApiResponses.class);
			for (com.wordnik.swagger.annotations.ApiResponse r : ar.value())
			{
				ApiResponse apiResponse = new ApiResponse(r.code(), r.message());
				addResponse(apiResponse);
			}
		}

	}

	/**
	 * Return the response class
	 * @return
	 */
	public Class<?> getResponse() {
		return response;
	}

}
