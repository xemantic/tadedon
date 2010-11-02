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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This code is copied from:
 * See <a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/samples/expenses/src/main/java/com/google/gwt/sample/expenses/client/SlidingPanel.java">SlidingPanel</a>
 * <p>
 * Created on Oct 28, 2010
 *
 * @author hshsce
 */
public class SlidingPanel extends Composite
	implements HasWidgets.ForIsWidget, HasOneWidget {

	private final List<Widget> widgets = new ArrayList<Widget>();
	private final LayoutPanel layoutPanel = new LayoutPanel();
	private int currentIndex = -1;

	public SlidingPanel() {
		initWidget(layoutPanel);
	}

	@Override
	public void add(IsWidget w) {
		add(asWidgetOrNull(w));
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(IsWidget w) {
		return remove(asWidgetOrNull(w));
	}

	@Override
	public void add(Widget w) {
		widgets.remove(w);
		widgets.add(w);

		// Display the first widget added by default
		if (currentIndex < 0) {
			layoutPanel.add(w);
			currentIndex = 0;
		}
	}

	@Override
	public void clear() {
		setWidget(null);
		widgets.clear();
	}

	@Override
	public Widget getWidget() {
		return widgets.get(currentIndex);
	}

	@Override
	public Iterator<Widget> iterator() {
		return Collections.unmodifiableList(widgets).iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return widgets.remove(w);
	}

	@Override
	public void setWidget(IsWidget w) {
		setWidget(asWidgetOrNull(w));
	}

	/**
	 * Set the widget to show, adding it to the end of our sliding set if we
	 * haven't seen it before. Nulls are ignored.
	 */
	@Override
	public void setWidget(Widget widget) {
		if (widget == null) {
			return;
		}

		int newIndex = widgets.indexOf(widget);

		if (newIndex < 0) {
			newIndex = widgets.size();
			add(widget);
		}

		show(newIndex);
	}

	private void show(int newIndex) {
		if (newIndex == currentIndex) {
			return;
		}

		boolean fromLeft = newIndex < currentIndex;
		currentIndex = newIndex;

		Widget widget = widgets.get(newIndex);
		final Widget current = layoutPanel.getWidget(0);

		// Initialize the layout.
		layoutPanel.add(widget);
		layoutPanel.setWidgetLeftWidth(current, 0, Unit.PCT, 100, Unit.PCT);
		if (fromLeft) {
			layoutPanel.setWidgetLeftWidth(widget, -100, Unit.PCT, 100,
					Unit.PCT);
		} else {
			layoutPanel
					.setWidgetLeftWidth(widget, 100, Unit.PCT, 100, Unit.PCT);
		}
		layoutPanel.forceLayout();

		// Slide into view.
		if (fromLeft) {
			layoutPanel.setWidgetLeftWidth(current, 100, Unit.PCT, 100,
					Unit.PCT);
		} else {
			layoutPanel.setWidgetLeftWidth(current, -100, Unit.PCT, 100,
					Unit.PCT);
		}
		layoutPanel.setWidgetLeftWidth(widget, 0, Unit.PCT, 100, Unit.PCT);
		layoutPanel.animate(500, new Layout.AnimationCallback() {
			public void onAnimationComplete() {
				// Remove the old widget when the animation completes.
				layoutPanel.remove(current);
			}

			public void onLayout(Layer layer, double progress) {
			}
		});
	}

}
