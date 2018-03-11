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
public class Info
{
	private String title;
	private String description;
	private String termsOfServiceUrl;
	private String contact;
	private String license;
	private String licenseUrl;

	public Info(String title, String description)
	{
		super();
		this.title = title;
		this.description = description;
	}

	public String getTermsOfServiceUrl()
	{
		return termsOfServiceUrl;
	}

	public void setTermsOfServiceUrl(String termsOfServiceUrl)
	{
		this.termsOfServiceUrl = termsOfServiceUrl;
	}

	public String getContact()
	{
		return contact;
	}

	public void setContact(String contact)
	{
		this.contact = contact;
	}

	public String getLicense()
	{
		return license;
	}

	public void setLicense(String license)
	{
		this.license = license;
	}

	public String getLicenseUrl()
	{
		return licenseUrl;
	}

	public void setLicenseUrl(String licenseUrl)
	{
		this.licenseUrl = licenseUrl;
	}

	public String getTitle()
	{
		return title;
	}

	public String getDescription()
	{
		return description;
	}
}
