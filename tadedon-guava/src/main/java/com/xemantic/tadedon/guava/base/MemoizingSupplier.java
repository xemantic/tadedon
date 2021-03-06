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
package com.xemantic.tadedon.guava.base;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.GuardedBy;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

/**
 * Fast memoizing supplier.
 * <p>
 * Created on Jun 24, 2010
 *
 * @author morisil
 * @param <T> type instance returned by supplier.
 * @see Suppliers#memoize(Supplier)
 */
public class MemoizingSupplier<T> implements Supplier<T> {

	private final Lock lock = new ReentrantLock();

	private final Supplier<T> deletage;

	@GuardedBy("lock")
	private final AtomicReference<T> reference = new AtomicReference<T>();


	/**
	 * Creates memoizing supplier which supplies instance of type {@code T}
	 * provided by the {@code delegate}. The first {@link #get()} invocation
	 * will retrieve the instance from delegate. The same instance is used for
	 * subsequent invocations.
	 *
	 * @param deletage the delegate supplier.
	 */
	public MemoizingSupplier(Supplier<T> deletage) {
		checkArgument(deletage != null, "delegate cannot be null");
		this.deletage = deletage;
	}

	/** {@inheritDoc} */
	@Override
	public T get() {
		T instance = reference.get();
		if (instance == null) {
			lock.lock();
			try {
				instance = reference.get();
				if (instance == null) { // instance could be already provided by other thread
					instance = deletage.get();
					reference.set(instance);
				}
			} finally {
				lock.unlock();
			}
		}
		return instance;
	}

    /**
     * Resets this memoizing supplier. On next call to the
     * {@link #get()} method, the {@code delegate} supplier will
     * be used to supply memoized value.
     */
	public void reset() {
	    reference.set(null);
	}

}
