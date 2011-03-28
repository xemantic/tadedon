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

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.xml.ws.Holder;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * Interceptor of methods annotated with {@literal @}{@link Transactional}.
 * <p>
 * Created on Mar 25, 2010
 *
 * @author morisil
 * @see PersistenceModule
 */
public class TransactionalMethodInterceptor implements MethodInterceptor {

	@Inject
	private TransactionManager transactionManager;

	@Inject
	private TransactionSupport transactionSupport;


	/** {@inheritDoc} */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
	    Method method = invocation.getMethod();
	    Class<?> klass = method.getDeclaringClass();
	    Logger logger = LoggerFactory.getLogger(klass);
		final Object result;
		Transaction transaction = transactionManager.getLocalTransaction();
		if (transaction == null) {
			transaction = transactionManager.newLocalTransaciton();
			try {
				logMethod("created ", transaction, invocation, logger);
				result = invokeInNewTransaction(new DefaultTransactionContext(invocation, transaction), logger);
			} finally {
				transactionManager.closeLocalTransaction();
			}
		} else {
			logMethod("joined  ", transaction, invocation, logger);
			result = invocation.proceed();
            logMethod("exit    ", transaction, invocation, logger);
		}
		return result;
	}

	private Object invokeInNewTransaction(DefaultTransactionContext context, Logger logger) throws Throwable {
		final EntityManager em = context.getTransaction().getEntityManager();
		try {
			em.getTransaction().begin();
			return context.getMethodInvocation().proceed();
		} catch (Throwable t) {
			context.m_throwable = t;
			throw t;
		} finally {
			try {
				EntityTransaction emTrx = em.getTransaction();
				if ((!context.getTransaction().isRollbackRequested()) &&
						transactionSupport.shouldCommit(context)) {
					if (emTrx.isActive()) {
					    if (logger.isDebugEnabled()) {
					        logger.debug("trx: {} commit   #{}()",
					                context.getTransaction().getId(),
					                context.getMethodInvocation().getMethod().getName());
					    }
						emTrx.commit();
					} else {
						logger.error("EntityManager transaction is already inactive, cannot commit");
					}
				} else {
					if (emTrx.isActive()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("trx: {} rollback #{}()",
                                    context.getTransaction().getId(),
                                    context.getMethodInvocation().getMethod().getName());
                        }
						emTrx.rollback();
					} else {
						logger.error("EntityManager transaction is already inactive, cannot rollback");
					}
				}
			} catch (Throwable t) {
				if (context.m_throwable == null) { // we are already throwing
					throw t;
				} else {
					logger.error("Exception occured while finishing transaction", t);
				}
			}
		}
	}


	private void logMethod(
	        String logKind,
	        Transaction transaction,
	        MethodInvocation invocation,
	        Logger logger) {
		if (logger.isDebugEnabled()) {
		    logger.debug(
					  "trx: {} {} #{}()",
    					new Object[] {
					          transaction.getId(),
					          logKind,
					          invocation.getMethod().getName()});
		}
		if (logger.isTraceEnabled()) {
	          logger.trace(
	                  "trx: {} {} #{}() args: {}",
	                    new Object[] {
                              transaction.getId(),
	                          logKind,
	                          invocation.getMethod().getName(),
	                          argsToString(invocation.getArguments()) });
		}
	}

	private String argsToString(Object[] args) {
	    StringBuilder b = new StringBuilder();
	    b.append('[');
	    if (args != null) {
	        appendArgs(b, args);
	    }
	    b.append(']');
	    return b.toString();
	}

	private void appendArgs(StringBuilder b, Object[] args) {
        Joiner joiner = Joiner.on(", ").useForNull("null");
        Iterable<Object> transformed = Iterables.transform(
                Arrays.asList(args), new Function<Object, Object>() {

            @Override
            public Object apply(Object arg) {
                if (arg instanceof Holder) {
                    return ((Holder<?>) arg).value;
                }
                return arg;
            }
        });
        joiner.appendTo(b, transformed);	    
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
