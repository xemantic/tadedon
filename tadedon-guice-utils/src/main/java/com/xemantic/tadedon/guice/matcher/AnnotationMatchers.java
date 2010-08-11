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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;

/**
 *
 * <p>
 * Created on Feb 25, 2010
 *
 * @author hshsce
 */
public final class AnnotationMatchers {

	private AnnotationMatchers() { /* util class non-instantiable */ }

	/**
	 * Returns a matcher which matches elements (methods, classes, etc.) and their
	 * super declarations (extended classes, implemented interfaces, overridden methods) with a
	 * given annotation.
	 *
	 * @param annotationType the annotation type.
	 * @return the matcher.
	 */
	public static Matcher<AnnotatedElement> superAnnotatedWith(Class<? extends Annotation> annotationType) {
		return new AnnotationTypeMatcher(annotationType);
	}

	/**
	 * Returns a matcher which matches elements (methods, classes, etc.) and their
	 * super declarations (extended classes, implemented interfaces, overridden methods) with a
	 * given annotation.
	 *
	 * @param annotation the annotation instance.
	 * @return the matcher.
	 */
	public static Matcher<AnnotatedElement> superAnnotatedWith(Annotation annotation) {
		return new AnnotationMatcher(annotation);
	}

	/**
	 * Returns a matcher which matches {@link TypeLiteral}'s raw type
	 * with a given {@code annotationType}.
	 *
	 * @param annotation the annotation instance.
	 * @return the matcher.
	 */
	public static Matcher<TypeLiteral<?>> typeAnnotatedWith(Class<? extends Annotation> annotationType) {
		return new TypeLiteralAnnotationTypeMatcher(annotationType);
	}

	/**
	 * Returns a matcher which matches {@link TypeLiteral} raw type's methods
	 * with a given {@code annotationType}.
	 *
	 * @param annotation the annotation instance.
	 * @return the matcher.
	 */
	public static Matcher<TypeLiteral<?>> typeAnnotatedWith(Annotation annotation) {
		return new TypeLiteralAnnotationMatcher(annotation);
	}

	/**
	 * Returns a matcher which matches {@link TypeLiteral}'s raw type methods
	 * with a given {@code annotation}.
	 *
	 * @param annotation the annotation instance.
	 * @return the matcher.
	 */
	public static Matcher<TypeLiteral<?>> methodAnnotatedWith(Class<? extends Annotation> annotationType) {
		return new TypeLiteralMethodAnnotationTypeMatcher(annotationType);
	}

	/**
	 * Returns a matcher which matches {@link TypeLiteral}'s raw type methods
	 * with a given {@code annotation}.
	 *
	 * @param annotation the annotation instance.
	 * @return the matcher.
	 */
	public static Matcher<TypeLiteral<?>> methodAnnotatedWith(Annotation annotation) {
		return new TypeLiteralMethodAnnotationMatcher(annotation);
	}

}
