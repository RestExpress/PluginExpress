package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @see https://swagger.io/specification/#requestBodyObject
 */
public class RequestBody extends Reference{
	private String description;
	private Map<String, MediaType> content;
	private Boolean required;
	
	public RequestBody(String ref) {
		super(ref);
	}
	
	public RequestBody(io.swagger.oas.annotations.parameters.RequestBody rb) {
		setDescription(OpenApi.nullIfEmpty(rb.description()));
		for(io.swagger.oas.annotations.media.Content c : rb.content()) {
			if(OpenApi.nullIfEmpty(c.mediaType()) != null) {
				getContent().put(c.mediaType(), new MediaType(c));
			}
		}
		setRequired(rb.required());
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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
	
	public Boolean getRequired() {
		return required;
	}
	
	public void setRequired(Boolean required) {
		this.required = (required) ? true : null;
	}
	
	public boolean isValid() {
		return description != null && required != null && content != null && !content.isEmpty();
	}
}
