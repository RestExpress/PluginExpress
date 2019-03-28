package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.enums.Explode;

/**
 * @see https://swagger.io/specification/#parameterObject
 */
public class Parameter extends Reference{
	private String name;
	private String in;
	private String description;
	private Boolean required;
	private Boolean deprecated;
	private Boolean allowEmptyValue;
	
	private String style;
	private Boolean explode;
	private Boolean allowReserved;
	private Schema schema;
	private String example;
	private Map<String, Example> examples;

	public Parameter(String ref) {
		super(ref);
	}
	
	public Parameter(io.swagger.v3.oas.annotations.Parameter p) {
		setName(OpenApi.nullIfEmpty(p.name()));
		setIn(OpenApi.nullIfEmpty(p.in().toString()));
		setDescription(OpenApi.nullIfEmpty(p.description()));
		setRequired(p.required());
		setDeprecated(p.deprecated());
		setAllowEmptyValue(p.allowEmptyValue());
		
		setStyle(OpenApi.nullIfEmpty(p.style().toString()));
		setExplode(p.explode() == Explode.TRUE);
		setAllowReserved(p.allowReserved());
		setSchema(new Schema(p.schema()));
		if(!getSchema().isValid()){
			setSchema(null);
		}
		setExample(OpenApi.nullIfEmpty(p.example()));
		for(io.swagger.v3.oas.annotations.media.ExampleObject e : p.examples()) {
			if(e.name().length() > 0) {
				getExamples().put(e.name(), new Example(e));				
			}
		}
		// TODO: check unmapped annatation vars
		/*
		 * ArraySchema array() default @ArraySchema();
		 * Content[] content() default {};
		 * boolean hidden() default false;
		 */
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIn() {
		return in;
	}
	
	public void setIn(String in) {
		this.in = in;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean getRequired() {
		return required;
	}
	
	public void setRequired(Boolean required) {
		this.required = required;
	}
	
	public Boolean getDeprecated() {
		return deprecated;
	}
	
	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}
	
	public Boolean getAllowEmptyValue() {
		return allowEmptyValue;
	}
	
	public void setAllowEmptyValue(Boolean allowEmptyValue) {
		this.allowEmptyValue = allowEmptyValue;
	}
	
	public String getStyle() {
		return style;
	}
	
	public void setStyle(String style) {
		this.style = style;
	}
	
	public Boolean getExplode() {
		return explode;
	}
	
	public void setExplode(Boolean explode) {
		this.explode = explode;
	}
	
	public Boolean getAllowReserved() {
		return allowReserved;
	}
	
	public void setAllowReserved(Boolean allowReserved) {
		this.allowReserved = allowReserved;
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	
	public String getExample() {
		return example;
	}
	
	public void setExample(String example) {
		this.example = example;
	}
	
	public Map<String, Example> getExamples() {
		if(examples == null) {
			examples = new HashMap<String,Example>();
		}
		return examples;
	}
	
	public void setExamples(Map<String, Example> examples) {
		this.examples = examples;
	}
	
}
