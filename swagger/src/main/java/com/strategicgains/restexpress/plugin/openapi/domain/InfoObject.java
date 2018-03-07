/*
    Copyright 2018, Strategic Gains, Inc.

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
 * @since Mar 6, 2018
 */
public class InfoObject
{
	private static final String DEFAULT_VERSION = "1.0";
	private String title = "Please set a title";
	private String description;
	private String termsOfService;
	private ContactObject contact;
	private LicenseObject license;
	private String version = DEFAULT_VERSION;

	public InfoObject()
	{
		super();
	}

	public InfoObject(String title, String version)
	{
		super();
		this.title = title;
		this.version = version;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getTermsOfService()
	{
		return termsOfService;
	}

	public void setTermsOfService(String termsOfService)
	{
		this.termsOfService = termsOfService;
	}

	public ContactObject getContact()
	{
		return contact;
	}

	public void setContact(ContactObject contact)
	{
		this.contact = contact;
	}

	public LicenseObject getLicense()
	{
		return license;
	}

	public void setLicense(LicenseObject license)
	{
		this.license = license;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}
}
