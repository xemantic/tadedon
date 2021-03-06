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
package com.xemantic.tadedon.gwt.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;

/**
 * 
 * <p>
 * Created on Aug 21, 2010
 *
 * @author hshsce
 */
public final class UncaughtExceptionHandlers {

    private UncaughtExceptionHandlers() { /* util class, non-instantiable */ }


    public static void installHandler() {
        final UncaughtExceptionHandler uncaughtExceptionHandler = GWT.getUncaughtExceptionHandler();
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable t) {
                if (GWT.isScript()) {
                    Window.alert("Exception occured: " + t.getLocalizedMessage());
                }
                if (uncaughtExceptionHandler != null) {
                    uncaughtExceptionHandler.onUncaughtException(t);
                }
            }
        });
    }

}
