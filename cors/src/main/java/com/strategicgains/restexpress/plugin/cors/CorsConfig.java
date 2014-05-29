package com.strategicgains.restexpress.plugin.cors;

import java.util.Properties;

import org.restexpress.common.exception.ConfigurationException;

/**
 * Supports reading properties for the values set on the CorsHeaderPlugin.
 * The properties file is expected to have values for the following:
 * <ul>
 * <li>cors.origins - a comma-delimited list of domain origins, or an asterisk ("*")</li>
 * <li>cors.exposedHeaders - a comma-delimited list of header names</li>
 * <li>cors.allowedHeaders - a comma-delimited list of header names</li>
 * <li>cors.maxAge - a numeric value representing the number of seconds to allow a browser to cache the CORS pre-flight request</li>
 * </ul>
 * A sample configuration looks something like the following:
 * <p/>
 * cors.origins = *<br/>
 * cors.exposedHeaders = Location<br/>
 * cors.allowedHeaders = Content-Type,Accept,Origin,Referer,User-Agent,Location,Authorization</br>
 * cors.maxAge = 2592000
 * <p/>
 * All values are required or a ConfigurationException is thrown.
 * 
 * @author toddf
 * @since May 29, 2014
 */
public class CorsConfig
{
	private static final String DOMAINS_PROPERTY = "cors.origins";
	private static final String EXPOSED_HEADERS_PROPERTY = "cors.exposedHeaders";
	private static final String ALLOWED_HEADERS_PROPERTY = "cors.allowedHeaders";
	private static final String MAX_AGE_PROPERTY = "cors.maxAge";

	private String origins;
	private String exposedHeaders;
	private String allowedHeaders;
	private long maxAge;

	public CorsConfig(Properties p)
	{
		origins = p.getProperty(DOMAINS_PROPERTY);

		if (origins == null)
		{
			throw new ConfigurationException("Please define CORS origins for property: " + DOMAINS_PROPERTY);
		}

		exposedHeaders = p.getProperty(EXPOSED_HEADERS_PROPERTY);

		if (exposedHeaders == null)
		{
			throw new ConfigurationException("Please define CORS exposed headers for property: " + EXPOSED_HEADERS_PROPERTY);
		}

		allowedHeaders = p.getProperty(ALLOWED_HEADERS_PROPERTY);

		if (allowedHeaders == null)
		{
			throw new ConfigurationException("Please define CORS allowed headers for property: " + ALLOWED_HEADERS_PROPERTY);
		}

		try
		{
			maxAge = Long.parseLong(p.getProperty(MAX_AGE_PROPERTY));
		}
		catch(NumberFormatException e)
		{
			throw new ConfigurationException("Please define CORS max age in seconds for property: " + MAX_AGE_PROPERTY);
		}
	}

	public String getOrigins()
	{
		return origins;
	}

	public String getExposedHeaders()
	{
		return exposedHeaders;
	}

	public String getAllowedHeaders()
	{
		return allowedHeaders;
	}

	public long getMaxAge()
	{
		return maxAge;
	}
}
