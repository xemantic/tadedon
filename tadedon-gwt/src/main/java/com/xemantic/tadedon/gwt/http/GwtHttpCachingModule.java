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
package com.xemantic.tadedon.gwt.http;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.xemantic.tadedon.servlet.CacheDisablingFilter;
import com.xemantic.tadedon.servlet.CacheForcingFilter;

/**
 * 
 * <p>
 * Created on Aug 22, 2010
 *
 * @author hshsce
 *
 */
public class GwtHttpCachingModule extends ServletModule {

    /** {@inheritDoc} */
    @Override
    protected void configureServlets() {
        bind(CacheDisablingFilter.class).in(Singleton.class);
        bind(CacheForcingFilter.class).in(Singleton.class);
        filterRegex("^.*\\.nocache\\..*$").through(CacheDisablingFilter.class);
        filterRegex("^.*\\.cache\\..*$").through(CacheForcingFilter.class);
    }

}
