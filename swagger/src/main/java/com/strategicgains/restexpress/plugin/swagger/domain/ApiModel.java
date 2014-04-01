/*
    Copyright 2013, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.restexpress.plugin.swagger.domain;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author toddf
 * @since Nov 21, 2013
 */
public class ApiModel
{
    private String id;
    private Set<String> required;
    private Map<String, TypeNode> properties;
    private String description;

    public String getId() {
        return id;
    }

    public ApiModel id(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ApiModel description(String description) {
        this.description = description;
        return this;
    }

    public void addRequired(String property) {
        if (required == null) {
            required = new HashSet<String>();
        }
        required.add(property);
    }

    public void addProperty(TypeNode property) {
        if (properties == null) {
            // use a linked hash map so order of insertion is preserved
            properties = new LinkedHashMap<String, TypeNode>();
        }
        if (!properties.containsKey(property.getProperty())) {
            properties.put(property.getProperty(), property);
        }
    }
}
