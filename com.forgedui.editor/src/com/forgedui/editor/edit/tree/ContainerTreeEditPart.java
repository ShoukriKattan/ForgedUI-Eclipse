// LICENSE
package com.forgedui.editor.edit.tree;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import com.forgedui.editor.edit.policy.EditorContainerEditPolicy;
import com.forgedui.model.Container;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ContainerTreeEditPart extends ElementTreeEditPart {
	
	/**
	 * @param e
	 */
	public ContainerTreeEditPart(Container c) {
		super(c);
	}

	public List<?> getModelChildren(){
		Container model = (Container)getModel();
		return model.getChildren();
	}
	
	public boolean isPropertyChildName(final String name) {
		boolean res = false;
		if (Container.PROPERTY_CHILDREN.equals(name)) {
			res = true;
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see com.forgedui.editor.edit.tree.ElementTreeEditPart#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
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

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new EditorContainerEditPolicy());
	}
}
