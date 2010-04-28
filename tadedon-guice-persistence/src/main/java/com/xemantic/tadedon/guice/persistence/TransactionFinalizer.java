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
 * Service for finalizing transaction.
 * <p>
 * Created on Apr 27, 2010
 *
 * @author hshsce
 */
@ImplementedBy(DefaultTransactionFinalizer.class)
public interface TransactionFinalizer {

	/**
	 * Finalizes transaction according to information in given {@code context}.
	 *
	 * @param context the transaction context.
	 */
	void finalize(TransactionContext context);

}
