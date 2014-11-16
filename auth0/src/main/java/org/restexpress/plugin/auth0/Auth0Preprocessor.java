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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.restexpress.Request;
import org.restexpress.exception.UnauthorizedException;
import org.restexpress.pipeline.Preprocessor;

import com.auth0.jwt.JWTVerifier;

/**
 * @author toddf
 * @since Nov 16, 2014
 */
public class Auth0Preprocessor
implements Preprocessor
{
	private static final Pattern PATTERN = Pattern.compile("^Bearer (.*)$", Pattern.CASE_INSENSITIVE);

	private JWTVerifier jwtVerifier;
	private Map<String, String> tokenMap = new HashMap<String, String>();

	public Auth0Preprocessor(String clientId, String secret)
	{
		jwtVerifier = new JWTVerifier(clientId, secret);
	}

	public void map(String from, String to)
	{
		tokenMap.put(from, to);
	}

	@Override
	public void process(Request request)
	{
		String token = getToken(request);

		try
		{
			Map<String, Object> decoded = jwtVerifier.verify(token);
			mapTokenValues(decoded, request);
		}
		catch (Exception e)
		{
			throw new UnauthorizedException("Token validation failed", e);
		}
	}

	private String getToken(Request request)
	throws UnauthorizedException
	{
		String authorization = request.getHeader(HttpHeaders.Names.AUTHORIZATION);

		if (authorization == null)
		{
			throw new UnauthorizedException("No Authorization header was found");
		}

		Matcher matcher = PATTERN.matcher(authorization);

		if (matcher.find())
		{
			return matcher.group(1);
		}
		else
		{
			throw new UnauthorizedException("Format is Authorization: Bearer [token]");
		}
	}

	private void mapTokenValues(Map<String, Object> jwt, Request request)
    {
		for (Entry<String, String> entry : tokenMap.entrySet())
		{
			Object value = jwt.get(entry.getKey());

			if (value != null)
			{
				request.putAttachment(entry.getValue(), value);
			}
		}
    }
}
