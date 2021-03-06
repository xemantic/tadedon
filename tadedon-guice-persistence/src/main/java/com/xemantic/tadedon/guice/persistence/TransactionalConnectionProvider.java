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

import java.sql.Connection;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Provider of {@link Connection} associated with current transaction.
 * <p>
 * Created on Apr 26, 2010
 *
 * @author hshsce
 */
@Singleton
public class TransactionalConnectionProvider implements Provider<Connection> {

	private final Provider<Transaction> m_transactionProvider;

	/**
	 * Creates instance with injected dependencies.
	 *
	 * @param transactionProvider the transaction provider.
	 */
	@Inject
	public TransactionalConnectionProvider(final Provider<Transaction> transactionProvider) {
		m_transactionProvider = transactionProvider;
	}

	/** {@inheritDoc} */
	@Override
	public Connection get() {
		return m_transactionProvider.get().getConnection();
	}

}
