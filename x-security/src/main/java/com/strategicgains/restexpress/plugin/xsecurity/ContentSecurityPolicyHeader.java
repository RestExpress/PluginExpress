package com.strategicgains.restexpress.plugin.xsecurity;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author pbj23000
 * @since 8 July, 2014
 *
 * See <a href="http://www.w3.org/TR/CSP/">Content Security Policy</a>
 */
public class ContentSecurityPolicyHeader
implements Postprocessor
{
    private static final String CONTENTSECURITYPOLICY = "Content-Security-Policy";

    @Override
    public void process(Request request, Response response)
    {
        // default-src
        // script-src
        // object-src
        // style-src
        // img-src
        // media-src
        // frame-src
        // font-src
        // connect-src
        // sandbox (optional)
        // report-uri
        response.addHeader(CONTENTSECURITYPOLICY, "default-src \'self\'");
    }
}
