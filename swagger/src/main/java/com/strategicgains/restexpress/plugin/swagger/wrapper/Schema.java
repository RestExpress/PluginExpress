package com.strategicgains.restexpress.plugin.swagger.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://swagger.io/specification/#schemaObject
 */
public class Schema extends Reference{
	private String title;
	private Double multipleOf;
	private String maximum;
	private Boolean exclusiveMaximum;
	private String minimum;
	private Boolean exclusiveMinimum;
	private Integer maxLength;
	private Integer minLength;
	private String pattern;
//	maxItems
//	minItems
//	uniqueItems
	private Integer maxProperties;
	private Integer minProperties;
	private Boolean required;
	@JsonProperty("enum")
	private String[] enumVal;
	
	private String type;
	private Class<?>[] allOf;
	private Class<?>[] oneOf;
	private Class<?>[] anyOf;
	private Class<?> not;
//	items
//	properties
//	additionalProperties
	private String description;
	private String format;
	@JsonProperty("default")
	private String defaultValue;
	
	private Class<?> implementation;
	private String example;
	
	public Schema(String ref) {
		super(ref);
	}
	
	public Schema(io.swagger.v3.oas.annotations.media.Schema schema) {
		setRef(OpenApi.nullIfEmpty(schema.ref()));
		setTitle(OpenApi.nullIfEmpty(schema.title()));
		setMultipleOf(schema.multipleOf());
		setMaximum(OpenApi.nullIfEmpty(schema.maximum()));
		setExclusiveMaximum(schema.exclusiveMaximum());
		setMinimum(schema.minimum());
		setExclusiveMinimum(schema.exclusiveMinimum());
		setMaxLength(schema.maxLength());
		setMinLength(schema.minLength());
		setPattern(OpenApi.nullIfEmpty(schema.pattern()));
		//TODO:		maxItems
		//TODO:		minItems
		//TODO:		uniqueItems	
		setMaxProperties(schema.maxProperties());
		setMinProperties(schema.minProperties());
		setRequired(schema.required());
		if(schema.allowableValues().length > 0) {
			setEnumVal(schema.allowableValues());
		}
		
		setType(OpenApi.nullIfEmpty(schema.type()));
		if(schema.allOf().length > 0) {
			setAllOf(schema.allOf());
		}
		if(schema.oneOf().length > 0) {
			setOneOf(schema.oneOf());
		}
		if(schema.anyOf().length > 0) {
			setAnyOf(schema.anyOf());
		}
		setNot(schema.not());
		//TODO:		items
		//TODO:		properties
		//TODO:		additionalProperties
		setDescription(OpenApi.nullIfEmpty(schema.description()));
		setFormat(OpenApi.nullIfEmpty(schema.format()));
		setDefaultValue(schema.defaultValue());
		
		
		setImplementation(schema.implementation());
		setExample(OpenApi.nullIfEmpty(schema.example()));
		
		//TODO: check unmapped vars
//		  String name() default "";
//		  String[] requiredProperties() default {};
//		  boolean nullable() default false;
//		  boolean readOnly() default false;
//		  boolean writeOnly() default false;
//		  ExternalDocumentation externalDocs() default @ExternalDocumentation();
//		  boolean deprecated() default false;
//		  String discriminatorProperty() default "";
//		  DiscriminatorMapping[] discriminatorMapping() default {};
//		  boolean hidden() default false;
	}

	public Class<?> getImplementation() {
		return implementation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getMultipleOf() {
		return multipleOf;
	}

	public void setMultipleOf(Double multipleOf) {
		this.multipleOf = multipleOf;
	}

	public String getMaximum() {
		return maximum;
	}

	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}

	public Boolean getExclusiveMaximum() {
		return exclusiveMaximum;
	}

	public void setExclusiveMaximum(Boolean exclusiveMaximum) {
		this.exclusiveMaximum = exclusiveMaximum;
	}

	public String getMinimum() {
		return minimum;
	}

	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}

	public Boolean getExclusiveMinimum() {
		return exclusiveMinimum;
	}

	public void setExclusiveMinimum(Boolean exclusiveMinimum) {
		this.exclusiveMinimum = exclusiveMinimum;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Integer getMaxProperties() {
		return maxProperties;
	}

	public void setMaxProperties(Integer maxProperties) {
		this.maxProperties = maxProperties;
	}

	public Integer getMinProperties() {
		return minProperties;
	}

	public void setMinProperties(Integer minProperties) {
		this.minProperties = minProperties;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String[] getEnumVal() {
		return enumVal;
	}

	public void setEnumVal(String[] enumVal) {
		this.enumVal = enumVal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Class<?>[] getAllOf() {
		return allOf;
	}

	public void setAllOf(Class<?>[] allOf) {
		this.allOf = allOf;
	}

	public Class<?>[] getOneOf() {
		return oneOf;
	}

	public void setOneOf(Class<?>[] oneOf) {
		this.oneOf = oneOf;
	}

	public Class<?>[] getAnyOf() {
		return anyOf;
	}

	public void setAnyOf(Class<?>[] anyOf) {
		this.anyOf = anyOf;
	}

	public Class<?> getNot() {
		return not;
	}

	public void setNot(Class<?> not) {
		this.not = not;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setImplementation(Class<?> implementation) {
		this.implementation = implementation;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public boolean isValid() {
		return getRef() != null || implementation != null;
	}
}
