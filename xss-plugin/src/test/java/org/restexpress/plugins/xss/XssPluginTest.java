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
package org.restexpress.plugins.xss;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;

/**
 * @author fredta2
 * @since Apr 1, 2014
 */
public class XssPluginTest
{
	private static final String COLLECTION_URL = "http://localhost:9001/tests";
	private static final String ITEM_URL = COLLECTION_URL + "/12345";

	private static final RestExpress SERVER = new RestExpress();
	private static final int SERVER_PORT = 9001;

	private HttpClient http = new DefaultHttpClient();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		SERVER.uri("/tests", new TestController())
			.action("readAll", HttpMethod.GET);

		SERVER.uri("/tests/{id}", new TestController());

		new XssPlugin().register(SERVER);
		SERVER.bind(SERVER_PORT);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		SERVER.shutdown();
	}

	@Test
	public void shouldEncodeCollection()
	throws Exception
	{
		HttpGet request = new HttpGet(COLLECTION_URL);
		HttpResponse response = (HttpResponse) http.execute(request);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
		assertEquals("[{\"value\":\"some stuff here\"},{\"value\":\"{'name':'<script>alert(\\\"hello there\\\");</script>'}\"},{\"value\":\"{'name':'Joe Blow','phone':'(303) 111-2222'}\"}]", result);
	}

	@Test
	public void shouldNotEncodeItem()
	throws Exception
	{
		HttpGet request = new HttpGet(ITEM_URL);
		HttpResponse response = (HttpResponse) http.execute(request);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
		assertEquals("{\"value\":\"{'name':'Joe Blow','phone':'(303) 111-2222'}\"}", result);
	}

	@Test
	public void shouldEncodeItem()
	throws Exception
	{
		HttpPost request = new HttpPost(ITEM_URL);
		HttpResponse response = (HttpResponse) http.execute(request);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
		assertEquals("{\"value\":\"{'name':'<script>alert(\\\"hello there\\\");</script>'}\"}", result);
	}

	@Test
	public void shouldHandleVoid()
	throws Exception
	{
		HttpDelete request = new HttpDelete(ITEM_URL);
		HttpResponse response = (HttpResponse) http.execute(request);
		String result = EntityUtils.toString(response.getEntity());
		assertEquals("", result);
	}

	@Test
	public void shouldHandleNull()
	throws Exception
	{
		HttpPut request = new HttpPut(ITEM_URL);
		HttpResponse response = (HttpResponse) http.execute(request);
		String result = EntityUtils.toString(response.getEntity());
		assertEquals("", result);
	}

	@SuppressWarnings("unused")
	private static class TestController
	{
		public DummyObject create(Request request, Response response)
		{
			return new DummyObject("{'name':'<script>alert(\"hello there\");</script>'}");
		}

		public DummyObject read(Request request, Response response)
		{
			return new DummyObject("{'name':'Joe Blow','phone':'(303) 111-2222'}");
		}

		public DummyObject update(Request request, Response response)
		{
			return null;
		}

		public void delete(Request request, Response response)
		{
		}

		public List<DummyObject> readAll(Request request, Response response)
		{
			List<DummyObject> objs = new ArrayList<DummyObject>();
			objs.add(new DummyObject("some stuff here"));
			objs.add(new DummyObject("{'name':'<script>alert(\"hello there\");</script>'}"));
			objs.add(new DummyObject("{'name':'Joe Blow','phone':'(303) 111-2222'}"));
			return objs;
		}
	}
}
