package com.strategicgains.restexpress.plugin.cors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.pipeline.SimpleConsoleLogMessageObserver;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

public class CorsPluginTest
{
	private static final String URL_PATH1 = "/cors/plugin/test/1";
	private static final String URL_PATH2 = "/cors/plugin/test2";
	private static final String URL_PATTERN3 = "/cors/{type}/test3/{id}.{format}";
	private static final String URL_PATTERN3_ALIAS = "/c/{type}/t/{id}.{format}";
	private static final String URL_PATH3 = "/cors/plugin/test3/42";
	private static final String URL_PATH3_ALIASED = "/c/plugin/t/42";
	private static final String URL_CODE_PATH = "/code";
	private static final int SERVER_PORT = 8888;
	private static final String SERVER_HOST = "http://localhost:" + SERVER_PORT;
	private static final String URL1 = SERVER_HOST + URL_PATH1;
	private static final String URL2 = SERVER_HOST + URL_PATH2;
	private static final String URL3 = SERVER_HOST + URL_PATH3;
	private static final String URL3_ALIASED = SERVER_HOST + URL_PATH3_ALIASED;
	private static final String URL_CODE = SERVER_HOST + URL_CODE_PATH;

	private RestExpress server = new RestExpress();
	private HttpClient http = new DefaultHttpClient();

	@Before
	public void createServer()
	{
		RestExpress.getDefaultSerializationProvider();

		server.uri(URL_PATH1, new TestController());
		server.uri(URL_PATH2, new TestController())
			.method(HttpMethod.POST);
		server.uri(URL_PATH2, new TestController())
			.method(HttpMethod.GET);
		server.uri(URL_PATTERN3, new TestController())
			.alias(URL_PATTERN3_ALIAS)
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST);

