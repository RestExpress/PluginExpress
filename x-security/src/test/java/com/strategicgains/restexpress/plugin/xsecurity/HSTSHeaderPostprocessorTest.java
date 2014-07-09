
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.strategicgains.restexpress.plugin.xsecurity.HSTSHeaderPostprocessor;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author cjm
 * @since 8 July, 2014
 */

public class HSTSHeaderPostprocessorTest
{
    private static final String HSTS = "Strict-Transport-Security";
    private Postprocessor postprocessor = new HSTSHeaderPostprocessor();

    @Test
    public void shouldAddHSTSHeader()
    {
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/foo?param1=bar&param2=baz&quux");
        httpRequest.addHeader("Host", "testing-host");
        Response response = new Response();
        postprocessor.process(new Request(httpRequest, null), response);
        assertTrue(response.hasHeaders());
        String result = response.getHeader(HSTS);
        assertNotNull(result);
    }
}