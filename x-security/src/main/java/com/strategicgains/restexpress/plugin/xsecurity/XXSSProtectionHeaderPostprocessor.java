package com.strategicgains.restexpress.plugin.xsecurity;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author pbj23000
 * @since 8 July, 2014
 *
 * See <a href="http://blogs.msdn.com/b/ie/archive/2008/07/02/ie8-security-part-iv-the-xss-filter.aspx">The XSS Filter</a>
 */
public class XXSSProtectionHeaderPostprocessor
implements Postprocessor
{
    private static final String XXSSPROTECTION = "X-XSS-Protection";

    @Override
    public void process(Request request, Response response)
    {
        // 0
        // 1; mode=block
        response.addHeader(XXSSPROTECTION, "1; mode=block");
    }
}
