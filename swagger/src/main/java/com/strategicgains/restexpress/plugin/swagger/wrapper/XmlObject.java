package com.strategicgains.restexpress.plugin.swagger.wrapper;

/**
 * @see https://swagger.io/specification/#xmlObject
 */
public class XmlObject {
	private String name;
	private String namespace;
	private String prefix;
	private Boolean attribute;
	private Boolean wrapped;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public Boolean getAttribute() {
		return attribute;
	}
	
	public void setAttribute(Boolean attribute) {
		this.attribute = attribute;
	}
	
	public Boolean getWrapped() {
		return wrapped;
	}
	
	public void setWrapped(Boolean wrapped) {
		this.wrapped = wrapped;
	}
}