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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.restexpress.Flags;
import org.restexpress.Request;
import org.restexpress.exception.UnauthorizedException;
import org.restexpress.pipeline.Preprocessor;

import com.auth0.jwt.JWTVerifier;

/**
 * Extracts the Auth0 JWT token, decodes it and places it as an attachment on the request.
 * Supports use of Flags.Auth.PUBLIC_ROUTE and Flags.Auth.NO_AUTHENTICATION, which both
 * cause all Auth0 processing to be skipped: no token is attached to the request.
 * 
 * @author toddf
 * @since Nov 16, 2014
 */
public class Auth0Preprocessor
implements Preprocessor
{
	/*
	 * Name given to the Request attachment that may be used to retrieve the decoded JWT.
	 * <p/>
	 * For example: Map<String, Object> jwt = request.getAttachment(Auth0Preprocessor.AUTH0_JWT);
	 */
	public static final String AUTH0_JWT = "auth0.jwt";
	private static final Pattern PATTERN = Pattern.compile("^Bearer (.*)$", Pattern.CASE_INSENSITIVE);

	private JWTVerifier jwtVerifier;

	public Auth0Preprocessor(String clientId, String secret)
	{
		jwtVerifier = new JWTVerifier(secret, clientId);
	}

	@Override
	public void process(Request request)
	{
		if (request.isFlagged(Flags.Auth.PUBLIC_ROUTE)
			|| request.isFlagged(Flags.Auth.NO_AUTHENTICATION)) return;
			
		String token = getToken(request);

		try
		{
			Map<String, Object> decoded = jwtVerifier.verify(token);
			request.putAttachment(AUTH0_JWT, decoded);
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
}
