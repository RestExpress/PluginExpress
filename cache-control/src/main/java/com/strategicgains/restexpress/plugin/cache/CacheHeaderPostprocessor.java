/*
    Copyright 2011, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.plugin.cache;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CACHE_CONTROL;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.EXPIRES;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.PRAGMA;

import java.util.Date;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Flags;
import org.restexpress.Parameters;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.HttpHeaderTimestampAdapter;

/**
 * For GET and HEAD requests, adds caching control headers.  May be used in conjunction
 * with DateHeaderPostprocessor to add Date header for GET and HEAD requests.
 * <p/>
 * If the route has a Parameters.Cache.MAX_AGE parameter, whose value is the
 * max-age in seconds then the following are added:
 * Cache-Control: max-age=<seconds><br/>
 * Expires: now + max-age
 * <p/>
 * If the route has a Flags.Cache.NO_CACHE flag, then the following
 * headers are set on the response:
 * Cache-Control: no-cache<br/>
 * Pragma: no-cache
 * <p/>
 * The MAX_AGE parameter takes precidence, in that, if present, the NO_CACHE flag
 * is ignored.
 * <p/>
 * To use: simply add server.addPostprocessor(new CacheHeaderPostprocessor()); in your main() method.
 * 
 * @author toddf
 * @since Oct 3, 2011
 */
public class CacheHeaderPostprocessor
implements Postprocessor
{
    private static final String NO_CACHE = "no-cache";

    DateAdapter fmt = new HttpHeaderTimestampAdapter();

	@Override
	public void process(Request request, Response response)
	{
		if (!request.isMethodGet() && !HttpMethod.HEAD.equals(request.getHttpMethod())) return;

		Object maxAge = request.getParameter(Parameters.Cache.MAX_AGE);

		if (maxAge != null)
		{
			response.addHeader(CACHE_CONTROL, String.format("max-age=%s", maxAge));
			response.addHeader(EXPIRES, fmt.format(computeExpiresDate((Integer) maxAge)));
		}
		else
		{
			if (request.isFlagged(Flags.Cache.DONT_CACHE))
			{
				response.addHeader(CACHE_CONTROL, NO_CACHE);
				response.addHeader(PRAGMA, NO_CACHE);
			}
		}
	}

	/**
     * @param maxAge
     */
    private Date computeExpiresDate(Integer maxAge)
    {
    	long millis = System.currentTimeMillis() + (((long) maxAge) * 1000l);
	    return new Date(millis);
    }
}
