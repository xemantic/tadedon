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
package com.xemantic.tadedon.guice.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * 
 * <p>
 * Created on Aug 16, 2010
 *
 * @author hshsce
 */
@Singleton
public class LoggerProvider implements Provider<Logger> {

    private final Logger m_logger = LoggerFactory.getLogger(LoggerProvider.class);

    /** {@inheritDoc} */
    public Logger get() {
        return m_logger;
    }

}
