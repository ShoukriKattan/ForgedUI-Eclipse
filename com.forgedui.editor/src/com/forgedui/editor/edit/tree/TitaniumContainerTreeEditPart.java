// LICENSE
package com.forgedui.editor.edit.tree;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPart;

import com.forgedui.model.Container;
import com.forgedui.model.titanium.TitaniumUIContainer;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumContainerTreeEditPart extends ElementTreeEditPart {

	/**
	 * @param e
	 */
	public TitaniumContainerTreeEditPart(TitaniumUIContainer c) {
		super(c);
	}

	public List<?> getModelChildren(){
		TitaniumUIContainer model = (TitaniumUIContainer)getModel();
		return model.getChildren();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (Container.PROPERTY_CHILDREN.equals(evt.getPropertyName())){
			if (evt.getNewValue()  != null)
				// new child
				addChild(createChild(evt.getNewValue()), ((IndexedPropertyChangeEvent)evt).getIndex());
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
