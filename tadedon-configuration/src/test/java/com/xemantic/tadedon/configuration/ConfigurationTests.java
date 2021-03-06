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

/**
 * 
 * <p>
 * Created on Aug 16, 2010
 *
 * @author hshsce
 */
public final class ConfigurationTests {

    private ConfigurationTests() { /* util class, non-instantiable */ }


    public static File getDefaultConfFile(String confName) {
        return new File("src/test/resources/" + Configurations.DEFAULT_CONFIGURATION_PATH, confName);
    }

    public static void deleteDirectoryContents(File directory) throws IOException {
        checkArgument(directory.isDirectory(), "Not a directory: %s", directory);
        File[] files = directory.listFiles();
        for (File file : files) {
            deleteRecursively(file);
        }
    }

    public static void deleteRecursively(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryContents(file);
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }

}
