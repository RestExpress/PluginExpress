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

import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractPlugin;

/**
 * @author pbj23000
 * @since July 6, 2014
 */
public class XSecurityPlugin
extends AbstractPlugin
{
    @Override
    public XSecurityPlugin register(RestExpress server)
    {
        if (isRegistered()) return this;

        super.register(server);

        server
                .addPostprocessor(new HSTSHeaderPostprocessor())
                //.addPostprocessor(new XFrameOptionsHeaderPostprocessor())
                //.addPostprocessor(new FrameOptionsHeaderPostprocessor())
                //.addPostprocessor(new XXSSProtectionHeaderPostprocessor())
                .addPostprocessor(new XContentTypeOptionsHeaderPostprocessor())
                //.addPostprocessor(new ContentSecurityPolicyHeaderPostprocessor())
                //.addPostprocessor(new XContentSecurityPolicyHeaderPostprocessor())
                //.addPostprocessor(new XWebKitCSPHeaderPostprocessor())
                //.addPostprocessor(new ContentSecurityPolicyReportOnlyHeaderPostprocessor())
        ;

        return this;
    }
}
