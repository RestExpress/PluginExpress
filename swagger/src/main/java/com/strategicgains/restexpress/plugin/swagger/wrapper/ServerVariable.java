package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see https://swagger.io/specification/#serverVariableObject
 */
public class ServerVariable {
	@JsonProperty("enum")
	private List<String> enumVar;
	@JsonProperty("default")
	private String defaultValue;
	private String description;
	
	public ServerVariable(String defaultValue) {
		setDefault(defaultValue);
	}
	
	public ServerVariable(io.swagger.v3.oas.annotations.servers.ServerVariable sv) {
		if(sv.allowableValues() != null && sv.allowableValues().length > 0) {
			setEnumVar(Arrays.asList(sv.allowableValues()));			
		}
		setDefault(sv.defaultValue());
		setDescription(OpenApi.nullIfEmpty(sv.description()));
	}

	public List<String> getEnumVar() {
		if(enumVar == null) {
			enumVar = new ArrayList<String>();
		}
		return enumVar;
	}
	
	public void setEnumVar(List<String> enumVar) {
		this.enumVar = enumVar;
	}
	
	public void addEnum(String string) {
		if(enumVar == null) {
			enumVar = new ArrayList<String>();
		}
		enumVar.add(string);
	}
	
	public String getDefault() {
		return defaultValue;
	}
	
	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
