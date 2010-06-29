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
package com.xemantic.tadadon.guava.base;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Suppliers;
import com.xemantic.tadedon.guava.base.MemoizingSupplier;

/**
 * Unit test for {@link MemoizingSupplier}.
 * <p>
 * Created on Jun 29, 2010
 *
 * @author hshsce
 */
public class MemoizingSupplierTest {

	/** Test of {@link MemoizingSupplier#MemoizingSupplier(com.google.common.base.Supplier)}. */
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionOnNullDelegate() {
		// when
		new MemoizingSupplier<Object>(null);
	}

	/** Test of {@link MemoizingSupplier#get()}. */
	@Test
	public void shouldReturnInstanceProvidedByDelegate() {
		// given
		Object instance = new Object();
		MemoizingSupplier<Object> supplier = new MemoizingSupplier<Object>(Suppliers.ofInstance(instance));

		// when
		Object suppliedInstance = supplier.get();

		// then
		Assert.assertSame(instance, suppliedInstance);
	}

}
