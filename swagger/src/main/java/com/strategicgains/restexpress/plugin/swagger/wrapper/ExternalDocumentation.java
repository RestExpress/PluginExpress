package com.strategicgains.restexpress.plugin.swagger.wrapper;

/**
 * @see https://swagger.io/specification/#externalDocumentationObject
 */
public class ExternalDocumentation {
	private String description;
	private String url;
	
	public ExternalDocumentation(String url) {
		setUrl(url);
	}
	
	public ExternalDocumentation(io.swagger.v3.oas.annotations.ExternalDocumentation externalDocs) {
		setDescription(OpenApi.nullIfEmpty(externalDocs.description()));
		setUrl(OpenApi.nullIfEmpty(externalDocs.url()));
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean isValid() {
		return description != null && url != null;
	}
}