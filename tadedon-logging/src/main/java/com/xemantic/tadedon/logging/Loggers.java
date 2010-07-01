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
package com.xemantic.tadedon.logging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

/**
 *
 * <p>
 * Created on May 21, 2010
 *
 * @author hshsce
 */
public final class Loggers {

	private Loggers() { /* util class, non-instantiable */ }

	/**
	 * Disables JUL console logging and redirects all logs to SLF4J.
	 * It sets JUL's root level logger to level {@code ALL}. It is achieved
	 * by passing {@code .level=ALL} line as the only option of JUL configuration.
	 *
	 * @see LogManager#readConfiguration(java.io.InputStream)
	 * @see SLF4JBridgeHandler#install()
	 */
	public static void redirectJulToSLF4J() {
		try {
			LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(".level=ALL".getBytes()));
		} catch (IOException e) {
			throw new RuntimeException("Should hever happen - memory based streams", e);
		}
		SLF4JBridgeHandler.install();
	}

	/**
	 * Prints logback status.
	 */
	public static void printLogbackStatus() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
	}

}
