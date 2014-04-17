/*
    Copyright 2013, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.plugin.swagger;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.RestExpress;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

/**
 * @author toddf
 * @since Nov 22, 2013
 */
public class SwaggerPluginTest
{
	private static final RestExpress SERVER = new RestExpress();
	private static final int PORT = 9001;
	private static final String BASE_URL = "http://localhost:" + PORT;
	private HttpClient http = new DefaultHttpClient();

	@BeforeClass
	public static void intialize()
	{
		RestAssured.port = PORT;

		DummyController controller = new DummyController();
		SERVER.setBaseUrl(BASE_URL);
		SERVER.uri("/anothers/{userId}", controller)
			.action("readAnother", HttpMethod.GET);

		SERVER.uri("/users.{format}", controller)
			.action("readAll", HttpMethod.GET)
			.action("options", HttpMethod.OPTIONS)
			.method(HttpMethod.POST)
			.name("Users Collection");

		SERVER.uri("/users/{userId}.{format}", controller)
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.action("options", HttpMethod.OPTIONS)
			.name("Individual User");

		SERVER.uri("/users/{userId}/orders.{format}", controller)
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name("User Orders Collection");

		SERVER.uri("/orders.{format}", controller)
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name("Orders Collection");

		SERVER.uri("/orders/{orderId}.{format}", controller)
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name("Individual Order");
		
		SERVER.uri("/orders/{orderId}/items.{format}", controller)
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name("Order Line-Items Collection");
		
		SERVER.uri("/orders/{orderId}/items/{itemId}.{format}", controller)
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name("Individual Order Line-Item");

		SERVER.uri("/products.{format}", controller)
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name("Orders Collection");

		SERVER.uri("/products/{orderId}.{format}", controller)
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name("Individual Order");
		
		SERVER.uri("/health", controller)
			.flag("somevalue")
			.action("health", HttpMethod.GET)
			.name("health");
		
		new SwaggerPlugin()
			.apiVersion("1.0")
			.swaggerVersion("1.2")
			.flag("flag1")
			.flag("flag2")
			.parameter("parm1", "value1")
			.parameter("parm2", "value2")
			.register(SERVER);
		
		SERVER.bind(PORT);
	}
	
	@AfterClass
	public static void shutdown()
	{
		SERVER.shutdown();
	}

	@Test
	public void shouldReturnApiResources()
	throws Exception
	{
		Response r = get("/api-docs");
		System.out.println(r.asString());
		SwaggerAssert.common(r);
		r.then()
			.body("apis", hasItem(hasEntry("path", "/anothers")))
			.body("apis", hasItem(hasEntry("path", "/users")))
			.body("apis", hasItem(hasEntry("path", "/orders")))
			.body("apis", hasItem(hasEntry("path", "/products")))
			.body("apis", hasItem(hasEntry("path", "/health")))
			.body("apis", not(hasItem(hasEntry("path", "/api-docs"))));
	}

	@Test
	public void shouldReturnUsersApi()
	{
		Response r = get("/api-docs/users");
		System.out.println(r.asString());
		SwaggerAssert.common(r);
		r.then()
			.body("basePath", equalTo(BASE_URL))
			.body("resourcePath", is("/users"));

		r.then()
			.root("apis[%s].%s")
//			.body(withArgs(0, "path"), is("/users.{format}"))
			.body(withArgs(0, "path"), is("/users"))
			.body(withArgs(0, "description"), is("Users Collection"))
			.body(withArgs(0, "operations"), hasItem(hasEntry("method", "GET")))			
			.body(withArgs(0, "operations"), hasItem(hasEntry("method", "POST")))
			.body(withArgs(0, "operations"), not(hasItem(hasEntry("method", "PUT"))))
			.body(withArgs(0, "operations"), not(hasItem(hasEntry("method", "DELETE"))))

//			.body(withArgs(1, "path"), is("/users/{userId}.{format}"))
			.body(withArgs(1, "path"), is("/users/{userId}"))
			.body(withArgs(1, "description"), is("Individual User"))
			.body(withArgs(1, "operations"), hasItem(hasEntry("method", "GET")))			
			.body(withArgs(1, "operations"), hasItem(hasEntry("method", "PUT")))
			.body(withArgs(1, "operations"), hasItem(hasEntry("method", "DELETE")))
			.body(withArgs(1, "operations"), not(hasItem(hasEntry("method", "POST"))))

//			.body(withArgs(2, "path"), is("/users/{userId}/orders.{format}"))
			.body(withArgs(2, "path"), is("/users/{userId}/orders"))
			.body(withArgs(2, "description"), is("User Orders Collection"))
			.body(withArgs(2, "operations"), hasItem(hasEntry("method", "GET")))			
			.body(withArgs(2, "operations"), hasItem(hasEntry("method", "POST")))
			.body(withArgs(2, "operations"), not(hasItem(hasEntry("method", "PUT"))))
			.body(withArgs(2, "operations"), not(hasItem(hasEntry("method", "DELETE"))));
	}

