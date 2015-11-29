// LICENSE
package com.forgedui.editor.edit.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import com.forgedui.editor.edit.policy.DeleteElementEditPolicy;
import com.forgedui.editor.edit.policy.EditorContainerEditPolicy;
import com.forgedui.editor.edit.policy.TitaniumTreeContainerEditPolicy;
import com.forgedui.editor.edit.policy.TreeEditPolicy;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.model.Element;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class ElementTreeEditPart extends AbstractTreeEditPart implements
		PropertyChangeListener {
	
	public ElementTreeEditPart(Element e){
		super(e);
	}
	
	@Override
	protected Image getImage() {
		return ModelLabelProvider.getIcon(getModel());
	}
	
	@Override
	protected String getText() {
		return ModelLabelProvider.getText(getModel());
	}
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	public void activate() {
		if (!isActive()) {
			getElement().addPropertyChangeListener(this);
		}
		super.activate();
	}

	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			getElement().removePropertyChangeListener(this);
		}
		super.deactivate();
	}

	protected void createEditPolicies() {
		// If this editpart is the root content of the viewer, then disallow removal
		// Its better to disallow this condiction at the request level, 
		// and not at installing the edit policy.
//		if (getParent() instanceof RootEditPart) {
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteElementEditPolicy());
			installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
					new TreeEditPolicy());
			installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE,new TitaniumTreeContainerEditPolicy());
			installEditPolicy(EditPolicy.CONTAINER_ROLE, new EditorContainerEditPolicy());	
//		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean isTypeVisiual = PropertyType.isVisualProperty(
				getModel().getClass().getName(), evt.getPropertyName());
		if (isTypeVisiual){
			refreshVisuals();
		}else if (Element.PROPERTY_NAME.equals(evt.getPropertyName())){
			refreshVisuals();
		}
	}
	
	public Element getElement() {
		return (Element)getModel();
	}
	
}
