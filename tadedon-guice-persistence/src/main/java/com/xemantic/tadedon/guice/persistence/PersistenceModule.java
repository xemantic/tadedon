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
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.util.Types;

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

	private final Module m_transactionSupportModule;

	/**
	 * Creates new persistence module using {@link DefaultTransactionSupport}.
	 */
	public PersistenceModule() {
		this(new AbstractModule() {
			@Override
			protected void configure() {
				bind(TransactionSupport.class).to(DefaultTransactionSupport.class);
			}
		});
	}

	/**
	 * Creates new persistence module using {@link TransactionSupport} provided
	 * in the private {transactionSupportModule}
	 *
	 * @param transactionSupportModule private module which must bind {@link TransactionSupport}.
	 */
	public PersistenceModule(Module transactionSupportModule) {
		m_transactionSupportModule = transactionSupportModule;
	}

	/** {@inheritDoc} */
	@Override
	protected void configure() {
		requireBinding(Key.get(Types.providerOf(EntityManagerFactory.class)));
		TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
		PrivateBinder privBinder = binder().newPrivateBinder();
		privBinder.install(m_transactionSupportModule);
		privBinder.requestInjection(interceptor);
		bindInterceptor(superAnnotatedWith(Transactional.class), any(), interceptor);
		bindInterceptor(any(), superAnnotatedWith(Transactional.class), interceptor);
		bind(Transaction.class).toProvider(TransactionProvider.class);
		bind(EntityManager.class).toProvider(EntityManagerProvider.class);
		bind(Connection.class).toProvider(TransactionalConnectionProvider.class);
	}

}
