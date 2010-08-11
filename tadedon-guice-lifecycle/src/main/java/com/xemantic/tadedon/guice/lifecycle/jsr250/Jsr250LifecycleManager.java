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

import java.util.Deque;
import java.util.Iterator;

import javax.annotation.PreDestroy;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.xemantic.tadedon.guice.lifecycle.LifecycleManager;

/**
 * 
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 */
@Singleton
public class Jsr250LifecycleManager implements LifecycleManager {

	private final Deque<Object> m_destroyableObjects;

	private final PostConstructInvoker m_postConstructInvoker;


	@Inject
	public Jsr250LifecycleManager(
			PostConstructInvoker postConstructInvoker,
			@Named("destroyableObjects") Deque<Object> destroyableObjects) {

		m_destroyableObjects = destroyableObjects;
		m_postConstructInvoker = postConstructInvoker;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize() {
		m_postConstructInvoker.invokeAll();
	}

	/** {@inheritDoc} */
	@Override
	public void destroy() {
		Jsr250Utils.invoke(PreDestroy.class, new Iterable<Object>() {
			@Override
			public Iterator<Object> iterator() {
				return m_destroyableObjects.descendingIterator();
			}
		});
		m_destroyableObjects.clear();
	}

	@Override
	public boolean isInitialized() {
		return m_postConstructInvoker.isInitialized();
	}

}
