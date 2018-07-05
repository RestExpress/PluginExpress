package com.strategicgains.restexpress.plugin.swagger.wrapper;

/**
 * @see https://swagger.io/specification/#securitySchemeObject
 */
public class SecurityScheme {
	private String type;
	private String description;
	private String name;
	private String in;
	private String scheme;
	private String bearerFormat;
	private OAuthFlows flows;
	private String openIdConnectUrl;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIn() {
		return in;
	}
	
	public void setIn(String in) {
		this.in = in;
	}
	
	public String getScheme() {
		return scheme;
	}
	
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	
	public String getBearerFormat() {
		return bearerFormat;
	}
	
	public void setBearerFormat(String bearerFormat) {
		this.bearerFormat = bearerFormat;
	}
	
	public OAuthFlows getFlows() {
		return flows;
	}
	
	public void setFlows(OAuthFlows flows) {
		this.flows = flows;
	}
	
	public String getOpenIdConnectUrl() {
		return openIdConnectUrl;
	}
	
	public void setOpenIdConnectUrl(String openIdConnectUrl) {
		this.openIdConnectUrl = openIdConnectUrl;
	}
}
