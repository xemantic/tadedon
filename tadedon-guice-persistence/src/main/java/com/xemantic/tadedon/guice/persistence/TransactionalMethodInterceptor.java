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

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;

import com.google.inject.Inject;

/**
 * Interceptor of methods annotated with {@literal @}{@link Transactional}.
 * <p>
 * Created on Mar 25, 2010
 *
 * @author hshsce
 * @see PersistenceModule
 */
public class TransactionalMethodInterceptor implements MethodInterceptor {

	@Inject
	private TransactionManager m_transactionManager;

	@Inject
	private TransactionFinalizer m_transactionFinalizer;

	@Inject
	private Logger m_logger;


	/** {@inheritDoc} */
	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object result;
		Transaction transaction = m_transactionManager.getLocalTransaction();
		if (m_transactionManager.getLocalTransaction() == null) {
			transaction = m_transactionManager.newLocalTransaciton();
			try {
				logMethodInvocation("new", transaction, invocation);
				result = invokeInNewTransaction(new DefaultTransactionContext(invocation, transaction));
			} finally {
				m_transactionManager.closeLocalTransaction();
			}
		} else {
			logMethodInvocation("existing", transaction, invocation);
			result = invocation.proceed();
		}
		return result;
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

	private void logMethodInvocation(String transactionKind, Transaction transaction, MethodInvocation invocation) {
		if (m_logger.isDebugEnabled()) {
			m_logger.debug(
					"Invoking method in {} transaction: {}, method: {}, args: {}",
					new Object[] {
							transactionKind,
							transaction.getId(),
							invocation.getMethod().toGenericString(),
							Arrays.asList(invocation.getArguments()) });
		}
	}

	private static class DefaultTransactionContext implements TransactionContext {

		private final MethodInvocation m_methodInvocation;

		private final Transaction m_transaction;

		private Throwable m_throwable = null;

		private DefaultTransactionContext(MethodInvocation invocation, Transaction transaction) {
			m_methodInvocation = invocation;
			m_transaction = transaction;
		}

		/** {@inheritDoc} */
		@Override
		public MethodInvocation getMethodInvocation() {
			return m_methodInvocation;
		}

		/** {@inheritDoc} */
		@Override
		public Transaction getTransaction() {
			return m_transaction;
		}

		/** {@inheritDoc} */
		@Override
		public Throwable getThrowable() {
			return m_throwable;
		}

	}

}
