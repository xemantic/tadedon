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

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.xemantic.tadedon.guice.persistence.testdata.Customer;
import com.xemantic.tadedon.guice.persistence.testdata.CustomerService;
import com.xemantic.tadedon.guice.persistence.testdata.CustomerStore;
import com.xemantic.tadedon.guice.persistence.testdata.RequestedRollbackService;
import com.xemantic.tadedon.logging.Loggers;

/**
 * Unit tests for {@link PersistenceModule}.
 * <p>
 * Created on Apr 22, 2010
 *
 * @author hshsce
 */
public class PersistenceModuleTest {

	/** Initializes logging. */
	@BeforeClass
	public static void initializeLogging() {
		Loggers.redirectJulToSLF4J();
	}

	/** Tests {@link CustomerService#capitalizeCustomerName(Customer)} method. */
	@Test
	public void shouldNotInteractWithEntityManagerOnNonTransactionalMethod() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		Customer customer = mock(Customer.class);
		given(customer.getName()).willReturn("foo");
		CustomerService service = injector.getInstance(CustomerService.class);

		// when
		service.capitalizeCustomerName(customer);

		// then
		verify(customer).getName();
		verify(customer).setName("FOO");
		verifyNoMoreInteractions(customer);
		verifyZeroInteractions(em);
	}

	/** Tests {@link CustomerService#getCustomerById(String)} method. */
	@Test
	public void shouldRollBackOnReadOnlyTransactionalMethod() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		Customer customer = mock(Customer.class);
		given(em.find(Customer.class, "42")).willReturn(customer);
		InOrder inOrder = inOrder(em, trx);
		CustomerService customerService = injector.getInstance(CustomerService.class);

		// when
		Customer customerById = customerService.getCustomerById("42");

		// then
		assertEquals(customer, customerById);
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).begin();
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).isActive();
		inOrder.verify(trx).rollback();
		inOrder.verify(em).isOpen();
		inOrder.verify(em).close();
		inOrder.verifyNoMoreInteractions();
		verifyZeroInteractions(customer);
	}

	/** Tests {@link CustomerService#getCustomerById(String)} method with null argument. */
	@Test
	public void shouldRollBackOnExceptionInReadOnlyTransactionalMethod() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		Customer customer = mock(Customer.class);
		given(em.find(Customer.class, "42")).willReturn(customer);
		InOrder inOrder = inOrder(em, trx);
		CustomerService customerService = injector.getInstance(CustomerService.class);

		// when
		try {
			customerService.getCustomerById(null); //will throw illegal argument exception
			fail("Should throw illegal argulemtn exception");
		} catch (IllegalArgumentException e) { /* expected exception */ }

		// then
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).begin();
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).isActive();
		inOrder.verify(trx).rollback();
		inOrder.verify(em).isOpen();
		inOrder.verify(em).close();
		inOrder.verifyNoMoreInteractions();
		verifyZeroInteractions(customer);
	}

	/** Tests {@link CustomerStore#put(Customer)} method. */
	@Test
	public void shouldCallPersistAndCommitWithinTransactionalMethod() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		Customer customer = mock(Customer.class);
		InOrder inOrder = inOrder(em, trx);
		CustomerStore store = injector.getInstance(CustomerStore.class);

		// when
		store.put(customer);

		// then
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).begin();
		inOrder.verify(em).persist(customer);
		inOrder.verify(trx).isActive();
		inOrder.verify(trx).commit();
		inOrder.verify(em).isOpen();
		inOrder.verify(em).close();
		inOrder.verifyNoMoreInteractions();
		verifyZeroInteractions(customer);
	}

	/** Tests {@link CustomerStore#put(Customer)} method. */
	@Test
	public void shouldRollbackWhenExceptionOccursInsideTransactionalMethod() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		Customer customer = mock(Customer.class);
		InOrder inOrder = inOrder(em, trx);
		CustomerStore store = injector.getInstance(CustomerStore.class);

		// when
		try {
			store.put(null); //will throw illegal argument exception
			fail("Should throw illegal argulemtn exception");
		} catch (IllegalArgumentException e) { /* expected exception */ }

		// then
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).begin();
		inOrder.verify(trx).isActive();
		inOrder.verify(trx).rollback();
		inOrder.verify(em).isOpen();
		inOrder.verify(em).close();
		inOrder.verifyNoMoreInteractions();
		verifyZeroInteractions(customer);
	}

	/** Tests {@link CustomerStore#put(Customer)} method. */
	@Test
	public void shouldBeginAndCommitOnlyOneTransactionDespiteSubsequentTransactionalMethod() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		InOrder inOrder = inOrder(em, trx);
		CustomerService service = injector.getInstance(CustomerService.class);

		// when
		try {
			service.persistCustomer(null); //will throw illegal argument exception
			fail("Should throw illegal argulemtn exception");
		} catch (IllegalArgumentException e) { /* expected exception */ }

		// then
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).begin();
		inOrder.verify(trx).isActive();
		inOrder.verify(trx).rollback();
		inOrder.verify(em).isOpen();
		inOrder.verify(em).close();
		inOrder.verifyNoMoreInteractions();
	}

	/** Tests {@link CustomerStore#put(Customer)} method. */
	@Test
	public void shouldBeginAndRollbackOnlyOneTransactionDespiteSubsequentTransactionalMethodWhenExceptionOccurs() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		Customer customer = new Customer();
		InOrder inOrder = inOrder(em, trx);
		CustomerService service = injector.getInstance(CustomerService.class);

		// when
		service.persistCustomer(customer);

		// then
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).begin();
		inOrder.verify(em).persist(customer);
		inOrder.verify(trx).isActive();
		inOrder.verify(trx).commit();
		inOrder.verify(em).isOpen();
		inOrder.verify(em).close();
		inOrder.verifyNoMoreInteractions();
	}

	/** Tests {@link RequestedRollbackService#requestRollback(boolean)} method. */
	@Test
	public void shouldCommitOnRequestRollbackSetToFalse() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		InOrder inOrder = inOrder(em, trx);
		RequestedRollbackService service = injector.getInstance(RequestedRollbackService.class);

		// when
		service.requestRollback(false);

		// then
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).begin();
		inOrder.verify(trx).isActive();
		inOrder.verify(trx).commit();
		inOrder.verify(em).isOpen();
		inOrder.verify(em).close();
		inOrder.verifyNoMoreInteractions();
	}

	/** Tests {@link RequestedRollbackService#requestRollback(boolean)} method. */
	@Test
	public void shouldRollbackOnRequestRollbackSetToTrue() {
		// given
		EntityManager em = mock(EntityManager.class);
		EntityTransaction trx = mock(EntityTransaction.class);
		Injector injector = newInjector(em, trx);
		InOrder inOrder = inOrder(em, trx);
		RequestedRollbackService service = injector.getInstance(RequestedRollbackService.class);

		// when
		service.requestRollback(true);

		// then
		inOrder.verify(em).getTransaction();
		inOrder.verify(trx).begin();
		inOrder.verify(trx).isActive();
		inOrder.verify(trx).rollback();
		inOrder.verify(em).isOpen();
		inOrder.verify(em).close();
		inOrder.verifyNoMoreInteractions();
	}

	/** Tests {@link TransactionalMethodInterceptor#get()}. */
	@Test
	@SuppressWarnings("null")
	public void shouldThrowIllegalStateExceptionWhenUsingTransactionProviderOutsideTransaction() {
		// given
		Injector injector = newInjector(mock(EntityManager.class), mock(EntityTransaction.class));
		Provider<Transaction> provider = injector.getProvider(Transaction.class);

		// when
		ProvisionException excetion = null;
		try {
			provider.get();
			fail("Provision exception should be thrown");
		} catch (ProvisionException e) {
			excetion = e;
		}

		// then
		assertTrue(excetion.getCause() instanceof IllegalStateException);
	}

	/** Tests {@link EntityManagerProvider#get()}. */
	@Test
	@SuppressWarnings("null")
	public void shouldThrowIllegalStateExceptionWhenUsingEntityManagerProviderOutsideTransaction() {
		// given
		Injector injector = newInjector(mock(EntityManager.class), mock(EntityTransaction.class));
		Provider<EntityManager> provider = injector.getProvider(EntityManager.class);

		// when
		ProvisionException excetion = null;
		try {
			provider.get();
			fail("Provision exception should be thrown");
		} catch (ProvisionException e) {
			excetion = e;
		}

		// then
		assertTrue(excetion.getCause() instanceof IllegalStateException);
	}

	/** Tests {@link TransactionalConnectionProvider#get()}. */
	@Test
	@SuppressWarnings("null")
	public void shouldThrowIllegalStateExceptionWhenUsingConnectionProviderOutsideTransaction() {
		// given
		Injector injector = newInjector(mock(EntityManager.class), mock(EntityTransaction.class));
		Provider<Connection> provider = injector.getProvider(Connection.class);

		// when
		ProvisionException excetion = null;
		try {
			provider.get();
			fail("Provision exception should be thrown");
		} catch (ProvisionException e) {
			excetion = e;
		}

		// then
		assertTrue(excetion.getCause() instanceof IllegalStateException);
	}

	private Injector newInjector(EntityManager em, EntityTransaction trx) {
		final EntityManagerFactory emf = mock(EntityManagerFactory.class);

		given(emf.createEntityManager()).willReturn(em);
		given(em.isOpen()).willReturn(true);
		given(em.getTransaction()).willReturn(trx);
		given(trx.isActive()).willReturn(true);

		return Guice.createInjector(new PersistenceModule(), new AbstractModule() {
			@Override
			protected void configure() {
				bind(Logger.class).toInstance(LoggerFactory.getLogger(PersistenceModuleTest.class));
				bind(EntityManagerFactory.class).toInstance(emf);
			}
		});
	}

}
