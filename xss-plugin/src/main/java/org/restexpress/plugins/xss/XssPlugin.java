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
import java.util.Map;

import org.owasp.encoder.Encode;
import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
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
	private Map<String, Encoding> encodings = new HashMap<String, Encoding>();

	public XssPlugin()
	{
		super();
		encode(ContentType.JSON, Encoding.JSON);
		encode(ContentType.XML, Encoding.XML);
	}

	public XssPlugin encode(String mediaType, Encoding encoding)
	{
		String[] segments = mediaType.split("\\s*,\\s*");

		for (String segment : segments)
		{
			encodings.put(parseSegment(segment), encoding);
		}

		return this;
	}

	private String parseSegment(String segment)
	{
		return segment.split("\\s*;\\s*")[0];
	}

	@Override
	public XssPlugin register(RestExpress server)
	{
		if (isRegistered()) return this;

		super.register(server);
		server.addFinallyProcessor(new XssEncodingPostprocessor(encodings));
		return this;
	}

	public class XssEncodingPostprocessor
	implements Postprocessor
	{
		private Map<String, Encoding> encodings;

		public XssEncodingPostprocessor(Map<String, Encoding> encodings)
		{
			super();
			this.encodings = new HashMap<String, Encoding>(encodings);
		}

		@Override
		public void process(Request request, Response response)
		{
			if (!response.hasBody()) return;
			if (!String.class.isAssignableFrom(response.getBody().getClass())) return;

			String contentType = parseSegment(response.getContentType());
			Encoding encoding = encodings.get(contentType);

			if (encoding == null) return;

			switch(encoding)
			{
				case JSON:
					response.setBody(Encode.forHtmlContent((String) response.getBody()));
				break;
				case XML:
					response.setBody(Encode.forJavaScript((String) response.getBody()));
				break;
				case HTML:
					response.setBody(Encode.forHtml((String) response.getBody()));
				break;
				case CSS:
					response.setBody(Encode.forCssString((String) response.getBody()));
				break;
				default:
			}
		}
	}
}
