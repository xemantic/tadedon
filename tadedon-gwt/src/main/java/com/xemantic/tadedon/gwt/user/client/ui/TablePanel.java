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

import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * <p>
 * Created on Nov 24, 2010
 *
 * @author hshsce
 */
public class TablePanel extends HTMLPanel {

	public TablePanel(String html) {
		super("table", html);
	}

	public void addRow(IsWidget rowWidget) {
		if (!(rowWidget.asWidget().getElement().cast() instanceof TableRowElement)) {
			throw new IllegalArgumentException("Cannot add widget which root element is not TableRowElement");
		}

		TableElement table = getElement().cast();
		TableSectionElement tbody = table.getTBodies().getItem(0);
		add(rowWidget.asWidget(), tbody);
	}

}
