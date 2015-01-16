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
package org.restexpress.plugin.logging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides basic timing for each request, enabling logging at different log levels.
 * 
 * @author toddf
 * @since Dec 3, 2014
 */
public class LoggingMessageObserver
extends MessageObserver
{
	// SECTION: INSTANCE VARIABLES

	private Map<String, Timer> timers = new ConcurrentHashMap<String, Timer>();
	private Logger logger;
	
	// SECTION: MESSAGE OBSERVER

	public LoggingMessageObserver(String name)
    {
		logger = LoggerFactory.getLogger(name);
    }

	@Override
    protected void onReceived(Request request, Response response)
    {
		timers.put(request.getCorrelationId(), new Timer());
    }

	@Override
    protected void onException(Throwable exception, Request request, Response response)
    {
		int status = response.getResponseStatus().getCode();
		String message = createExceptionMessage(exception, request, response);

		if (status >= 400 && status <= 499)
		{
			logger.warn(message, exception);
		}
		else
		{
			logger.error(message, exception);
		}
    }

	@Override
    protected void onComplete(Request request, Response response)
    {
		Timer timer = timers.remove(request.getCorrelationId());
		if (timer != null) timer.stop();
		
		logger.info(createCompleteMessage(request, response, timer));
    }

	/**
	 * Create the message to be logged when a request is completed successfully.
	 * Sub-classes can override.
	 * 
	 * @param request
	 * @param response
	 * @param timer
	 * @return a string message.
	 */
	protected String createCompleteMessage(Request request, Response response, Timer timer)
    {
	    StringBuilder sb = new StringBuilder(request.getEffectiveHttpMethod().toString());
		sb.append(" ");
		sb.append(request.getUrl());
		
		if (timer != null)
		{
			sb.append(" responded with ");
			sb.append(response.getResponseStatus().toString());
			sb.append(" in ");
			sb.append(timer.toString());
		}
		else
		{
			sb.append(" responded with ");
			sb.append(response.getResponseStatus().toString());
			sb.append(" (no timer found)");
		}

	    return sb.toString();
    }

	/**
	 * Create the message to be logged when a request results in an exception.
	 * Sub-classes can override.
	 * 
	 * @param exception the exception that occurred.
	 * @param request the request.
	 * @param response the response.
	 * @return a string message.
	 */
	protected String createExceptionMessage(Throwable exception, Request request, Response response)
    {
		StringBuilder sb = new StringBuilder(request.getEffectiveHttpMethod().toString());
		sb.append(' ');
		sb.append(request.getUrl());
		sb.append(" threw exception: ");
		sb.append(exception.getClass().getSimpleName());
		return sb.toString();
    }
	
	
	// SECTION: INNER CLASS
	
	protected class Timer
	{
		private long startMillis = 0;
		private long stopMillis = 0;
		
		public Timer()
		{
			super();
			this.startMillis = System.currentTimeMillis();
		}
		
		public void stop()
		{
			this.stopMillis = System.currentTimeMillis();
		}
		
		public String toString()
		{
			long stopTime = (stopMillis == 0 ? System.currentTimeMillis() : stopMillis);

			return String.valueOf(stopTime - startMillis) + "ms";
		}
	}
}
