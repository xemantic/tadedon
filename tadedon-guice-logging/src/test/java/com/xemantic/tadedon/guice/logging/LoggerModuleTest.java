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

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.impl.StaticLoggerBinder;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Test for {@link LoggerModule}.
 * <p>
 * Created on Aug 16, 2010
 *
 * @author morisil
 */
public class LoggerModuleTest {

    @Test
    public void shouldInjectLogger() throws InterruptedException {
        // given
        final Logger defaultLogger = mock(Logger.class);
        final Logger serviceLogger = mock(Logger.class);
        StaticLoggerBinder.setLoggerFactory(new ILoggerFactory() {
            @Override
            public Logger getLogger(String name) {
                if (LoggingService.class.getName().equals(name)) {
                    return serviceLogger;
                }
                return defaultLogger;
            }
        });
        Injector injector = Guice.createInjector(new LoggerModule());
        LoggingService loggingService = injector.getInstance(LoggingService.class);

        // when
        loggingService.logInfoMessage();

        // then
        verifyZeroInteractions(defaultLogger);
        verify(serviceLogger).info("logging info message");
    }

    public static class LoggingService {

        private final Logger logger;

        @Inject
        public LoggingService(Logger logger) {
            this.logger = logger;
        }

        public void logInfoMessage() {
            logger.info("logging info message");
        }

    }

}
