package com.strategicgains.restexpress.plugin.swagger.wrapper;

/**
 * @see https://swagger.io/specification/#headerObject
 */
public class Header extends Reference{
	private String name;
	private String description;
	private Boolean required;
	private Boolean deprecated;

	private Schema schema;
	private String example;

	public Header(String ref) {
		super(ref);
	}

	public Header(io.swagger.v3.oas.annotations.headers.Header h) {
		setName(OpenApi.nullIfEmpty(h.name()));
		setDescription(OpenApi.nullIfEmpty(h.description()));
		setRequired(h.required());
		setDeprecated(h.deprecated());
		
		setSchema(new Schema(h.schema()));
		if(!getSchema().isValid()){
			setSchema(null);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}