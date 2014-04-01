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
package com.strategicgains.restexpress.plugin.swagger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strategicgains.restexpress.plugin.swagger.domain.ApiModel;
import com.strategicgains.restexpress.plugin.swagger.domain.TypeNode;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This class implements a simplified least-common denominator reflection-based
 * introspector in roughly the same behavior as the serializers supported in
 * RestExpress (jackson/gson)
 * 
 * Research notes: In developing this class, it was discovered that jackson has
 * facilities to generate a variant of json-schema directly from an
 * ObjectMapper. However, as of this writing, it didn't support handling the
 * swagger-annotations. It also produced schema that while obviously
 * syntactically correct, didn't leverage the "$ref" construct and produce the
 * "flat" model structure that the Swagger spec prefers.
 * 
 * Furthermore, the serializer is overridden with something non-jackson, I
 * didn't want to tie the swagger model generation to APIs that get unhooked.
 * 
 * So, with all that said, this class, much like gson and jackson:
 * <ul>
 * <li>any top-level class that is to be interpreted as a swagger "complex"
 * type, MUST be annotated with ApiModel</li>
 * <li>non-static, non-transient private fields with the ApiModelProperty
 * annotation are considered for properties of complex types, any
 * accessors/getters methods are ignored</li>
 * <li>arrays and classes that implement java.util.Collection are treated as the
 * "container" type. Additionally, classes that implement java.util.Set have the
 * "uniqueItems" property set to true.</li>
 * <li>Properties from the class and all it's ancestors are scanned. If
 * properties duplicate in the class hierarchy, the "lowest" subclass takes
 * precedence over ancestors.</li>
 * </ul>
 * 
 * @author russtrotter
 */
public class ModelResolver
{
	private Map<String, ApiModel> models;

	public ModelResolver()
	{
		this(new HashMap<String, ApiModel>());
	}

	public ModelResolver(Map<String, ApiModel> models)
	{
		this.models = models;
	}

	public TypeNode resolve(Class<?> cls)
	{
		return createNode(cls);
	}

	private ApiModel resolveClass(Class<?> target)
	{
		String id = target.getSimpleName();
		ApiModel model = models.get(id);
		if (model != null)
		{
			return model;
		}

		if (target.isPrimitive())
		{
			throw new IllegalArgumentException("Unable to resolve primitive class: " + target);
		}

		if (target.isSynthetic())
		{
			throw new IllegalArgumentException("Unable to resolve synthetic class: " + target);
		}

		if (target.isArray())
		{
			throw new IllegalArgumentException("Unable to use arrays for models: " + target);
		}

		// com.wordnik.swagger.annotations.ApiModel apiModel =
		// target.getAnnotation(com.wordnik.swagger.annotations.ApiModel.class);
		// if (apiModel == null) {
		// throw new IllegalArgumentException("Missing ApiModel annotation on: "
		// + target);
		// }

		model = new ApiModel().id(id);
		models.put(id, model);
		Map<String, TypeNode> properties = new HashMap<String, TypeNode>();

		for (Class<?> cls = target; !Object.class.equals(cls) && cls != null; cls = cls.getSuperclass())
		{
			processFields(properties, cls);
		}

		List<TypeNode> sorted = new ArrayList<TypeNode>(properties.values());

		// sort the properties based on property "position" attribute of
		// annotation
		Collections.sort(sorted, new Comparator<TypeNode>()
		{
			@Override
			public int compare(TypeNode o1, TypeNode o2)
			{
				return o1.getPosition() - o2.getPosition();
			}
		});

		for (TypeNode property : sorted)
		{
			model.addProperty(property);

			if (property.isRequired())
			{
				model.addRequired(property.getProperty());
			}
		}

		return model;
	}

	private TypeNode createNode(Type target)
	{
		TypeNode node = new TypeNode();

		if (target instanceof Class)
		{
			Class<?> targetClass = (Class<?>) target;

			if (String.class.equals(target))
			{
				node.type("string").primitive(true);
			}
			else if (Integer.class.equals(target) || Integer.TYPE.equals(target))
			{
				node.type("integer").format("int32").primitive(true);
			}
			else if (Long.class.equals(target) || Long.TYPE.equals(target))
			{
				node.type("integer").format("int64").primitive(true);
			}
			else if (Boolean.class.equals(target) || Boolean.TYPE.equals(target))
			{
				node.type("boolean").primitive(true);
			}
			else if (Float.class.equals(target) || Float.TYPE.equals(target))
			{
				node.type("number").format("float").primitive(true);
			}
			else if (Double.class.equals(target) || Double.TYPE.equals(target))
			{
				node.type("number").format("double").primitive(true);
			}
			else if (Byte.class.equals(target) || Byte.TYPE.equals(target))
			{
				node.type("string").format("byte").primitive(true);
			}
			else if (Date.class.equals(target))
			{
				node.type("string").format("date").primitive(true);
			}
			else if (targetClass.isArray())
			{
				node.type("array");
				TypeNode componentModel = createNode(targetClass.getComponentType());
				node.items(componentModel);
			}
			else if (targetClass.isEnum())
			{
				node.type("string").primitive(true);

				for (Object obj : targetClass.getEnumConstants())
				{
					node.addEnum(obj.toString());
				}
			}
			else if (Void.class.equals(target) || Void.TYPE.equals(target))
			{
				node.type("void").primitive(true);
			}
			else
			{
				ApiModel subModel = resolveClass(targetClass);
				node.ref(subModel.getId());
			}
		}
		else if (target instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) target;
			Class<?> rawType = (Class<?>) parameterizedType.getRawType();

			if (Collection.class.isAssignableFrom(rawType))
			{
				node.type("array");

				if (Set.class.isAssignableFrom(rawType))
				{
					node.uniqueItems(true);
				}

				TypeNode componentModel = createNode(parameterizedType.getActualTypeArguments()[0]);
				node.items(componentModel);
			}
			else if(Map.class.isAssignableFrom(rawType))
			{
				
			}
			else
			{
				throw new IllegalArgumentException("Unhandled generic type: " + target);
			}
		}
		else
		{
			throw new UnsupportedOperationException("Unhandled type: " + target);
		}

		return node;
	}

	private void processFields(Map<String, TypeNode> properties, Class<?> target)
	{
		for (Field field : target.getDeclaredFields())
		{
			if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) == 0)
			{
				ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
				if (apiModelProperty != null && !properties.containsKey(field.getName()))
				{
					TypeNode property = createNode(field.getGenericType())
					    .description(apiModelProperty.notes())
					    .required(apiModelProperty.required())
					    .position(apiModelProperty.position())
					    .property(field.getName());
					properties.put(field.getName(), property);
				}
				else
				{
					TypeNode property = createNode(field.getGenericType())
						.property(field.getName());
					properties.put(field.getName(), property);
				}
			}
		}
	}
}
