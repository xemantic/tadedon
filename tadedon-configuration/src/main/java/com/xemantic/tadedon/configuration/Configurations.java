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
package com.xemantic.tadedon.configuration;

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import com.xemantic.tadedon.guava.base.MoreResources;

/**
 * Utilities supporting commons-configuration library.
 * <p>
 * Created on Aug 15, 2010
 *
 * @author hshsce
 */
public final class Configurations {

    public static final String DEFAULT_CONFIGURATION_PATH = "META-INF/default-configuration";

    public static final String DEFAULT_CONFIGURATION_LIST = DEFAULT_CONFIGURATION_PATH + "/configuration.list";

    private static final Logger LOG = LoggerFactory.getLogger(Configurations.class);


    private Configurations() { /* util class, non-instantiable */ }

    /**
     * Returns configuration for given {@code confDir} and {@code confName}.
     *
     * @param confDir the configuration directory.
     * @param confName the configuration file name.
     * @return the configuration.
     * @throws ConfigurationException 
     */
    public static FileConfiguration getConfiguration(File confDir, String confName) {
        return getConfiguration(new File(confDir, confName));
    }

    public static FileConfiguration getConfiguration(File confFile) {
        String confName = confFile.getName();
        FileConfiguration conf;
        if (confName.endsWith(".properties")) {
            conf = newPropertiesConfiguration(confFile);
        } else if (confName.endsWith(".xml")) {
            conf = newXmlConfiguration(confFile);            
        } else {
            throw new IllegalArgumentException(
                    "Unknown configuration file: " + confName + " it should have either " +
                    ".properties or .xml extension");
        }
        return conf;
    }

    public static XMLConfiguration newXmlConfiguration(File confFile) {
        checkArgument(confFile.exists(), "confFile %s does not exist", confFile);
        try {
            return new XMLConfiguration(confFile);
        } catch (ConfigurationException e) {
            throw new RuntimeException("Cannot create configuration", e);
        }
    }

    public static PropertiesConfiguration newPropertiesConfiguration(File confFile) {
        checkArgument(confFile.exists(), "confFile %s does not exist", confFile);
        try {
            return new PropertiesConfiguration(confFile);
        } catch (ConfigurationException e) {
            throw new RuntimeException("Cannot create configuration", e);
        }
    }

    public static PropertiesConfiguration newPropertiesConfiguration(URL confUrl) {
        try {
            return new PropertiesConfiguration(confUrl);
        } catch (ConfigurationException e) {
            throw new RuntimeException("Cannot create configuration", e);
        }
    }

    public static Properties toProperties(Configuration configuraton) {
        Properties properties = new Properties();
        for (@SuppressWarnings("unchecked") Iterator<String> iterator = configuraton.getKeys(); iterator.hasNext();) {
            String key = iterator.next();
            Object objValue = configuraton.getProperty(key);
            String value;
            if (objValue instanceof String) {
                value = (String) objValue;
            } else {
                value = objValue.toString();
            }
            properties.setProperty(key, value);
        }
        return properties;
    }

    public static void mergeAllDefaults(File confDir) {
        LOG.info("Merging default configurations from classpath with configuraiton dir: {}", confDir);
        Set<String> defaultConfigurations = getDefaultConfigurations();
        LOG.debug("Default configuration sources: {}", defaultConfigurations);
        for (String confName : defaultConfigurations) {
            mergeDefaults(confDir, confName);
        }
    }

    public static void mergeDefaults(File confDir, String confName) {
        URL url = getDefaultConfigurationUrl(confName);
        File confFile = new File(confDir, confName);
        if (confFile.exists()) {
            merge(url, confFile);
        } else {
            MoreResources.copyRisky(url, confFile);                
        }        
    }

    public static URL getDefaultConfigurationUrl(String configName) {
        return Resources.getResource(DEFAULT_CONFIGURATION_PATH + "/" + configName);
    }

    public static Set<String> getDefaultConfigurations() {
        ImmutableSet.Builder<String> builder = ImmutableSet.<String>builder();
        List<URL> configurationLists = MoreResources.getResources(DEFAULT_CONFIGURATION_LIST);
        for (URL url : configurationLists) {
            builder.addAll(getLines(url));
        }
        return builder.build();
    }

    public static List<String> getLines(URL url) {
        try {
            return Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read lines from url: " + url, e);
        }        
    }

    public static void merge(URL defaultConfUrl, File confFile) {
        String confName = confFile.getName();
        if (confName.endsWith(".xml")) {
            if (MoreResources.equalRisky(defaultConfUrl, confFile)) {
                LOG.warn("Not merging the same configs: {} <-> {} ", defaultConfUrl, confFile);
            } else {
                LOG.warn("Merging of XML files is unsupported at the moment, " +
                        "you have to adjust configuration file manually: {}", confFile);
            }
        } else if (confName.endsWith(".properties")) {
            merge(newPropertiesConfiguration(defaultConfUrl), newPropertiesConfiguration(confFile));
        }
    }
 
    public static void merge(PropertiesConfiguration defaultConf, PropertiesConfiguration conf){
        LOG.debug("Merging: {} <-> {}", defaultConf.getURL(), conf.getURL());
        for (@SuppressWarnings("unchecked") Iterator<String> iterator = defaultConf.getKeys(); iterator.hasNext();) {
            String key = iterator.next();
            if (!conf.containsKey(key)) {
                copyProperty(key, defaultConf, conf);
            }
        }
        try {
            conf.save();
        } catch (ConfigurationException e) {
            throw new RuntimeException("Could no save configuration file: " + conf.getFileName(), e);
        }
    }

    /**
     * Copies property identified by {@code key} from the {@code srcConf} to {@code dstConf}.
     * This method will also copy property comments.
     *
     * @param defaultConf
     * @param conf
     * @param key
     */
    public static void copyProperty(String key, PropertiesConfiguration srcConf, PropertiesConfiguration dstConf) {
        LOG.debug("Copying property: {}", key);
        Object value = srcConf.getProperty(key);
        dstConf.setProperty(key, value);
        String comment = srcConf.getLayout().getComment(key);
        if (comment != null) {
            dstConf.getLayout().setComment(key, comment);
        }
    }

}
