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

/**
 * Service which steers transaction lifecycle.
 * <p>
 * Created on May 8, 2010
 *
 * @author hshsce
 */
public interface TransactionSupport {

	/**
	 * Decides whether transaction should be committed or
	 * rolled back based on given {@code context}. When this
	 * method returns {@code false}, it will always cause transaction
	 * rollback.
	 *
	 * @param context the context of transaction.
	 * @return {@code true} if transaction should commit, {@code false} otherwise.
	 */
	boolean shouldCommit(TransactionContext context);

}
