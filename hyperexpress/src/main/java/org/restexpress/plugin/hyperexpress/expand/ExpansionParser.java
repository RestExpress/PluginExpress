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
package org.restexpress.plugin.hyperexpress.expand;

import java.util.Arrays;

import org.restexpress.Request;
import org.restexpress.Response;

import com.strategicgains.hyperexpress.expand.Expansion;

/**
 * @author toddf
 * @since Aug 8, 2014
 */
public class ExpansionParser
{
	private static final String EXPAND_HEADER_NAME = "expand";
	private static final String EXPAND_SEPARATOR = ",\\s*";
//	private static final String EXPAND_SEGMENT_SEPARATOR = "\\.";

	public static Expansion parseFrom(Request request, Response response)
    {
		String expandString = request.getHeader(EXPAND_HEADER_NAME);

		if (expandString == null || expandString.trim().isEmpty())
		{
			return new Expansion(response.getSerializationSettings().getMediaType());
		}

		String[] expansions = expandString.split(EXPAND_SEPARATOR);
		return new Expansion(response.getSerializationSettings().getMediaType(), Arrays.asList(expansions));
    }
}
