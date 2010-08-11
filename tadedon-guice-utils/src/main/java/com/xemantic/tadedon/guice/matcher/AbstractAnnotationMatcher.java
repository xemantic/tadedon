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

import java.lang.annotation.Annotation;

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

/**
 * Base class for annotation {@link Matcher}s.
 * <p>
 * Created on Aug 10, 2010
 *
 * @author hshsce
 */
public abstract class AbstractAnnotationMatcher<T> extends AbstractMatcher<T> {

	protected final Annotation m_annotation;


	protected AbstractAnnotationMatcher(Annotation annotation) {
		m_annotation = checkNotNull(annotation, "annotation");
		Annotations.checkForRuntimeRetention(m_annotation.annotationType());
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return 37 * m_annotation.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object other) {
		return ((other.getClass().equals(getClass()))
				&& ((AbstractAnnotationMatcher<?>) other).m_annotation.equals(m_annotation));
	}

}
