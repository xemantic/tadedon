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

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;

/**
 * Activity which has {@link Place}.
 * <p>
 * Created on Nov 3, 2010
 *
 * @author hshsce
 */
public abstract class PlaceEnabledActivity<P extends Place> extends AbstractActivity
	implements HasPlace<P> {

	private P m_place;


	/** {@inheritDoc} */
	@Override
	public void setPlace(P place) {
		m_place = place;
	}

	/** {@inheritDoc} */
	@Override
	public P getPlace() {
		return m_place;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {  
		return ((obj != null) &&
				isInstaceOfThis(obj) &&
				m_place.equals(((PlaceEnabledActivity) obj).m_place));
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return m_place.hashCode();
	}

	protected abstract boolean isInstaceOfThis(Object obj);

}
