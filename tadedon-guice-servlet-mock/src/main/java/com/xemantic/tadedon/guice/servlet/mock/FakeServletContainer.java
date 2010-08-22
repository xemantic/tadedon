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
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
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
 *     private static FakeServletContainer servletContainer;
 * 
 *     {@literal @BeforeClass}
 *     public static void startServletContainer() throws ServletException {
 *         Injector injector = Guice.createInjector(
 *                 new FakeServletContainerModule(),
 *                 new ServletModule() {
 *                     {@literal @Override}
 *                     protected void configureServlets() {
 *                         serve("/MyServlet").with(MyServlet.class);
 *                     }
 *                 });
 *         servletContainer = injector.getInstance(FakeServletContainer.class);
 *         servletContainer.start();
 *     }
 * 
 *     {@literal @Test}
 *     public void shouldInvokeMyServlet() throws IOException, ServletException {
 *         // given
 *         MockHttpServletRequest request = servletContainer.newRequest("GET", "/MyServlet");
 *         MockHttpServletResponse response = new MockHttpServletResponse();
 * 
 *         // when
 *         servletContainer.service(request, response);
 * 
 *         // then
 *         // list of expectation ...
 *         // e.g. assertThat(response.getContentAsString(), is("My response"));
 *     }
 * 
 *     {@literal @AfterClass}
 *     public static void stopServletContainer() {
 *         if (servletContainer != null) {
 *             servletContainer.stop();
 *         }
 *     }
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

	private final AtomicLong m_sessionIdProvider = new AtomicLong(0);

	private ServletContext m_context;

	private HttpSession m_session;


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
	 * @param context the servlet context.
	 * @throws ServletException if {@link GuiceFilter} cannot be initialized.
	 * @see #start()
	 */
	public void start(ServletContext context) throws ServletException {
		m_session = new MockHttpSession();
		m_context = context;
		MockFilterConfig config = new MockFilterConfig(m_context);
		m_filter.init(config);
		m_listener.contextInitialized(new ServletContextEvent(m_context));
	}

	/**
	 * Stops the container.
	 */
	public void stop() {
		m_filter.destroy();
		m_listener.contextDestroyed(new ServletContextEvent(m_context));
	}

	public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException {
	    if (request instanceof MockHttpServletRequest) {
	        MockHttpServletRequest mockRequest = (MockHttpServletRequest) request;
	        mockRequest.setServletPath(mockRequest.getRequestURI());
	    }
	    m_filter.doFilter(request, response, new MockFilterChain());
	}

	/**
	 * Returns container's servlet context.
	 *
	 * @return the servlet context
	 */
	public ServletContext getContext() {
		return m_context;
	}

	public MockHttpSession newSession() {
	    MockHttpSession session = new MockHttpSession(m_context, String.valueOf(m_sessionIdProvider.getAndIncrement()));
	    m_session = session;
	    return session;
	}

    public MockHttpSession newSession(String sessionId) {
        MockHttpSession session = new MockHttpSession(m_context, sessionId);
        m_session = session;
        return session;
    }
	
	public void setSession(HttpSession session) {
		m_session = session;
	}

	public HttpSession getSession() {
		return m_session;
	}

	public MockHttpServletRequest newRequest() {
	    MockHttpServletRequest request = new MockHttpServletRequest(m_context);
        request.setSession(m_session);
	    return request;
	}

    public MockHttpServletRequest newRequest(String method, String requestUri) {
        MockHttpServletRequest request = new MockHttpServletRequest(m_context, method, requestUri);
        request.setSession(m_session);
        return request;
    }
	
}
