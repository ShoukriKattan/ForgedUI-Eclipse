// LICENSE
package com.forgedui.editor.edit;

import org.eclipse.gef.EditPolicy;

import com.forgedui.editor.edit.policy.DeleteElementEditPolicy;
import com.forgedui.editor.edit.policy.TabbedBarButtonEditPolicy;
import com.forgedui.model.titanium.TabbedBar;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TabbedBarEditPart extends TitaniumContainerEditPart<TabbedBar> {
	
	/*@Override
	protected void refreshChildren() {
		super.refreshChildren();
		List<?> children = getModelChildren();
		final Dimension size = new Dimension(getElement().getSupport().viewToModel(
				getFigure().getClientArea().getSize()));
		final PrecisionDimension d = new PrecisionDimension(size.preciseWidth() / (double)children.size(), size.height);
		double x = 0.0;
		double x_next = d.preciseWidth();
		int x_int = (int)x;
		for (int i = 0; i < children.size(); i++) {
			final TabbedBarButton tbb = (TabbedBarButton)children.get(i);
			tbb.setDimension(new PrecisionDimension(x_next - x_int, d.preciseHeight()));
			tbb.setLocation(new Point(x_int, 0.0));
			x = x_next;
			x_next += d.preciseWidth();
			x_int = (int)x;
		}
	}*/

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TabbedBarButtonEditPolicy());
		installEditPolicy(DeleteElementEditPolicy.KEY, new DeleteElementEditPolicy());
	}
	
}
