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
package com.xemantic.tadedon.guice.lifecycle.jsr250;

import static com.xemantic.tadedon.guice.matcher.AnnotationMatchers.*;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.google.inject.AbstractModule;
import com.google.inject.PrivateBinder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.xemantic.tadedon.guice.lifecycle.LifecycleManager;

/**
 * 
 * <p>
 * Created on Aug 9, 2010
 *
 * @author hshsce
 */
public class Jsr250LifecycleModule extends AbstractModule {

	/** {@inheritDoc} */
	@Override
	protected void configure() {
		PrivateBinder privBinder = binder().newPrivateBinder();

		PostConstructInvoker postConstructInvoker = new PostConstructInvoker();
		privBinder.bind(PostConstructInvoker.class).toInstance(postConstructInvoker);

		LinkedBlockingDeque<Object> destroyableObjects = new LinkedBlockingDeque<Object>();
		privBinder.bind(new TypeLiteral<Deque<Object>>() {})
					.annotatedWith(Names.named("destroyableObjects"))
					.toInstance(destroyableObjects);


		PostConstructAnnotatedTypeListener postConstructListener = new PostConstructAnnotatedTypeListener(postConstructInvoker);
		PreDestroyAnnotatedTypeListener preDestroyListener = new PreDestroyAnnotatedTypeListener(destroyableObjects);

		privBinder.bind(LifecycleManager.class).to(Jsr250LifecycleManager.class);

		bindListener(methodAnnotatedWith(PostConstruct.class), postConstructListener);
		bindListener(methodAnnotatedWith(PreDestroy.class), preDestroyListener);

		privBinder.expose(LifecycleManager.class);
	}

}
