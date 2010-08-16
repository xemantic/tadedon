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
package com.xemantic.tadedon.guava.base;

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.io.Files;
import com.google.common.io.Resources;

/**
 * Utility class extending abilities of guava's {@link Resources}.
 * <p>
 * Created on Aug 16, 2010
 *
 * @author hshsce
 */
public final class MoreResources {

    private MoreResources() { /* util class, non-instantiable */ }


    public static void copy(URL url, File file) throws IOException {
        Files.copy(Resources.newInputStreamSupplier(url), file);
    }

    public static void copyRisky(URL url, File file) {
        try {
            copy(url, file);
        } catch (IOException e) {
            throw new RuntimeException("Cannot copy url: " + url + " to file: " + file, e);
        }        
    }

    public static List<URL> getResources(String name) {
        Enumeration<URL> resources;
        try {
            resources = Resources.class.getClassLoader().getResources(name);
        } catch (IOException e) {
            throw new RuntimeException("Cannot get resources " + name, e);
        }
        checkArgument(resources.hasMoreElements(), "resources %s not found.", name);
        Builder<URL> builder = ImmutableList.<URL>builder();
        while (resources.hasMoreElements()) {
            URL url = (URL) resources.nextElement();
            builder.add(url);
        }
        return builder.build();
    }

}
