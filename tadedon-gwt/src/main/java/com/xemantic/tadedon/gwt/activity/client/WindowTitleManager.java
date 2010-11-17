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
package com.xemantic.tadedon.gwt.activity.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.xemantic.tadedon.gwt.activity.shared.ActivityChangeEvent;
import com.xemantic.tadedon.gwt.activity.shared.HasActivityDescription;


/**
 * 
 * <p>
 * Created on Nov 13, 2010
 *
 * @author hshsce
 */
public class WindowTitleManager {

	private String m_prefix = "";

	private String m_suffix = "";


	public void setTitlePrefix(String prefix) {
		m_prefix = prefix;
	}

	public void setTitleSuffix(String suffix) {
		m_suffix = suffix;
	}

	public String getTitlePrefix() {
		return m_prefix;
	}

	public String getTitleSuffix() {
		return m_suffix;
	}

	public void start(EventBus eventBus) {
		eventBus.addHandler(ActivityChangeEvent.TYPE, new ActivityChangeEvent.Handler() {
			@Override
			public void onActivityChange(ActivityChangeEvent event) {
				Activity activity = event.getActivity();
				if (activity instanceof HasActivityDescription) {
					String description = ((HasActivityDescription) activity).getActivityDescription();
					Window.setTitle(m_prefix + description + m_suffix);
				}
			}
		});
	}

}
