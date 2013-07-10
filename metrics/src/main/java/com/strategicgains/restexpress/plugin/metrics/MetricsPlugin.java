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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.pipeline.MessageObserver;
import com.strategicgains.restexpress.pipeline.Postprocessor;
import com.strategicgains.restexpress.pipeline.Preprocessor;
import com.strategicgains.restexpress.plugin.Plugin;
import com.strategicgains.restexpress.util.StringUtils;

/**
 * Enables full metrics on all routes in the service suite via the Coda Hale Metrics library.  Metrics are be
 * made available via JMX using the Yammer (Coda Hale) JmxPublisher as follows (not recommended for production):
 * <p/>
 * <code>
 * final JmxReporter reporter = JmxReporter.forRegistry(registry).build();
 * reporter.start();
 * </code>
 * And can also be published to Graphite by configuring the Yammer (Coda Hale) Metrics publisher as follows:
 * <p/>
 * <code>
 * MetricRegistry registry = new MetricRegistry();
 * MetricsPlugin metricsPlugin = new MetricsPlugin(registry)...
 * 
 * final Graphite graphite = new Graphite(new InetSocketAddress("graphite.example.com", 2003));
 * final GraphiteReporter reporter = GraphiteReporter.forRegistry(metricsPlugin.getMetricRegistry())
 * 	.prefixedWith(server.getName())
 * 	.convertRatesTo(TimeUnit.SECONDS)
 * 	.convertDurationsTo(TimeUnit.MILLISECONDS)
 * 	.filter(MetricFilter.ALL)
 * 	.build(graphite);
 * reporter.start(10, TimeUnit.SECONDS);
 * </code>
 * <p/>
 * This plugin maintains metrics for the following:
 * currently active requests (counter), all exceptions occurred (counter), all times (timer, milliseconds/hours),
 * times by route (timer, milliseconds/hours), exceptions by route (counter), counters by return status (counter).
 * <p/>
 * In addition, the overall response time is set in the response header, X-Response-Time, in milliseconds.
 * <p/>
 * Note: Named routes are reported using their name.  Unnamed routes are reported using the URL pattern.
 * 
 * @author toddf
 * @since Feb 11, 2013
 */
