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

import static com.google.inject.matcher.Matchers.*;
import static com.xemantic.tadedon.guice.matcher.AnnotationMatchers.*;

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.PrivateBinder;
import com.google.inject.Singleton;

/**
 * Guice module enabling interceptors of {@literal @}{@link Transactional} annotation.
 * <p>
 * It requires {@link EntityManagerFactory} binding. And provides:
 * <ul>
 * <li>{@link Transaction} provider</li>
 * <li>{@link EntityManager} provider</li>
 * <li>{@link Connection} provider</li>
 * </ul>
 * <p>
 * Created on Mar 25, 2010
 *
 * @author hshsce
 */
public class PersistenceModule extends AbstractModule {

	private final Class<? extends TransactionSupport> m_transactionSupportClass;

	/**
	 * Creates new persistence module using {@link DefaultTransactionSupport}.
	 */
	public PersistenceModule() {
		this(DefaultTransactionSupport.class);
	}

	/**
	 * Creates new persistence module using given transaction support.
	 * <p>
	 * Note: the transaction support class should be annotated with appropriate scope where
	 * {@literal @}{@link Singleton} seems the most appropriate.
	 *
	 * @param transactionSupportClass the transaction support class.
	 */
	public PersistenceModule(Class<? extends TransactionSupport> transactionSupportClass) {
		m_transactionSupportClass = transactionSupportClass;
	}

	/** {@inheritDoc} */
	@Override
	protected void configure() {
		requireBinding(EntityManagerFactory.class);
		TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
		PrivateBinder privBinder = binder().newPrivateBinder();
		privBinder.bind(TransactionSupport.class).to(m_transactionSupportClass);
		privBinder.requestInjection(interceptor);
		bindInterceptor(superAnnotatedWith(Transactional.class), any(), interceptor);
		bindInterceptor(any(), superAnnotatedWith(Transactional.class), interceptor);
		bind(Transaction.class).toProvider(TransactionProvider.class);
		bind(EntityManager.class).toProvider(EntityManagerProvider.class);
		bind(Connection.class).toProvider(TransactionalConnectionProvider.class);
	}

}
