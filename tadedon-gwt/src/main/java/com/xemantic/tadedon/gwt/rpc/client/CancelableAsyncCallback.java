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
package com.xemantic.tadedon.gwt.rpc.client;

import com.xemantic.tadedon.gwt.rpc.client.AsyncCallbackAdapter;

/**
 * 
 * <p>
 * Created on Aug 30, 2010
 *
 * @author hshsce
 */
public abstract class CancelableAsyncCallback<T> extends AsyncCallbackAdapter<T> {

    private final Object m_mutex = new Object();

    private boolean m_canceled;

    public void cancel() {
        synchronized (m_mutex) {
            m_canceled = true;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onSuccess(T result) {
        boolean canceled;
        synchronized (m_mutex) {
            canceled = m_canceled;
        }
        if (!canceled) {
            onRealSucccess(result);
        }
    }

    protected abstract void onRealSucccess(T result);

}
