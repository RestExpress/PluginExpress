package com.strategicgains.restexpress.plugin.swagger.wrapper;

/**
 * @see https://swagger.io/specification/#contactObject
 */
public class Contact {
	private String name;
	private String url;
	private String email;
	
	public Contact(String name, String url, String email) {
		setName(name);
		setUrl(url);
		setEmail(email);
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}