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

import javax.persistence.EntityManager;

/**
 * Transaction initiated on method annotated {@literal @}{@link Transactional}.
 * <p>
 * Created on Apr 26, 2010
 *
 * @author hshsce
 */
public interface Transaction {

	/**
	 * Returns transaction id.
	 *
	 * @return the id.
	 */
	String getId();

	/**
	 * Returns entity associated with this transaction.
	 *
	 * @return the entity manager.
	 */
	EntityManager getEntityManager();

	/**
	 * Returns connection associated with this transaction.
	 *
	 * @return the connection.
	 */
	Connection getConnection();

	/**
	 * Request the transaction to be rolled back just after invocation of the method
	 * annotated {@literal @}{@link Transactional} which initiated this transaction.
	 */
	void requestRollback();

	/**
	 * Indicates whether {@link Transaction#requestRollback()} was called.
	 *
	 * @return {@code true} if this transaction should be rolled back, {@code false} otherwise.
	 */
	boolean isRollbackRequested();

}
