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

import java.util.logging.Level;

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
	 * Initializes jul logger to forward everything to slf4j, and prints logback status.
	 */
	public static void initialize() {
		java.util.logging.Logger.getLogger("").setLevel(Level.ALL);
		SLF4JBridgeHandler.install();
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
	}

}
