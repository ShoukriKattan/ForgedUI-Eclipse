// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.forgedui.model.Container;
import com.forgedui.model.Diagram;
import com.forgedui.model.Element;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DiagramEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener{
	
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setLayoutManager(new FreeformLayout());
		f.setBorder(new MarginBorder(5));
		return f;
	}
	
	public void activate() {
        if (!isActive()) {
            super.activate();
            ((Diagram) this.getModel()).addPropertyChangeListener(this);
        }
    }

	public void deactivate() {
        if (isActive()) {
            ((Diagram) this.getModel()).removePropertyChangeListener(this);
            super.deactivate();
        }
    }

	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		List<?> children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			EditPart editPart = (EditPart) children.get(i);
			editPart.refresh();
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		// these properties are fired when Elements are added into or removed from 
		// the Diagram instance and must cause a call of refreshChildren()
		// to update the diagram's contents.
		if (Container.PROPERTY_CHILDREN.equals(prop)
				|| Diagram.RESOLUTION.equals(prop)
				|| Diagram.PLATFORM.equals(prop)) {
			refreshChildren();
		}
	}
	
	@Override
	protected List<Element> getModelChildren() {
		List<Element> children = new ArrayList<Element>(((Diagram)getModel()).getChildren());
		children.add(((Diagram)getModel()).getScreen());
		return children; // return a list of elements
	}

	@Override
	protected void createEditPolicies() {
		//TODO add another edit policies
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
	}
	
}
