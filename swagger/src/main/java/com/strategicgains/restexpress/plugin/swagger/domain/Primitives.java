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
public enum Primitives
{
	INTEGER("integer", "int32"),
	LONG("integer", "int64"),
	FLOAT("number", "float"),
	DOUBLE("number", "double"),
	STRING("string", null),
	BYTE("string", "byte"),
	BOOLEAN("boolean", null),
	DATE("string", "date"),
	DATE_TIME("string", "date-time"),
	VOID("void", null);

	private String type;
	private String format;

	private Primitives(String type, String format)
	{
		this.type = type;
		this.format = format;
	}

	public String type()
	{
		return type;
	}

	public String format()
	{
		return format;
	}
}
