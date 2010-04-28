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
import com.xemantic.tadedon.guice.persistence.Transactional;

/**
 * Test service for {@link Transaction#requestRollback()} functionality.
 * <p>
 * Created on Apr 28, 2010
 *
 * @author hshsce
 */
@Singleton
public class RequestedRollbackService {

	private final Provider<Transaction> m_transactionProvider;

	/**
	 * Creates service instance with injected dependencies.
	 *
	 * @param transactionProvider the transaction provider.
	 */
	@Inject
	public RequestedRollbackService(Provider<Transaction> transactionProvider) {
		m_transactionProvider = transactionProvider;
	}

	/**
	 * Request rollback on current transaction if the {@code requestRollback} flag is set.
	 *
	 * @param requestRollback request rollback flag.
	 */
	@Transactional
	public void requestRollback(boolean requestRollback) {
		if (requestRollback) {
			m_transactionProvider.get().requestRollback();
		}
	}

}
