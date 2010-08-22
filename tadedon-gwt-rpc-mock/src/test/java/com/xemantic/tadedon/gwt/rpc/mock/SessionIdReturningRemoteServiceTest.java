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
package com.xemantic.tadedon.gwt.rpc.mock;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.xemantic.tadedon.guice.servlet.mock.FakeServletContainer;
import com.xemantic.tadedon.guice.servlet.mock.FakeServletContainerModule;

/**
 * Unit test for {@link SessionIdReturningRemoteService}.
 * <p>
 * Created on Aug 22, 2010
 *
 * @author hshsce
 */
public class SessionIdReturningRemoteServiceTest {

    private static Injector injector;

    private static FakeServletContainer servletContainer;


    /**
     * Sets up tests.
     *
     * @throws ServletException if the servlet container cannot be started.
     */
    @BeforeClass
    public static void startServletContainer() throws ServletException {
        injector = Guice.createInjector(
                new ServletModule(),
                new FakeServletContainerModule(),
                new RemoteServiceInterceptionModule());

        servletContainer = injector.getInstance(FakeServletContainer.class);
        servletContainer.start();
    }

    /**
     * Tests {@link SessionIdReturningRemoteService#getSessionId())}.
     */
    @Test
    public void shouldReturnSessionId() {
        // given
        servletContainer.newSession("42"); // magic number
        SessionIdReturningRemoteService service = injector.getInstance(SessionIdReturningRemoteService.class);

        // when
        String sessionId = service.getSessionId();

        // then
        assertThat(sessionId, is("42"));
    }

    /**
     * Tests {@link SessionIdReturningRemoteService#getSessionId())}.
     */
    @Test
    public void shouldPreserveSessionAcrossRequests() throws IOException {
        // given
        servletContainer.newSession("42"); // magic number
        SessionIdReturningRemoteService service = injector.getInstance(SessionIdReturningRemoteService.class);

        // when
        String sessionId1 = service.getSessionId();
        String sessionId2 = service.getSessionId();

        // then
        assertThat(sessionId1, is("42"));
        assertThat(sessionId2, is("42"));
    }

    /**
     * Tears down tests.
     */
    @AfterClass
    public static void stopServletContainer() {
        if (servletContainer != null) {
            servletContainer.stop();
        }
    }

}
