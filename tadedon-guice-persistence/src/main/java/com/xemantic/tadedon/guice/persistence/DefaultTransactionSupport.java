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

import com.google.inject.Singleton;

/**
 * Default transaction support.
 * It will decide on rolling back transaction
 * (instead of committing it) if the {@link Transactional#readOnly()} on
 * called method was set to {@code true} or any {@link Throwable} was thrown
 * during transaction execution.
 * <p>
 * Created on May 8, 2010
 *
 * @author hshsce
 */
@Singleton
public class DefaultTransactionSupport implements TransactionSupport {

	/** {@inheritDoc} */
	@Override
	public boolean shouldCommit(TransactionContext context) {
		Method method = context.getMethodInvocation().getMethod();
		return ((!PersistenceUtils.isTransactionReadOnly(method)) &&
				(context.getThrowable() == null));
	}

}
