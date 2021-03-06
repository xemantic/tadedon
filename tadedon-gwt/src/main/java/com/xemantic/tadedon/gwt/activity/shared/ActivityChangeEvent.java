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
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * <p>
 * Created on Nov 13, 2010
 *
 * @author hshsce
 */
public class ActivityChangeEvent extends GwtEvent<ActivityChangeEvent.Handler> {

	public interface Handler extends EventHandler {

		void onActivityChange(ActivityChangeEvent event);

	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final Activity activity;


	public ActivityChangeEvent(Activity activity) {
		this.activity = activity;
	}

	/** {@inheritDoc} */
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	/** {@inheritDoc} */
	@Override
	protected void dispatch(Handler handler) {
		handler.onActivityChange(this);
	}

	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}

}
