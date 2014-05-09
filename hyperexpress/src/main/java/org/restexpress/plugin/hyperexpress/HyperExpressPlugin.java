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

import org.restexpress.ContentType;
import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractPlugin;

import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.ResourceFactoryStrategy;
import com.strategicgains.hyperexpress.domain.hal.HalResourceFactory;

/**
 * @author toddf
 * @since May 7, 2014
 */
public class HyperExpressPlugin
extends AbstractPlugin
{
	private Class<?> domainMarkerClass;

	/**
	 * Default constructor. Use this constructor if your domain classes implement the Linkable interface.
	 * 
	 * @see Linkable
	 */
	public HyperExpressPlugin()
	{
		this(Linkable.class);
	}

	/**
	 * Use this constructor to indicate the base class or interface that your domain model extends.
	 * Instances of this marker class will be converted to HyperExpress Resources and links injected
	 * into them.
	 * 
	 * @param domainMarkerClass the base class or interface to indicate your linkable domain objects.
	 */
	public HyperExpressPlugin(Class<?> domainMarkerClass)
	{
		super();
		this.domainMarkerClass = domainMarkerClass;
	}

	/**
	 * Register the HyperExpress plugin with a RestExpress server instance. Must occur before RestExpress.bind().
	 * 
	 * @param server the RestExpress server instance.
	 */
	@Override
    public HyperExpressPlugin register(RestExpress server)
    {
		if (isRegistered()) return this;

		server
		    .addPreprocessor(new RequestHeaderTokenBinder())
		    .addPostprocessor(new LinkInjectionPostprocessor(domainMarkerClass));
		
	    return (HyperExpressPlugin) super.register(server);
    }

	@Override
    public void bind(RestExpress server)
    {
		ResourceFactoryStrategy hal = new HalResourceFactory();
		HyperExpress.registerResourceFactoryStrategy(hal, ContentType.JSON);
		HyperExpress.registerResourceFactoryStrategy(hal, ContentType.HAL_JSON);
	    super.bind(server);
    }

	public HyperExpressPlugin addResourceFactory(ResourceFactoryStrategy factoryStrategy, String contentType)
	{
		HyperExpress.registerResourceFactoryStrategy(factoryStrategy, contentType);
		return this;
	}
}
