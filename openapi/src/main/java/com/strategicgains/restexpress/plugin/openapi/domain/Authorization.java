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

import java.util.List;

/**
 * @author toddf
 * @since Apr 1, 2014
 */
public class Authorization
{
	private AuthorizationTypes type; // 'basicAuth' | 'apiKey' | 'oauth2'
	private ApiKeys passAs; // 'header' | 'query'
	private ApiKeys keyname; // 'header' | 'query'
	private List<Scope> scopes;
	private GrantTypes grantTypes;

	public Authorization(AuthorizationTypes type, ApiKeys passAs,
	    ApiKeys keyname)
	{
		super();
		this.type = type;
		this.passAs = passAs;
		this.keyname = keyname;
	}

	public List<Scope> getScopes()
	{
		return scopes;
	}

	public void setScopes(List<Scope> scopes)
	{
		this.scopes = scopes;
	}

	public GrantTypes getGrantTypes()
	{
		return grantTypes;
	}

	public void setGrantTypes(GrantTypes grantTypes)
	{
		this.grantTypes = grantTypes;
	}

	public AuthorizationTypes getType()
	{
		return type;
	}

	public ApiKeys getPassAs()
	{
		return passAs;
	}

	public ApiKeys getKeyname()
	{
		return keyname;
	}
}
