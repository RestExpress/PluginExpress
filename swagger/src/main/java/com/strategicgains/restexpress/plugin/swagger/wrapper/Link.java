package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.Map;

/**
 * @see https://swagger.io/specification/#linkObject
 */
public class Link extends Reference{
	private String operationRef;
	private String operationId;
	private Map<String, Object> parameters;
	private String requestBody;
	private String description;
	private Server server;

	public Link(String ref) {
		super(ref);
	}
	
	public Link(io.swagger.v3.oas.annotations.links.Link l) {
		setOperationRef(OpenApi.nullIfEmpty(l.operationRef()));
		setOperationId(OpenApi.nullIfEmpty(l.operationId()));
		
		for(io.swagger.v3.oas.annotations.links.LinkParameter lp: l.parameters()) {
			String name = OpenApi.nullIfEmpty(lp.name());
			if(name != null) {
				getParameters().put(name, new LinkParameter(lp));				
			}
		}
		
		setRequestBody(OpenApi.nullIfEmpty(l.requestBody()));
		setDescription(OpenApi.nullIfEmpty(l.description()));
		setServer(new Server(l.server()));
	}
	
	public String getOperationRef() {
		return operationRef;
	}
	
	public void setOperationRef(String operationRef) {
		this.operationRef = operationRef;
	}
	
	public String getOperationId() {
		return operationId;
	}
	
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	public String getRequestBody() {
		return requestBody;
	}
	
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
}