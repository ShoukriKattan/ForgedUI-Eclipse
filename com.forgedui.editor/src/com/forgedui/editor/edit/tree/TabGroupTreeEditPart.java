// LICENSE
package com.forgedui.editor.edit.tree;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPart;

import com.forgedui.model.titanium.TabGroup;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TabGroupTreeEditPart extends ElementTreeEditPart {


	public TabGroupTreeEditPart(TabGroup e) {
		super(e);
	}
	
	@Override
	protected List getModelChildren() {
		return ((TabGroup)getModel()).getTabs();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (TabGroup.TAB_PROP.equals(evt.getPropertyName())){
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
