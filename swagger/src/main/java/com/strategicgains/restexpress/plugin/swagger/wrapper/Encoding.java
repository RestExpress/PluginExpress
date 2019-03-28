package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @see https://swagger.io/specification/#encodingObject
 */
public class Encoding {
	private String contentType;
	private Map<String, Header> headers;
	private String style;
	private Boolean explode;
	private Boolean allowReserved;
	
	public Encoding(io.swagger.v3.oas.annotations.media.Encoding e) {
		setContentType(OpenApi.nullIfEmpty(e.contentType()));
		for(io.swagger.v3.oas.annotations.headers.Header h : e.headers()) {
			getHeaders().put(e.name(), new Header(h));
		}
		setStyle(OpenApi.nullIfEmpty(e.style()));
		setExplode(e.explode());
		setAllowReserved(e.allowReserved());
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public Map<String, Header> getHeaders() {
		if(headers == null) {
			headers = new HashMap<String, Header>();
		}
		return headers;
	}
	
	public void setHeaders(Map<String, Header> headers) {
		this.headers = headers;
	}
	
	public String getStyle() {
		return style;
	}
	
	public void setStyle(String style) {
		this.style = style;
	}
	
	public Boolean getExplode() {
		return explode;
	}
	
	public void setExplode(Boolean explode) {
		this.explode = explode;
	}
	
	public Boolean getAllowReserved() {
		return allowReserved;
	}
	
	public void setAllowReserved(Boolean allowReserved) {
		this.allowReserved = allowReserved;
	}
}