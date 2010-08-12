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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 *
 * <p>
 * Created on Feb 25, 2010
 *
 * @author hshsce
 */
public class AnnotationTypeMatcher extends AbstractAnnotationTypeMatcher<AnnotatedElement> implements Serializable {

	private static final long serialVersionUID = -5607162731470802707L;


	/**
	 * Creates annotation matcher.
	 *
	 * @param annotationType the annotation type to match against.
	 */
	public AnnotationTypeMatcher(Class<? extends Annotation> annotationType) {
		super(annotationType);
	}

	/** {@inheritDoc} */
	@Override
	public boolean matches(AnnotatedElement element) {
		return (Annotations.findAnnotation(element, m_annotationType) != null);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "superAnnotatedWith(" + m_annotationType + ".class)";
	}

}
