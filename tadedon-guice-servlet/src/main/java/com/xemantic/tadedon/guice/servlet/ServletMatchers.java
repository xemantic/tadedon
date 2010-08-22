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
package com.xemantic.tadedon.guice.servlet;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

/**
 * Matchers for {@link HttpServlet} derived class and <em>do</em> servlet methods.
 * <p>
 * Created on Jul 3, 2010
 *
 * @author hshsce
 */
public final class ServletMatchers {

	private ServletMatchers() { /* util class, non-instantiable */ }

	/**
	 * Matcher for subclasses of {@link HttpServlet}.
	 */
	@SuppressWarnings("unchecked")
	public static final Matcher<Class> CLASS = Matchers.subclassesOf(HttpServlet.class);

	/**
	 * Matcher for <em>do</em> and {@code service} servlet methods.
	 */
	public static final Matcher<Method> METHOD = new AbstractMatcher<Method>() {
		@Override
		public boolean matches(Method method) {
			return METHODS.contains(method.getName());
		}

		@Override
		public boolean equals(Object other) {
			return (other == this);
		}

		@Override
		public int hashCode() {
			return 37;
		}

		@Override
		public String toString() {
			return "servletMethod()";
		}

		private static final long serialVersionUID = 0;
	};

	/**
	 * Returns matcher for subclasses of {@link HttpServlet}. It returns
	 * the {@link #CLASS} instance.
	 *
	 * @return the servlet class matcher.
	 */
	@SuppressWarnings("unchecked")
	public static Matcher<Class> servletClass() {
		return CLASS;
	}

	/**
	 * Returns matcher for <em>do</em> and {@code service} servlet methods.
	 * It returns the {@link #METHOD} instance.
	 *
	 * @return the servlet method matcher.
	 */
	public static Matcher<Method> servletMethod() {
		return METHOD;
	}

	private static final Set<String> METHODS =
		new HashSet<String>(Arrays.asList(
				"doGet",
				"doPost",
				"doPut",
				"doDelete",
				"service"));

}
