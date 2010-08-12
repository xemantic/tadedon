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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockServletContext;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Servlet container representation which does not
 * involve any HTTP stack.
 * <p>
 * Note: this class is configured using {@link FakeServletContainerModule}.
 * <p>
 * This class keeps single instance of {@link GuiceFilter}
 * and {@link GuiceServletContextListener}. This utility is
 * intended as unit testing support for the code based on
 * <a href="http://code.google.com/p/google-guice/wiki/ServletModule">guice-servlet</a>
 * library.
 * <p>
 * Typical usage in test class (junit):
 * <pre>
 * public class MyServletTest {
 *
 *     private static Injector injector;
 *
 *     private static FakeServletContainer servletContainer;
 *
 *     {@literal @BeforeClass}
 *     public static void startServletContainer() throws ServletException {
 *         injector = Guice.createInjector(
 *              new ServletModule(),
 *              new FakeServletContainerModule(),
 *              new ServletInterceptionModule());
 *
 *         servletContainer = injector.getInstance(FakeServletContainer.class);
 *         servletContainer.start(); // you can provide ServletContext here
 *     }
 *
 *     {@literal @Test}
 *     public void shouldDoSomethingOnMyServlet() throws IOException {
 *         // given
 *         MyServlet servlet = injector.getInstance(MyServlet.class);
 *
 *         ...
 *     }
 *
 *     {@literal @AfterClass}
 *     public static void stopServletContainer() {
 *         if (servletContainer != null) {
 *             servletContainer.stop();
 *         }
 *     }
 *
 * }
 * </pre>
 * Created on Jul 2, 2010
 *
 * @author hshsce
 * @see FakeServletContainerModule
 * @see ServletInterceptionModule
 */
@Singleton
public class FakeServletContainer {

	private final GuiceFilter m_filter;

	private final GuiceServletContextListener m_listener;

	private ServletContext m_servletContext;

	/**
	 * Creates servlet container instance.
	 *
	 * @param filter the guice filter.
	 * @param listener the listener instance.
	 */
	@Inject
	public FakeServletContainer(GuiceFilter filter, GuiceServletContextListener listener) {
		m_filter = filter;
		m_listener = listener;
	}

	/**
	 * Starts the container with {@link MockServletContext}.
	 *
	 * @throws ServletException if {@link GuiceFilter} cannot be initialized.
	 * @see #start(ServletContext)
	 */
	public void start() throws ServletException {
		start(new MockServletContext());
	}

	/**
	 * Starts the container with using given {@code servletContext}.
	 *
	 * @param servletContext the servlet context.
	 * @throws ServletException if {@link GuiceFilter} cannot be initialized.
	 * @see #start()
	 */
	public void start(ServletContext servletContext) throws ServletException {
		m_servletContext = servletContext;
		MockFilterConfig config = new MockFilterConfig(m_servletContext);
		m_filter.init(config);
		m_listener.contextInitialized(new ServletContextEvent(m_servletContext));
	}

	/**
	 * Stops the container.
	 */
	public void stop() {
		m_filter.destroy();
		m_listener.contextDestroyed(new ServletContextEvent(m_servletContext));
	}

	/**
	 * Returns container's servlet context.
	 *
	 * @return the servlet context
	 */
	public ServletContext getServletContext() {
		return m_servletContext;
	}

}
