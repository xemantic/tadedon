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

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

/**
 * Unit test for {@link UriReturningServlet}.
 * <p>
 * Created on Jul 3, 2010
 *
 * @author hshsce
 */
public class UriReturningServletTest {

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
		UriReturningServlet servlet = injector.getInstance(UriReturningServlet.class);
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/UriReturningServlet");
		MockHttpServletResponse response = new MockHttpServletResponse();

		// when
		servlet.doGet(request, response);

		// then
		Assert.assertThat(response.getContentAsString(), is("/UriReturningServlet"));
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
