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
package com.xemantic.tadedon.gwt.rpc.mock;

import static com.google.inject.matcher.Matchers.*;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.inject.AbstractModule;
import com.google.inject.servlet.GuiceFilter;
import com.xemantic.tadedon.guice.servlet.mock.FakeServletContainer;

/**
 * Module for unit testing GWT RPC services.
 * <p>
 * Note: it requires bindings provided by
 * {@link com.xemantic.tadedon.guice.servlet.mockFakeServletContainerModule} module.
 * <p>
 * Created on Aug 21, 2010
 *
 * @author hshsce
 */
public class RemoteServiceInterceptionModule extends AbstractModule {

	/** {@inheritDoc} */
	@Override
	protected void configure() {
	    requireBinding(GuiceFilter.class);
	    requireBinding(FakeServletContainer.class);
		RemoteServiceMethodInterceptor interceptor = new RemoteServiceMethodInterceptor();
		requestInjection(interceptor);
		bindInterceptor(subclassesOf(RemoteService.class), any(), interceptor);
	}

}
