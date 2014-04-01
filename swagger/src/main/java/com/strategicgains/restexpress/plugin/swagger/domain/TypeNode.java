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
public class TypeNode
{
	private String type;
	private String format;
	private String $ref;
	private Boolean uniqueItems;
	private String description;
	private TypeNode items;

	// Note, since "enum" is a java reserved word, we have to override the
	// serialized name
	@JsonProperty("enum")
	private Set<String> _enum;
	// The transient fields disable gson/jackson serialization. We only use it
	// during
	// model building

	private transient boolean primitive = false;
	private transient String property;
	private transient boolean required;
	private transient int position = 0;

	public String getType()
	{
		return type;
	}

	public TypeNode type(String type)
	{
		this.type = type;
		return this;
	}

	public String getFormat()
	{
		return format;
	}

	public TypeNode format(String format)
	{
		this.format = format;
		return this;
	}

	public String getRef()
	{
		return $ref;
	}

	public TypeNode ref(String ref)
	{
		this.$ref = ref;
		return this;
	}

	public Boolean getUniqueItems()
	{
		return uniqueItems;
	}

	public boolean isRequired()
	{
		return required;
	}

	public TypeNode required(boolean required)
	{
		this.required = required;
		return this;
	}

	public TypeNode uniqueItems(Boolean uniqueItems)
	{
		this.uniqueItems = uniqueItems;
		return this;
	}

	public String getDescription()
	{
		return description;
	}

	public TypeNode description(String description)
	{
		// treat empty strings as nulls so we don't get JSON
		// generated for this field in those cases
		if (description.length() > 0)
		{
			this.description = description;
		}
		return this;
	}

	public TypeNode getItems()
	{
		return items;
	}

	public TypeNode items(TypeNode items)
	{
		this.items = items;
		return this;
	}

	public int getPosition()
	{
		return position;
	}

	public TypeNode position(int position)
	{
		this.position = position;
		return this;
	}

	public String getProperty()
	{
		return property;
	}

	public TypeNode property(String property)
	{
		this.property = property;
		return this;
	}

	public TypeNode addEnum(String value)
	{
		if (_enum == null)
		{
			_enum = new HashSet<String>();
		}
		_enum.add(value);
		return this;
	}

	public boolean isPrimitive()
	{
		return primitive;
	}

	public TypeNode primitive(boolean primitive)
	{
		this.primitive = primitive;
		return this;
	}
}
