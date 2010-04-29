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

import com.google.inject.ImplementedBy;

/**
 * Thread local transaction manager.
 * <p>
 * Created on Apr 29, 2010
 *
 * @author hshsce
 */
@ImplementedBy(DefaultTransactionManager.class)
public interface TransactionManager {

	/**
	 * Returns transaction associated with this thread.
	 *
	 * @return the transaction or {@code null} if no transaction associated with this thread.
	 */
	Transaction getLocalTransaction();

	/**
	 * Creates new transaction associated with this thread.
	 *
	 * @return the new transaction.
	 */
	Transaction newLocalTransaciton();

	/**
	 * Closes transaction associated with current thread.
	 */
	void closeLocalTransaction();

}
