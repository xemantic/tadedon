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

import org.junit.Before;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xemantic.tadedon.guice.lifecycle.LifecycleEnabledComponent;
import com.xemantic.tadedon.guice.lifecycle.LifecycleManagerTest;

/**
 * 
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 *
 */
public class Jsr250LifecycleManagerTest extends LifecycleManagerTest {

	private Injector m_injector;

	@Before
	public void setUpInjector() {
		m_injector = Guice.createInjector(
				new Jsr250LifecycleModule(),
				new AbstractModule() {
					@Override
					protected void configure() {
						bind(LifecycleEnabledComponent.class).to(Jsr250LifecycleEnabledComponent.class);
					}
				});
	}

	/** {@inheritDoc} */
	@Override
	protected Injector getInjector() {
		return m_injector;
	}

}
