/*
 * Copyright (c) 2010 Nordic Consulting & Development Company.
 * All rights reserved.
 * NCDC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.xemantic.tadedon.gwt.event.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;

/**
 * 
 * <p>
 * Created on Aug 21, 2010
 *
 * @author morisil
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
