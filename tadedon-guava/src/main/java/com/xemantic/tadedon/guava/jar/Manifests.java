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
package com.xemantic.tadedon.guava.jar;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import com.google.common.io.Closeables;

/**
 * Utilities for {@link Manifest} files.
 * <p>
 * Created on Aug 20, 2010
 *
 * @author hshsce
 */
public class Manifests {

	public static final String MANIFEST_RESOURCE = "META-INF/MANIFEST.MF";

	public static final String IMPLEMENTATION_VERSION_PROPERTY = "Implementation-Version";

	public static final String BUILD_NUMBER_PROPERTY = "Build-Number";


	/**
	 * @param stringClass
	 * @return
	 */
	public static Manifest getManifest(Class<?> classFromJarWithManifest) {
		checkArgument(classFromJarWithManifest != null, "classFromJarWithManifest cannot be null");
		ClassLoader classLoader = classFromJarWithManifest.getClassLoader();
		checkArgument(classLoader != null, "klass seems to be loaded by bootstrap classloader: " + classFromJarWithManifest.getName());
		String classResource = classFromJarWithManifest.getName().replace(".", "/") + ".class";
		URL classUrl = classLoader.getResource(classResource);
		if (classUrl == null) {
			throw new AssertionError("null resource for given class");
		}
		String path = classUrl.getPath();
		int classResourceIndex = path.indexOf(classResource);
		String basePath = path.substring(0, classResourceIndex);
		Manifest manifest = null;
		Enumeration<URL> manifestUrls = getManifestResources(classLoader);
		while (manifestUrls.hasMoreElements()) {
			URL manifestUrl = (URL) manifestUrls.nextElement();
			if (manifestUrl.getPath().startsWith(basePath)) {
				manifest = getManifest(manifestUrl);
				break;

			}
		}
		return manifest;
	}

	public static Enumeration<URL> getManifestResources(ClassLoader classLoader) {
		try {
			return classLoader.getResources(MANIFEST_RESOURCE);
		} catch (IOException e) {
			throw new RuntimeException("Cannot retrieve resources: " + MANIFEST_RESOURCE, e);
		}
	}

	public static Manifest getManifest(URL url) {
		try {
			Manifest manifest = new Manifest();
			boolean threw = true;
			InputStream in = url.openStream();
			try {
				manifest.read(in);
				threw = false;
			} finally {
				Closeables.close(in, threw);
			}
			return manifest;
		} catch (IOException e) {
			throw new RuntimeException("Cannot read manifest file from given URL", e);
		}
	}

	public static String getVersion(Class<?> classFromJarWithManifest) {
		return getVersion(classFromJarWithManifest);
	}

	public static String getVersion(Manifest manifest) {
		return manifest.getMainAttributes().getValue(IMPLEMENTATION_VERSION_PROPERTY);
	}

	public static String getBuildNumber(Class<?> classFromJarWithManifest) {
		return getBuildNumber(getManifest(classFromJarWithManifest));
	}

	public static String getBuildNumber(Manifest manifest) {
		return manifest.getMainAttributes().getValue(BUILD_NUMBER_PROPERTY);
	}

	public static String getFullVersion(Class<?> classFromJarWithManifest) {
		return getFullVersion(getManifest(classFromJarWithManifest));
	}

	public static String getFullVersion(Manifest manifest) {
		String version = getVersion(manifest);
		String buildNumber = getBuildNumber(manifest);
		if (version == null) {
			return null;
		}
		if (buildNumber != null) {
			version += "-";
			version += buildNumber;
		}
		return version;
	}

}
