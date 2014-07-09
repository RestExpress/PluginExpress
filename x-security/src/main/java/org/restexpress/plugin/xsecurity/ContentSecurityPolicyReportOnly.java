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

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author pbj23000
 * @since 8 July, 2014
 *
 * See <a href="http://www.w3.org/TR/CSP/">Content Security Policy</a>
 */
public class ContentSecurityPolicyReportOnly
implements Postprocessor
{
    private static final String CONTENTSECURITYPOLICYREPORTONLY = "Content-Security-Policy-Report-Only";

    @Override
    public void process(Request request, Response response) {
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
        response.addHeader(CONTENTSECURITYPOLICYREPORTONLY, "default-src 'self'; report-uri http://loghost.example.com/reports.jsp");
    }
}
