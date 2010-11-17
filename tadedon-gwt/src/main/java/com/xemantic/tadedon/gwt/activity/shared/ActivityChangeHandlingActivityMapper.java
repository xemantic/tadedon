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
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * <p>
 * Created on Nov 12, 2010
 *
 * @author hshsce
 */
public class ActivityChangeHandlingActivityMapper implements ActivityMapper {

	private final ActivityMapper m_activityMapper;

	private final EventBus m_eventBus;


	public ActivityChangeHandlingActivityMapper(
			ActivityMapper activityMapper,
			EventBus eventBus) {

		m_activityMapper = activityMapper;
		m_eventBus = eventBus;
	}

	/** {@inheritDoc} */
	@Override
	public Activity getActivity(Place place) {
		final Activity activity = m_activityMapper.getActivity(place);
		if (activity == null) {
			return null;
		}
		return new AbstractActivityWrapper(activity) {
			@Override
			public void start(final AcceptsOneWidget panel, EventBus eventBus) {
				activity.start(new AcceptsOneWidget() {
					@Override
					public void setWidget(IsWidget w) {
						if (activity instanceof HasActivityDescription) {
							m_eventBus.fireEvent(new ActivityChangeEvent(activity));
						}
						panel.setWidget(w);
					}
				}, eventBus);
			}
		};
	}

}
