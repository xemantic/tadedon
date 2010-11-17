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
package com.xemantic.tadedon.gwt.activity.shared;

import com.google.gwt.activity.shared.Activity;

/**
 * 
 * <p>
 * Created on Nov 12, 2010
 *
 * @author hshsce
 */
public abstract class AbstractActivityWrapper implements Activity {

	private final Activity m_activity;


	protected AbstractActivityWrapper(Activity activity) {
		m_activity = activity;
	}

	/** {@inheritDoc} */
	@Override
	public String mayStop() {
		return m_activity.mayStop();
	}

	/** {@inheritDoc} */
	@Override
	public void onCancel() {
		m_activity.onCancel();
	}

	/** {@inheritDoc} */
	@Override
	public void onStop() {
		m_activity.onStop();
	}

}
