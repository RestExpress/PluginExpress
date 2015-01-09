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

import com.strategicgains.util.date.HttpHeaderTimestampAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author toddf
 * @since Oct 4, 2011
 */
public class DateHeaderPostprocessorTest
{
	private Postprocessor processor = new DateHeaderPostprocessor();

	@Test
	public void shouldAddDateHeaderOnGet()
	throws ParseException
	{
		FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo?param1=bar&param2=blah&yada");
		httpRequest.headers().add("Host", "testing-host");
		Response response = new Response();
		processor.process(new Request(httpRequest, null), response);
		assertTrue(response.hasHeaders());
		String dateHeader = response.getHeader(HttpHeaders.Names.DATE);
		assertNotNull(dateHeader);
		Date dateValue = new HttpHeaderTimestampAdapter().parse(dateHeader);
		assertNotNull(dateValue);
	}

	@Test
	public void shouldAddDateHeaderOnHead()
	throws ParseException
	{
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.HEAD, "/foo?param1=bar&param2=blah&yada");
		httpRequest.headers().add("Host", "testing-host");
		Response response = new Response();
		processor.process(new Request(httpRequest, null), response);
		assertTrue(response.hasHeaders());
		String dateHeader = response.getHeader(HttpHeaders.Names.DATE);
		assertNotNull(dateHeader);
		Date dateValue = new HttpHeaderTimestampAdapter().parse(dateHeader);
		assertNotNull(dateValue);
	}

	@Test
	public void shouldNotAddDateHeaderOnPost()
	{
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/foo?param1=bar&param2=blah&yada");
		httpRequest.headers().add("Host", "testing-host");
		Response response = new Response();
		processor.process(new Request(httpRequest, null), response);
		assertFalse(response.hasHeader(HttpHeaders.Names.DATE));
	}

	@Test
	public void shouldNotAddDateHeaderOnPut()
	{
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/foo?param1=bar&param2=blah&yada");
		httpRequest.headers().add("Host", "testing-host");
		Response response = new Response();
		processor.process(new Request(httpRequest, null), response);
		assertFalse(response.hasHeader(HttpHeaders.Names.DATE));
	}

	@Test
	public void shouldNotAddDateHeaderOnDelete()
	{
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, "/foo?param1=bar&param2=blah&yada");
		httpRequest.headers().add("Host", "testing-host");
		Response response = new Response();
		processor.process(new Request(httpRequest, null), response);
		assertFalse(response.hasHeader(HttpHeaders.Names.DATE));
	}

	@Test
	public void shouldNotAddDateHeaderOnOptions()
	{
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.OPTIONS, "/foo?param1=bar&param2=blah&yada");
		httpRequest.headers().add("Host", "testing-host");
		Response response = new Response();
		processor.process(new Request(httpRequest, null), response);
		assertFalse(response.hasHeader(HttpHeaders.Names.DATE));
	}
}
