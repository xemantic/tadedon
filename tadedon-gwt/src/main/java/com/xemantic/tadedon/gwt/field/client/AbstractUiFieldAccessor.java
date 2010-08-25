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

import java.util.Map;

/**
 * Base for generating {@link UiFieldAccessor} instances.
 * <p>
 * Created on Aug 25, 2010
 *
 * @author hshsce
 */
public abstract class AbstractUiFieldAccessor<O> implements UiFieldAccessor<O> {

    protected O m_owner;


    /** {@inheritDoc} */
    @Override
    public void initializeAccess(O owner) {
        if (owner == null) {
            throw new IllegalArgumentException("owner cannot be null");
        }
        m_owner = owner;        
    }

    /** {@inheritDoc} */
    @Override
    public Object getUiField(String fieldName) {
        Accessor accessor = getAccessorMap().get(fieldName);
        if (accessor == null) {
            return null;
        }
        return accessor.get();
    }

    protected abstract Map<String, Accessor> getAccessorMap();

}
