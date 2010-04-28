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
import com.google.inject.Singleton;
import com.xemantic.tadedon.guice.persistence.Transaction;

/**
 * Default customer service.
 * <p>
 * Created on Mar 25, 2010
 *
 * @author hshsce
 */
@Singleton
public class DefaultCustomerService implements CustomerService {

	private final Provider<Transaction> m_transactionProvider;

	private final CustomerStore m_store;

	/**
	 * Creates instance with injected dependencies.
	 *
	 * @param transactionProvider transaction provider.
	 * @param store customer store.
	 */
	@Inject
	public DefaultCustomerService(Provider<Transaction> transactionProvider, CustomerStore store) {
		m_transactionProvider = transactionProvider;
		m_store = store;
	}

	/** {@inheritDoc} */
	@Override
	public Customer capitalizeCustomerName(Customer customer) {
		customer.setName(customer.getName().toUpperCase());
		return customer;
	}

	/** {@inheritDoc} */
	@Override
	public Customer getCustomerById(String id) {
		if (id == null) {
			throw new IllegalArgumentException("id cannot be null");
		}
		return m_transactionProvider.get().getEntityManager().find(Customer.class, id);
	}

	/** {@inheritDoc} */
	@Override
	public void persistCustomer(Customer customer) {
		m_store.put(customer);
	}

}
