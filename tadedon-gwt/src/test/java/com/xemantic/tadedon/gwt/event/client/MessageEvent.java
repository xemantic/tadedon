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

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * <p>
 * Created on Aug 12, 2010
 *
 * @author hshsce
 *
 */
public class MessageEvent extends GwtEvent<MessageEventHandler> {

	public static final Type<MessageEventHandler> TYPE = new Type<MessageEventHandler>();

	private final String m_message;


	public MessageEvent(String message) {
		m_message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return m_message;
	}

	/** {@inheritDoc} */
	@Override
	public Type<MessageEventHandler> getAssociatedType() {
		return TYPE;
	}

	/** {@inheritDoc} */
	@Override
	protected void dispatch(MessageEventHandler handler) {
		handler.onMessage(this);
	}

}
