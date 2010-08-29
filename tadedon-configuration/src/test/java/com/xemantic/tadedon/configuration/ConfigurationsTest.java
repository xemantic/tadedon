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

import static com.xemantic.tadedon.configuration.ConfigurationTests.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * Unit test for {@link Configurations}.
 * <p>
 * Created on Aug 16, 2010
 *
 * @author hshsce
 */
public class ConfigurationsTest {

    private File testConfDir;

    @Before
    public void createTestConfDir() throws IOException {
        testConfDir = new File("target/test-conf-dir");
        System.out.println("Creating dir: " + testConfDir.getAbsolutePath() + " : " + testConfDir.getCanonicalPath());
        if (!testConfDir.mkdir()) {
            throw new IOException("Cannot create directory: " + testConfDir);
        }
    }

    @Test
    public void shouldCopyPropertyWithComments() throws IOException, ConfigurationException {
        // given
        File confFile1 = new File("src/test/data/conf1.properties");
        File confFile2 = new File("src/test/data/conf2.properties");
        File confFile3 = new File("src/test/data/conf3.properties");
        File outConfFile = new File(testConfDir, "outConf.properties");
        Files.copy(confFile1, outConfFile);
        PropertiesConfiguration conf2 = new PropertiesConfiguration(confFile2);
        PropertiesConfiguration outConf = new PropertiesConfiguration(outConfFile);

        // when
        Configurations.copyProperty("bar", conf2, outConf);
        outConf.save();

        // then
        assertThat(Files.equal(outConfFile, confFile3), is(true));
    }

    /**
     * copying of XML properties does not work at the moment
     * @throws IOException
     * @throws ConfigurationException
     */
    @Test
    @Ignore
    public void shouldCopyXmlPropertyWithoutComments() throws IOException, ConfigurationException {
        // given
        File confFile1 = new File("src/test/data/conf1.xml");
        File confFile2 = new File("src/test/data/conf2.xml");
        File confFile3 = new File("src/test/data/conf3.xml");
        File outConfFile = new File(testConfDir, "outConf.xml");
        Files.copy(confFile1, outConfFile);
        XMLConfiguration conf2 = new XMLConfiguration(confFile2);
        XMLConfiguration outConf = new XMLConfiguration(outConfFile);

        // when
        //Configurations.copyProperty("bar", conf2, outConf);
        outConf.save();

        // then
        assertThat(Files.equal(outConfFile, confFile3), is(true));
    }

    @Test
    public void shouldCopyAllConfigurationsOnMergeAllDefaultsIfOutputDirIsEmpty() throws IOException {
        // given classpath with default configuration 

        // when
        Configurations.mergeAllDefaults(testConfDir);

        // then
        assertThat(testConfDir.list().length, is(4));
        assertThat(new File(testConfDir, "Component1.properties").exists(), is(true));
        assertThat(new File(testConfDir, "Component2.properties").exists(), is(true));
        assertThat(new File(testConfDir, "Component3.xml").exists(), is(true));
        assertThat(new File(testConfDir, "Component4.xml").exists(), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component1.properties"), getDefaultConfFile("Component1.properties")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component2.properties"), getDefaultConfFile("Component2.properties")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component3.xml"), getDefaultConfFile("Component3.xml")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component4.xml"), getDefaultConfFile("Component4.xml")), is(true));
    }

    @Test
    public void shouldAddOneMissingConfigurationFileOnMergeAllDefaults() throws IOException {
        // given classpath with default configuration and 
        Files.copy(getDefaultConfFile("Component1.properties"), new File(testConfDir, "Component1.properties"));
        Files.copy(getDefaultConfFile("Component2.properties"), new File(testConfDir, "Component2.properties"));
        Files.copy(getDefaultConfFile("Component3.xml"), new File(testConfDir, "Component3.xml"));

        // when
        Configurations.mergeAllDefaults(testConfDir);

        // then
        assertThat(testConfDir.list().length, is(4));
        assertThat(new File(testConfDir, "Component1.properties").exists(), is(true));
        assertThat(new File(testConfDir, "Component2.properties").exists(), is(true));
        assertThat(new File(testConfDir, "Component3.xml").exists(), is(true));
        assertThat(new File(testConfDir, "Component4.xml").exists(), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component1.properties"), getDefaultConfFile("Component1.properties")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component2.properties"), getDefaultConfFile("Component2.properties")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component3.xml"), getDefaultConfFile("Component3.xml")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component4.xml"), getDefaultConfFile("Component4.xml")), is(true));
    }

    @Test
    public void shouldUpdateOneConfigurationFileOnMergeAllDefaults() throws IOException {
        // given classpath with default configuration and 
        Files.copy(getDefaultConfFile("Component1.properties"), new File(testConfDir, "Component1.properties"));
        Files.copy(getDefaultConfFile("Component1.properties"), new File(testConfDir, "Component2.properties"));
        Files.copy(getDefaultConfFile("Component3.xml"), new File(testConfDir, "Component3.xml"));
        Files.copy(getDefaultConfFile("Component4.xml"), new File(testConfDir, "Component4.xml"));

        // when
        Configurations.mergeAllDefaults(testConfDir);

        // then
        assertThat(testConfDir.list().length, is(4));
        assertThat(new File(testConfDir, "Component1.properties").exists(), is(true));
        assertThat(new File(testConfDir, "Component2.properties").exists(), is(true));
        assertThat(new File(testConfDir, "Component3.xml").exists(), is(true));
        assertThat(new File(testConfDir, "Component4.xml").exists(), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component1.properties"), getDefaultConfFile("Component1.properties")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component2.properties"), new File("src/test/data/Component2-updated.properties")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component3.xml"), getDefaultConfFile("Component3.xml")), is(true));
        assertThat(Files.equal(new File(testConfDir, "Component4.xml"), getDefaultConfFile("Component4.xml")), is(true));
    }

    @After
    public void deleteTestConfDir() throws IOException {
        System.out.println("Deleting dir: " + testConfDir.getAbsolutePath() + " : " + testConfDir.getCanonicalPath());
        Files.deleteRecursively(testConfDir);
    }

}
