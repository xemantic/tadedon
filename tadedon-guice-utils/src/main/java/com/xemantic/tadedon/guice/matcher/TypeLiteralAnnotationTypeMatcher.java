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

import org.springframework.core.annotation.AnnotationUtils;

import com.google.inject.TypeLiteral;

/**
 * 
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 *
 */
public class TypeLiteralAnnotationTypeMatcher extends AbstractAnnotationTypeMatcher<TypeLiteral<?>> implements Serializable {

	private static final long serialVersionUID = -63277540228271408L;


	/**
	 * Creates annotation matcher.
	 *
	 * @param annotation the annotation to match against.
	 */
	public TypeLiteralAnnotationTypeMatcher(Class<? extends Annotation> annotationType) {
		super(annotationType);
	}

	/** {@inheritDoc} */
	@Override
	public boolean matches(TypeLiteral<?> element) {
		return (AnnotationUtils.findAnnotation(element.getRawType(), m_annotationType) != null);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "typeAnnotatedWith(" + m_annotationType + ".class)";
	}

}
