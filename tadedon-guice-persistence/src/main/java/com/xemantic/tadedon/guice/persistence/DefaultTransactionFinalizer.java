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

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.google.inject.Singleton;

/**
 * Default transaction finalizer.
 * <p>
 * Created on Apr 27, 2010
 *
 * @author hshsce
 */
@Singleton
public class DefaultTransactionFinalizer implements TransactionFinalizer {

	/** {@inheritDoc} */
	@Override
	public void finalize(TransactionContext context) {
		EntityManager em = context.getTransaction().getEntityManager();
		EntityTransaction emTrx = em.getTransaction();
		Method method = context.getMethodInvocation().getMethod();
		if (emTrx.isActive()) {
			if ((context.getThrowable() != null) ||
					(PersistenceUtils.isTransactionReadOnly(method)) ||
					context.getTransaction().isRollbackRequested()) {

				emTrx.rollback();
			} else {
				emTrx.commit();
			}
		}
	}

}
