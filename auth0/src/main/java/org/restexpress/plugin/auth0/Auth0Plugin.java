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
package org.restexpress.plugin.auth0;

import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractPlugin;

/**
 * Implements Auth0 token authentication.
 * 
 * @author toddf
 * @since Nov 16, 2014
 */
public class Auth0Plugin
extends AbstractPlugin
{
	private Auth0Preprocessor auth0;

	public Auth0Plugin(String clientId, String secrect)
	{
		super();
		this.auth0 = new Auth0Preprocessor(clientId, secrect);
	}

	/**
	 * After Auth0 authenticates the JWT, extract the 'from' properties
	 * attaching them to the Request (as attachments) using the 'to' name.
	 * 
	 * @param from the name of a JWT property to extract.
	 * @param to the name to give the Request attachment.
	 * @return this plugin.
	 */
	public Auth0Plugin extractJwt(String from, String to)
	{
		auth0.map(from, to);
		return this;
	}

	@Override
	public Auth0Plugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		server.addPreprocessor(auth0);

		return this;
	}
}
