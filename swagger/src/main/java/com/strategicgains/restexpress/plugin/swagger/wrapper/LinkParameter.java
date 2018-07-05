package com.strategicgains.restexpress.plugin.swagger.wrapper;

public class LinkParameter {
	private String name;
	private String expression;
	
	public LinkParameter(io.swagger.oas.annotations.links.LinkParameter lp) {
		setName(OpenApi.nullIfEmpty(lp.name()));
		setExpression(OpenApi.nullIfEmpty(lp.expression()));
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
}
