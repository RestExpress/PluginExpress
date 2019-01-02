package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @see https://swagger.io/specification/#componentsObject
 */
public class Components {
	private Map<String, Schema> schemas;
	private Map<String, Response> responses;
	private Map<String, Parameter> parameters;
	private Map<String, Example> examples;
	private Map<String, RequestBody> requestBodies;
	private Map<String, Header> headers;
	private Map<String, SecurityScheme> securitySchemes;
	private Map<String, Link> links;
	private Map<String, Callback> callbacks;
	
	public Map<String, Schema> getSchemas() {
		if(schemas == null) {
			schemas = new HashMap<String, Schema>();
		}
		return schemas;
	}
	
	public void setSchemas(Map<String, Schema> schemas) {
		this.schemas = schemas;
	}
	
	public Map<String, Response> getResponses() {
		if(responses == null) {
			responses = new HashMap<String, Response>();
		}
		return responses;
	}
	
	public void setResponses(Map<String, Response> responses) {
		this.responses = responses;
	}
	
	public Map<String, Parameter> getParameters() {
		if(parameters == null) {
			parameters = new HashMap<String, Parameter>();
		}
		return parameters;
	}
	
	public void setParameters(Map<String, Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public Map<String, Example> getExamples() {
		if(examples == null) {
			examples = new HashMap<String, Example>();
		}
		return examples;
	}
	
	public void setExamples(Map<String, Example> examples) {
		this.examples = examples;
	}
	
	public Map<String, RequestBody> getRequestBodies() {
		if(requestBodies == null) {
			requestBodies = new HashMap<String, RequestBody>();
		}
		return requestBodies;
	}

	public void setRequestBodies(Map<String, RequestBody> requestBodies) {
		this.requestBodies = requestBodies;
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
	
	public Map<String, SecurityScheme> getSecuritySchemes() {
		if(securitySchemes == null) {
			securitySchemes = new HashMap<String, SecurityScheme>();
		}
		return securitySchemes;
	}
	
	public void setSecuritySchemes(Map<String, SecurityScheme> securitySchemes) {
		this.securitySchemes = securitySchemes;
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

	public Map<String, Callback> getCallbacks() {
		if(callbacks == null) {
			callbacks = new HashMap<String, Callback>();
		}
		return callbacks;
	}

	public void setCallbacks(Map<String, Callback> callbacks) {
		this.callbacks = callbacks;
	}
	
}
