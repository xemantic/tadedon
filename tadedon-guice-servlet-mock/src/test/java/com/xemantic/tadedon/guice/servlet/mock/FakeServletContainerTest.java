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

import javax.servlet.ServletException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

/**
 * Unit test for {@link FakeServletContainer}.
 * <p>
 * Created on Aug 22, 2010
 *
 * @author hshsce
 *
 */
public class FakeServletContainerTest {

    private static Injector injector;

    private static FakeServletContainer servletContainer;

    /**
     * Sets up fake servlet container.
     *
     * @throws ServletException if the servlet container cannot be started.
     */
    @BeforeClass
    public static void startServletContainer() throws ServletException {
        injector = Guice.createInjector(
                new FakeServletContainerModule(),
                new ServletModule() {
                    @Override
                    protected void configureServlets() {
                        filter("/*").through(FooHeaderAddingFilter.class);
                        serve("/UriReturningServlet").with(UriReturningServlet.class);
                        serve("/SessionIdReturningServlet").with(SessionIdReturningServlet.class);
                    }
                });

        servletContainer = injector.getInstance(FakeServletContainer.class);
        servletContainer.start();
    }

    /**
     * Tests {@link UriReturningServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     *
     * @throws IOException if an input or output error is detected during request handling
     * @throws ServletException if container cannot handle request. 
     */
    @Test
    public void shouldReturnUriAndAddFooHeader() throws IOException, ServletException {
        // given
        MockHttpServletRequest request = servletContainer.newRequest("GET", "/UriReturningServlet");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        servletContainer.service(request, response);

        // then
        assertThat(response.getContentAsString(), is("/UriReturningServlet"));
        assertThat((String) response.getHeader("Foo"), is("foo"));
    }

    /**
     * Tests {@link SessionIdReturningServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     *
     * @throws IOException if an input or output error is detected during request handling
     * @throws ServletException if container cannot handle request. 
     */
    @Test
    public void shouldReturnSessionIdAndAddFooHeader() throws IOException, ServletException {
        // given
        servletContainer.newSession("42"); // magic number
        MockHttpServletRequest request = servletContainer.newRequest("GET", "/SessionIdReturningServlet");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        servletContainer.service(request, response);

        // then
        assertThat(response.getContentAsString(), is("42"));
        assertThat((String) response.getHeader("Foo"), is("foo"));
    }

    /**
     * Tests {@link SessionIdReturningServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     *
     * @throws IOException if an input or output error is detected during request handling
     * @throws ServletException if container cannot handle request. 
     */
    @Test
    public void shouldPreserveSessionAcrossRequest() throws IOException, ServletException {
        // given
        servletContainer.newSession("42"); // magic number
        MockHttpServletRequest request1 = servletContainer.newRequest("GET", "/SessionIdReturningServlet");
        MockHttpServletResponse response1 = new MockHttpServletResponse();
        MockHttpServletRequest request2 = servletContainer.newRequest("GET", "/SessionIdReturningServlet");
        MockHttpServletResponse response2 = new MockHttpServletResponse();

        // when
        servletContainer.service(request1, response1);
        servletContainer.service(request2, response2);

        // then
        assertThat(response1.getContentAsString(), is("42"));
        assertThat((String) response1.getHeader("Foo"), is("foo"));
        assertThat(response2.getContentAsString(), is("42"));
        assertThat((String) response2.getHeader("Foo"), is("foo"));
    }

    /**
     * Tears down fake servlet container.
     */
    @AfterClass
    public static void stopServletContainer() {
        if (servletContainer != null) {
            servletContainer.stop();
        }
    }

}
