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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel where added widgets are displayed as elements of HTML list.
 * <p>
 * Created on Sep 13, 2010
 *
 * @author hshsce
 */
public class ListPanel extends ComplexPanel implements InsertPanel.ForIsWidget {

    private final Element m_listElement;

    public enum Type {
        UL, OL
    }

    /**
     * Creates new list panel of specified root element {@code type}.
     *
     * @param type the list element type.
     */
    public @UiConstructor ListPanel(Type type) {
        Document doc = Document.get();
        switch (type) {
        case UL:
            m_listElement = doc.createULElement();
            break;
        case OL:
            m_listElement = doc.createOLElement();
            break;
        default:
            throw new AssertionError("Unknown list element type: " + type);
        }
        setElement(m_listElement);
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(Widget w) {
        Element li = DOM.getParent(w.getElement());
        boolean removed = super.remove(w);
        if (removed) {
            m_listElement.removeChild(li);
        }
        return removed;
    }

    /** {@inheritDoc} */
    @Override
    public void add(Widget w) {
        LIElement li = Document.get().createLIElement();
        m_listElement.appendChild(li);
        add(w, (com.google.gwt.user.client.Element) li.cast());
    }

    /** {@inheritDoc} */
    @Override
    public void insert(IsWidget w, int beforeIndex) {
        insert(asWidgetOrNull(w), beforeIndex);
    }

	/** {@inheritDoc} */
	@Override
	public void insert(Widget w, int beforeIndex) {
		checkIndexBoundsForInsertion(beforeIndex);
		LIElement li = Document.get().createLIElement();
		m_listElement.insertBefore(li, m_listElement.getChild(beforeIndex));
		insert(w, (com.google.gwt.user.client.Element) li.cast(), beforeIndex, false);
	}

}
