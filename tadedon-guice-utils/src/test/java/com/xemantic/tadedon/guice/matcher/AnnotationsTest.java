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

import java.lang.annotation.Documented;

import org.junit.Test;

/**
 * Unit test for {@link Annotations}.
 * <p>
 * Created on Aug 10, 2010
 *
 * @author hshsce
 */
public class AnnotationsTest {

	/** Tests {@link AnnotationMatchers#checkForRuntimeRetention(Class)}. */
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionDueToMissingRuntimeRetention() {
		Annotations.checkForRuntimeRetention(Override.class);
	}

	/** Tests {@link AnnotationMatchers#checkForRuntimeRetention(Class)}. */
	@Test
	public void shouldVerifyRuntimeRetentionOnGivenAnnotation() {
		Annotations.checkForRuntimeRetention(Documented.class);
	}

}
