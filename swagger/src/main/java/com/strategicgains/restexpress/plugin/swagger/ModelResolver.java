package com.strategicgains.restexpress.plugin.swagger;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// jackson 2.1.4 isn't generating $ref
//
public class ModelResolver {
    private Map<String, ApiModel> models;

    public ModelResolver(Map<String, ApiModel> models) {
        this.models = models;
    }

    public SchemaNode resolve(Class<?> cls) {
        return createNode(cls);
    }

    private ApiModel resolveClass(Class<?> target) {
        String id = target.getSimpleName();
        ApiModel model = models.get(id);
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

        model = new ApiModel().id(id);
        models.put(id, model);

        Map<String, SchemaNode> properties = new HashMap<String, SchemaNode>();
        for (Class<?> cls = target; !Object.class.equals(cls); cls = cls.getSuperclass()) {
            processFields(properties, cls);
        }
        List<SchemaNode> sorted = new ArrayList<SchemaNode>(properties.values());
        Collections.sort(sorted, new Comparator<SchemaNode>() {
            @Override
            public int compare(SchemaNode o1, SchemaNode o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });

        for (SchemaNode property : sorted) {
            model.addProperty(property);
            if (property.isRequired()) {
                model.addRequired(property.getProperty());
            }
        }
        return model;
    }

    private SchemaNode createNode(Type target) {
        SchemaNode node = new SchemaNode();
        if (target instanceof Class) {
            Class targetClass = (Class) target;
            if (String.class.equals(target)) {
                node.type("string").primitive(true);
            } else if (Integer.class.equals(target) || Integer.TYPE.equals(target)) {
                node.type("integer").format("int32").primitive(true);
            } else if (Long.class.equals(target) || Long.TYPE.equals(target)) {
                node.type("integer").format("int64").primitive(true);
            } else if (Boolean.class.equals(target) || Boolean.TYPE.equals(target)) {
                node.type("boolean").primitive(true);
            } else if (Float.class.equals(target) || Float.TYPE.equals(target)) {
                node.type("number").format("float").primitive(true);
            } else if (Double.class.equals(target) || Double.TYPE.equals(target)) {
                node.type("number").format("double").primitive(true);
            } else if (Byte.class.equals(target) || Byte.TYPE.equals(target)) {
                node.type("string").format("byte").primitive(true);
            } else if (Date.class.equals(target)) {
                node.type("string").format("date").primitive(true);
            } else if (targetClass.isArray()) {
                node.type("array");
                SchemaNode componentModel = createNode(targetClass.getComponentType());
                node.items(componentModel);
            } else if (targetClass.isEnum()) {
                node.type("string").primitive(true);
                for (Object obj : targetClass.getEnumConstants()) {
                    node.addEnum(obj.toString());
                }
            } else if (Void.class.equals(target) || Void.TYPE.equals(target)) {
                node.type("void").primitive(true);
            } else {
                ApiModel subModel = resolveClass(targetClass);
                node.ref(subModel.getId());
            }
        } else if (target instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) target;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            if (Collection.class.isAssignableFrom(rawType)) {
                node.type("array");
                if (Set.class.isAssignableFrom(rawType)) {
                    node.uniqueItems(true);
                }
                SchemaNode componentModel = createNode(parameterizedType.getActualTypeArguments()[0]);
                node.items(componentModel);
            } else {
                throw new IllegalArgumentException("Unhandled generic type: " + target);
            }
        } else {
            throw new UnsupportedOperationException("Unhandled type: " + target);
        }
        return node;
    }

    private void processFields(Map<String, SchemaNode> properties, Class<?> target) {
        for (Field field : target.getDeclaredFields()) {
            if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) == 0) {
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                if (apiModelProperty != null && !properties.containsKey(field.getName())) {
                    SchemaNode property = createNode(field.getGenericType())
                            .description(apiModelProperty.notes())
                            .required(apiModelProperty.required())
                            .position(apiModelProperty.position())
                            .property(field.getName());
                    properties.put(field.getName(), property);
                }
            }
        }
    }
}
