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

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Servlet returning {@link HttpSession} id as {@code text/plain}.
 * For unit testing purposes.
 * <p>
 * Created on Jul 4, 2010
 *
 * @author hshsce
 */
@Singleton
public class SessionIdReturningServlet extends HttpServlet {

	private static final long serialVersionUID = 3068583235461507403L;

	private final SessionIdExtractor m_sessionIdExtractor;

	/**
	 * Creates servlet instance with injected dependencies.
	 *
	 * @param sessionIdExtractor the session id extractor.
	 */
	@Inject
	public SessionIdReturningServlet(SessionIdExtractor sessionIdExtractor) {
		m_sessionIdExtractor = sessionIdExtractor;
	}

	/** {@inheritDoc} */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().write(m_sessionIdExtractor.getSessionId());
	}

}
