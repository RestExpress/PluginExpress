package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @see https://swagger.io/specification/#discriminatorObject
 */
public class Discriminator {
	private String propertyName;
	private Map<String, String> mapping;
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public Map<String, String> getMapping() {
		if(mapping == null) {
			mapping = new HashMap<String, String>();
		}
		return mapping;
	}
	
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
}