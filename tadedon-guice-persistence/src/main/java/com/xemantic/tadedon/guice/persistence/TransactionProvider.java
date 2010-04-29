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
package com.xemantic.tadedon.guice.persistence;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Transaction provider.
 * <p>
 * Created on Apr 29, 2010
 *
 * @author hshsce
 */
@Singleton
public class TransactionProvider implements Provider<Transaction> {

	private final TransactionManager m_manager;

	/**
	 * Creates instance with injected dependencies.
	 *
	 * @param manager the transaction manager.
	 */
	@Inject
	public TransactionProvider(TransactionManager manager) {
		m_manager = manager;
	}

	/** {@inheritDoc} */
	@Override
	public Transaction get() {
		Transaction transaction = m_manager.getLocalTransaction();
		if (transaction == null) {
			throw new IllegalStateException("Trying to get transaction outside @Transactional method scope");
		}
		return transaction;
	}

}
