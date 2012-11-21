/*
 * Copyright 2012, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.restexpress.plugin.cors;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.pipeline.Postprocessor;
import com.strategicgains.restexpress.plugin.AbstractPlugin;
import com.strategicgains.restexpress.util.StringUtils;

/**
 * This RestExpress plugin supports CORS behavior by setting the 'Access-Control-Allow-Origin'
 * header on the response for GET requests.
 * <p/>
 * Usage requires passing in the appropriate domain names that are allowed to perform cross-domain
 * behaviors.  To support cross-domain behavior globally (not recommended) send in '*'.
 * <p/>
 * Usage:
 * RestExpress server = new RestExpress();
 * ...
 * new CorsHeaderPlugin("*").register(server);
 * or...
 * server.registerPlugin(new CorsHeaderPlugin("*"));
 * 
 * @author toddf
 * @since Jun 8, 2012
 */
public class CorsHeaderPlugin
extends AbstractPlugin
{
	private String[] originDomains;

	public CorsHeaderPlugin(String... origins)
	{
		super();
		this.originDomains = origins;
	}

	@Override
	public CorsHeaderPlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);
		server.addFinallyProcessor(new CorsHeaderPostprocessor(originDomains));
		return this;
	}

	@Override
	public void bind(RestExpress server)
	{
		// do nothing during bind().
	}
	
	private class CorsHeaderPostprocessor
	implements Postprocessor
	{
	    private static final String CORS_ORIGIN_HEADER = "Access-Control-Allow-Origin";

	    private String corsHeader = null;

		public CorsHeaderPostprocessor(String... originDomains)
		{
			super();
			corsHeader = StringUtils.join(" ", (Object[]) originDomains);
		}

		@Override
		public void process(Request request, Response response)
		{
			if (request.isMethodGet() && !response.hasHeader(CORS_ORIGIN_HEADER) && corsHeader != null)
			{
				response.addHeader(CORS_ORIGIN_HEADER, corsHeader);
			}
		}
	}
}
