/*
 * Copyright 2012, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.plugin.cors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.restexpress.ContentType;
import org.restexpress.Flags;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.common.util.StringUtils;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.plugin.AbstractPlugin;
import org.restexpress.route.RouteBuilder;
import org.restexpress.util.Callback;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;

/**
 * This RestExpress plugin supports CORS behavior by setting the
 * 'Access-Control-Allow-Origin' header.  It also, by default, sets up the
 * pre-flight CORS request by creating an OPTIONS route for all URI patterns
 * registered in the service suite.
 * <p/>
 * Usage requires passing in the appropriate domain names that are allowed to
 * perform cross-domain behaviors. To support cross-domain behavior globally
 * (not recommended) send in '*' (wildcard). Or, arguably more secure, use '{origin}',
 * which will cause the plugin to return the Access-Control-Allow-Origin set
 * to the incoming Origin header set by the browser (or exclude the header altogether, if no Origin
 * header is present).
 * <p/>
 * Usage: RestExpress server = new RestExpress(); ... new
 * CorsHeaderPlugin("{origin}").register(server);
 * or... server.registerPlugin(new CorsHeaderPlugin("{origin}"));
 * <p/>
 * You can also disable the OPTIONS route that gets automatically generated
 * to support the pre-flight CORS request by calling .noPreflightSupport()
 * before calling register() as follows:<br/>
 * new CorsHeaderPlugin("{origin}").noPreflightSupport().register();
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

	private Set<String> allowedOrigins;
	private String exposeHeadersHeader;
	private String allowHeadersHeader;
	private Long maxAge;
	private Map<String, Set<HttpMethod>> methodsByPattern = new LinkedHashMap<>();
	private List<RouteBuilder> routeBuilders = new ArrayList<>();
	private boolean isPreflightSupported = true;
	private boolean shouldAllowCredentials = false;
	private List<String> flags = new ArrayList<>();
	private Map<String, Object> parameters = new HashMap<>();

	public CorsHeaderPlugin(String... origins)
	{
		super();
		this.allowedOrigins = (origins != null ? new HashSet<>(Arrays.asList(origins)) : null);
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

	public CorsHeaderPlugin allowCredentials()
	{
		shouldAllowCredentials = true;
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
		server.iterateRouteBuilders(new Callback<RouteBuilder>()
		{
			@Override
			public void process(RouteBuilder builder)
			{
				RouteMetadata rmd = builder.asMetadata();
				Set<HttpMethod> methods = new HashSet<>();

				for (String methodString : rmd.getMethods())
				{
					methods.add(HttpMethod.valueOf(methodString));
				}

				if (isPreflightSupported && !methods.contains(HttpMethod.OPTIONS))
				{
					methods.add(HttpMethod.OPTIONS);
				}

				String pathPattern = rmd.getUri().getPattern();
                updateAcceptedMethods(methods, pathPattern);

                for (String alias : rmd.getAliases())
                {
                	updateAcceptedMethods(methods, alias);
                }
			}

			private void updateAcceptedMethods(Set<HttpMethod> methods, String pathPattern)
            {
	            Set<HttpMethod> existingMethods = methodsByPattern.get(pathPattern);
                
                if (existingMethods != null)
				{
					existingMethods.addAll(methods);
				}
				else
				{
					existingMethods = methods;
				}

				methodsByPattern.put(pathPattern, existingMethods);
            }
		});

	    CorsOptionsController corsController = new CorsOptionsController(allowedOrigins, exposeHeadersHeader, allowHeadersHeader, maxAge, shouldAllowCredentials, methodsByPattern);

	    if (isPreflightSupported)
		{
			addPreflightOptionsRequestSupport(server, corsController);
		}

		server.addFinallyProcessor(new CorsHeaderPostprocessor(corsController));
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
	private void addPreflightOptionsRequestSupport(RestExpress server, CorsOptionsController corsOptionsController)
    {
	    RouteBuilder rb;

	    for (String pattern : methodsByPattern.keySet())
	    {
	    	rb = server.uri(pattern, corsOptionsController)
		    	.action("options", HttpMethod.OPTIONS)
		    	.noSerialization()
		    	// Disable both authentication and authorization which usually use a header such as Authorization.
		    	// When browser does CORS preflight with OPTIONS request, such headers are not included.
		    	.flag(Flags.Auth.PUBLIC_ROUTE)
		    	.flag(Flags.Auth.NO_AUTHORIZATION);

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

	public class CorsHeaderPostprocessor
	implements Postprocessor
	{
		CorsOptionsController cors;

		public CorsHeaderPostprocessor(CorsOptionsController corsController)
		{
			super();
			cors = corsController;
		}

		@Override
		public void process(Request request, Response response)
		{
			if (!HttpMethod.OPTIONS.equals(request.getEffectiveHttpMethod()))
			{
				cors.options(request, response);
			}
		}
	}

	private class CorsOptionsController
	{
		private static final String WILDCARD_ORIGIN = "*";
		private static final String CORS_ALLOW_HEADERS_HEADER = "Access-Control-Allow-Headers";
		private static final String CORS_ALLOW_METHODS_HEADER = "Access-Control-Allow-Methods";
		private static final String CORS_EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";
		private static final String CORS_ALLOW_CREDENTIALS_HEADER = "Access-Control-Allow-Credentials";
		private static final String CORS_MAX_AGE_HEADER = "Access-Control-Max-Age";
		private static final String ORIGIN_PARAMETER = "{origin}";

		private Map<String, String> allowedMethodsByPattern = new HashMap<>();
		private Long maxAge;
		private Set<String> allowedOrigins;
		private String allowHeadersHeader;
		private String exposeHeadersHeader = null;
		private boolean allowCredentials = false;
		private boolean echoesOrigin;

		public CorsOptionsController(Set<String> allowedOrigins, String exposeHeadersHeader, String allowHeadersHeader, Long maxAge, boolean allowCredentials, Map<String, Set<HttpMethod>> methodsByPattern)
		{
			super();
			this.allowedOrigins = (allowedOrigins != null ? new HashSet<>(allowedOrigins) : null);
			this.exposeHeadersHeader = exposeHeadersHeader;
			this.maxAge = (maxAge == null ? null : maxAge);
			this.allowHeadersHeader = allowHeadersHeader;
			this.echoesOrigin = (allowedOrigins == null || allowedOrigins.contains(ORIGIN_PARAMETER));
			this.allowCredentials = allowCredentials;

			for (Entry<String, Set<HttpMethod>> entry : methodsByPattern.entrySet())
			{
				String pathPattern = entry.getKey().replaceFirst("\\.\\{format\\}", "");
				allowedMethodsByPattern.put(pathPattern, StringUtils.join(",", entry.getValue()));
			}
		}

        public void options(Request request, Response response)
		{
			String origin = computeOrigin(request);

			if (origin != null)
			{
				response.addHeader(CORS_ALLOW_ORIGIN_HEADER, origin);
				
				if (!isWildcard(origin))
				{
					response.addHeader(HttpHeaderNames.VARY.toString(), "origin");
				}
			}

			response.addHeader(CORS_ALLOW_METHODS_HEADER, allowedMethodsByPattern.get(request.getResolvedRoute().getPattern()));

			if (HttpMethod.OPTIONS.equals(request.getEffectiveHttpMethod()))
			{
				response.addHeader("Content-Length","0");
				response.setContentType(ContentType.TEXT_PLAIN);
			}

			if (maxAge != null && !response.hasHeader(CORS_MAX_AGE_HEADER))
			{
				response.addHeader(CORS_MAX_AGE_HEADER, String.valueOf(maxAge));
			}

			if (allowHeadersHeader != null)
			{
				response.addHeader(CORS_ALLOW_HEADERS_HEADER, allowHeadersHeader);
			}

			if (exposeHeadersHeader != null)
			{
				response.addHeader(CORS_EXPOSE_HEADERS_HEADER, exposeHeadersHeader);
			}
		
			if (allowCredentials)
			{
				response.addHeader(CORS_ALLOW_CREDENTIALS_HEADER, "true");
			}
		}

		private boolean isWildcard(String origin)
		{
			return (origin != null && origin.trim().equals(WILDCARD_ORIGIN));
		}

		private String computeOrigin(Request request)
		{
			String origin = request.getHeader(HttpHeaderNames.ORIGIN.toString());

			if (echoesOrigin || allowedOrigins.contains(origin))
			{
				return origin;
			}
			else if (allowedOrigins.contains(WILDCARD_ORIGIN))
			{
				return WILDCARD_ORIGIN;
			}

			return null;
		}
	}
}
