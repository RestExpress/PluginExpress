package com.strategicgains.restexpress.plugin.xsecurity;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author cjm
 * @since 8 July, 2014
 *
 * See <a href="http://tools.ietf.org/html/rfc6797">HTTP Strict Transport Security</a>
 */
public class HSTSHeaderPostprocessor
implements Postprocessor
{
    private static final String HSTS = "Strict-Transport-Security";

    @Override
    public void process(Request request, Response response)
    {
        // use 6 months
        response.addHeader(HSTS, "max-age=15768000; includeSubDomains");
    }
}
