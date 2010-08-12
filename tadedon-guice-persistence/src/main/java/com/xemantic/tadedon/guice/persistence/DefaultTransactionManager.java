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
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Default thread local transaction manager.
 * <p>
 * Created on Apr 29, 2010
 *
 * @author hshsce
 */
@Singleton
public class DefaultTransactionManager implements TransactionManager {

	private final Provider<EntityManagerFactory> m_entityManagerFactoryProvider;

	private final AtomicLong m_transactionIdSequencer = new AtomicLong(1);

	private final ThreadLocal<DefaultTransaction> m_localTransaction = new ThreadLocal<DefaultTransaction>();

	private final Logger m_logger;


	/**
	 * Creates instance with injected dependencies.
	 *
	 * @param entityManagerFactory the entity manager factory.
	 * @param logger the logger.
	 */
	@Inject
	public DefaultTransactionManager(Provider<EntityManagerFactory> entityManagerFactoryProvider, Logger logger) {
		m_entityManagerFactoryProvider = entityManagerFactoryProvider;
		m_logger = logger;
	}

	/** {@inheritDoc} */
	@Override
	public Transaction getLocalTransaction() {
		return m_localTransaction.get();
	}

	/** {@inheritDoc} */
	@Override
	public Transaction newLocalTransaciton() {
		final DefaultTransaction transaction = new DefaultTransaction(m_entityManagerFactoryProvider.get().createEntityManager());
		m_localTransaction.set(transaction);
		m_logger.debug("Transaction created, id: {}", transaction.getId());
		return transaction;
	}

	/** {@inheritDoc} */
	@Override
	public void closeLocalTransaction() {
		DefaultTransaction transaction = m_localTransaction.get();
		m_localTransaction.set(null);
		if (transaction.m_entityManager.isOpen()) {
			transaction.m_entityManager.close();
		}
		m_logger.debug("Transaction closed, id: {}", transaction.getId());
	}

	private class DefaultTransaction implements Transaction {

		private final String m_id = String.valueOf(m_transactionIdSequencer.getAndIncrement());

		private final EntityManager m_entityManager;

		private Connection m_connection;

		private boolean m_shouldRollback;

		private DefaultTransaction(final EntityManager entityManager) {
			m_entityManager = entityManager;
		}

		/** {@inheritDoc} */
		@Override
		public String getId() {
			return m_id;
		}

		/** {@inheritDoc} */
		@Override
		public EntityManager getEntityManager() {
			return m_entityManager;
		}

		/** {@inheritDoc} */
		@Override
		public Connection getConnection() {
			if (m_connection == null) {
				m_connection = m_entityManager.unwrap(Connection.class);
			}
			return m_connection;
		}

		/** {@inheritDoc} */
		@Override
		public void requestRollback() {
			m_shouldRollback = true;
		}

		/** {@inheritDoc} */
		@Override
		public boolean isRollbackRequested() {
			return m_shouldRollback;
		}

	}

}
