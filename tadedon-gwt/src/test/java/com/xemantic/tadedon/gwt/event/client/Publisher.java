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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * 
 * <p>
 * Created on Aug 12, 2010
 *
 * @author hshsce
 *
 */
@Singleton
public class Publisher {

	private final EventBus m_eventBus;


	@Inject
	public Publisher(EventBus eventBus) {
		m_eventBus = eventBus;
	}

	public void publish(String message) {
		m_eventBus.fireEvent(new MessageEvent(message));
	}

}
