/*
 * Copyright 2010 Xemantic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xemantic.tadedon.gwt.event.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Unit test for {@link EventBusGinModule}.
 * <p>
 * Created on Aug 12, 2010
 *
 * @author hshsce
 */
public class EventBusGinModuleGwtTest extends GWTTestCase {

	/** {@inheritDoc} */
	@Override
	public String getModuleName() {
		return "com.xemantic.tadedon.gwt.event.Event";
	}

	public void testShouldPassEventsBetweenPublisherAndReceiver() {
		// give
		EventBusGinModuleGinjector injector = GWT.create(EventBusGinModuleGinjector.class);
		Publisher publisher = injector.getPublisher();
		Receiver receiver = injector.getReceiver();

		// when
		publisher.publish("foo");

		// then
		assertEquals("foo", receiver.getReceivedMessage());
	}

}
