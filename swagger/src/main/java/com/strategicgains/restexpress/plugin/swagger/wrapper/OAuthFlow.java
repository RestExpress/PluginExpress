package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

/** 
 * @see https://swagger.io/specification/#oauthFlowObject
 */
public class OAuthFlow {
	private String authorizationUrl;
	private String tokenUrl;
	private String refreshUrl;
	private Map<String, String> scopes;
	
	public String getAuthorizationUrl() {
		return authorizationUrl;
	}
	
	public void setAuthorizationUrl(String authorizationUrl) {
		this.authorizationUrl = authorizationUrl;
	}
	
	public String getTokenUrl() {
		return tokenUrl;
	}
	
	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}
	
	public String getRefreshUrl() {
		return refreshUrl;
	}
	
	public void setRefreshUrl(String refreshUrl) {
		this.refreshUrl = refreshUrl;
	}
	
	public Map<String, String> getScopes() {
		if(scopes == null) {
			scopes = new HashMap<String, String>();
		}
		return scopes;
	}

	public void setScopes(Map<String, String> scopes) {
		this.scopes = scopes;
	}
}