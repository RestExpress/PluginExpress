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

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.domain.Resource;

/**
 * If the Response contains an instance of the given domainMarkerClass, or a Collection (or Array)
 * of the same, generates a HyperExpress Resource from it, injecting links and namespaces
 * as applicable.
 * <p/>
 * This postprocessor works in conjunction with HyperExpress relationship definitions, token bindings
 * and Jackson-based custom serializers. It will copy any extenders of the domainMarkerClass into
 * an instance of Resource, depending on the content type of the requested Accept header.
 * 
 * @author toddf
 * @since Apr 21, 2014
 * @see HyperExpress
 */
public class LinkInjectionPostprocessor
implements Postprocessor
{
	private Class<?> domainMarker;

	public LinkInjectionPostprocessor(Class<?> domainMarkerClass)
	{
		super();
		this.domainMarker = domainMarkerClass;
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

				if (domainMarker.isAssignableFrom((Class<?>) t))
				{
					shouldHALify = true;
					// TODO: do sensible defaults, but allow caller to set 'rel'.
					// maybe this method simply returns the elements and they get embedded here.
					r = HyperExpress.createCollectionResource((Collection<Object>) body, (Class<?>) t,
							response.getSerializationSettings().getMediaType());
				}
			}
		}
		else if (bodyClass.isArray())
		{
			if (isDomainClass(bodyClass.getComponentType()))
			{
				shouldHALify = true;
				// TODO: do sensible defaults, but allow caller to set 'rel'.
				// maybe this method simply returns the elements and they get embedded here.
				r = HyperExpress.createCollectionResource(Arrays.asList((Object[]) body), bodyClass.getComponentType(),
					response.getSerializationSettings().getMediaType());
			}
		}

		if (shouldHALify)
		{
			response.setBody(r);
		}

		HyperExpress.clearTokenBindings();
	}

	private boolean isDomainClass(Class<?> aClass)
	{
		return domainMarker.isAssignableFrom(aClass);
	}

	private boolean isCollection(Class<?> aClass)
	{
		return (Collection.class.isAssignableFrom(aClass));
	}
}
