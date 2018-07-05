package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @see https://swagger.io/specification/#serverObject
 */
public class Server {
	private String url;
	private String description;
	private Map<String, ServerVariable> variables;
	
	public Server(String url, String description) {
		setUrl(url);
		setDescription(description);
	}

	public Server(io.swagger.oas.annotations.servers.Server s) {
		setUrl(OpenApi.nullIfEmpty(s.url()));
		setDescription(OpenApi.nullIfEmpty(s.description()));
		
		for(io.swagger.oas.annotations.servers.ServerVariable sv : s.variables()) {
			String name = OpenApi.nullIfEmpty(sv.name());
			if(name != null) {
				getVariables().put(name, new ServerVariable(sv));				
			}
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, ServerVariable> getVariables() {
		if(variables == null) {
			variables = new HashMap<String, ServerVariable>();
		}
		return variables;
	}

	public void setVariables(Map<String, ServerVariable> variables) {
		this.variables = variables;
	}

	public void addVariable(String key, ServerVariable value) {
		if(variables == null) {
			variables = new HashMap<String, ServerVariable>();
		}
		variables.put(key, value);
	}
	
	public boolean isValid() {
		return url != null && description != null && (variables != null &&  !variables.isEmpty());
	}
	
}