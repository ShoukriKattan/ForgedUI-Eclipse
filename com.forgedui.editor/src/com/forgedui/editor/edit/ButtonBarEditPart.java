// LICENSE
package com.forgedui.editor.edit;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;

import com.forgedui.editor.edit.policy.DeleteElementEditPolicy;
import com.forgedui.editor.edit.policy.TabbedBarButtonEditPolicy;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.TabbedBarButton;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ButtonBarEditPart extends TitaniumContainerEditPart<ButtonBar> {
	
	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		List<?> children = getModelChildren();
		Dimension size = new Dimension(((Element)getModel()).getSupport().viewToModel(
				getFigure().getClientArea().getSize()));
		Dimension d = null;
		if(children.size() != 0)
		d = new Dimension(size.width / children.size(), size.height);
		for (int i = 0; i < children.size(); i++) {
			((TabbedBarButton)children.get(i)).setDimension(d.getCopy());
			((TabbedBarButton)children.get(i)).setLocation(new Point(i*size.width*1.0 / children.size(), 0));
		}
	}

	@Override
	protected void createEditPolicies() {
		// Installing the special layout for the move of the childs.
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TabbedBarButtonEditPolicy());
		installEditPolicy(DeleteElementEditPolicy.KEY, new DeleteElementEditPolicy());
	}

}
