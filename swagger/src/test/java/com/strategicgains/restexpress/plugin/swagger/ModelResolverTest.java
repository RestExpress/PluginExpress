/*
    Copyright 2014, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.plugin.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.strategicgains.restexpress.plugin.swagger.domain.DataType;

/**
 * @author russtrotter
 */
public class ModelResolverTest
{
	private ModelResolver builder = new ModelResolver();

	@Test
	public void shouldResolveDummyModel()
	{
		DataType n = builder.resolve(DummyModel.class);
		assertNotNull(n);
		assertEquals(DummyModel.class.getSimpleName(), n.getRef());
	}

	@Test
	public void shouldResolveDummyModelArray()
	{
		DataType n = builder.resolve(DummyModel[].class);
		assertNotNull(n);
		assertEquals("array", n.getType());
		assertEquals(DummyModel.class.getSimpleName(), n.getItems().getRef());
	}

//	@Test
//	public void shouldResolveDummyModelArrayList()
//	{
//		List<DummyModel> list = new ArrayList<DummyModel>();
//		DataType n = builder.resolve(list.getClass());
//		assertNotNull(n);
//		assertEquals("array", n.getType());
//		assertEquals(DummyModel.class.getSimpleName(), n.getItems().getRef());
//	}

	@Test
	public void shouldResolveAnother()
	{
		DataType n = builder.resolve(Another.class);
		assertNotNull(n);
		assertEquals(Another.class.getSimpleName(), n.getRef());
	}

	@Test
	public void shouldResolveAnotherArray()
	{
		DataType n = builder.resolve(Another[].class);
		assertNotNull(n);
		assertEquals("array", n.getType());
		assertEquals(Another.class.getSimpleName(), n.getItems().getRef());
	}

//	@Test
//	public void shouldResolveAnotherArrayList()
//	{
//		DataType n = builder.resolve(new ArrayList<Another>().getClass());
//		assertNotNull(n);
//		assertEquals("array", n.getType());
//		assertEquals(Another.class.getSimpleName(), n.getItems().getRef());
//	}

//	@Test
//	public void shouldResolveAnotherMap()
//	{
//		DataType n = builder.resolve(new HashMap<String, Another>().getClass());
//		assertNotNull(n);
//		assertEquals("array", n.getType());
//		assertEquals(Another.class.getSimpleName(), n.getItems().getRef());
//	}
}
