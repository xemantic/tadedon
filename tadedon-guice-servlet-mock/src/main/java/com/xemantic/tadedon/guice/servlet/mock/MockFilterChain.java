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

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 * <p>
 * Created on Aug 27, 2010
 *
 * @author hshsce
 */
public abstract class MockFilterChain implements FilterChain {

    private Throwable m_throwable;

    private Object m_result;


    /** {@inheritDoc} */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) {
        try {
            m_result = invoke();
        } catch (Throwable t) {
            m_throwable = t;
        }
    }

    protected abstract Object invoke() throws Throwable;

    /**
     * Returns throwable thrown during execution of
     * {@link #doFilter(ServletRequest, ServletResponse)}
     * or {@code null} if no throwable occurred.
     *
     * @return the throwable or {@code null} if nothing was thrown.
     */
    public Throwable getThrowable() {
        return m_throwable;
    }

    /**
     * Returns invocation result.
     *
     * @return the result.
     */
    public Object getResult() {
        return m_result;
    }

}
