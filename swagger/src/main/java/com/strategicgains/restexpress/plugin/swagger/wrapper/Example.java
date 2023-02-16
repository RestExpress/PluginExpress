package com.strategicgains.restexpress.plugin.swagger.wrapper;

/**
 * @see https://swagger.io/specification/#exampleObject
 */
public class Example extends Reference{
	private String summary;
	private String description;
	private String value;
	private String externalValue;
	
	public Example(String ref) {
		super(ref);
	}
	
	public Example(io.swagger.v3.oas.annotations.media.ExampleObject e) {
		setSummary(OpenApi.nullIfEmpty(e.summary()));
		//TODO: description in annotation missing setDescription(OpenApi.nullIfEmpty(e.description()));
		setValue(OpenApi.nullIfEmpty(e.value()));
		setExternalValue(OpenApi.nullIfEmpty(e.externalValue()));
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
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getExternalValue() {
		return externalValue;
	}
	
	public void setExternalValue(String externalValue) {
		this.externalValue = externalValue;
	}
}