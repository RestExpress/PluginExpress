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
package org.restexpress.plugin.xsecurity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.plugin.xsecurity.XContentTypeOptionsHeaderPostprocessor;

/**
 * @author pbj23000
 * @since 8 July, 2014
 */

public class XContentTypeOptionsPostprocessorTest
{
    private static final String XCONTENTTYPEOPTIONS = "X-Content-Type-Options";
    private static final String NOSNIFF = "nosniff";
    private Postprocessor postprocessor = new XContentTypeOptionsHeaderPostprocessor();

    @Test
    public void shouldAddXContentTypeOptionsHeader()
    {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo?param1=bar&param2=baz&quux");
        httpRequest.addHeader("Host", "testing-host");
        Response response = new Response();
        postprocessor.process(new Request(httpRequest, null), response);
        assertTrue(response.hasHeaders());
        String result = response.getHeader(XCONTENTTYPEOPTIONS);
        assertNotNull(result);
        assertEquals(NOSNIFF, result);
    }
}
