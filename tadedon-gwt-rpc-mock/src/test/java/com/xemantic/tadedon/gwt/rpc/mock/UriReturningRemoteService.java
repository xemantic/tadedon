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
package com.xemantic.tadedon.gwt.rpc.mock;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * <p>
 * Created on Aug 22, 2010
 *
 * @author hshsce
 */
public class UriReturningRemoteService implements RemoteService {

    private final Provider<HttpServletRequest> m_requestProvider;


    @Inject
    public UriReturningRemoteService(Provider<HttpServletRequest> requestProvider) {
        m_requestProvider = requestProvider;
    }

    public String getUri() {
        return m_requestProvider.get().getRequestURI();
    }

}
