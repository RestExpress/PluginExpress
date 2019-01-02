package com.strategicgains.restexpress.plugin.swagger.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * https://swagger.io/specification/#oasObject
 *
 */
public class OpenApi {
	private String openapi;
	private Info info;
	private List<Server> servers;
	private Map<String, PathItem> paths;
	private Components components;
	private SecurityRequirement security;
	private List<Tag> tags;
	private ExternalDocumentation externalDocs;
	
	public OpenApi(Info info) {
		setOpenapi("3.0.0");
		setInfo(info);
		servers = new ArrayList<Server>();
		paths = new TreeMap<String, PathItem>();
	}

	public String getOpenapi() {
		return openapi;
	}

	public void setOpenapi(String openapi) {
		this.openapi = openapi;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

	public Map<String, PathItem> getPaths() {
		return paths;
	}

	public void setPaths(Map<String, PathItem> paths) {
		this.paths = paths;
	}

	public void addPath(String path, PathItem value) {
		getPaths().put(path, value);
	}

	public Components getComponents() {
		return components;
	}

	public void setComponents(Components components) {
		this.components = components;
	}

	public SecurityRequirement getSecurity() {
		return security;
	}

	public void setSecurity(SecurityRequirement security) {
		this.security = security;
	}

	public List<Tag> getTags() {
		if(tags == null) {
			tags = new ArrayList<Tag>();
		}
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public ExternalDocumentation getExternalDocs() {
		return externalDocs;
	}

	public void setExternalDocs(ExternalDocumentation externalDocs) {
		this.externalDocs = externalDocs;
	}
	
	public static String nullIfEmpty(String str) {
		if(str != null && !str.isEmpty()){
			return str;
		}else {
			return null;
		}
	}
}
