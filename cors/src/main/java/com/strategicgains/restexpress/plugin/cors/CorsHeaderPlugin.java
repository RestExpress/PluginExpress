/*
 * Copyright 2012, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.plugin.cors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.common.util.StringUtils;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.plugin.AbstractPlugin;
import org.restexpress.route.Route;
import org.restexpress.route.RouteBuilder;
import org.restexpress.util.Callback;

/**
 * This RestExpress plugin supports CORS behavior by setting the
 * 'Access-Control-Allow-Origin' header.  It also, by default, sets up the
 * pre-flight CORS request by creating an OPTIONS route for all URI patterns
 * registered in the service suite.
 * <p/>
 * Usage requires passing in the appropriate domain names that are allowed to
 * perform cross-domain behaviors. To support cross-domain behavior globally
 * (not recommended) send in '*'.
 * <p/>
 * Usage: RestExpress server = new RestExpress(); ... new
 * CorsHeaderPlugin("*").register(server);
 * or... server.registerPlugin(new CorsHeaderPlugin("*"));
 * <p/>
 * You can also disable the OPTIONS route that gets automatically generated
 * to support the pre-flight CORS request by calling .noPreflightSupport()
 * before calling register() as follows:<br/>
 * new CorsHeaderPlugin("*").noPreflightSupport().register();
 * <p/>
 * Additionally, you can set flag() and parameter() values on the OPTIONS
 * preflight route (just like normal RestExpress routes) by calling .flag(String)
 * and .parameter(String, Object) after before register() as follows:<br/>
 * new CorsHeaderPlugin("*").flag(NO_SECURITY).parameter("s42",Boolean.FALSE).register();
 * <p/>
 * Note that flag() and parameter() values have no effect if noPreflightSupport() is called.
 * <p/>
 * For deeper information on the CORS specification, see: http://www.w3.org/TR/cors/
 * 
 * @author toddf
 * @since Jun 8, 2012
 */
