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
package com.strategicgains.restexpress.plugin.openapi;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author toddf
 * @since Apr 1, 2014
 */
public class Another
{
	private String aString;
	private int anInt;
	private Integer[] intArray;
	private Map<String, String> mapStringString;
	private Map<Object, Object> mapObjectObject;
	private List<DummyModel> listDummyModel;
	Collection<Another> anothers;
}
