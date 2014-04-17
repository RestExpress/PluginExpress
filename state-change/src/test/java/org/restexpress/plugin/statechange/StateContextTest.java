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
package org.restexpress.plugin.statechange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author toddf
 * @since Feb 18, 2014
 */
public class StateContextTest
{
	@Before
	public void setup()
	{
		StateContext.clear();
	}

	@After
	public void teardown()
	{
		StateContext.clear();
	}

	@Test
	public void shouldPut()
	{
		StateContext.put("key", "some value");
		assertEquals("some value", StateContext.get("key"));
		assertEquals(1, StateContext.getContext().size());
	}

	@Test
	public void shouldClearAfterLastRemove()
	{
		StateContext.remove("key");
		assertNull(StateContext.getContext());
	}
}
