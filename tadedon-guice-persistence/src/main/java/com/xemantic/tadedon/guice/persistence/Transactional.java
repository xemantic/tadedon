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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Transactional annotation. Methods annotated {@literal @Transactional}
 * will be executed within new transaction (see {@link Transaction}).
 * <p>
 * Created on Mar 25, 2010
 *
 * @author hshsce
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Inherited
@Documented
public @interface Transactional {

	/**
	 * Indicates that no <em>commit()</em> but only <em>rollback()</em> operation
	 * should be performed at the end of transaction handling.
	 *
	 * @return {@code true} if transaction should not be committed, {@code false} otherwise.
	 * @see TransactionFinalizer
	 * @see PersistenceUtils#isTransactionReadOnly(java.lang.reflect.Method)
	 */
	boolean readOnly() default false;

}
