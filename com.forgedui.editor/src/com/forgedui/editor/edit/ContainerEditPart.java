// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import com.forgedui.editor.edit.policy.ChangeConstraintEditPolicy;
import com.forgedui.editor.edit.policy.ContainerEditPolicy;
import com.forgedui.editor.edit.policy.EditorContainerEditPolicy;
import com.forgedui.model.Container;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public abstract class ContainerEditPart extends ElementEditPart {
	
	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		List<?> children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			EditPart editPart = (EditPart) children.get(i);
			editPart.refresh();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (Container.PROPERTY_CHILDREN.equals(e.getPropertyName())){
			refreshChildren();
		} else {
			super.propertyChange(e);
		}
	}
	
	@Override
	protected List<?> getModelChildren_(){
		Container model = (Container)getModel();
		return model.getChildren();
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		// Installing the real container editor policy for the container.
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new EditorContainerEditPolicy());	
//		installEditPolicy(AddElementEditPolicy.KEY, new AddElementEditPolicy());
		installEditPolicy(ContainerEditPolicy.KEY, new ContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ChangeConstraintEditPolicy());
		
	}

}
