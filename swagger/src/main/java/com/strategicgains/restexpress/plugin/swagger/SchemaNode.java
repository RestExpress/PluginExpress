package com.strategicgains.restexpress.plugin.swagger;

public class SchemaNode {

    private String type;
    private String format;
    private String ref;
    private Boolean uniqueItems;
    private String description;
    private transient boolean required;

    public String getType() {
        return type;
    }

    public SchemaNode type(String type) {
        this.type = type;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public SchemaNode format(String format) {
        this.format = format;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public SchemaNode ref(String ref) {
        this.ref = ref;
        return this;
    }

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    public boolean isRequired() {
        return required;
    }

    public SchemaNode required(boolean required) {
        this.required = required;
        return this;
    }

    public SchemaNode uniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SchemaNode description(String description) {
        this.description = description;
        return this;
    }
}
