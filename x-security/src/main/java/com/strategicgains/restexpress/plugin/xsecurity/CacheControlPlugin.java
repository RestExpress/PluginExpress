/*
    Copyright 2011, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.plugin.cache;

import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractPlugin;

/**
 * @author cjm
 * @since July 6, 2014
 */
public class XSecurityPlugin
extends AbstractPlugin
{
	@Override
	public XSecurityPlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);

		server
			.addPostprocessor(new DateHeaderPostprocessor())
			.addPostprocessor(new CacheHeaderPostprocessor())
			.addPostprocessor(new EtagHeaderPostprocessor());

		return this;
	}
}
