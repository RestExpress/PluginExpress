package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see https://swagger.io/specification/#operationObject
 */
public class Operation {
	private List<String> tags;
	private String summary;
	private String description;
	private ExternalDocumentation externalDocs;
	private String operationId;
	private List<Parameter> parameters;
	private RequestBody requestBody;
	private Map<String, Response> responses;
	private Map<String, Callback>callbacks;
	private Boolean deprecated;
	private List<Map<String, String[]>> security;
	private List<Server> servers;
	
	public Operation() {
		
	}
	
	public Operation(io.swagger.oas.annotations.Operation op) {
		setTags(Arrays.asList(op.tags()));
		setSummary(OpenApi.nullIfEmpty(op.summary()));
		setDescription(OpenApi.nullIfEmpty(op.description()));
		setExternalDocs(new ExternalDocumentation(op.externalDocs()));
		if(!getExternalDocs().isValid()){
			setExternalDocs(null);
		}
		setOperationId(OpenApi.nullIfEmpty(op.operationId()));
		
		for(io.swagger.oas.annotations.Parameter p : op.parameters()) {
			getParameters().add(new Parameter(p));
		}
		
		setRequestBody(new RequestBody(op.requestBody()));
		if(!getRequestBody().isValid()) {
			setRequestBody(null);
		}
		
		for(io.swagger.oas.annotations.responses.ApiResponse r : op.responses()) {
			if(OpenApi.nullIfEmpty(r.responseCode()) != null) {
				getResponses().put(r.responseCode(), new Response(r));
			}
		}
		
		//TODO: callbacks ????  == Extension[] extensions() 
		
		setDeprecated(op.deprecated());
		
		for(io.swagger.oas.annotations.security.SecurityRequirement sr : op.security()) {
			if(OpenApi.nullIfEmpty(sr.name()) != null) {
				Map<String, String[]> sec = new HashMap<String, String[]>();
				sec.put(sr.name(), sr.scopes());
				getSecurity().add(sec);
			}
		}
		
		for(io.swagger.oas.annotations.servers.Server s : op.servers()) {
			getServers().add(new Server(s));
		}
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public ExternalDocumentation getExternalDocs() {
		return externalDocs;
	}

	public void setExternalDocs(ExternalDocumentation externalDocs) {
		this.externalDocs = externalDocs;
	}

	public String getOperationId() {
		return operationId;
	}
	
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	
	public List<Parameter> getParameters() {
		if(parameters == null) {
			parameters = new ArrayList<Parameter>();
		}
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public RequestBody getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
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
	
	public Map<String, Callback> getCallbacks() {
		if(callbacks == null) {
			callbacks = new HashMap<String, Callback>();
		}
		return callbacks;
	}

	public void setCallbacks(Map<String, Callback> callbacks) {
		this.callbacks = callbacks;
	}

	public Boolean getDeprecated() {
		return deprecated;
	}
	
	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}
	
	public List<Map<String, String[]>> getSecurity() {
		if(security == null) {
			security = new ArrayList<Map<String, String[]>>();
		}
		return security;
	}

	public void setSecurity(List<Map<String, String[]>> security) {
		this.security = security;
	}

	public List<Server> getServers() {
		if(servers == null) {
			servers = new ArrayList<Server>();
		}
		return servers;
	}
	
	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

}
