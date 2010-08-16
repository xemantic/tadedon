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
package com.xemantic.tadedon.guice.configuration;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Unit test for {@link GuiceConfigurations}.
 * <p>
 * Created on Aug 17, 2010
 *
 * @author hshsce
 */
public class GuiceConfigurationsTest {
 
    @Test
    public void shouldBindProperties() throws Exception {
        // given 
        Injector injector = Guice.createInjector(new AbstractModule() {
            /** {@inheritDoc} */
            @Override
            protected void configure() {
                GuiceConfigurations.bindProperties(binder(), new File("src/test/data"), "conf1.properties");
            }
        });

        // when
        PropertyInjectedComponent component = injector.getInstance(PropertyInjectedComponent.class);

        // then
        assertThat(component.getFoo(), is("foo"));
    }

    @Test
    public void shouldBindConfigurations() throws Exception {
        // given 
        Injector injector = Guice.createInjector(new AbstractModule() {
            /** {@inheritDoc} */
            @Override
            protected void configure() {
                GuiceConfigurations.bindConfiguration(binder(), new File("src/test/data"));
            }
        });

        // when
        ConfigurableComponent component = injector.getInstance(ConfigurableComponent.class);

        // then
        assertThat(component.getConf1Map().size(), is(1));
        assertThat(component.getConf1Map().get("foo"), is("foo"));
        assertThat(component.getConf1Properties().size(), is(1));
        assertThat(component.getConf1Properties().getProperty("foo"), is("foo"));
        assertThat(component.getConf1Configuration().getString("foo"), is("foo"));
        assertThat(component.getConf2Configuration().getString("bar"), is("bar"));
        assertThat(component.getConf2XmlConfiguration().getString("bar"), is("bar"));
    }

}
