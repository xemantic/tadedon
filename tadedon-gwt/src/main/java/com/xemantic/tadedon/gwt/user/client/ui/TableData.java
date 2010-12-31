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

import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * <p>
 * Created on Nov 24, 2010
 *
 * @author hshsce
 */
public class TableData extends ComplexPanel implements InsertPanel.ForIsWidget {

	public TableData() {
		setElement(DOM.createTD());
	}

	/**
	 * Adds a new child widget to the panel.
	 *
	 * @param w the widget to be added
	 */
	@Override
	public void add(Widget w) {
		add(w, getElement());
	}

	public void insert(IsWidget w, int beforeIndex) {
		insert(asWidgetOrNull(w), beforeIndex);
	}

	/** {@inheritDoc} */
	@Override
	public void insert(Widget w, int beforeIndex) {
		insert(w, getElement(), beforeIndex, true);
	}

	public void setColspan(int colspan) {
		TableCellElement cellElement = getElement().cast();
		cellElement.setColSpan(colspan);
	}

}
