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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.GuiceFilter;

/**
 * Intercepts method invocation on service which expects
 * session or request scope to be available.
 * <p>
 * Created on Jul 3, 2010
 *
 * @author hshsce
 */
public class HttpRequestedMethodInterceptor implements MethodInterceptor {

    @Inject
    private GuiceFilter m_guiceFilter;

    @Inject
    private FakeServletContainer m_servletContainer;


    /** {@inheritDoc} */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MethodInvokingFilterChain chain = new MethodInvokingFilterChain(invocation);
        MockHttpServletRequest request = m_servletContainer.newRequest("GET", "/foo");
        m_guiceFilter.doFilter(request, new MockHttpServletResponse(), chain);
        Throwable throwable = chain.getThrowable();
        if (throwable != null) {
            throw throwable;
        }
        return chain.getResult();
    }

}
