/*
 * Copyright 2010-2011 Xemantic
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import com.google.inject.Stage;
import com.xemantic.tadedon.logging.Loggers;

/**
 * Unit test for {@link GuiceServlets}.
 * <p>
 * Created on Aug 16, 2010
 *
 * @author hshsce
 */
public class GuiceServletsTest {

    @BeforeClass
    public static void redirectJulToSLF4J() {
        Loggers.redirectJulToSLF4J();
    }

    @Test
    public void shouldGetProductionStageWhenNoStageSpecified() throws Exception {
        // given
        MockServletContext context = new MockServletContext();

        // when
        Stage stage = GuiceServlets.getStage(context);

        // then
        assertThat(stage, is(Stage.PRODUCTION));
    }

    @Test
    public void shouldGetProductionStageWhenProductionIsSpecified() throws Exception {
        // given
        MockServletContext context = new MockServletContext();
        context.addInitParameter(GuiceServlets.STAGE_PARAMETER, "PRODUCTION");

        // when
        Stage stage = GuiceServlets.getStage(context);

        // then
        assertThat(stage, is(Stage.PRODUCTION));
    }

    @Test
    public void shouldGetDevelopmentStageWhenDevelopmentIsSpecified() throws Exception {
        // given
        MockServletContext context = new MockServletContext();
        context.addInitParameter(GuiceServlets.STAGE_PARAMETER, "DEVELOPMENT");

        // when
        Stage stage = GuiceServlets.getStage(context);

        // then
        assertThat(stage, is(Stage.DEVELOPMENT));
    }

    @Test
    public void shouldGetDevelopmentStageWhenJunkIsSpecified() throws Exception {
        // given
        MockServletContext context = new MockServletContext();
        context.addInitParameter(GuiceServlets.STAGE_PARAMETER, "${application.stage}"); // will be filtered later

        // when
        Stage stage = GuiceServlets.getStage(context);

        // then
        assertThat(stage, is(Stage.DEVELOPMENT));
    }

    @Test
    public void shouldGetProductionStageWhenNoFooParamStageSpecified() throws Exception {
        // given
        MockServletContext context = new MockServletContext();

        // when
        Stage stage = GuiceServlets.getStage(context, "fooParam");

        // then
        assertThat(stage, is(Stage.PRODUCTION));
    }

    @Test
    public void shouldGetProductionStageWhenFooParamProcutionIsSpecified() throws Exception {
        // given
        MockServletContext context = new MockServletContext();
        context.addInitParameter("fooParam", "PRODUCTION");

        // when
        Stage stage = GuiceServlets.getStage(context, "fooParam");

        // then
        assertThat(stage, is(Stage.PRODUCTION));
    }

    @Test
    public void shouldGetDevelopmentStageWhenFooParamDevelopmentIsSpecified() throws Exception {
        // given
        MockServletContext context = new MockServletContext();
        context.addInitParameter("fooParam", "DEVELOPMENT");

        // when
        Stage stage = GuiceServlets.getStage(context, "fooParam");

        // then
        assertThat(stage, is(Stage.DEVELOPMENT));
    }

    @Test
    public void shouldGetDevelopmentStageWhenFooParamJunkIsSpecified() throws Exception {
        // given
        MockServletContext context = new MockServletContext();
        context.addInitParameter("fooParam", "${application.stage}"); // will be filtered later

        // when
        Stage stage = GuiceServlets.getStage(context, "fooParam");

        // then
        assertThat(stage, is(Stage.DEVELOPMENT));
    }

}
