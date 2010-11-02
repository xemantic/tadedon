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

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
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

	public OneWidgetDeckPanel() {
		m_panel.add(new Widget() {{ setElement(Document.get().createDivElement()); }}); // empty widget
		initWidget(m_panel);
		m_panel.showWidget(0);
	}

	/** {@inheritDoc} */
	@Override
	public void setWidget(IsWidget w) {
		setWidget(asWidgetOrNull(w));
	}

	/** {@inheritDoc} */
	@Override
	public void setWidget(final Widget widget) {
		m_panel.add(new SimplePanel() {
			{
				setWidget(widget);
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
		m_panel.showWidget(m_panel.getWidgetCount() - 1);
	}

	/** {@inheritDoc} */
	@Override
	public Widget getWidget() {
		return m_panel.getWidget(m_panel.getVisibleWidget());
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
