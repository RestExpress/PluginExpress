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
package com.strategicgains.restexpress.plugin.metrics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.restexpress.Request;
import org.restexpress.Response;

import com.strategicgains.util.date.DateAdapterConstants;

/**
 * Creates a template string, suitable for logging, from data in the request, response, and the duration of
 * a request.
 * 
 * @author toddf
 * @since Feb 14, 2013
 */
public class LogOutputFactory
{
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DateAdapterConstants.TIMESTAMP_OUTPUT_FORMAT);

	private String machineName = null;
	
	/**
	 * Create a new LogOutputFactory with no machine/JVM name.
	 */
	public LogOutputFactory()
	{
		super();
	}

	/**
	 * Create a new LogOutputFactory, setting a machine or JVM name.  The name is useful in distinguishing
	 * output from a cluster of servers.  When logs are aggregated it is important to know which machine (or VM) 
	 * the log entries came from in order to diagnose issues.
	 *  
	 * @param name a name identifying this jvm/machine, or null.
	 */
	public LogOutputFactory(String name)
	{
		super();
		this.machineName = name;
	}

	/**
	 * Creates a pre-populated StringBuilder instance.  The output contains the following:
	 * "Time=<now> RequestTime=<duration> URL=<request URL> Method=<http method> Format=<requested format e.g. 'json'> Resource=<route pattern> Machine=<machineName> CorrelationId=<request correlation id> Status=<http status> User-Agent=<user agent string> Referrer=<referrer http header>"
	 * <p/>
	 * However, if jvmId, User-Agent, or Referer are null, they are not included in the output.  The CorrelationId
	 * value is the RestExpress Request correlation id, which is unique within a single JVM.
	 * 
	 * @param request a RestExpress Request instance.
	 * @param response a RestExpress Response instance.
	 * @param duration the duration of the request, in milliseconds.
	 * @return a pre-populated StringBuilder.
	 */
	public StringBuilder createStringBuilder(Request request, Response response, Long duration)
	{
	    StringBuilder builder = new StringBuilder();
		builder.append("Time=" + ((DateFormat) DATE_FORMAT.clone()).format(new Date()));
		builder.append(" RequestTime=" + duration);
		builder.append(" URL=" + request.getUrl());
		builder.append(" Method=" + request.getHttpMethod().name());
		builder.append(" Format=" + request.getFormat());
		
		if (request.getResolvedRoute() != null)
		{
			builder.append(" Resource=" + request.getResolvedRoute().getFullPattern());
		}

		if (machineName != null)
		{
			builder.append(" Machine=" + machineName);
		}

		builder.append(" CorrelationId=" + request.getCorrelationId());
		builder.append(" Status=" + response.getResponseStatus().code());

		if (request.getHeader("User-Agent") != null)
		{
			builder.append(" UserAgent=" + request.getHeader("User-Agent"));
		}

		if (request.getHeader("Referer") != null)
		{
			builder.append(" UrlReferer=" + request.getHeader("Referer"));
		}
		
		return builder;
	}

	/**
	 * Creates a string suitable for logging output.
	 * 
	 * @param request a RestExpress Request instance.
	 * @param response a RestExpress Response instance.
	 * @param duration the duration of the request, in milliseconds.
	 * @return a String as described above.
	 */
	public String create(Request request, Response response, Long duration)
	{
		return createStringBuilder(request, response, duration).toString();
	}
}
