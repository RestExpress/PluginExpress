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

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
	private static final RestExpress SERVER = new RestExpress();
	private static final int SERVER_PORT = 9001;

	private HttpClient http = new DefaultHttpClient();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		SERVER.uri("/tests", new TestController());

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
	public void test()
	throws Exception
	{
		HttpGet request = new HttpGet("http://localhost:9001/tests");
		HttpResponse response = (HttpResponse) http.execute(request);
		String result = response.getEntity().toString();
		System.out.println(result);
	}

	@SuppressWarnings("unused")
	private static class TestController
	{
		public DummyObject create(Request request, Response response)
		{
			return null;
		}

		public DummyObject read(Request request, Response response)
		{
			return null;
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
			return null;
		}
	}
}
