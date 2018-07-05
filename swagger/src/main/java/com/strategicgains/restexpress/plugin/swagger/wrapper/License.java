package com.strategicgains.restexpress.plugin.swagger.wrapper;

/**
 * @see https://swagger.io/specification/#licenseObject
 */
public class License {
	private String name;
	private String url;
	
	public License(String name) {
		setName(name);
	}
	
	public License(String name, String url) {
		setName(name);
		setUrl(url);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}