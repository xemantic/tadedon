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
package com.xemantic.tadedon.gwt.dev;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;

import com.google.gwt.core.ext.ServletContainerLauncher;
import com.google.gwt.core.ext.TreeLogger;

/**
 * Maven friendly {@link ServletContainerLauncher}.
 * It exposes all the libraries from the system classpath (except
 * {@code gwt-dev}) as webapp classpath. 
 * <p>
 * Usage: add {@code -server com.xemantic.tadedon.gwt.dev.JettyLauncher} in
 * configuration of your dev mode launcher. 
 * <p>
 * Created on Jul 29, 2010
 *
 * @author hshsce
 */
public class JettyLauncher extends com.google.gwt.dev.shell.jetty.JettyLauncher {

    @Override
    protected WebAppContext createWebAppContext(TreeLogger logger, File appRootDir) {
        return new LauncherWebAppContext(appRootDir.getAbsolutePath(), "/");
    }

    protected static final class LauncherWebAppContext extends WebAppContext {

        private class LauncherWebAppClassLoader extends WebAppClassLoader {

            private final Set<String> gwtDevMetaInfServices = new HashSet<String>();

            private final Set<String> systemClasses = new HashSet<String>();


            public LauncherWebAppClassLoader() throws IOException {
                super(bootStrapOnlyClassLoader, LauncherWebAppContext.this);
                URL[] urls = ((URLClassLoader) systemClassLoader).getURLs();
                for (URL url : urls) { // add all the classpath entries except gwt-dev
                    URLClassLoader loader = new URLClassLoader(new URL[] { url }, null);
                    if (loader.getResource("com/google/gwt/dev/About.properties") != null) { // it is gwt-dev
                    	File file;
                    	try {
                            file = new File(url.toURI());
                    	} catch(URISyntaxException e) {
                            file = new File(url.getPath());
                    	}
                    	ZipFile zipFile = new JarFile(file);
                    	Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    	while (entries.hasMoreElements()) {
                    		ZipEntry entry = entries.nextElement();
                    		String name = entry.getName();
                    		if (name.matches("^META-INF/services/.+$")) {
                    			gwtDevMetaInfServices.add(name);
                    			InputStream in = zipFile.getInputStream(entry);
                    			String serviceClass = IOUtils.toString(in).split(" ")[0].trim();
                    			systemClasses.add(serviceClass);
                    		}
                    	}
                    	continue;
                    }
                	if (!url.getPath().endsWith("/target/test-classes/")) { // exclude unit test from classpath
                		addClassPath(url.toString());
                	}
                }
            }

            @Override
            public URL findResource(String name) {
                // For a system path, load from the outside world.
                URL found;
                if (isSystemPath(name)) {
                    found = systemClassLoader.getResource(name);
                    if (found != null) {
                        return found;
                    }
                }

                // Always check this ClassLoader first.
                found = super.findResource(name);
                if (found != null) {
                    return found;
                }

                return super.findResource(name);
            }

            /**
             * Override to additionally consider the most commonly available JSP
             * and XML implementation as system resources. (In fact, Jasper is
             * in gwt-dev via embedded Tomcat, so we always hit this case.)
             */
            @Override
            public boolean isSystemPath(String name) {
            	if (name.startsWith("META-INF")) {
            		return gwtDevMetaInfServices.contains(name);
            	}
                name = name.replace('/', '.');
                return
                    name.startsWith("org.mortbay.") ||
                	name.startsWith("javax.servlet.") ||
                	name.startsWith("org.apache.xalan.") ||
                	name.startsWith("org.apache.jasper.") ||
                	name.startsWith("com.google.gwt.dev") ||
                	systemClasses.contains(name);
            }

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                // For system path, always prefer the outside world.
                if (isSystemPath(name)) {
                    try {
                        return systemClassLoader.loadClass(name);
                    } catch (ClassNotFoundException e) { } // just proceed
                }

                try {
                    return super.findClass(name);
                } catch (ClassNotFoundException e) {
                    // Don't allow server classes to be loaded from the outside.
                    if (isServerPath(name)) {
                        throw e;
                    }
                }

                return super.findClass(name);
            }

        }

        /**
         * Parent ClassLoader for the Jetty web app, which can only load JVM
         * classes. We would just use <code>null</code> for the parent ClassLoader
         * except this makes Jetty unhappy.
         */
        private final ClassLoader bootStrapOnlyClassLoader = new ClassLoader(null) {};

        /**
         * In the usual case of launching {@link com.google.gwt.dev.DevMode}, this
         * will always by the system app ClassLoader.
         */
        private final ClassLoader systemClassLoader = Thread.currentThread().getContextClassLoader();


        @SuppressWarnings("unchecked")
        public LauncherWebAppContext(String webApp, String contextPath) {
            super(webApp, contextPath);

            // Prevent file locking on Windows; pick up file changes.
            getInitParams().put(
                    "org.mortbay.jetty.servlet.Default.useFileMappedBuffer",
                    "false");

            // Since the parent class loader is bootstrap-only, prefer it first.
            setParentLoaderPriority(true);
        }

        /** {@inheritDoc} */
        @Override
        protected void doStart() throws Exception {
            setClassLoader(new LauncherWebAppClassLoader());
            super.doStart();
        }

        /** {@inheritDoc} */
        @Override
        protected void doStop() throws Exception {
            super.doStop();
            setClassLoader(null);
        }

    }

}
