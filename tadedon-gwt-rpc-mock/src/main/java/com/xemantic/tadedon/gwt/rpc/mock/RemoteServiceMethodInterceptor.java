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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.inject.Inject;
import com.google.inject.servlet.GuiceFilter;
import com.xemantic.tadedon.guice.servlet.mock.FakeServletContainer;
import com.xemantic.tadedon.guice.servlet.mock.MethodInvokingFilterChain;

/**
 * Wraps any invocation of {@link RemoteService}'s  method in
 * {@link GuiceFilter}'s
 * {@link GuiceFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 * invocation. 
 * <p>
 * Created on Aug 21, 2010
 *
 * @author hshsce
 */
public class RemoteServiceMethodInterceptor implements MethodInterceptor {

	@Inject
	private GuiceFilter m_guiceFilter;

    @Inject
    private FakeServletContainer m_servletContainer;


	/** {@inheritDoc} */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		MethodInvokingFilterChain chain = new MethodInvokingFilterChain(invocation);
		MockHttpServletRequest request = m_servletContainer.newRequest("POST", "/gwt.rpc");
		m_guiceFilter.doFilter(request, new MockHttpServletResponse(), chain);
		Throwable throwable = chain.getThrowable();
		if (throwable != null) {
			throw throwable;
		}
		return chain.getResult();
	}

}