		server.uri(URL_CODE_PATH, new TestController())
			.method(HttpMethod.GET);
		server.uri(URL_CODE_PATH, new TestController())
			.method(HttpMethod.PUT);
		server.uri(URL_CODE_PATH, new TestController())
			.method(HttpMethod.DELETE);

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
	throws Throwable
	{
		new CorsHeaderPlugin("http://localhost:8888", "http://www.strategicgains.com")
			.allowHeaders("Location")
			.exposeHeaders("Accept", "Content-Type")
			.maxAge(42)
			.register(server);
		server.bind(SERVER_PORT);

		String origin = "http://localhost:8888";
		HttpOptions request = new HttpOptions(URL1);
		request.addHeader(HttpHeaders.Names.ORIGIN, origin);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Accept,Content-Type", response.getHeaders("Access-Control-Expose-Headers")[0].getValue());
		assertEquals("Location", response.getHeaders("Access-Control-Allow-Headers")[0].getValue());
		assertEquals("42", response.getHeaders("Access-Control-Max-Age")[0].getValue());
		assertEquals(origin, response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		Header[] vary = response.getHeaders(HttpHeaders.Names.VARY);
		assertEquals(1, vary.length);
		assertEquals("origin", vary[0].getValue());
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
	throws Throwable
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
		assertEquals(0, response.getHeaders(HttpHeaders.Names.VARY).length);
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldMergeDuplicateRoutes()
	throws Throwable
	{
		new CorsHeaderPlugin("*")
			.allowHeaders("Location")
			.exposeHeaders("Accept", "Content-Type")
			.maxAge(42)
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(URL_CODE);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Accept,Content-Type", response.getHeaders("Access-Control-Expose-Headers")[0].getValue());
		assertEquals("Location", response.getHeaders("Access-Control-Allow-Headers")[0].getValue());
		assertEquals("42", response.getHeaders("Access-Control-Max-Age")[0].getValue());
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(0, response.getHeaders(HttpHeaders.Names.VARY).length);
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		System.out.println("*****************************************" + methods);
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("OPTIONS"));
		assertTrue(methods.contains("PUT"));
		assertTrue(methods.contains("DELETE"));
		assertFalse(methods.contains("POST"));
		request.releaseConnection();
	}

	@Test
	public void shouldReturnOptionsIdentifiersInUrlPattern()
	throws Throwable
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
		assertEquals(0, response.getHeaders(HttpHeaders.Names.VARY).length);
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldSupportAliasing()
	throws Throwable
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
		assertEquals(0, response.getHeaders(HttpHeaders.Names.VARY).length);
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
	throws Throwable
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
		assertEquals(0, response.getHeaders(HttpHeaders.Names.VARY).length);
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
	throws Throwable
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
		assertEquals(0, response.getHeaders(HttpHeaders.Names.VARY).length);
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
	public void shouldReturnOrigin()
	throws Throwable
	{
		new CorsHeaderPlugin("{origin}")
			.register(server);
		server.bind(SERVER_PORT);

		String origin = "http://foobar.xyz";
		HttpOptions request = new HttpOptions(URL2);
		request.addHeader(HttpHeaders.Names.ORIGIN, origin);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(origin, response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(ContentType.TEXT_PLAIN, response.getHeaders(HttpHeaders.Names.CONTENT_TYPE)[0].getValue());
		assertEquals("0", response.getHeaders(HttpHeaders.Names.CONTENT_LENGTH)[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		Header[] vary = response.getHeaders(HttpHeaders.Names.VARY);
		assertEquals(1, vary.length);
		assertEquals("origin", vary[0].getValue());
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldNotReturnOrigin()
	throws Throwable
	{
		new CorsHeaderPlugin("strategicgains.com")
			.register(server);
		server.bind(SERVER_PORT);

		String origin = "http://foobar.xyz";
		HttpOptions request = new HttpOptions(URL2);
		request.addHeader(HttpHeaders.Names.ORIGIN, origin);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(0, response.getHeaders("Access-Control-Allow-Origin").length);
		assertEquals(ContentType.TEXT_PLAIN, response.getHeaders(HttpHeaders.Names.CONTENT_TYPE)[0].getValue());
		assertEquals("0", response.getHeaders(HttpHeaders.Names.CONTENT_LENGTH)[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		Header[] vary = response.getHeaders(HttpHeaders.Names.VARY);
		assertEquals(0, vary.length);
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldReturnOriginForNull()
	throws Throwable
	{
		new CorsHeaderPlugin(null)
			.register(server);
		server.bind(SERVER_PORT);

		String origin = "http://foobar.xyz";
		HttpOptions request = new HttpOptions(URL2);
		request.addHeader(HttpHeaders.Names.ORIGIN, origin);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(origin, response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(ContentType.TEXT_PLAIN, response.getHeaders(HttpHeaders.Names.CONTENT_TYPE)[0].getValue());
		assertEquals("0", response.getHeaders(HttpHeaders.Names.CONTENT_LENGTH)[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		Header[] vary = response.getHeaders(HttpHeaders.Names.VARY);
		assertEquals(1, vary.length);
		assertEquals("origin", vary[0].getValue());
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldReturnOriginForNoArg()
	throws Throwable
	{
		new CorsHeaderPlugin()
			.register(server);
		server.bind(SERVER_PORT);

		String origin = "http://foobar.xyz";
		HttpOptions request = new HttpOptions(URL2);
		request.addHeader(HttpHeaders.Names.ORIGIN, origin);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(origin, response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(ContentType.TEXT_PLAIN, response.getHeaders(HttpHeaders.Names.CONTENT_TYPE)[0].getValue());
		assertEquals("0", response.getHeaders(HttpHeaders.Names.CONTENT_LENGTH)[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		Header[] vary = response.getHeaders(HttpHeaders.Names.VARY);
		assertEquals(1, vary.length);
		assertEquals("origin", vary[0].getValue());
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldReturnOriginWithEcho()
	throws Throwable
	{
		new CorsHeaderPlugin("xyzt")
			.echoOrigin()
			.register(server);
		server.bind(SERVER_PORT);

		String origin = "http://foobar.xyz";
		HttpOptions request = new HttpOptions(URL2);
		request.addHeader(HttpHeaders.Names.ORIGIN, origin);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(origin, response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(ContentType.TEXT_PLAIN, response.getHeaders(HttpHeaders.Names.CONTENT_TYPE)[0].getValue());
		assertEquals("0", response.getHeaders(HttpHeaders.Names.CONTENT_LENGTH)[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		Header[] vary = response.getHeaders(HttpHeaders.Names.VARY);
		assertEquals(1, vary.length);
		assertEquals("origin", vary[0].getValue());
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldReturnOriginOnGet()
	throws Throwable
	{
		new CorsHeaderPlugin("{origin}")
			.register(server);
		server.bind(SERVER_PORT);

		String origin = "http://foobar.xyz";
		HttpGet request = new HttpGet(URL2);
		request.addHeader(HttpHeaders.Names.ORIGIN, origin);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(origin, response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(ContentType.JSON, response.getHeaders(HttpHeaders.Names.CONTENT_TYPE)[0].getValue());
		assertEquals("\"GET\"", EntityUtils.toString(response.getEntity()));
		assertEquals("5", response.getHeaders(HttpHeaders.Names.CONTENT_LENGTH)[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		Header[] vary = response.getHeaders(HttpHeaders.Names.VARY);
		assertEquals(1, vary.length);
		assertEquals("origin", vary[0].getValue());
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@Test
	public void shouldNotReturnAllowOriginHeader()
	throws Throwable
	{
		new CorsHeaderPlugin("{origin}")
			.register(server);
		server.bind(SERVER_PORT);
	
		HttpOptions request = new HttpOptions(URL2);
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Origin").length);
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		Header[] vary = response.getHeaders(HttpHeaders.Names.VARY);
		assertEquals(0, vary.length);
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		assertTrue(methods.contains("OPTIONS"));
		request.releaseConnection();		
	}

	@Test
	public void shouldNotSupportPreflight()
	throws Throwable
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

	@Test
	public void shouldMatchDuplicateUrls()
	throws Throwable
	{
		TestController controller = new TestController();

		server.uri("/users/{userId}/courses/{courseId}/items/last", controller)
			.method(HttpMethod.POST);

		server.uri("/users/{userId}/courses/{courseId}/items/first", controller)
			.method(HttpMethod.POST);

		server.uri("/users/{userId}/courses/{courseId}/items/{itemId}", controller)
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE);

		new CorsHeaderPlugin("*")
			.register(server);
		server.bind(SERVER_PORT);

		HttpOptions request = new HttpOptions(SERVER_HOST + "/users/ffffffff532b3a55e4b0875ee2253e38/courses/54da4f3be4b070da7e3cab15/items/first");
		HttpResponse response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		String methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("GET"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();

		request = new HttpOptions(SERVER_HOST + "/users/ffffffff532b3a55e4b0875ee2253e38/courses/54da4f3be4b070da7e3cab15/items/last");
		response = (HttpResponse) http.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("*", response.getHeaders("Access-Control-Allow-Origin")[0].getValue());
		assertEquals(0, response.getHeaders("Access-Control-Expose-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Allow-Headers").length);
		assertEquals(0, response.getHeaders("Access-Control-Max-Age").length);
		methods = response.getHeaders("Access-Control-Allow-Methods")[0].getValue();
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertFalse(methods.contains("GET"));
		assertFalse(methods.contains("PUT"));
		assertFalse(methods.contains("DELETE"));
		request.releaseConnection();
	}

	@SuppressWarnings("unused")
	private class TestController
	{
        public String create(Request request, Response response)
        {
        	return request.getEffectiveHttpMethod().name();
		}

		public String read(Request request, Response response)
		{
        	return request.getEffectiveHttpMethod().name();
		}
		
		public String update(Request request, Response response)
		{
        	return request.getEffectiveHttpMethod().name();
		}
		
		public String delete(Request request, Response response)
		{
        	return request.getEffectiveHttpMethod().name();
		}
		
		public String readAll(Request request, Response response)
		{
        	return request.getEffectiveHttpMethod().name();
		}
	}
}
