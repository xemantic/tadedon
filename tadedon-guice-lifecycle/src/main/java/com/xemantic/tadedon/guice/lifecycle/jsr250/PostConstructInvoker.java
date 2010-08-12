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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.concurrent.ThreadSafe;

import com.google.inject.Singleton;

/**
 * 
 * 
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 *
 */
@ThreadSafe
@Singleton
public class PostConstructInvoker {

	private Lock m_lock = new ReentrantLock();

	private Collection<Object> m_objects = new LinkedList<Object>();

	private boolean m_initialized = false;

 
	public void addObject(Object object) {
		m_lock.lock();
		try {
			if (!m_initialized) {
				m_objects.add(object);
			} else {
				Jsr250Utils.invoke(PostConstruct.class, Arrays.asList(object));
			}
		} finally {
			m_lock.unlock();
		}
	}

	public void invokeAll() {
		m_lock.lock();
		try {
			Jsr250Utils.invoke(PostConstruct.class, m_objects);
			m_objects = null;
			m_initialized = true;
		} finally {
			m_lock.unlock();
		}
	}

	public boolean isInitialized() {
		m_lock.lock();
		try {
			return m_initialized;
		} finally {
			m_lock.unlock();
		}
	}

}
