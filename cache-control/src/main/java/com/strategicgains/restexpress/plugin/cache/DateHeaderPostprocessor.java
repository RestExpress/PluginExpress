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

import static io.netty.handler.codec.http.HttpHeaders.Names.DATE;

import java.util.Date;

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.HttpHeaderTimestampAdapter;

/**
 * For GET and HEAD requests, adds a Date: <timestamp> header, if not already present.
 * This enables clients to determine age of a representation for caching purposes.
 * <timestamp> is in RFC1123 full date format.
 * <p/>
 * To use: simply add server.addPostprocessor(new DateHeaderPostprocessor()); in your main() method.
 * <p/>
 * Note that HEAD requests are not provided with a Date header via this postprocessor.
 * This is due to the fact that most external caches forward HEAD requests to the origin
 * server as a GET request and cache the result.
 * 
 * @author toddf
 * @since Oct 3, 2011
 */
public class DateHeaderPostprocessor
implements Postprocessor
{
	DateAdapter fmt = new HttpHeaderTimestampAdapter();

	@Override
	public void process(Request request, Response response)
	{
		if ((request.isMethodGet() || HttpMethod.HEAD.equals(request.getHttpMethod()))
			&& !response.hasHeader(DATE))
		{
			Date date = new Date(System.currentTimeMillis());
			response.addHeader(DATE, fmt.format(date));
		}
	}
}
