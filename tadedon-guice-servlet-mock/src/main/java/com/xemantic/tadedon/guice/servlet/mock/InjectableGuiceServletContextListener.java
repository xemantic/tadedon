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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Instance of {@link GuiceServletContextListener} which can be
 * injected. The {@link #getInjector()} method returns default injector
 * passed via constructor.
 * <p>
 * Created on Jul 3, 2010
 *
 * @author hshsce
 */
@Singleton
public class InjectableGuiceServletContextListener extends GuiceServletContextListener {

	private final Injector m_injector;

	/**
	 * Creates listener instance.
	 *
	 * @param injector the injector (container).
	 */
	@Inject
	public InjectableGuiceServletContextListener(Injector injector) {
		m_injector = injector;
	}

	/** {@inheritDoc} */
	@Override
	protected Injector getInjector() {
		return m_injector;
	}

}
