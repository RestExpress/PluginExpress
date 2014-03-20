package com.strategicgains.restexpress.plugin.swagger;

import org.junit.Test;

import java.util.HashMap;

public class ModelResolverTest {
    private ModelResolver builder = new ModelResolver(new HashMap<String, ApiModel>());

    @Test
    public void testBuilder() {
        builder.resolve(DummyModel.class);
    }
}
