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
package com.xemantic.tadedon.guice.lifecycle;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Injector;
import com.xemantic.tadedon.logging.Loggers;

/**
 * Unit test for {@link LifecycleManager}.
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 */
public abstract class LifecycleManagerTest {

    @BeforeClass
    public static void redirectJulToSlf4J() {
        Loggers.redirectJulToSLF4J();
    }

	@Test
	public void shouldNotInitializeComponentBeforeManagerInitialization() {
		// given
		LifecycleEnabledComponent component = getInjector().getInstance(LifecycleEnabledComponent.class);

		// when nothing happens 

		// then
		assertThat(component.isInitialized(), is(false));
		assertThat(component.isDestroyed(), is(false));
	}

	@Test
	public void shouldInitializeComponentAfterManagerInitialization() {
		// given
		LifecycleManager lifecycleManager = getInjector().getInstance(LifecycleManager.class);
		LifecycleEnabledComponent component = getInjector().getInstance(LifecycleEnabledComponent.class);

		// when
		lifecycleManager.initialize();

		// then
		assertThat(component.isInitialized(), is(true));
		assertThat(component.isDestroyed(), is(false));
	}

	@Test
	public void shouldDestroyComponentAfterManagerDestroy() {
		// given
		LifecycleManager lifecycleManager = getInjector().getInstance(LifecycleManager.class);
		LifecycleEnabledComponent component = getInjector().getInstance(LifecycleEnabledComponent.class);

		// when
		lifecycleManager.initialize();
		lifecycleManager.destroy();

		// then
		assertThat(component.isInitialized(), is(true));
		assertThat(component.isDestroyed(), is(true));
	}

	/**
	 * New injector should be created for every test.
	 *
	 * @return
	 */
	protected abstract Injector getInjector();

}
