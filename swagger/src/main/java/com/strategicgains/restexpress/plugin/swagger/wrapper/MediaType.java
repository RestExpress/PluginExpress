package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @see https://swagger.io/specification/#mediaTypeObject
 */
public class MediaType {
	private Schema schema;
	private String example;
	private Map<String, Example> examples;
	private Map<String, Encoding> encoding;
	
	public MediaType(io.swagger.oas.annotations.media.Content c) {
		setSchema(new Schema(c.schema()));
		if(!getSchema().isValid()){
			setSchema(null);
		}
		
		//TODO:	setExample(); ??
		
		for(io.swagger.oas.annotations.media.ExampleObject e : c.examples()) {
			getExamples().put(e.name(), new Example(e));
		}
		
		for(io.swagger.oas.annotations.media.Encoding e : c.encoding()) {
			getEncoding().put(e.name(), new Encoding(e));
		}
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
			examples = new HashMap<String, Example>();
		}
		return examples;
	}
	
	public void setExamples(Map<String, Example> examples) {
		this.examples = examples;
	}
	
	public Map<String, Encoding> getEncoding() {
		if(encoding == null) {
			encoding = new HashMap<String, Encoding>();
		}
		return encoding;
	}
	
	public void setEncoding(Map<String, Encoding> encoding) {
		this.encoding = encoding;
	}
}