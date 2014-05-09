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

/**
 * @author toddf
 * @since Nov 22, 2013
 */
public class ApiOperationParameters
extends DataType
{
	private String paramType; // path, query, body, header, form
	private String name;
	private String format;
	private boolean required; // must be true for query paramType
	private Boolean allowMultiple;
	private String allowableValues; // values allowed for the parameter, matches
									// Swagger annotation value in
									// @ApiImplicitParam
	private String defaultValue;

	public ApiOperationParameters(String paramType, String name, String type,
	    boolean isRequired)
	{
		super();
		this.paramType = paramType;
		this.name = name;
		this.required = isRequired;
		setType(type);
	}

	/**
	 * Allowable values for the operation parameter.
	 * 
	 * @param value
	 */
	public void setAllowableValues(String value)
	{
		allowableValues = value;
	}

	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

}
