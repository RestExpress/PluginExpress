package com.strategicgains.restexpress.plugin.swagger.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see https://swagger.io/specification/#referenceObject
 */
public class Reference {
	@JsonProperty("$ref")
	private String ref;
	
	public Reference() {}
	
	public Reference(String ref) {
		setRef(ref);
	}
	
	public String getRef() {
		return ref;
	}
	
	public void setRef(String ref) {
		this.ref = ref;
	}
}
