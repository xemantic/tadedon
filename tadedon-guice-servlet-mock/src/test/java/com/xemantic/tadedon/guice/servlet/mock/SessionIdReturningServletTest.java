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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

/**
 * Unit test for {@link SessionIdReturningServlet}.
 * <p>
 * Created on Jul 4, 2010
 *
 * @author hshsce
 */
public class SessionIdReturningServletTest {

	private static Injector injector;

	private static FakeServletContainer servletContainer;

	/**
	 * Test set up.
	 *
	 * @throws ServletException is the servlet container cannot be started.
	 */
	@BeforeClass
	public static void startServletContainer() throws ServletException {
		injector = Guice.createInjector(
				new ServletModule(),
				new FakeServletContainerModule(),
				new ServletInterceptionModule());

		servletContainer = injector.getInstance(FakeServletContainer.class);
		servletContainer.start();
	}

	/**
	 * Tests {@link UriReturningServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 *
	 * @throws IOException if an input or output error is detected when the servlet handles the GET request
	 */
	@Test
	public void shouldReturnUri() throws IOException {
		// given
		SessionIdReturningServlet servlet = injector.getInstance(SessionIdReturningServlet.class);
		ServletContext context = servletContainer.getServletContext();
		MockHttpServletRequest request = new MockHttpServletRequest(context, "GET", "/SessionIdReturningServlet");
		MockHttpSession session = new MockHttpSession(context, "42"); // magic number
		request.setSession(session);
		MockHttpServletResponse response = new MockHttpServletResponse();

		// when
		servlet.doGet(request, response);

		// then
		assertThat(response.getContentAsString(), is("42"));
	}

	/**
	 * Test tear down.
	 */
	@AfterClass
	public static void stopServletContainer() {
		if (servletContainer != null) {
			servletContainer.stop();
		}
	}

}