public class CorsHeaderPlugin
extends AbstractPlugin
{
	private static final String CORS_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";

	private String allowOriginHeader;
	private String exposeHeadersHeader;
	private String allowHeadersHeader;
	private Long maxAge;
	private Map<String, Set<HttpMethod>> methodsByPattern = new HashMap<String, Set<HttpMethod>>();
	private List<RouteBuilder> routeBuilders = new ArrayList<RouteBuilder>();
	private boolean isPreflightSupported = true;
	private List<String> flags = new ArrayList<String>();
	private Map<String, Object> parameters = new HashMap<String, Object>();

	public CorsHeaderPlugin(String... origins)
	{
		super();
		allowOriginHeader = StringUtils.join(" ", (Object[]) origins);
	}
	
	/**
	 * Sets the maxAge for caching on the preflight OPTIONS request.
	 * 
	 * @param seconds
	 * @return
	 */
	public CorsHeaderPlugin maxAge(long seconds)
	{
		if (isRegistered()) throw new UnsupportedOperationException("CorsHeaderPlugin.maxAge(long) must be called before register()");

		this.maxAge = Long.valueOf(seconds);
		return this;
	}

	/** 
	 * Set the value of the 'Access-Control-Expose-Headers' header on responses.
	 * 
	 * @param exposeHeaders
	 * @return
	 */
	public CorsHeaderPlugin exposeHeaders(String... exposeHeaders)
	{
		exposeHeadersHeader = StringUtils.join(",", (Object[])exposeHeaders);
		return this;
	}
	
	public CorsHeaderPlugin allowHeaders(String... allowHeaders)
	{
		allowHeadersHeader = StringUtils.join(",", (Object[])allowHeaders);
		return this;
	}

	/**
	 * Disables the OPTIONS method that gets automatically generated for each URI pattern in the service suite.
	 * Must be called before register().
	 */
	public CorsHeaderPlugin noPreflightSupport()
	{
		if (isRegistered()) throw new UnsupportedOperationException("CorsHeaderPlugin.noPreflightSupport() must be called before register()");

		isPreflightSupported = false;
		return this;
	}

	/**
	 * Set a flag on the automatically generated OPTIONS route for pre-flight CORS requests.
	 * 
	 * @param flagValue
	 * @return a reference to this plugin to support method chaining.
	 */
	public CorsHeaderPlugin flag(String flagValue)
	{
		if (isRegistered()) throw new UnsupportedOperationException("CorsHeaderPlugin.flag(String) must be called before register()");

		if (!flags.contains(flagValue))
		{
			flags.add(flagValue);
		}

		return this;
	}

	public CorsHeaderPlugin parameter(String name, Object value)
	{
		if (isRegistered()) throw new UnsupportedOperationException("CorsHeaderPlugin.parameter(String, Object) must be called before register()");

		parameters.put(name, value);
		return this;
	}

	@Override
	public CorsHeaderPlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);
		CorsHeaderPostprocessor proc = new CorsHeaderPostprocessor(allowOriginHeader, exposeHeadersHeader);
		server.addFinallyProcessor(proc);

		server.iterateRouteBuilders(new Callback<RouteBuilder>()
		{
			@Override
			public void process(RouteBuilder builder)
			{
				List<Route> routes = builder.build();
				String pathPattern = routes.get(0).getPattern();
				Set<HttpMethod> methods = new HashSet<HttpMethod>();

				for (Route route : routes)
				{
					methods.add(route.getMethod());
				}
				
				if (isPreflightSupported)
				{
					if (!methods.contains(HttpMethod.OPTIONS))
					{
						methods.add(HttpMethod.OPTIONS);
					}
				}

                Set<HttpMethod> allMethods = methodsByPattern.get(pathPattern);
                allMethods.addAll(methods);
				methodsByPattern.put(pathPattern, allMethods);
			}
		});

		if (isPreflightSupported)
		{
			addPreflightOptionsRequestSupport(server);
		}

		return this;
	}

	@Override
	public void bind(RestExpress server)
	{
		// This method intentionally left blank.
	}

    /**
     * Add an 'OPTIONS' method supported on all routes for the pre-flight CORS request.
     * 
     * @param server a RestExpress server instance.
     */
	private void addPreflightOptionsRequestSupport(RestExpress server)
    {
	    CorsOptionsController corsOptionsController = new CorsOptionsController(allowOriginHeader, maxAge, methodsByPattern, allowHeadersHeader);
	    RouteBuilder rb;
	    
	    for (String pattern : methodsByPattern.keySet())
	    {
	    	rb = server.uri(pattern, corsOptionsController)
		    	.action("options", HttpMethod.OPTIONS)
		    	.noSerialization();

	    	for (String flag : flags)
	    	{
	    		rb.flag(flag);
	    	}
	    	
	    	for (Entry<String, Object> entry : parameters.entrySet())
	    	{
	    		rb.parameter(entry.getKey(), entry.getValue());
	    	}

	    	routeBuilders.add(rb);
	    }
    }

	private class CorsHeaderPostprocessor
	implements Postprocessor
	{
		private static final String CORS_EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";

		private String allowOriginHeader = null;
		private String exposeHeadersHeader = null;

		public CorsHeaderPostprocessor(String allowOriginHeader, String exposeHeadersHeader)
		{
			super();
			this.allowOriginHeader = allowOriginHeader;
			this.exposeHeadersHeader = exposeHeadersHeader;
		}

		@Override
		public void process(Request request, Response response)
		{
			if (!response.hasHeader(CORS_ALLOW_ORIGIN_HEADER) && allowOriginHeader != null)
			{
				response.addHeader(CORS_ALLOW_ORIGIN_HEADER, allowOriginHeader);
			}

			if (!response.hasHeader(CORS_EXPOSE_HEADERS_HEADER) && exposeHeadersHeader != null)
			{
				response.addHeader(CORS_EXPOSE_HEADERS_HEADER, exposeHeadersHeader);
			}
		}
	}

	private class CorsOptionsController
	{
		private static final String CORS_MAX_AGE_HEADER = "Access-Control-Max-Age";
		private static final String CORS_ALLOW_METHODS_HEADER = "Access-Control-Allow-Methods";
		private static final String CORS_ALLOW_HEADERS_HEADER = "Access-Control-Allow-Headers";

		private String allowOriginsHeader;
		private Map<String, String> allowedMethodsByPattern = new HashMap<String, String>();
		private Long maxAge;
		private String allowHeadersHeader;

		public CorsOptionsController(String allowOriginsHeader, Long maxAge, Map<String, Set<HttpMethod>> methodsByPattern, String allowHeadersHeader)
		{
			super();
			this.allowOriginsHeader = allowOriginsHeader;
			this.maxAge = (maxAge == null ? null : Long.valueOf(maxAge));
			this.allowHeadersHeader = allowHeadersHeader;
			
			for (Entry<String, Set<HttpMethod>> entry : methodsByPattern.entrySet())
			{
				allowedMethodsByPattern.put(entry.getKey(), StringUtils.join(",", entry.getValue()));
			}
		}

		@SuppressWarnings("unused")
        public void options(Request request, Response response)
		{
			response.addHeader(CORS_ALLOW_ORIGIN_HEADER, allowOriginsHeader);
			String pat = request.getResolvedRoute().getPattern();
			response.addHeader(CORS_ALLOW_METHODS_HEADER, allowedMethodsByPattern.get(request.getResolvedRoute().getPattern()));
			response.addHeader("Content-Length","0");
			response.setContentType(ContentType.TEXT_PLAIN);

			if (maxAge != null
				&& !response.hasHeader(CORS_MAX_AGE_HEADER))
			{
				response.addHeader(CORS_MAX_AGE_HEADER, String.valueOf(maxAge));
			}
			
			if (allowHeadersHeader != null)
			{
				response.addHeader(CORS_ALLOW_HEADERS_HEADER, allowHeadersHeader);
			}
		}
	}
}
