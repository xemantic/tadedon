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
package com.xemantic.tadedon.guice.lifecycle.jsr250;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.google.inject.Singleton;
import com.xemantic.tadedon.guice.lifecycle.LifecycleEnabledComponent;

/**
 * 
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 *
 */
@Singleton
public class Jsr250LifecycleEnabledComponent implements LifecycleEnabledComponent {

	private boolean m_initialized = false;

	private boolean m_destroyed = false;

	/** {@inheritDoc} */
	@Override
	@PostConstruct
	public void initialize() {
		m_initialized = true;
	}

	/** {@inheritDoc} */
	@Override
	@PreDestroy
	public void destroy() {
		m_destroyed = true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isInitialized() {
		return m_initialized;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isDestroyed() {
		return m_destroyed;
	}

}
