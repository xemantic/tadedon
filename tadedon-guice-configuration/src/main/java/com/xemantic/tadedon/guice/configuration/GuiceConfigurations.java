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

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.xemantic.tadedon.configuration.Configurations;

/**
 * Utilities for binding commons-configuration artifacts.
 * <p>
 * Created on Aug 16, 2010
 *
 * @author hshsce
 */
public final class GuiceConfigurations {

    private GuiceConfigurations() { /* util class, non-instantiable */ }


    public static void bindConfiguration(Binder binder, File confDir) {
        checkArgument(confDir.isDirectory(), "confDir must be directory: %s", confDir);
        for (File file : confDir.listFiles()) {
            if (file.isFile()) {
                bindConfiguration(binder, file.getName(), Configurations.getConfiguration(file));
            }
        }
    }

    public static void bindConfiguration(Binder binder, String confName, Configuration configuration) {
        int dotIndex = confName.lastIndexOf(".");
        String confKey = confName.substring(0, dotIndex);
        binder.bind(Configuration.class).annotatedWith(Names.named(confKey)).toInstance(configuration);
        if (configuration instanceof PropertiesConfiguration) {
            binder.bind(PropertiesConfiguration.class).annotatedWith(Names.named(confKey)).toInstance((PropertiesConfiguration) configuration);
            Properties properties = Configurations.toProperties(configuration);
            binder.bind(Properties.class).annotatedWith(Names.named(confKey)).toInstance(properties);
            binder.bind(new TypeLiteral<Map<String, String>>() {}).annotatedWith(Names.named(confKey)).toInstance(Maps.fromProperties(properties));
        } else if (configuration instanceof XMLConfiguration) {
            binder.bind(XMLConfiguration.class).annotatedWith(Names.named(confKey)).toInstance((XMLConfiguration) configuration);
        }        
    }

    public static void bindProperties(Binder binder, File confDir, String confName) {
        bindProperties(binder, Configurations.getConfiguration(confDir, confName));
    }

    public static void bindProperties(Binder binder, Configuration configuration) {
        for (@SuppressWarnings("unchecked") Iterator<String> iterator = configuration.getKeys(); iterator.hasNext();) {
            String key = iterator.next();
            String value = (String) configuration.getProperty(key);
            binder.bindConstant().annotatedWith(Names.named(key)).to(value);
        }
    }

}
