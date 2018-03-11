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
package com.strategicgains.restexpress.plugin.openapi.domain;

/**
 * @author toddf
 * @since Apr 1, 2014
 */
public class ImplicitGrantType
{
	private String tokenName;
	private LoginEndpoint loginEndpoint;

	public ImplicitGrantType(String loginEndpoint)
	{
		super();
		this.loginEndpoint = new LoginEndpoint(loginEndpoint);
	}

	public String getTokenName()
	{
		return tokenName;
	}

	public void setTokenName(String tokenName)
	{
		this.tokenName = tokenName;
	}

	public String getLoginEndpoint()
	{
		return (loginEndpoint == null ? null : loginEndpoint.getUrl());
	}

	public class LoginEndpoint
	{
		private String url;

		public LoginEndpoint(String url)
		{
			super();
			this.url = url;
		}

		public String getUrl()
		{
			return url;
		}
	}
}
