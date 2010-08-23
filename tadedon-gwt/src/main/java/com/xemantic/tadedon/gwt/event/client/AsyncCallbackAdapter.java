/*
 * Copyright (c) 2010 Nordic Consulting & Development Company.
 * All rights reserved.
 * NCDC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.xemantic.tadedon.gwt.event.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * <p>
 * Created on Aug 21, 2010
 *
 * @author morisil
 */
public abstract class AsyncCallbackAdapter<T> implements AsyncCallback<T> {

    /** {@inheritDoc} */
    @Override
    public void onFailure(Throwable caught) {
        if (GWT.getUncaughtExceptionHandler() != null) {
            GWT.getUncaughtExceptionHandler().onUncaughtException(caught);
        }
    }

}
