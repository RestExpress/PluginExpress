package com.strategicgains.restexpress.plugin.swagger.wrapper;

/**
 * @see https://swagger.io/specification/#securityRequirementObject
 */
public class SecurityRequirement extends Reference{

	public SecurityRequirement() {
		super();
	}
	
	public SecurityRequirement(String ref) {
		super(ref);
	}
}
