/*
    Copyright 2015, Strategic Gains, Inc.

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
package org.restexpress.plugin.correlationid;

import java.util.UUID;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.plugin.AbstractPlugin;
import org.restexpress.util.RequestContext;

/**
 * This RestExpress plugin retrieves the Correlation-ID header from the request
 * and adds it to the global, thread-safe RequestContext to be available in deeper
 * levels of the application.
 * 
 * If the Correlation-Id header does not exist on the request, one is assigned
 * using a UUID string.
 * 
 * The Correlation-Id header is assigned to the response on its way out, also.
 * 
 * @author tfredrich
 * @since Jul 25, 2015
 */
public class CorrelationIdPlugin
extends AbstractPlugin
implements Preprocessor, Postprocessor
{
	public static final String CORRELATION_ID = "Correlation-Id";

	@Override
    public AbstractPlugin register(RestExpress server)
    {
		if (isRegistered()) return this;

		super.register(server);
		server.addPreprocessor(this);
		server.addPostprocessor(this);
		return this;
    }

	/**
	 * Preprocessor method to pull the Correlation-Id header or assign one
	 * as well as placing it in the RequestContext.
	 * 
	 * @param request the incoming Request.
	 */
	@Override
	public void process(Request request)
	{
		String correlationId = request.getHeader(CORRELATION_ID);

		// Generation on empty causes problems, since the request already has a value for Correlation-Id in this case.
		// The method call request.addHeader() only adds ANOTHER header to the request and doesn't fix the problem.
		// Currently then, the plugin only adds the header if it doesn't exist, assuming that if a client sets the
		// header to ANYTHING, that's the correlation ID they desired.
		if (correlationId == null) // || correlationId.trim().isEmpty())
		{
			correlationId = UUID.randomUUID().toString();
		}

		RequestContext.put(CORRELATION_ID, correlationId);
		request.addHeader(CORRELATION_ID, correlationId);
	}

	/**
	 * Postprocessor method that assigns the Correlation-Id from the
	 * request to a header on the Response.
	 * 
	 * @param request the incoming Request.
	 * @param response the outgoing Response.
	 */
	@Override
    public void process(Request request, Response response)
    {
		if (!response.hasHeader(CORRELATION_ID))
		{
			response.addHeader(CORRELATION_ID, request.getHeader(CORRELATION_ID));
		}
    }
}
