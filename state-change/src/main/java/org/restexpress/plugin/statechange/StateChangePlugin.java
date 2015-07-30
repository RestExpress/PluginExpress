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
package org.restexpress.plugin.statechange;

import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractPlugin;

/**
 * The StateChangePlugin enables augmentation of the request with enriched data, which is passed via
 * the StateContext down to lower layers of the service stack. It is with the MMC
 * that lower levels of the service can access this enrichment data.
 * </p>
 * The plugin supports the concept of Enrichers, which when added via the enrichedBy(Enricher) method,
 * are executed during the RestExpress preprocessor life-cycle to extract data from the request. Additionally,
 * individual controller methods can place data in the StateContext via calls to StateContext.put(String, Object).
 * </p>
 * Enrichers implement the Enricher interface, providing an implementation of the enrich(Request) method.
 * 
 * @author toddf
 * @since Feb 18, 2014
 */
public class StateChangePlugin
extends AbstractPlugin
{
	public static final String EFFECTIVE_HTTP_METHOD = "effectiveHttpMethod";
	public static final String HREF = "href";
	public static final String HTTP_METHOD = "httpMethod";
	public static final String TOKEN = "token";

	private StateChangePreprocessor preprocessor = new StateChangePreprocessor();

	@Override
	public StateChangePlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);

		server
			.addPreprocessor(preprocessor)
			.addFinallyProcessor(new StateChangePostprocessor());

		return this;
	}

	public StateChangePlugin enrichedBy(Enricher enricher)
	{
		preprocessor.enrichedBy(enricher);
		return this;
	}
}
