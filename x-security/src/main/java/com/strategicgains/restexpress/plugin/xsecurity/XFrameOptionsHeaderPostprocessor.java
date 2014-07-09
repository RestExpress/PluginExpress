package com.strategicgains.restexpress.plugin.xsecurity;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author pbj23000
 * @since 8 July, 2014
 *
 * See <a href="http://tools.ietf.org/html/draft-ietf-websec-x-frame-options-01">X-Frame-Options</a>
 */
public class XFrameOptionsHeaderPostprocessor
implements Postprocessor
{
    private static final String XFRAMEOPTIONS = "X-Frame-Options";

    @Override
    public void process(Request request, Response response) {
        // deny
        // sameorigin
        // allow-from DOMAIN
        response.addHeader(XFRAMEOPTIONS, "deny");
    }
}
