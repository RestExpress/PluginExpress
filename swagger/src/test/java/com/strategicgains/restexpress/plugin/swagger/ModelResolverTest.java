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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import com.strategicgains.restexpress.plugin.swagger.domain.TypeNode;

/**
 * @author russtrotter
 */
public class ModelResolverTest
{
	private ModelResolver builder = new ModelResolver();

	@Test
	public void shouldResolveDummyModel()
	{
		TypeNode n = builder.resolve(DummyModel.class);
		assertNotNull(n);
	}

	@Test
	public void shouldResolveDummyModelArray()
	{
		TypeNode n = builder.resolve(DummyModel[].class);
		assertNotNull(n);
	}

	@Test
	public void shouldResolveDummyModelList()
	{
		TypeNode n = builder.resolve(new ArrayList<DummyModel>().getClass());
		assertNotNull(n);
	}

	@Test
	public void shouldResolveAnother()
	{
		TypeNode n = builder.resolve(Another.class);
		assertNotNull(n);
	}

	@Test
	public void shouldResolveAnotherArray()
	{
		TypeNode n = builder.resolve(Another[].class);
		assertNotNull(n);
	}

	@Test
	public void shouldResolveAnotherCollection()
	{
		TypeNode n = builder.resolve(new ArrayList<Another>().getClass());
		assertNotNull(n);
	}
}
