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

import java.util.ArrayList;
import java.util.List;

import org.restexpress.Request;
import org.restexpress.pipeline.Preprocessor;

/**
 * By default, adds httpMethod, href (requested URL), effectiveHttpMethod, and authorization, if the Authorization
 * header is present, to the StateContext.
 * <p/>
 * Additional items may be added to the MMC by adding Enricher instances to the preprocessor via the enrichedBy()
 * method.  These enrichers can pull data from the Request and put it in the StateContext as desired. 
 * 
 * @author toddf
 * @since Feb 18, 2014
 */
public class StateChangePreprocessor
implements Preprocessor
{
	private List<Enricher> enrichers = new ArrayList<Enricher>();

	@Override
	public void process(Request request)
	{
		StateContext.put("httpMethod", request.getHttpMethod());
		StateContext.put("href", request.getUrl());
		StateContext.put("effectiveHttpMethod", request.getEffectiveHttpMethod());

		String token = request.getHeader("Authorization");

		if (token != null)
		{
			StateContext.put("authorization", token);
		}

		for (Enricher enricher : enrichers)
		{
			enricher.enrich(request);
		}
	}

	public StateChangePreprocessor enrichedBy(Enricher enricher)
	{
		if (!enrichers.contains(enricher))
		{
			enrichers.add(enricher);
		}

		return this;
	}
}
