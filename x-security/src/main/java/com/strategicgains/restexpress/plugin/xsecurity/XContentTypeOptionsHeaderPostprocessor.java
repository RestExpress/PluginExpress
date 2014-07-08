/*
    Copyright 2011, Strategic Gains, Inc.

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
package com.strategicgains.restexpress.plugin.xsecurity;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author cjm
 * @since 8 July, 2014
 */
public class XContentTypeOptionsHeaderPostprocessor
implements Postprocessor
{
    private static final String XCONTENTTYPEOPTIONS = "X-Content-Type-Options";
    private static final String NOSNIFF = "nosniff";

    @Override
    public void process(Request request, Response response)
    {
        response.addHeader(XCONTENTTYPEOPTIONS, NOSNIFF);
    }
}
