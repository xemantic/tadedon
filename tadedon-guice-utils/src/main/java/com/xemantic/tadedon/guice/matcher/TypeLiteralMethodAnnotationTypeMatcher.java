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
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

import com.google.inject.TypeLiteral;

/**
 * Matcher w
 * <p>
 * Created on Aug 10, 2010
 *
 * @author hshsce
 */
public class TypeLiteralMethodAnnotationTypeMatcher extends AbstractAnnotationTypeMatcher<TypeLiteral<?>> implements Serializable {

	private static final long serialVersionUID = -3338703060117277662L;


	/**
	 * Creates annotation matcher.
	 *
	 * @param annotationType the annotation type to match against.
	 */
	public TypeLiteralMethodAnnotationTypeMatcher(Class<? extends Annotation> annotationType) {
		super(annotationType);
	}

	/** {@inheritDoc} */
	@Override
	public boolean matches(TypeLiteral<?> element) {
		Method[] methods = element.getRawType().getMethods();
		boolean matches = false;
		for (Method method : methods) {
			Annotation annotation = AnnotationUtils.findAnnotation(method, m_annotationType);
			if (annotation != null) {
				matches = true;
				break;
			}
		}
		return matches;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "methodAnnotatedWith(" + m_annotationType + ".class)";
	}

}
