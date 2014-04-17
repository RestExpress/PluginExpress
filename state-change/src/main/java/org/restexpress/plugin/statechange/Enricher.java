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
package org.restexpress.plugin.statechange;

import org.restexpress.Request;

/**
 * An interface that defines how additional items may be added to the StateContext during the pre-processor
 * life-cycle. An enricher can pull data from the Request and put it in the StateContext as desired for
 * later, downstream layers to leverage per-request data. 
 * 
 * @author toddf
 * @since Feb 18, 2014
 */
public interface Enricher
{
	public void enrich(Request request);
}
