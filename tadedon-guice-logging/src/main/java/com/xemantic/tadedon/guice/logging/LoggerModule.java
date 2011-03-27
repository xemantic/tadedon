/*
 * Copyright 2010-2011 Xemantic
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
package com.xemantic.tadedon.guice.logging;

import static com.google.inject.matcher.Matchers.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

/**
 * Logger module.
 * <p>
 * Created on Aug 16, 2010
 *
 * @author morisil
 */
public class LoggerModule extends AbstractModule {

    /** {@inheritDoc} */
    @Override
    protected void configure() {
        // temporary logger injected in constructor
        bind(Logger.class).toInstance(
                LoggerFactory.getLogger(LoggerModule.class));
        bindListener(any(), new LoggerTypeListener());
    }

}
