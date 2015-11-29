// LICENSE
package com.forgedui.editor.edit.tree;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.TitleBar;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitleBarTreeEditPart extends ElementTreeEditPart {

	/**
	 * @param e
	 */
	public TitleBarTreeEditPart(TitleBar model) {
		super(model);
	}
	
	@Override
	public List<?> getModelChildren() {
		List<Element> list = new ArrayList<Element>();
		TitleBar model = (TitleBar)getModel();
		if (model.getLeftNavButton() != null){
			list.add(model.getLeftNavButton());
		}
		if (model.getRightNavButton() != null){
			list.add(model.getRightNavButton());
		}
		if (model.getTitleControl() != null){
			list.add(model.getTitleControl());
		}
		return list;
	}

	public boolean isPropertyChildName(final String name) {
		boolean res = false;
		if (TitleBar.PROPERTY_LEFT_NAV_BUTTON.equals(name) || 
				TitleBar.PROPERTY_RIGHT_NAV_BUTTON.equals(name) || 
				TitleBar.PROPERTY_TITLE_CONTROL.equals(name)) {
			res = true;
		}
		return res;
	}

	
	/* (non-Javadoc)
	 * @see com.forgedui.editor.edit.tree.ElementTreeEditPart#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (isPropertyChildName(evt.getPropertyName())){
			if (evt.getNewValue() != null) {
				// new child
				addChild(createChild(evt.getNewValue()), -1);
			} else {
				// remove child
				EditPart part = (EditPart)getViewer().getEditPartRegistry().get(evt.getOldValue());
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
