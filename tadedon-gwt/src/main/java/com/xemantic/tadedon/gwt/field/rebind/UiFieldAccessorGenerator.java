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

import java.io.PrintWriter;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.uibinder.rebind.MortalLogger;

/**
 * 
 * <p>
 * Created on Aug 24, 2010
 *
 * @author hshsce
 *
 */
public class UiFieldAccessorGenerator extends Generator {

    /** {@inheritDoc} */
    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
        TypeOracle oracle = context.getTypeOracle();
        JClassType interfaceType;
        try {
            interfaceType = oracle.getType(typeName);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }

        String implName = interfaceType.getName().replace('.', '_') + "Impl";
        String packageName = interfaceType.getPackage().getName();

        PrintWriter out = context.tryCreate(logger, packageName, implName);
        if (out != null) {
            UiFieldAccessorGenerators.write(new MortalLogger(logger), context, packageName, implName, interfaceType, out);
        }
        context.commit(logger, out);
        return packageName + "." + implName;
    }

}
