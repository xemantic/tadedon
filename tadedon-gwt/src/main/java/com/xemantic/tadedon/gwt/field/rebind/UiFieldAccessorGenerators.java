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
import java.io.PrintWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.uibinder.rebind.MortalLogger;
import com.google.gwt.uibinder.rebind.model.OwnerClass;
import com.google.gwt.uibinder.rebind.model.OwnerField;
import com.xemantic.tadedon.gwt.field.client.UiFieldAccessor;

/**
 * 
 * <p>
 * Created on Aug 25, 2010
 *
 * @author hshsce
 *
 */
public class UiFieldAccessorGenerators {

    public static void write(
            MortalLogger logger,
            GeneratorContext context,
            String packageName,
            String implName,
            JClassType interfaceType,
            PrintWriter out) throws UnableToCompleteException {

        // Check for possible misuse 'GWT.create(UiFielcAccessor.class)'
        JClassType accessorItself = context.getTypeOracle().findType(UiFieldAccessor.class.getCanonicalName());
        if (accessorItself.equals(interfaceType)) {
            logger.die("You must use a subtype of UiFieldAccessor in GWT.create(). E.g.,\n"
                    + "  interface Accessor extends UiFieldAccessor<MyClass> {}\n"
                    + "  GWT.create(Accessor.class);");
        }

        JClassType[] accessorTypes = interfaceType.getImplementedInterfaces();
        if (accessorTypes.length == 0) {
            throw new RuntimeException("No implemented interfaces for " + interfaceType.getName());
        }

        JClassType accessorType = accessorTypes[0];

        JClassType[] typeArgs = accessorType.isParameterized().getTypeArgs();
        if (typeArgs.length < 1) {
            throw new RuntimeException("Owner type parameter is required for type %s" + accessorType.getName());
        }

        JClassType ownerType = typeArgs[0];

        OwnerClass ownerClass = new OwnerClass(ownerType, logger);
        Set<String> fields = UiFieldAccessorGenerators.getFieldNames(ownerClass);
        UiFieldAccessorGenerators.write(packageName, implName, ownerType.getName(), interfaceType.getName(), fields, out);
    }

    public static void write(
            String packageName,
            String className,
            String ownerType,
            String interfaceType,
            Iterable<String> fields,
            PrintWriter out) {

        StringTemplate template = UiFieldAccessorGenerators.getStringTemplate();
        template.setAttribute("package", packageName);
        template.setAttribute("className", className);
        template.setAttribute("ownerType", ownerType);
        template.setAttribute("interfaceType", interfaceType);
        template.setAttribute("fields", fields);
        out.print(template.toString());
    }

    public static StringTemplate getStringTemplate() {
        StringTemplate template = new StringTemplate(readTemplateFile(), DefaultTemplateLexer.class);
        template.setAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
        template.setAttribute("date", new Date());
        return template;
    }

    public static String readTemplateFile() {
        URL url = UiFieldAccessorGenerator.class.getResource("UiFieldAccessor.template");
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read resource from url: " + url, e);
        }        
    }

    public static Set<String> getFieldNames(OwnerClass ownerClass) {
        Collection<OwnerField> fields = ownerClass.getUiFields();
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (OwnerField ownerField : fields) {
            builder.add(ownerField.getName());
        }
        return builder.build();
    }

}
