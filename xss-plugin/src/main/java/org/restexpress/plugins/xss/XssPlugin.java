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
package org.restexpress.plugins.xss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.IOP.Encoding;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.contenttype.MediaRange;
import org.restexpress.contenttype.MediaTypeParser;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.plugin.AbstractPlugin;

/**
 * Cross-site scripting (XSS) prevention outbound encoding plugin.
 * 
 * @author toddf
 * @since Mar 31, 2014
 */
public class XssPlugin
extends AbstractPlugin
{
	private Map<MediaRange, Encoding> encodings = new HashMap<MediaRange, Encoding>();

	public XssPlugin()
	{
		super();
	}

	public XssPlugin encode(String contentType, Encoding encoding)
	{
		List<MediaRange> mediaRanges = MediaTypeParser.parse(contentType);

		for (MediaRange mediaRange : mediaRanges)
		{
			if (!encodings.containsKey(mediaRange))
			{
				encodings.put(mediaRange, encoding);
			}
		}

		return this;
	}

	@Override
	public void bind(RestExpress server)
	{
		super.bind(server);
		server.addFinallyProcessor(new XssEncodingPostprocessor(encodings));
	}

	public class XssEncodingPostprocessor
	implements Postprocessor
	{
		private Map<MediaRange, Encoding> encodings;

		public XssEncodingPostprocessor(Map<MediaRange, Encoding> encodings)
		{
			super();
			this.encodings = new HashMap<MediaRange, Encoding>(encodings);
		}

		@Override
		public void process(Request request, Response response)
		{
			response.getContentType()
		}
	}
}
