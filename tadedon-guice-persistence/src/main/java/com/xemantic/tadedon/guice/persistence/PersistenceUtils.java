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

import static com.google.common.base.Preconditions.*;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

/**
 * Persistence utilities.
 * <p>
 * Created on Apr 27, 2010
 *
 * @author hshsce
 */
public final class PersistenceUtils {

	private PersistenceUtils() { /* util class non-instantiable */ }

	/**
	 * Checks {@link Transactional#readOnly()} property for given {@code method}.
	 * <p>
	 * Algorithm starts with method annotation checking also overridden methods
	 * from supertypes. If no annotation is found on the {@code method}, annotation
	 * of declaring class and it's supertypes will be used. Absence of any
	 * {@code Transactional} annotation will cause {@link IllegalArgumentException}.
	 *
	 * @param method the method starting transaction.
	 * @return {@code true} if method should be run in read-only transaction, {@code false}
	 * 			otherwise.
	 * @throws IllegalArgumentException if no {@link Transactional} annotation is found
	 * 			either on the {@code method}}, or on declaring class.
	 */
	public static boolean isTransactionReadOnly(Method method) {
		final Transactional methodTransactional = AnnotationUtils.findAnnotation(method, Transactional.class);
		final boolean readOnly;
		if (methodTransactional != null) {
			readOnly = methodTransactional.readOnly();
		} else {
			final Class<?> klass = method.getDeclaringClass();
			final Transactional classTransactional = klass.getAnnotation(Transactional.class);
			checkNotNull(classTransactional, "Either method or declaring class should be annotated @Transacetional, method: %s", method);
			readOnly = classTransactional.readOnly();
		}
		return readOnly;
	}

}
