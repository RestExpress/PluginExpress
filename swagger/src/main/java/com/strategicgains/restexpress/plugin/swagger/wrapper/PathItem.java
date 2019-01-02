package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.List;

/**
 * @see https://swagger.io/specification/#pathObject
 * @see https://swagger.io/specification/#pathItemObject
 */
public class PathItem extends Reference{
	private String summary;
	private String description;
	
	private Operation get;
	private Operation put;
	private Operation post;
	private Operation delete;
	private Operation options;
	private Operation head;
	private Operation patch;
	private Operation trace;
	
	private List<Server> servers;
	private List<Parameter> parameters;

	public PathItem() {
		super();
	}
	
	public PathItem(String ref) {
		super(ref);
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

	public Operation getGet() {
		return get;
	}

	public void setGet(Operation get) {
		this.get = get;
	}

	public Operation getPut() {
		return put;
	}

	public void setPut(Operation put) {
		this.put = put;
	}

	public Operation getPost() {
		return post;
	}

	public void setPost(Operation post) {
		this.post = post;
	}

	public Operation getDelete() {
		return delete;
	}

	public void setDelete(Operation delete) {
		this.delete = delete;
	}

	public Operation getOptions() {
		return options;
	}

	public void setOptions(Operation options) {
		this.options = options;
	}

	public Operation getHead() {
		return head;
	}

	public void setHead(Operation head) {
		this.head = head;
	}

	public Operation getPatch() {
		return patch;
	}

	public void setPatch(Operation patch) {
		this.patch = patch;
	}

	public Operation getTrace() {
		return trace;
	}

	public void setTrace(Operation trace) {
		this.trace = trace;
	}

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}
	
	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void add(String method, Operation operation) {
		switch(method.toUpperCase()) {
			case "OPTIONS":
				setOptions(operation);
				break;
			case "GET":
				setGet(operation);
				break;
			case "HEAD":
				setHead(operation);
				break;
			case "POST":
				setPost(operation);
				break;
			case "PUT":
				setPut(operation);
				break;
			case "PATCH":
				setPatch(operation);
				break;
			case "DELETE":
				setDelete(operation);
				break;
			case "TRACE":
				setTrace(operation);
				break;
		}
	}

	public Operation get(String method) {
		switch(method.toUpperCase()) {
			case "OPTIONS":
				return getOptions();
			case "GET":
				return getGet();
			case "HEAD":
				return getHead();
			case "POST":
				return getPost();
			case "PUT":
				return getPut();
			case "PATCH":
				return getPatch();
			case "DELETE":
				return getDelete();
			case "TRACE":
				return getTrace();
			}
		return null;
	}
}