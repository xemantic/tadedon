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
public class Receiver {

	private final EventBus m_eventBus;

	private String m_receivedMessage;

	@Inject
	public Receiver(EventBus eventBus) {
		m_eventBus = eventBus;
		m_eventBus.addHandler(MessageEvent.TYPE, new MessageEventHandler() {
			@Override
			public void onMessage(MessageEvent event) {
				m_receivedMessage = event.getMessage();
			}
		});
	}

	/**
	 * @return the receivedMessage
	 */
	public String getReceivedMessage() {
		return m_receivedMessage;
	}

}