	@Test
	public void testApiUsersRoute()
	throws ClientProtocolException, IOException
	{
		HttpGet request = new HttpGet("http://localhost:9001/api-docs/users");
		HttpResponse response = (HttpResponse) http.execute(request);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity);
//		System.out.println(json);
		assertTrue(json.contains("\"apiVersion\":\"1.0\""));
		assertTrue(json.contains("\"swaggerVersion\":\"1.2\""));
		assertTrue(json.contains("\"basePath\":\"http://localhost:9001\""));
		assertTrue(json.contains("\"resourcePath\":\"/users\""));
		assertTrue(json.contains("\"nickname\":\"GET Users Collection\""));
		assertTrue(json.contains("\"nickname\":\"POST Users Collection\""));
		assertFalse(json.contains("\"nickname\":\"OPTIONS Users Collection\""));
		assertTrue(json.contains("\"nickname\":\"GET Individual User\""));
		assertTrue(json.contains("\"nickname\":\"PUT Individual User\""));
		assertTrue(json.contains("\"nickname\":\"DELETE Individual User\""));
		assertFalse(json.contains("\"nickname\":\"OPTIONS Individual User\""));
		assertTrue(json.contains("\"summary\":\"\""));
		request.releaseConnection();
	}

	@Test
	public void testApiOrdersRoute()
	throws ClientProtocolException, IOException
	{
		HttpGet request = new HttpGet("http://localhost:9001/api-docs/orders");
		HttpResponse response = (HttpResponse) http.execute(request);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity);
//		System.out.println(json);
		assertTrue(json.contains("\"apiVersion\":\"1.0\""));
		assertTrue(json.contains("\"swaggerVersion\":\"1.2\""));
		assertTrue(json.contains("\"basePath\":\"http://localhost:9001\""));
		assertTrue(json.contains("\"resourcePath\":\"/orders\""));
		request.releaseConnection();
	}

	@Test
	public void testApiProductsRoute()
	throws ClientProtocolException, IOException
	{
		HttpGet request = new HttpGet("http://localhost:9001/api-docs/products");
		HttpResponse response = (HttpResponse) http.execute(request);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity);
//		System.out.println(json);
		assertTrue(json.contains("\"apiVersion\":\"1.0\""));
		assertTrue(json.contains("\"swaggerVersion\":\"1.2\""));
		assertTrue(json.contains("\"basePath\":\"http://localhost:9001\""));
		assertTrue(json.contains("\"resourcePath\":\"/products\""));
		request.releaseConnection();
	}

	@Test
	public void testApiHealthRoute()
	throws ClientProtocolException, IOException
	{
		HttpGet request = new HttpGet("http://localhost:9001/api-docs/health");
		HttpResponse response = (HttpResponse) http.execute(request);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity);
//		System.out.println(json);
		assertTrue(json.contains("\"apiVersion\":\"1.0\""));
		assertTrue(json.contains("\"swaggerVersion\":\"1.2\""));
		assertTrue(json.contains("\"basePath\":\"http://localhost:9001\""));
		assertTrue(json.contains("\"resourcePath\":\"/health\""));
		request.releaseConnection();
	}
}
