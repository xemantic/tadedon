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

import java.lang.reflect.Method;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.GuiceFilter;

/**
 * Configures {@link HttpRequestedMethodInterceptor}.
 * <p>
 * Created on Jul 3, 2010
 *
 * @author hshsce
 */
public class HttpRequestedServiceInterceptionModule extends AbstractModule {

    private final Matcher<? super Class<?>> m_classMatcher;

    private final Matcher<? super Method> m_methodMatcher;

    public HttpRequestedServiceInterceptionModule(Class<?> serviceClass) {
        this(Matchers.subclassesOf(serviceClass));
    }

    public HttpRequestedServiceInterceptionModule(Matcher<? super Class<?>> classMatcher) {
        this(classMatcher, Matchers.any());
    }

    public HttpRequestedServiceInterceptionModule(
            Matcher<? super Class<?>> classMatcher,
            Matcher<? super Method> methodMatcher) {

        m_classMatcher = classMatcher;
        m_methodMatcher = methodMatcher;
    }

    /** {@inheritDoc} */
    @Override
    protected void configure() {
        requireBinding(GuiceFilter.class);
        requireBinding(FakeServletContainer.class);
        HttpRequestedMethodInterceptor interceptor = new HttpRequestedMethodInterceptor();
        requestInjection(interceptor);
        bindInterceptor(m_classMatcher, m_methodMatcher, interceptor);
    }

}
