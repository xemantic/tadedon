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

import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * 
 * <p>
 * Created on Aug 17, 2010
 *
 * @author hshsce
 */
@Singleton
public class ConfigurableComponent {

    private final Map<String, String> m_conf1Map;

    private final Properties m_conf1Properties;

    private final Configuration m_conf1Configuration;

    private final Configuration m_conf2Configuration;

    private final XMLConfiguration m_conf2XmlConfiguration;


    @Inject
    public ConfigurableComponent(
            @Named("conf1Config") Map<String, String> conf1Map,
            @Named("conf1Config") Properties conf1Properties,
            @Named("conf1Config") Configuration conf1Configuration,
            @Named("conf2Config") Configuration conf2Configuration,
            @Named("conf2Config") XMLConfiguration conf2XmlConfiguration) {

        m_conf1Map = conf1Map;
        m_conf1Properties = conf1Properties;
        m_conf1Configuration = conf1Configuration;
        m_conf2Configuration = conf2Configuration;
        m_conf2XmlConfiguration = conf2XmlConfiguration;
    }

    /**
     * @return the conf1Map
     */
    public Map<String, String> getConf1Map() {
        return m_conf1Map;
    }

    /**
     * @return the conf1Properties
     */
    public Properties getConf1Properties() {
        return m_conf1Properties;
    }

    /**
     * @return the conf1Configuration
     */
    public Configuration getConf1Configuration() {
        return m_conf1Configuration;
    }

    /**
     * @return the conf2Configuration
     */
    public Configuration getConf2Configuration() {
        return m_conf2Configuration;
    }

    /**
     * @return the conf2XmlConfiguration
     */
    public XMLConfiguration getConf2XmlConfiguration() {
        return m_conf2XmlConfiguration;
    }

}
