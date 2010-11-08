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
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * Mapper which wraps other {@link ActivityWrapper}
 * in order to set associated {@link Place} on {@link Activity} which
 * implement {@link HasPlace}.  
 * <p>
 * Created on Nov 3, 2010
 *
 * @author hshsce
 */
public class PlaceSettingActivityMapper implements ActivityMapper {

	private final ActivityMapper m_activityMapper;

	public PlaceSettingActivityMapper(ActivityMapper delegateActivityMapper) {
		m_activityMapper = delegateActivityMapper;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Activity getActivity(Place place) {
		Activity activity = m_activityMapper.getActivity(place);
		if (activity instanceof HasPlace) {
			((HasPlace) activity).setPlace(place);
		}
		return activity;
	}

}
