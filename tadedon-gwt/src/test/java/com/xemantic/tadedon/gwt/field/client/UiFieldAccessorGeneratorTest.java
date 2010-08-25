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
package com.xemantic.tadedon.gwt.field.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.HasText;
import com.xemantic.tadedon.gwt.field.client.widget.UiFieldAccessorDemoWidget;

/**
 * 
 * <p>
 * Created on Aug 24, 2010
 *
 * @author hshsce
 *
 */
public class UiFieldAccessorGeneratorTest extends GWTTestCase {

    /** {@inheritDoc} */
    @Override
    public String getModuleName() {
        return "com.xemantic.tadedon.gwt.field.Field";
    }

    public void testShouldCreateUiFieldAccessorForDemoWidget() {
        // given
        UiFieldAccessorDemoWidget widget = new UiFieldAccessorDemoWidget();

        // when
        HasText fooLabel = widget.getUiField("fooLabel");
        HasText barButton = widget.getUiField("barButton");

        // then
        assertEquals("foo", fooLabel.getText());
        assertEquals("bar", barButton.getText());
    }

}
