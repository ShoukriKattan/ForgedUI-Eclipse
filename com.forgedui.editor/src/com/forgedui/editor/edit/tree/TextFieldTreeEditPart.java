// LICENSE
package com.forgedui.editor.edit.tree;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.TextField;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TextFieldTreeEditPart extends ElementTreeEditPart {

	/**
	 * @param e
	 */
	public TextFieldTreeEditPart(Element e) {
		super(e);
	}
	
	@Override
	protected List<?> getModelChildren() {
		List<Element> list = new ArrayList<Element>();
		TextField model = (TextField)getModel();
		if (model.getLeftButton() != null){
			list.add(model.getLeftButton());
		}
		if (model.getRightButton() != null){
			list.add(model.getRightButton());
		}
		if (model.getKeyboardToolbar() != null && model.getKeyboardToolbar().length > 0){
			for (int i = 0 ; i < model.getKeyboardToolbar().length ; i++) { 
				list.add(model.getKeyboardToolbar()[i]);
			}
		}
		return list;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (TextField.PRO_LEFT_BUTTON.equals(e.getPropertyName())
				|| TextField.PRO_RIGHT_BUTTON.equals(e.getPropertyName())
				|| TextField.PRO_TOOLBAR.equals(e.getPropertyName())){
			if (e.getNewValue() != null)
				// new child
				addChild(createChild(e.getNewValue()), -1);
			else {
				// remove child
				EditPart part = (EditPart) getViewer().getEditPartRegistry()
						.get(e.getOldValue());
				if (part != null) {
					removeChild(part);
				}
			}
			refreshChildren();
		} else {
			super.propertyChange(e);
		}
	}

}
