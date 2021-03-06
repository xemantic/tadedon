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
package com.xemantic.tadedon.guice.matcher;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

/**
 * Annotation utilities.
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 */
public final class Annotations {

	private Annotations() { /* util class, non-instantiable */ }

	/**
	 * Finds if the {@code element} is annotated with the {@code annotationType}.
	 * If the {@code element} is instance of {@link Class} or {@link Method},
	 * the {@link AnnotationUtils#findAnnotation(Class, Class)} or
	 * {@link AnnotationUtils#findAnnotation(Method, Class)} method will be used respectively. Thus it can
	 * also find annotations from extended classes and implemented interfaces.
	 *
	 * @param element the element
	 * @param annotationType the annotation type.
	 * @return annotation instance or {@code null} if no annotation is found.
	 * @throws IllegalArgumentException if the {@code element} is not instance of {@link Class} or {@link Method}.
	 */
	public static Annotation findAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {
		final Annotation annotation;
		if (element instanceof Class<?>) {
			annotation = AnnotationUtils.findAnnotation((Class<?>) element, annotationType);
		} else if (element instanceof Method) {
			annotation = AnnotationUtils.findAnnotation((Method) element, annotationType);
		} else {
			annotation = element.getAnnotation(annotationType);
		}
		return annotation;
	}

	/**
	 * Check if given {@code annotationType} has {@link Retention} set to {@link RetentionPolicy#RUNTIME}.
	 *
	 * @param annotationType the annotation type to check.
	 */
	public static void checkForRuntimeRetention(Class<? extends Annotation> annotationType) {
		Retention retention = annotationType.getAnnotation(Retention.class);
		checkArgument(
				((retention != null) && (retention.value() == RetentionPolicy.RUNTIME)),
				"Annotation %s is missing RUNTIME retention", annotationType.getSimpleName());
	}

}
