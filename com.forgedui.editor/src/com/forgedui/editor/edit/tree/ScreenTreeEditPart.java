package com.forgedui.editor.edit.tree;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;

import com.forgedui.model.Element;
import com.forgedui.model.Screen;

/**
 * Edit tree part for the screen
 * 
 * @author Tareq Doufish
 *
 */
public class ScreenTreeEditPart extends ElementTreeEditPart {

	/**
	 * @param c
	 */
	public ScreenTreeEditPart(Element c) {
		super(c);
	}

	@Override
	public List<?> getModelChildren() {
		Screen screen = (Screen) getModel();
		List<Element> children = new ArrayList<Element>();
		
		if (screen.getDialog() != null){
			children.add(screen.getDialog());
		}
		
		if (screen.getWindow() != null) {
			children.add(screen.getWindow());
		}

		if (screen.getTabGroup() != null) {
			children.add(screen.getTabGroup());
		}

		return children;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (Screen.SCREE_WINDOW_PR_WINDOW.equals(evt.getPropertyName())
				|| Screen.SCREE_WINDOW_PR_TAB_GROUP.equals(evt
						.getPropertyName())
				|| Screen.SCREE_WINDOW_PR_DIALOG.equals(evt
						.getPropertyName())) {
			if (evt.getNewValue() != null)
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
