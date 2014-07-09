package com.strategicgains.restexpress.plugin.xsecurity;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author pbj23000
 * @since 8 July, 2014
 *
 * See <a href="http://tools.ietf.org/html/draft-ietf-websec-frame-options-00">Frame Options</a>
 */
public class FrameOptionsHeaderPostprocessor
implements Postprocessor
{
    private static final String FRAMEOPTIONS = "Frame-Options";

    @Override
    public void process(Request request, Response response) {
        // deny
        // sameorigin
        // allow-from DOMAIN
        response.addHeader(FRAMEOPTIONS, "deny");
    }
}
