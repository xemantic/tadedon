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
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Interceptor of methods annotated with {@literal @}{@link Transactional}.
 * <p>
 * Created on Mar 25, 2010
 *
 * @author hshsce
 * @see PersistenceModule
 */
public class TransactionalMethodInterceptor implements MethodInterceptor, Provider<Transaction> {

	@Inject
	private EntityManagerFactory m_entityManagerFactory;

	@Inject
	private TransactionFinalizer m_transactionFinalizer;

	@Inject
	private Logger m_logger;

	private final AtomicLong m_transactionIdSequencer = new AtomicLong(1);

	private final ThreadLocal<Transaction> m_localTransaction = new ThreadLocal<Transaction>();


	/** {@inheritDoc} */
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object result;
		Transaction transaction = m_localTransaction.get();
		if (transaction == null) {
			result = invokeInNewTransaction(invocation);
		} else {
			if (m_logger.isDebugEnabled()) {
				m_logger.debug(
						"Invoking method in existing transaction: {}, method: {}, args: {}",
						new Object[] {
								transaction.getId(),
								invocation.getMethod().toGenericString(),
								Arrays.asList(invocation.getArguments()) });
			}
			result = invocation.proceed();
		}
		return result;
	}

	private Object invokeInNewTransaction(MethodInvocation invocation) throws Throwable {
		EntityManager em = m_entityManagerFactory.createEntityManager();
		final DefaultTransaction transaction = new DefaultTransaction(em);
		if (m_logger.isDebugEnabled()) {
			m_logger.debug(
					"Invoking method in new transaction: {}, method: {}, args: {}",
					new Object[] {
							transaction.getId(),
							invocation.getMethod().toGenericString(),
							Arrays.asList(invocation.getArguments()) });
		}
		try {
			m_localTransaction.set(transaction);
			return invokeInNewTransaction(new DefaultTransactionContext(invocation, transaction));
		} finally {
			m_localTransaction.set(null);
			if (transaction.m_entityManager.isOpen()) {
				transaction.m_entityManager.close();
			}
			m_logger.debug("Transaction closed, id: {}", transaction.getId());
		}
	}

	private Object invokeInNewTransaction(DefaultTransactionContext context) throws Throwable {
		final EntityManager em = context.getTransaction().getEntityManager();
		try {
			em.getTransaction().begin();
			return context.getMethodInvocation().proceed();
		} catch (Throwable t) {
			context.m_throwable = t;
			throw t;
		} finally {
			m_transactionFinalizer.finalize(context);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Transaction get() {
		Transaction transaction = m_localTransaction.get();
		if (transaction == null) {
			throw new IllegalStateException("Trying to get transaction outside @Transactional method scope");
		}
		return transaction;
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

	private class DefaultTransactionContext implements TransactionContext {

		private final MethodInvocation m_methodInvocation;

		private final DefaultTransaction m_transaction;

		private Throwable m_throwable = null;

		private DefaultTransactionContext(MethodInvocation invocation, DefaultTransaction transaction) {
			m_methodInvocation = invocation;
			m_transaction = transaction;
		}

		public MethodInvocation getMethodInvocation() {
			return m_methodInvocation;
		}

		public Transaction getTransaction() {
			return m_transaction;
		}

		/**
		 * @return the throwable
		 */
		public Throwable getThrowable() {
			return m_throwable;
		}

	}

}
