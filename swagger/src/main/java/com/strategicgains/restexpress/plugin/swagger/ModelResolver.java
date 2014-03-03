package com.strategicgains.restexpress.plugin.swagger;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// jackson 2.1.4 isn't generating $ref
//
public class ModelResolver {
    private Map<Class, ApiModel> models = new HashMap<Class, ApiModel>();

    public void generate(Class<?> cls) {
        resolveClass(cls);
    }

    private ApiModel resolveClass(Class<?> target) {
        ApiModel model = models.get(target);
        if (model != null) {
            return model;
        }

        if (target.isPrimitive()) {
            throw new IllegalArgumentException("Unable to resolve primitive class: " + target);
        }
        if (target.isSynthetic()) {
            throw new IllegalArgumentException("Unable to resolve synthetic class: " + target);
        }
        if (target.isArray()) {
            throw new IllegalArgumentException("Unable to use arrays for models: " + target);
        }
        com.wordnik.swagger.annotations.ApiModel apiModel = target.getAnnotation(com.wordnik.swagger.annotations.ApiModel.class);
        if (apiModel == null) {
            throw new IllegalArgumentException("Missing ApiModel annotation on: " + target);
        }

        model = new ApiModel().id(target.getSimpleName());
        models.put(target, model);

        Map<String, SchemaNode> properties = new HashMap<String, SchemaNode>();
        for (Class<?> cls = target; !Object.class.equals(cls); cls = cls.getSuperclass()) {
            processFields(properties, cls);
        }
        // TODO: sort based on field annotation @ApiModelProperty.position
        for (Map.Entry<String, SchemaNode> e : properties.entrySet()) {
            model.addProperty(e.getKey(), e.getValue());
            if (e.getValue().isRequired()) {
                model.addRequired(e.getKey());
            }
        }
        return model;
    }

    private void processFields(Map<String, SchemaNode> properties, Class<?> target) {
        for (Field field : target.getDeclaredFields()) {
            if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) == 0) {
                Class<?> fieldType = field.getType();
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                if (apiModelProperty != null) {
                    SchemaNode property = new SchemaNode().description(apiModelProperty.notes());
                    properties.put(field.getName(), property);
                    if (String.class.equals(fieldType)) {
                        property.type("string");
                    } else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
                        property.type("integer").format("int32");
                    } else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
                        property.type("integer").format("int64");
                    } else if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
                        property.type("boolean");
                    } else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
                        property.type("number").format("float");
                    } else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
                        property.type("number").format("double");
                    } else if (Byte.class.equals(fieldType) || Byte.TYPE.equals(fieldType)) {
                        property.type("string").format("byte");
                    } else if (Date.class.equals(fieldType)) {
                        property.type("string").format("date");
                    } else if (fieldType.isArray()) {
                        property.type("array");
                        ApiModel componentModel = resolveClass(fieldType.getComponentType());
                        // TODO: fixme, needs items submap
                        property.ref(componentModel.getId());
                    } else if (Set.class.isAssignableFrom(fieldType)) {
                        property.type("array").uniqueItems(true);
                    } else if (Collection.class.isAssignableFrom(fieldType)) {
                        ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
                        ApiModel componentModel = resolveClass((Class<?>)parameterizedType.getActualTypeArguments()[0]);
                        // TODO: fixme, needs items submap
                        property.ref(componentModel.getId());
                    } else {
                        ApiModel subModel = resolveClass(fieldType);
                        property.ref(subModel.getId());
                    }
                }
            }
        }
    }
}
