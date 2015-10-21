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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import io.netty.handler.codec.http.HttpMethod;

import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;

/**
 * @author tfredrich
 * @since Jul 25, 2015
 */
public class CorrelationIdPluginTest
{
	private static final RestExpress SERVER = new RestExpress();

	private HttpClient http = new DefaultHttpClient();

	@BeforeClass
	public static void beforeClass()
	{
		SERVER.registerPlugin(new CorrelationIdPlugin());
		SERVER.uri("/test", new TestController())
			.method(HttpMethod.GET)
			.noSerialization();
		SERVER.bind();
	}

	@AfterClass
	public static void afterClass()
	{
		SERVER.shutdown(false);
	}

	@Test
	public void shouldGenerateCorrelationIdOnNull()
	throws Exception
	{
		HttpGet request = new HttpGet("http://localhost:8081/test");
		HttpResponse response = (HttpResponse) http.execute(request);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity);
		assertNotNull(json);
		UUID.fromString(json);
	}

	// Generation on empty causes problems, since the request already has a value for Correlation-Id in this case.
	// The method call request.addHeader() only adds ANOTHER header to the request and doesn't fix the problem.
	// Currently then, the plugin only adds the header if it doesn't exist, assuming that if a client sets the
	// header to ANYTHING, that's the correlation ID they desired.
//	@Test
//	public void shouldGenerateCorrelationIdOnEmpty()
//	throws Exception
//	{
//		HttpGet request = new HttpGet("http://localhost:8081/test");
//		request.setHeader(CorrelationIdPlugin.CORRELATION_ID, "");
//		HttpResponse response = (HttpResponse) http.execute(request);
//		HttpEntity entity = response.getEntity();
//		String json = EntityUtils.toString(entity);
//		assertNotNull(json);
//		UUID.fromString(json);
//	}

	@Test
	public void shouldUseExistingCorrelationId()
	throws Exception
	{
		String myCorrelationId = "myArbitrarilyAssignedCorrelationId";
		HttpGet request = new HttpGet("http://localhost:8081/test");
		request.setHeader(CorrelationIdPlugin.CORRELATION_ID, myCorrelationId);
		HttpResponse response = (HttpResponse) http.execute(request);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity);
		assertNotNull(json);
		assertEquals(myCorrelationId, json);
	}

	private static class TestController
	{
		@SuppressWarnings("unused")
        public String read(Request req, Response resp)
		{
			return req.getHeader(CorrelationIdPlugin.CORRELATION_ID);
		}
	}

	@Test
	public void shouldSetCorrelationIdToResponse()
	throws Exception
	{
		String myCorrelationId = "myArbitrarilyAssignedCorrelationId";
		HttpGet request = new HttpGet("http://localhost:8081/test");
		request.addHeader(CorrelationIdPlugin.CORRELATION_ID, myCorrelationId);
		HttpResponse response = (HttpResponse) http.execute(request);
		Header[] headers = response.getHeaders(CorrelationIdPlugin.CORRELATION_ID);
		assertEquals(1, headers.length);
		String header = headers[0].getValue();
		assertEquals(myCorrelationId, header);
	}
}
