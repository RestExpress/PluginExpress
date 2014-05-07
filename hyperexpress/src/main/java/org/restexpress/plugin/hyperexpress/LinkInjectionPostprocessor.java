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
package org.restexpress.plugin.hyperexpress;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.domain.Resource;
import com.strategicgains.hyperexpress.util.Pluralizer;

/**
 * If the Response contains an instance of the given domainBaseClass, or a Collection (or Array)
 * of the same, generates a HyperExpress Resource from it, injecting links and namespaces
 * as applicable.
 * <p/>
 * This postprocessor works in conjunction with HyperExpress relationship definitions, token bindings
 * and Jackson-based custom serializers.
 * 
 * @author toddf
 * @since Apr 21, 2014
 * @see HyperExpress
 */
public class LinkInjectionPostprocessor
implements Postprocessor
{
	private Class<?> domainClass;

	public LinkInjectionPostprocessor(Class<?> domainBaseClass)
	{
		super();
		this.domainClass = domainBaseClass;
	}

	@SuppressWarnings("unchecked")
    @Override
	public void process(Request request, Response response)
	{
		Object body = response.getBody();

		if (body == null || !response.isSerialized()) return;

		boolean shouldHALify = false;
		Resource r = null;
		Class<?> bodyClass = body.getClass();

		if (isDomainClass(bodyClass))
		{
			shouldHALify = true;
			r = HyperExpress.createResource(body, response.getSerializationSettings().getMediaType());
		}
		else if (isCollection(bodyClass))
		{
			Type type = request.getResolvedRoute().getAction().getGenericReturnType();

			if (type instanceof ParameterizedType)
			{
				Type t = (((ParameterizedType) type).getActualTypeArguments())[0];

				if (domainClass.isAssignableFrom((Class<?>) t))
				{
					shouldHALify = true;
					r = createEmbeddedCollectionResource((Class<?>)t, (Collection<Object>) body,
						response.getSerializationSettings().getMediaType());
				}
			}
		}
		else if (bodyClass.isArray())
		{
			if (isDomainClass(bodyClass.getComponentType()))
			{
				shouldHALify = true;
				r = createEmbeddedCollectionResource(bodyClass.getComponentType(), Arrays.asList((Object[]) body),
					response.getSerializationSettings().getMediaType());
			}
		}

		if (shouldHALify)
		{
			response.setBody(r);
		}

		HyperExpress.clearTokenBindings();
	}

	@SuppressWarnings("unchecked")
    private Resource createEmbeddedCollectionResource(Class<?> childType, Collection<Object> children, String contentType)
	{
		Resource r = HyperExpress.createCollectionResource(childType, contentType);
		Resource childResource = null;
		String childRel = Pluralizer.pluralize(childType.getSimpleName().toLowerCase());

		for (Object child : children)
		{
			HyperExpress.bindTokensFor(child);
			childResource = HyperExpress.createResource(child, contentType);
			r.addResource(childRel, childResource);
		}

		if (children.isEmpty())
		{
			r.addResources(childRel, Collections.EMPTY_LIST);
		}

		return r;
	}

	private boolean isDomainClass(Class<?> aClass)
	{
		return domainClass.isAssignableFrom(aClass);
	}

	private boolean isCollection(Class<?> aClass)
	{
		return (Collection.class.isAssignableFrom(aClass));
	}
}
