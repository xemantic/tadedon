/*
 * Copyright 2010-2011 Xemantic
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
 * @author morisil
 */
@Singleton
public class DefaultTransactionManager implements TransactionManager {

	private final Provider<EntityManagerFactory> entityManagerFactoryProvider;

	private final AtomicLong transactionIdSequencer = new AtomicLong(1);

	private final ThreadLocal<DefaultTransaction> localTransaction = new ThreadLocal<DefaultTransaction>();

	private final Logger logger;


	/**
	 * Creates instance with injected dependencies.
	 *
	 * @param entityManagerFactory the entity manager factory.
	 * @param logger the logger.
	 */
	@Inject
	public DefaultTransactionManager(
	        Provider<EntityManagerFactory> entityManagerFactoryProvider,
	        Logger logger) {

		this.entityManagerFactoryProvider = entityManagerFactoryProvider;
		this.logger = logger;
	}

	/** {@inheritDoc} */
	@Override
	public Transaction getLocalTransaction() {
		return localTransaction.get();
	}

	/** {@inheritDoc} */
	@Override
	public Transaction newLocalTransaciton() {
		final DefaultTransaction transaction = new DefaultTransaction(entityManagerFactoryProvider.get().createEntityManager());
		localTransaction.set(transaction);
		logger.debug("trx: {} created", transaction.getId());
		return transaction;
	}

	/** {@inheritDoc} */
	@Override
	public void closeLocalTransaction() {
		DefaultTransaction transaction = localTransaction.get();
		localTransaction.set(null);
		if (transaction.entityManager.isOpen()) {
			transaction.entityManager.close();
		}
		logger.debug("trx: {} closed", transaction.getId());
	}

	private class DefaultTransaction implements Transaction {

		private final String id = String.valueOf(transactionIdSequencer.getAndIncrement());

		private final EntityManager entityManager;

		private Connection connection;

		private boolean shouldRollback;

		private DefaultTransaction(final EntityManager entityManager) {
			this.entityManager = entityManager;
		}

		/** {@inheritDoc} */
		@Override
		public String getId() {
			return id;
		}

		/** {@inheritDoc} */
		@Override
		public EntityManager getEntityManager() {
			return entityManager;
		}

		/** {@inheritDoc} */
		@Override
		public Connection getConnection() {
			if (connection == null) {
				connection = entityManager.unwrap(Connection.class);
			}
			return connection;
		}

		/** {@inheritDoc} */
		@Override
		public void requestRollback() {
			shouldRollback = true;
		}

		/** {@inheritDoc} */
		@Override
		public boolean isRollbackRequested() {
			return shouldRollback;
		}

	}

}
