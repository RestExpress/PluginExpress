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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * @author russtrotter
 */
public class DataType
{
	private String type;
	private String format;
	private String $ref;
	private Boolean uniqueItems;
	private String description;
	private Items items;

	// Note, since "enum" is a java reserved word, we have to override the
	// serialized name
	@JsonProperty("enum")
	private Set<String> enumeration;

	// The transient fields disable gson/jackson serialization. We only use it
	// during model building
	private transient boolean primitive = false;
	private transient String property;
	private transient boolean required;
	private transient int position = 0;

	public String getType()
	{
		return type;
	}

	public DataType setType(Primitives primitive)
	{
		setType(primitive.type());
		setFormat(primitive.format());
		setPrimitive(true);
		return this;
	}

	public DataType setType(String type)
	{
		this.type = type;
		return this;
	}

	public String getFormat()
	{
		return format;
	}

	public DataType setFormat(String format)
	{
		this.format = format;
		return this;
	}

	public String getRef()
	{
		return $ref;
	}

	public DataType setRef(String ref)
	{
		this.$ref = ref;
		return this;
	}

	public Boolean getUniqueItems()
	{
		return uniqueItems;
	}

	public DataType setUniqueItems(Boolean uniqueItems)
	{
		this.uniqueItems = uniqueItems;
		return this;
	}

	public boolean isRequired()
	{
		return required;
	}

	public DataType setRequired(boolean required)
	{
		this.required = required;
		return this;
	}

	public String getDescription()
	{
		return description;
	}

	public DataType setDescription(String description)
	{
		// treat empty strings as nulls so we don't get JSON
		// generated for this field in those cases
		if (description != null && description.length() > 0)
		{
			this.description = description;
		}

		return this;
	}

	public Items getItems()
	{
		return items;
	}

	public DataType setItems(Items items)
	{
		this.items = items;
		return this;
	}

	public int getPosition()
	{
		return position;
	}

	public DataType setPosition(int position)
	{
		this.position = position;
		return this;
	}

	public String getProperty()
	{
		return property;
	}

	public DataType setProperty(String property)
	{
		this.property = property;
		return this;
	}

	public DataType addEnum(String value)
	{
		if (enumeration == null)
		{
			enumeration = new HashSet<String>();
		}

		enumeration.add(value);
		return this;
	}

	public boolean isPrimitive()
	{
		return primitive;
	}

	public DataType setPrimitive(boolean primitive)
	{
		this.primitive = primitive;
		return this;
	}
}
