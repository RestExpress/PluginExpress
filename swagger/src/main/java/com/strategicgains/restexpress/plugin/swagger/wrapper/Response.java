package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

import io.swagger.oas.annotations.responses.ApiResponse;

/**
 * @see https://swagger.io/specification/#responsesObject
 * @see https://swagger.io/specification/#responseObject
 *
 */
public class Response {
	private String description;
	private Map<String, Header> headers;
	private Map<String, MediaType> content;
	private Map<String, Link> links;
	
	public Response(ApiResponse r) {
		setDescription(OpenApi.nullIfEmpty(r.description()));
		
		for(io.swagger.oas.annotations.headers.Header h : r.headers()) {
			String name = OpenApi.nullIfEmpty(h.name());
			if(name != null) {
				getHeaders().put(name, new Header(h));
			}
		}
		
		for(io.swagger.oas.annotations.media.Content c : r.content()) {
			String name = OpenApi.nullIfEmpty(c.mediaType());
			if(name != null) {
				getContent().put(c.mediaType(), new MediaType(c));
			}
		}
		
		for(io.swagger.oas.annotations.links.Link l : r.links()) {
			String name = OpenApi.nullIfEmpty(l.name());
			if(name != null) {
				getLinks().put(name, new Link(l));
			}
		}
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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
	
	public Map<String, MediaType> getContent() {
		if(content == null) {
			content = new HashMap<String, MediaType>();
		}
		return content;
	}
	
	public void setContent(Map<String, MediaType> content) {
		this.content = content;
	}
	
	public Map<String, Link> getLinks() {
		if(links == null) {
			links = new HashMap<String, Link>();
		}
		return links;
	}

	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}
}