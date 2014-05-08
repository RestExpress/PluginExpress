/*
    Copyright 2014, Strategic Gains, Inc.

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
 * @since Apr 4, 2014
 */
public class Items
{
	private String type;
	private String format;
	private String $ref;

	public Items()
	{
		super();
	}

	public Items(DataType dataType)
	{
		this();
		this.type = dataType.getType();
		this.format = dataType.getFormat();
		this.$ref = dataType.getRef();
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public String getRef()
	{
		return $ref;
	}

	public void setRef(String ref)
	{
		this.$ref = ref;
	}
}