public class MetricsPlugin
extends MessageObserver
implements Plugin, Preprocessor, Postprocessor
{
    private static final ConcurrentHashMap<String, Timer> ROUTE_TIMERS = new ConcurrentHashMap<String, Timer>();
	private static final ConcurrentHashMap<String, Long> START_TIMES_BY_CORRELATION_ID = new ConcurrentHashMap<String, Long>();
	private static final ConcurrentHashMap<String, Counter> EXCEPTION_COUNTERS_BY_ROUTE = new ConcurrentHashMap<String, Counter>();
	private static final ConcurrentHashMap<Integer, Counter> COUNTERS_BY_RESPONSE = new ConcurrentHashMap<Integer, Counter>();

	private MetricRegistry metrics;
    private Counter activeRequestsCounter;
    private Counter allExceptionsCounter;
    private Timer allTimesTimer;
    private boolean isRegistered = false;

	public MetricsPlugin(MetricRegistry registry)
	{
		super();
		this.metrics = registry;
	}


	// SECTION: PLUGIN

	/**
	 * Register the MetricsPlugin with the RestExpress server.
	 * 
	 * @param server a RestExpress server instance.
	 * @return MetricsPlugin
	 */
	@Override
	public MetricsPlugin register(RestExpress server)
	{
		if (isRegistered) return this;

		server.registerPlugin(this);
		this.isRegistered = true;

		server
			.addMessageObserver(this)
			.addPreprocessor(this)
			.addPostprocessor(this);

		return this;
	}

	/**
	 * This is called by RestExpress during it's bind() operation--right before it starts listening.
	 */
	@Override
	public void bind(RestExpress server)
	{
		this.activeRequestsCounter = metrics.counter("active-requests");
	    this.allExceptionsCounter = metrics.counter("all-exceptions");
	    this.allTimesTimer = metrics.timer("all-times");
	}

	/**
	 * Called on RestExpress shutdown.
	 */
	@Override
	public void shutdown(RestExpress server)
	{
		// Do nothing (no resources allocated that need releasing).
	}


	// SECTION: MESSAGE OBSERVER

	@Override
	protected void onReceived(Request request, Response response)
	{
		activeRequestsCounter.inc();
		START_TIMES_BY_CORRELATION_ID.put(request.getCorrelationId(), System.nanoTime());
	}

	@Override
	protected void onException(Throwable exception, Request request, Response response)
	{
		allExceptionsCounter.inc();

		String name = getRouteName(request);
		if (name == null || name.isEmpty()) return;

		Counter exceptionCounter = EXCEPTION_COUNTERS_BY_ROUTE.get(name);
		if (exceptionCounter == null) return;

		exceptionCounter.inc();
	}

	@Override
	protected void onComplete(Request request, Response response)
	{
		activeRequestsCounter.dec();
		Long duration = computeDurationMillis(START_TIMES_BY_CORRELATION_ID.remove(request.getCorrelationId()));

		if (duration != null && duration.longValue() > 0)
		{
			allTimesTimer.update(duration, TimeUnit.MILLISECONDS);

			String name = getRouteName(request);
			if (name == null || name.isEmpty()) return;

			ROUTE_TIMERS.get(name).update(duration, TimeUnit.MILLISECONDS);
		}

		Counter responseCounter = COUNTERS_BY_RESPONSE.get(response.getResponseStatus().getCode());
		
		if (responseCounter == null)
		{
			responseCounter = metrics.counter(getResponseCounterName(response.getResponseStatus()));
			COUNTERS_BY_RESPONSE.putIfAbsent(response.getResponseStatus().getCode(), responseCounter);
		}

		responseCounter.inc();
		publish(request, response, duration);
	}

	/**
	 * Do some additional processing to publish the metrics, if necessary.  This is a TemplateMethod,
	 * called at the end of onComplete() for sub-classes to do additional processing or publishing.
	 * <p/>
	 * Default behavior is to do nothing.  Override if you need additional functionality.
	 * 
	 * @param request the Request being processed/measured.
	 * @param response the Response being sent back to the client.
	 * @param duration the duration of the request in milliseconds (the time between onReceived() and onComplete() begin called).
	 */
	protected void publish(Request request, Response response, Long duration)
    {
		// Default is to do nothing.  Sub-classes can override.
    }


	// SECTION: PREPROCESSOR

	@Override
	public void process(Request request)
	{
		String name = getRouteName(request);

		if (name == null || name.isEmpty()) return;

		if (!ROUTE_TIMERS.containsKey(name))
		{
			ROUTE_TIMERS.putIfAbsent(name, metrics.timer(getTimerName(name)));
		}

		if (!EXCEPTION_COUNTERS_BY_ROUTE.containsKey(name))
		{
			EXCEPTION_COUNTERS_BY_ROUTE.putIfAbsent(name, metrics.counter(getExceptionCounterName(name)));
		}
	}


	// SECTION: POSTPROCESSOR

	/**
	 * Set the 'X-Response-Time' header to the response time in milliseconds.
	 */
	@Override
	public void process(Request request, Response response)
	{
		Long duration = computeDurationMillis(START_TIMES_BY_CORRELATION_ID.get(request.getCorrelationId()));
		
		if (duration != null && duration.longValue() > 0)
		{
			response.addHeader("X-Response-Time", String.valueOf(duration));
		}
	}

	
	// SECTION: UTILITY - PRIVATE

	private Long computeDurationMillis(Long started)
	{
		Long duration = 0L;

		if (started != null && started.longValue() > 0)
		{
			duration = Math.max(0L, (System.nanoTime() - started) / 1000000);
		}

		return duration;
	}

	private String getRouteName(Request request)
	{
		if (request.getResolvedRoute() == null) return StringUtils.EMPTY_STRING;

		String name = request.getResolvedRoute().getName();
		
		if (name == null || name.trim().isEmpty())
		{
			name = request.getResolvedRoute().getPattern();
		}

		return name;
	}

	private String getTimerName(String routeName)
	{
		return routeName + "-times";
	}

	private String getExceptionCounterName(String routeName)
	{
		return routeName + "-exceptions";
	}

	private String getResponseCounterName(HttpResponseStatus responseStatus)
    {
	    return responseStatus.getCode() + "-" + responseStatus.getReasonPhrase() + "-count";
    }
}
