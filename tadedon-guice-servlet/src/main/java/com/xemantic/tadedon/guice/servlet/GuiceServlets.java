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
package com.xemantic.tadedon.guice.servlet;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Stage;
import com.google.inject.internal.Preconditions;

/**
 * Utility class for servlet based guice applications.
 * <p>
 * Created on Aug 16, 2010
 *
 * @author hshsce
 */
public class GuiceServlets {

    private static final Logger LOG = LoggerFactory.getLogger(GuiceServlets.class);

    /**
     * {@code com.xemantic.tadedon.APPLICATION_STAGE}
     */
    public static final String STAGE_PARAMETER = "com.xemantic.tadedon.APPLICATION_STAGE";


    private GuiceServlets() { /* util class, non-instantiable */ }

    /**
     * Returns {@link Stage} based on
     * {@code com.xemantic.tadedon.APPLICATION_STAGE} {@link ServletContext} init parameter.
     *
     * @param stageParam
     */
    public static Stage getStage(ServletContext context) {
        return getStage(context, STAGE_PARAMETER);
    }

    /**
     * Returns {@link Stage} based on given {@code stageParameter} {@link ServletContext}
     * init parameter.
     *
     * @param stageParam
     */
    public static Stage getStage(ServletContext context, String stageParameter) {
        Preconditions.checkArgument(context != null, "context cannot be null");
        Preconditions.checkArgument(stageParameter != null, "stageParameter cannot be null");
        Stage stage;
        String stageName = context.getInitParameter(stageParameter);
        if (stageName == null) {
            LOG.warn("No " + stageParameter + " init parameter provided in ServletContext - " +
            		 "check your web.xml, defaulting to PRODUCTION stage");
            stage = Stage.PRODUCTION;
        } else {
            try {
                stage = Stage.valueOf(stageName);
            } catch (IllegalArgumentException e) {
                LOG.warn("Illegal stage provided in web.xml file: " + stageName + ", assuming DEVELOPMENT stage");
                stage = Stage.DEVELOPMENT;
            }
        }
        return stage;
    }

}
