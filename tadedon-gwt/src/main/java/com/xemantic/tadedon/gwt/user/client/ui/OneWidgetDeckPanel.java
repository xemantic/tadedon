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
package com.xemantic.tadedon.gwt.user.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wrapper around {@link DeckPanel} which keeps only one widget.
 * <p>
 * Created on Oct 29, 2010
 *
 * @author hshsce
 */
public class OneWidgetDeckPanel extends Composite
	implements HasOneWidget, HasAnimation {

	private DeckPanel m_panel = new DeckPanel();

	private Widget m_widget = null;

	private interface Style {
		String WIDGET_ID = "tadedon-OneWidgetDeckPanel";
		String NULL_WIDGET = "nullWidget";
	}

	public OneWidgetDeckPanel() {
		/* 
		 * Widget which is shown until no other widget is set with setWidget method
		 * or shown when null is passed to setWidget method.
		 */
		m_panel.add(new Widget() {
				{
					setElement(Document.get().createDivElement());
					setStyleName(Style.NULL_WIDGET);
				}
			});
		initWidget(m_panel);
		setStyleName(Style.WIDGET_ID);
		m_panel.showWidget(0);
	}

	/** {@inheritDoc} */
	@Override
	public void setWidget(IsWidget w) {
		setWidget(asWidgetOrNull(w));
	}

	/** {@inheritDoc} */
	@Override
	public void setWidget(Widget widget) {
		m_widget = widget;
		if (widget != null) {
			addWidget(widget);
			m_panel.showWidget(m_panel.getWidgetCount() - 1);
		} else {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					if (m_widget == null) {
						m_panel.showWidget(0);
					}
				}
			});
		}
	}

	private void addWidget(final Widget widget) {
		// wrap widget in composite with lifecycle controlled by setVisible method
		m_panel.add(new Composite() { 
			{
				initWidget(widget);
			}
			@Override
			public void setVisible(boolean visible) {
				if (!visible) {
					if (!added) { // first invocation, see DeckPanel#finishWidgetInitialization()
						added = true;
					} else { // last invocation
						m_panel.remove(this);
					}
				}
				super.setVisible(visible);
			}
			boolean added = false;
		});		
	}

	/** {@inheritDoc} */
	@Override
	public Widget getWidget() {
		int index = m_panel.getVisibleWidget();
		if (m_panel.getVisibleWidget() == 0) {
			return null;
		}
		return m_panel.getWidget(index);
	}

	/** {@inheritDoc} */
	@Override
	public void setAnimationEnabled(boolean enable) {
		m_panel.setAnimationEnabled(enable);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAnimationEnabled() {
		return m_panel.isAnimationEnabled();
	}

}
