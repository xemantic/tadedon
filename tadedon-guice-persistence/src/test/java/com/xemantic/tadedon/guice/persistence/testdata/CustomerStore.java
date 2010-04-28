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
package com.xemantic.tadedon.guice.persistence.testdata;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.xemantic.tadedon.guice.persistence.Transaction;
import com.xemantic.tadedon.guice.persistence.Transactional;

/**
 * Test customer store service.
 * <p>
 * Created on Apr 28, 2010
 *
 * @author hshsce
 */
public class CustomerStore {

	private final Provider<Transaction> m_transactionProvider;

	/**
	 * Creates instances with injected dependencies.
	 *
	 * @param transactionProvider transaction provider.
	 */
	@Inject
	public CustomerStore(Provider<Transaction> transactionProvider) {
		m_transactionProvider = transactionProvider;
	}

	/**
	 * Puts customer into db.
	 *
	 * @param customer the customer instance.
	 */
	@Transactional
	public void put(Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("customer cannot be null");
		}
		m_transactionProvider.get().getEntityManager().persist(customer);
	}

}
