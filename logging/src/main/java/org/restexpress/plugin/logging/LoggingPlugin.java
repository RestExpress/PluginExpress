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

import org.restexpress.RestExpress;
import org.restexpress.pipeline.MessageObserver;
import org.restexpress.plugin.AbstractPlugin;

/**
 * Adds SLF4J logging to your API to provide configurable output.
 * 
 * @author toddf
 * @since Nov 22, 2014
 */
public class LoggingPlugin
extends AbstractPlugin
{
	private String name;

	public LoggingPlugin()
	{
		super();
	}

	public LoggingPlugin(String logName)
	{
		this();
		this.name = logName;
	}

	@Override
	public LoggingPlugin register(RestExpress restExpress)
	{
		if (isRegistered()) return this;

		super.register(restExpress);

		restExpress.addMessageObserver(createObserver(restExpress));
		return this;
	}

	private MessageObserver createObserver(RestExpress server)
    {
		String logName = (name == null ? server.getName() : name);
		return new LoggingMessageObserver(logName);
    }
}
