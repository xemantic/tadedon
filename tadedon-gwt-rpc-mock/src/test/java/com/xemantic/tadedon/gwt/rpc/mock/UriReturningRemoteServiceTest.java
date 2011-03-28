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

import javax.servlet.ServletException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.xemantic.tadedon.guice.servlet.mock.FakeServletContainer;
import com.xemantic.tadedon.guice.servlet.mock.FakeServletContainerModule;
import com.xemantic.tadedon.logging.Loggers;

/**
 * Unit test for {@link UriReturningRemoteService}.
 * <p>
 * Created on Aug 22, 2010
 *
 * @author hshsce
 */
public class UriReturningRemoteServiceTest {

    private static Injector injector;

    private static FakeServletContainer servletContainer;

    /**
     * Test set up.
     * 
     * @throws ServletException if the servlet container cannot be started.
     */
    @BeforeClass
    public static void startServletContainer() throws ServletException {
        Loggers.redirectJulToSLF4J();
        injector = Guice.createInjector(
                new ServletModule(),
                new FakeServletContainerModule(),
                new RemoteServiceInterceptionModule());

        servletContainer = injector.getInstance(FakeServletContainer.class);
        servletContainer.start();
    }

    /**
     * Tests {@link UriReturningRemoteService#getUri()}.
     */
    @Test
    public void shouldReturnUri() {
        // given
        UriReturningRemoteService service = injector.getInstance(UriReturningRemoteService.class);

        // when
        String uri = service.getUri();

        // then
        assertThat(uri, is("/gwt.rpc"));
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
