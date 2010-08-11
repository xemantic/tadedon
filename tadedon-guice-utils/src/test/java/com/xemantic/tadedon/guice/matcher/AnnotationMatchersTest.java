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

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.annotation.security.RolesAllowed;

import org.junit.Test;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

/**
 * Unit test for {@link AnnotationMatchers} util class.
 * <p>
 * Created on Feb 25, 2010
 *
 * @author hshsce
 */
public class AnnotationMatchersTest {

	/** Tests standard {@link Matchers} for comparison. */
	@Test
	public void shouldMatchClassAnnotationInCaseOfStandardMatchers() {
		//given
		Matcher<AnnotatedElement> matcher = Matchers.annotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(FooService.class);
		//then
		assertTrue("should match annotation", matches);
	}

	/** Tests standard {@link Matchers} for comparison. */
	@Test
	public void shouldNotMatchSuperClassAnnotationInCaseOfStandardMatchers() {
		//given
		Matcher<AnnotatedElement> matcher = Matchers.annotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(DefaultFooService.class);
		//then
		assertFalse("should not match annotation", matches);
	}

	/** Tests {@link AnnotationMatchers}. */
	@Test
	public void shouldMatchClassAnnotationByType() {
		//given
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(FooService.class);
		//then
		assertTrue("should match annotation", matches);
	}

	/** Tests {@link AnnotationMatchers}. */
	@Test
	public void shouldMatchSuperClassAnnotationByType() {
		//given
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(DefaultFooService.class);
		//then
		assertTrue("should match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldMatchMethodAnnotationByType() throws SecurityException, NoSuchMethodException {
		//given
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(FooService.class.getMethod("doSomething"));
		//then
		assertTrue("should match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldMatchSuperMethodAnnotationByType() throws SecurityException, NoSuchMethodException {
		//given
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(FooService.class.getMethod("doSomething"));
		//then
		assertTrue("should match annotation", matches);
	}

	/** Tests {@link AnnotationMatchers}. */
	@Test
	public void shouldMatchClassAnnotation() {
		//given
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(FooService.class.getAnnotation(RolesAllowed.class));
		//when
		boolean matches = matcher.matches(FooService.class);
		//then
		assertTrue("should match annotation", matches);
	}

	/** Tests {@link AnnotationMatchers}. */
	@Test
	public void shouldMatchSuperClassAnnotation() {
		//given
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(FooService.class.getAnnotation(RolesAllowed.class));
		//when
		boolean matches = matcher.matches(DefaultFooService.class);
		//then
		assertTrue("should match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldNotMatchClassAnnotationWhenDifferentAnnotationInstanceIsProvided() throws SecurityException, NoSuchMethodException {
		//given
		Method method = FooService.class.getMethod("doSomething");
		RolesAllowed methodAnnotation = method.getAnnotation(RolesAllowed.class);
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(methodAnnotation);
		//when
		boolean matches = matcher.matches(DefaultFooService.class);
		//then
		assertFalse("should not match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldMatchMethodAnnotation() throws SecurityException, NoSuchMethodException {
		//given
		Method method = FooService.class.getMethod("doSomething");
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(method.getAnnotation(RolesAllowed.class));
		//when
		boolean matches = matcher.matches(method);
		//then
		assertTrue("should match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldMatchSuperMethodAnnotation() throws SecurityException, NoSuchMethodException {
		//given
		Method method = DefaultFooService.class.getMethod("doSomething");
		Method superMethod = FooService.class.getMethod("doSomething");
		RolesAllowed superAnnotation = superMethod.getAnnotation(RolesAllowed.class);
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(superAnnotation);
		//when
		boolean matches = matcher.matches(method);
		//then
		assertTrue("should match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers#superAnnotatedWith(java.lang.annotation.Annotation)}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldNotMatchMethodAnnotationWhenDifferentAnnotationInstanceIsProvided() throws SecurityException, NoSuchMethodException {
		//given
		Method method = FooService.class.getMethod("doSomething");
		RolesAllowed classAnnotation = FooService.class.getAnnotation(RolesAllowed.class);
		Matcher<AnnotatedElement> matcher = AnnotationMatchers.superAnnotatedWith(classAnnotation);
		//when
		boolean matches = matcher.matches(method);
		//then
		assertFalse("should not match annotation", matches);
	}

	/** Tests {@link AnnotationMatchers#type}. */
	@Test
	public void shouldMatchTypeLiteralAnnotationByType() {
		//given
		Matcher<TypeLiteral<?>> matcher = AnnotationMatchers.typeAnnotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(new TypeLiteral<FooService>() {});
		//then
		assertTrue("should match annotation", matches);
	}

	/** Tests {@link AnnotationMatchers}. */
	@Test
	public void shouldMatchSuperTypeLiteralAnnotationByType() {
		//given
		Matcher<TypeLiteral<?>> matcher = AnnotationMatchers.typeAnnotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(new TypeLiteral<DefaultFooService>() {});
		//then
		assertTrue("should match annotation", matches);
	}

	/** Tests {@link AnnotationMatchers}. */
	@Test
	public void shouldMatchTypeLiteralAnnotation() {
		//given
		Matcher<TypeLiteral<?>> matcher = AnnotationMatchers.typeAnnotatedWith(FooService.class.getAnnotation(RolesAllowed.class));
		//when
		boolean matches = matcher.matches(new TypeLiteral<FooService>() {});
		//then
		assertTrue("should match annotation", matches);
	}

	/** Tests {@link AnnotationMatchers}. */
	@Test
	public void shouldMatchSuperTypeLiteralAnnotation() {
		//given
		Matcher<TypeLiteral<?>> matcher = AnnotationMatchers.typeAnnotatedWith(FooService.class.getAnnotation(RolesAllowed.class));
		//when
		boolean matches = matcher.matches(new TypeLiteral<DefaultFooService>() {});
		//then
		assertTrue("should match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldMatchTypeLiteralMethodAnnotationByType() throws SecurityException, NoSuchMethodException {
		//given
		Matcher<TypeLiteral<?>> matcher = AnnotationMatchers.methodAnnotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(new TypeLiteral<FooService>() {});
		//then
		assertTrue("should match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldMatchSuperTypeLiteralMethodAnnotationByType() throws SecurityException, NoSuchMethodException {
		//given
		Matcher<TypeLiteral<?>> matcher = AnnotationMatchers.methodAnnotatedWith(RolesAllowed.class);
		//when
		boolean matches = matcher.matches(new TypeLiteral<DefaultFooService>() {});
		//then
		assertTrue("should match annotation", matches);
	}

	/**
	 * Tests {@link AnnotationMatchers}.
	 *
	 * @throws SecurityException should never happen.
	 * @throws NoSuchMethodException should never happen.
	 */
	@Test
	public void shouldNotMatchTypeLiteralMethodAnnotationWhenDifferentAnnotationInstanceIsProvided() throws SecurityException, NoSuchMethodException {
		//given
		Method method = AnnotationMatchersTest.class.getMethod("rolesAllowedAnnotatedMethod");
		Annotation methodAnnotation = method.getAnnotation(RolesAllowed.class);
		Matcher<TypeLiteral<?>> matcher = AnnotationMatchers.methodAnnotatedWith(methodAnnotation);
		//when
		boolean matches = matcher.matches(new TypeLiteral<DefaultFooService>() {});
		//then
		assertFalse("should not match annotation", matches);
	}

	@RolesAllowed("different_role")
	public void rolesAllowedAnnotatedMethod() { /* utility method used by test reflection to obtain annotation instance */ }

}
