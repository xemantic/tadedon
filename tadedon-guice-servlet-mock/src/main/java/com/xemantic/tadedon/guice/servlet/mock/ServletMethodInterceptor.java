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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;
import com.google.inject.servlet.GuiceFilter;

/**
 * Wraps any invocation of {@link HttpServlet}'s <em>do</em>
 * method in {@link GuiceFilter}'s
 * {@link GuiceFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 * invocation.
 * <p>
 * Created on Jul 3, 2010
 *
 * @author hshsce
 */
public class ServletMethodInterceptor implements MethodInterceptor {

	@Inject
	private GuiceFilter m_guiceFilter;


	/** {@inheritDoc} */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		if (args.length < 2) {
			throw new AssertionError(
					"Cannot invoke as servlet method - " +
					"both HttpServletRequest and HttpServletResponse parameters are" +
					"required - method: " + invocation.getMethod());
		}
		if (!(args[0] instanceof HttpServletRequest)) {
			throw new AssertionError(
					"Cannot invoke as servlet method - " +
					"the first argument is not an instance of HttpServletRequest");
		}
		if (!(args[1] instanceof HttpServletResponse)) {
			throw new AssertionError(
					"Cannot invoke as servlet method - " +
					"the second argument is not an instance of HttpServletResponse");
		}
		MethodInvokingFilterChain filterChain = new MethodInvokingFilterChain(invocation);
		m_guiceFilter.doFilter((HttpServletRequest) args[0], (HttpServletResponse) args[1], filterChain);
		Throwable throwable = filterChain.getThrowable();
		if (throwable != null) {
			throw throwable;
		}
		return filterChain.getResult();
	}

}
