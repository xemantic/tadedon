/*
 * Copyright 2011 Xemantic
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

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.ProvisionException;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * Type listener for all the types
 * where slf4j {@code Logger} should be injected.
 * <p>
 * Created on Mar 25, 2011
 *
 * @author morisil
 */
@Singleton
public class LoggerTypeListener implements TypeListener {

    @Override
    public <I> void hear(
            final TypeLiteral<I> type,
            TypeEncounter<I> encounter) {

        Field[] fields = type.getRawType().getDeclaredFields();
        for (final Field field : fields) {
            Class<?> fieldType = field.getType();
            if (Logger.class.isAssignableFrom(fieldType)) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        Class<?> rawType = type.getRawType();
                        Logger logger = LoggerFactory.getLogger(rawType);
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        try {
                            field.set(injectee, logger);
                        } catch (IllegalAccessException e) {
                            throw new ProvisionException(
                                    "Cannot inject logger", e);
                        }
                    }
                });
            }
        }
    }

}
