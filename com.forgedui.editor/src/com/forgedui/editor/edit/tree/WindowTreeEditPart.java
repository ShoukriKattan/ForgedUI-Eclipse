// LICENSE
package com.forgedui.editor.edit.tree;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.Window;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class WindowTreeEditPart extends TitaniumContainerTreeEditPart {

	/**
	 * @param c
	 */
	public WindowTreeEditPart(TitaniumUIContainer c) {
		super(c);
	}
	
	@Override
	public List<?> getModelChildren() {
		Window window = (Window)getModel();
		List<Element> children = new ArrayList<Element>(window.getChildren());
		if (window.getTitleBar() != null){
			children.add(window.getTitleBar());
		}
		return children;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (Window.TITLE_BAR_PROP.equals(evt.getPropertyName())){
			if (evt.getNewValue()  != null)
				// new child
				addChild(createChild(evt.getNewValue()), -1);
			else {
				// remove child
				EditPart part = (EditPart) getViewer().getEditPartRegistry()
						.get(evt.getOldValue());
				if (part != null) {
					removeChild(part);
				}
			}
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}

}
