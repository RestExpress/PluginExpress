package com.strategicgains.restexpress.plugin.cors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.pipeline.SimpleConsoleLogMessageObserver;

public class CorsPluginTest
{
	private static final String URL_PATH1 = "/cors/plugin/test/1";
	private static final String URL_PATH2 = "/cors/plugin/test2";
	private static final String URL_PATTERN3 = "/cors/{type}/test3/{id}.{format}";
	private static final String URL_PATTERN3_ALIAS = "/c/{type}/t/{id}.{format}";
	private static final String URL_PATH3 = "/cors/plugin/test3/42";
	private static final String URL_PATH3_ALIASED = "/c/plugin/t/42";
	private static final int SERVER_PORT = 8888;
	private static final String SERVER_HOST = "http://localhost:" + SERVER_PORT;
	private static final String URL1 = SERVER_HOST + URL_PATH1;
	private static final String URL2 = SERVER_HOST + URL_PATH2;
	private static final String URL3 = SERVER_HOST + URL_PATH3;
	private static final String URL3_ALIASED = SERVER_HOST + URL_PATH3_ALIASED;	

	private RestExpress server = new RestExpress();
	private HttpClient http = new DefaultHttpClient();

	@Before
	public void createServer()
	{
		RestExpress.getSerializationProvider();

		server.uri(URL_PATH1, new TestController());
		server.uri(URL_PATH2, new TestController())
			.method(HttpMethod.POST);
		server.uri(URL_PATH2, new TestController())
			.method(HttpMethod.GET);
		server.uri(URL_PATTERN3, new TestController())
			.alias(URL_PATTERN3_ALIAS)
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST);
		server.addMessageObserver(new SimpleConsoleLogMessageObserver());
		server.enforceHttpSpec();
	}

	@After
	public void shutdownServer()
	{
		server.shutdown();
	}

	@Test
	public void shouldReturnOptionsWithoutFormat()
	throws Exception
	{
		new CorsHeaderPlugin("http://localhost:8888", "http://www.strategicgains.com")
			.allowHeaders("Location")
			.exposeHeaders("Accept", "Content-Type")
			.maxAge(42)
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(URL1);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Accept,Content-Type", response.getHeaders("Access-Control-Expose-Headers")[0].getValue());
		assertEquals("Location", response.getHeaders("Access-Control-Allow-Headers")[0].getValue());
		assertEquals("42", response.getHeaders("Access-Control-Max-Age")[0].getValue());
		assertEquals("http://localhost:8888 http://www.strategicgains.com", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("PUT"));
		assertTrue(methods.contains("DELETE"));
		assertTrue(methods.contains("OPTIONS"));
		request.releaseConnection();
	}

	@Test
	public void shouldReturnOptionsForSpecifiedMethods()
	throws ClientProtocolException, IOException
	{
		new CorsHeaderPlugin("*")
			.allowHeaders("Location")
			.exposeHeaders("Accept", "Content-Type")
			.maxAge(42)
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(URL2);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Accept,Content-Type", response.getHeaders("Access-Control-Expose-Headers")[0].getValue());
		assertEquals("Location", response.getHeaders("Access-Control-Allow-Headers")[0].getValue());
		assertEquals("42", response.getHeaders("Access-Control-Max-Age")[0].getValue());
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldReturnOptionsIdentifiersInUrlPattern()
	throws ClientProtocolException, IOException
	{
		new CorsHeaderPlugin("*")
			.allowHeaders("Location")
			.exposeHeaders("Accept", "Content-Type")
			.maxAge(42)
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(URL3 + ".json");
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Accept,Content-Type", response.getHeaders("Access-Control-Expose-Headers")[0].getValue());
		assertEquals("Location", response.getHeaders("Access-Control-Allow-Headers")[0].getValue());
		assertEquals("42", response.getHeaders("Access-Control-Max-Age")[0].getValue());
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldSupportAliasing()
	throws ClientProtocolException, IOException
	{
		new CorsHeaderPlugin("*")
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(URL3_ALIASED);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldSupportAliasingWithFormat()
	throws ClientProtocolException, IOException
	{
		new CorsHeaderPlugin("*")
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(URL3_ALIASED + ".json");
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldReturnDefaultOptions()
	throws ClientProtocolException, IOException
	{
		new CorsHeaderPlugin("*")
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(URL2);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();

		request = new HttpOptions(URL1);
		response = (HttpResponse) http.execute(request);
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("PUT"));
		assertTrue(methods.contains("DELETE"));
		assertTrue(methods.contains("OPTIONS"));
		request.releaseConnection();
	}

	@Test
	public void shouldNotSupportPreflight()
	throws Exception
	{
		new CorsHeaderPlugin("*")
			.noPreflightSupport()
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(URL1);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(405, response.getStatusLine().getStatusCode());
		String methods = response.getHeaders("Allow")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("PUT"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("DELETE"));
		assertFalse(methods.contains("OPTIONS"));
		request.releaseConnection();

		request = new HttpOptions(URL2);
		response = (HttpResponse) http.execute(request);
		assertEquals(405, response.getStatusLine().getStatusCode());
		methods = response.getHeaders("Allow")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		assertFalse(methods.contains("OPTIONS"));
		request.releaseConnection();
	}

	@SuppressWarnings("unused")
	private class TestController
	{
        public void create(Request request, Response response)
        {
		}

		public void read(Request request, Response response)
		{
		}
		
		public void update(Request request, Response response)
		{
		}
		
		public void delete(Request request, Response response)
		{
		}
		
		public void readAll(Request request, Response response)
		{
		}
	}
}
