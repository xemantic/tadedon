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

import static com.google.inject.internal.Preconditions.*;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import com.google.inject.matcher.AbstractMatcher;

/**
 *
 * <p>
 * Created on Feb 25, 2010
 *
 * @author hshsce
 */
public class AnnotationTypeMatcher extends AbstractMatcher<AnnotatedElement> implements Serializable {

	private static final long serialVersionUID = 6191937886210143389L;

	private final Class<? extends Annotation> m_annotationType;

	/**
	 * Creates annotation matcher.
	 *
	 * @param annotationType the annotation type to match against.
	 */
	public AnnotationTypeMatcher(final Class<? extends Annotation> annotationType) {
		m_annotationType = checkNotNull(annotationType, "annotation type");
	}

	/** {@inheritDoc} */
	@Override
	public boolean matches(AnnotatedElement element) {
		return (AnnotationMatchers.findAnnotation(element, m_annotationType) != null);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object other) {
		return ((other instanceof AnnotationTypeMatcher)
				&& ((AnnotationTypeMatcher) other).m_annotationType.equals(m_annotationType));
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return 37 * m_annotationType.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "superAnnotatedWith(" + m_annotationType + ".class)";
	}

}
