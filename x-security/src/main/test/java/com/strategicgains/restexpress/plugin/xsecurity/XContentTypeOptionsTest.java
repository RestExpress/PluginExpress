package com.strategicgains.restexpress.plugin.xsecurity;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author cjm
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
        HttpRequest.addHeader("Host", "testing-host");
        Response response = new Response();
        postprocessor.process(new Request(httpRequest, null), response);
        assertTrue(response.hasHeaders());
        String result = response.getHeader(XCONTENTTYPEOPTIONS);
        assertNotNull(result);
        assertEquals(NOSNIFF, result);
    }
}
