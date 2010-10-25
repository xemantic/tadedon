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
import java.net.URL;
import java.net.URLClassLoader;

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

            private static final String META_INF_SERVICES = "META-INF/services/";

            public LauncherWebAppClassLoader() throws IOException {
                super(bootStrapOnlyClassLoader, LauncherWebAppContext.this);

                URL[] urls = ((URLClassLoader) systemClassLoader).getURLs();
                for (URL url : urls) { // add all the classpath entries except gwt-dev
                    URL resource = (new URLClassLoader(new URL[] { url }, null))
                        .getResource("com/google/gwt/dev/About.properties");
                    if (resource == null) {
                        addClassPath(url.getFile());
                    }
                }
            }

            @Override
            public URL findResource(String name) {
                // Specifically for
                // META-INF/services/javax.xml.parsers.SAXParserFactory
                String checkName = name;
                if (checkName.startsWith(META_INF_SERVICES)) {
                    checkName = checkName.substring(META_INF_SERVICES.length());
                }

                // For a system path, load from the outside world.
                URL found;
                if (isSystemPath(checkName)) {
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

                // See if the outside world has it.
                found = systemClassLoader.getResource(name);
                if (found == null) {
                    return null;
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
                name = name.replace('/', '.');
                return super.isSystemPath(name)
                    || name.startsWith("org.apache.jasper.")
                    || name.startsWith("org.apache.xerces.");
            }

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                // For system path, always prefer the outside world.
                if (isSystemPath(name)) {
                    try {
                        return systemClassLoader.loadClass(name);
                    } catch (ClassNotFoundException e) {
                    }
                }

                try {
                    return super.findClass(name);
                } catch (ClassNotFoundException e) {
                    // Don't allow server classes to be loaded from the outside.
                    if (isServerPath(name)) {
                        throw e;
                    }
                }

                // See if the outside world has a URL for it.
                String resourceName = name.replace('.', '/') + ".class";
                URL found = systemClassLoader.getResource(resourceName);
                if (found == null) {
                    return null;
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
