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
package com.xemantic.tadedon.guice.servlet.mock;

import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Extractor of {@link HttpSession} id.
 * Used in {@link SessionIdReturningServletTest}.
 * <p>
 * Created on Jul 4, 2010
 *
 * @author hshsce
 */
@Singleton
public class SessionIdExtractor {

	private final Provider<HttpSession> m_sessionProvider;

	/**
	 * Creates extractor instance.
	 *
	 * @param sessionProvider the provider of {@link HttpSession} instance.
	 */
	@Inject
	public SessionIdExtractor(Provider<HttpSession> sessionProvider) {
		m_sessionProvider = sessionProvider;
	}

	/**
	 * Returns id of {@link HttpSession}.
	 *
	 * @return the session id.
	 */
	public String getSessionId() {
		return m_sessionProvider.get().getId();
	}

}
