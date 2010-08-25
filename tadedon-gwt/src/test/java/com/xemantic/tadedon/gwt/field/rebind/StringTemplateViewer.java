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
package com.xemantic.tadedon.gwt.field.rebind;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * 
 * <p>
 * Created on Aug 25, 2010
 *
 * @author hshsce
 *
 */
public class StringTemplateViewer {

    public static void main(String[] args) throws IOException {
        URL url = UiFieldAccessorGenerator.class.getResource("UiFieldAccessor.template");
        String templateFile = Resources.toString(url, Charsets.UTF_8);
        StringTemplate template = new StringTemplate(templateFile, DefaultTemplateLexer.class);
        template.setAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
        template.setAttribute("date", new Date());
        template.setAttribute("package", "com.example");
        template.setAttribute("className", "UiFieldAccessorDemo_Impl");
        template.setAttribute("ownerType", "FooOwner");
        template.setAttribute("fields", Arrays.asList("foo", "bar"));
        System.out.println(template.toString());
    }
}
