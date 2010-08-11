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

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * 
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 */
public class PostConstructAnnotatedTypeListener implements TypeListener {

	@Inject
	private PostConstructInvoker m_initializer;

	private InjectionListener<Object> m_injectionListener = new InjectionListener<Object>() {
		@Override
		public void afterInjection(Object injectee) {
			m_initializer.addObject(injectee);
		}
	};

	/** {@inheritDoc} */
	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		encounter.register(m_injectionListener);
	}

}
