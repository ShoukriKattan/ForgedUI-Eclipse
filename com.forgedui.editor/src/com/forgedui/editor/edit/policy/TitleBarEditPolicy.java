// LICENSE
package com.forgedui.editor.edit.policy;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.forgedui.editor.edit.command.SetTitleBarButtonCommand;
import com.forgedui.editor.edit.command.SetTitleBarCenterViewCommand;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.TabbedBar;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.model.titanium.TitleBar;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class TitleBarEditPolicy extends AddElementEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Command res = null;
		final Rectangle r = (Rectangle) getConstraintFor(request);
		boolean add2TitleFlag = false;
		TitleBar model = null;
		if (getHost().getModel() instanceof TitleBar) {
			model = (TitleBar) getHost().getModel();
			String prompt = ResourceHelper.getString(model.getTitlePrompt(),
					model.getTitlepromptid());
			if (prompt == null) {
				prompt = "";
			}
			final Dimension dim = model.getDimension();
			//if (prompt.length() == 0) {
				add2TitleFlag = (0 <= r.y && r.y <= dim.height);
			//} else {
			///	add2TitleFlag = (dim.height / 2 <= r.y && r.y <= dim.height);
			//}
		}
		if (add2TitleFlag) {
			final Dimension dim = model.getDimension();
			if (request.getNewObject() instanceof Button) {
				if (r.x < dim.width / 3) {
					if (model.getLeftNavButton() == null) {
						res = new SetTitleBarButtonCommand(model, (Button)request.getNewObject(), true);
					}
				} else if (r.x > 2 * dim.width / 3) {
					if (model.getRightNavButton() == null) {
						res = new SetTitleBarButtonCommand(model, (Button)request.getNewObject(), false);
					}
				}
			}
			if (res == null && r.x >= dim.width / 3 && r.x <= 2 * dim.width / 3) {
				if (request.getNewObject() instanceof TitaniumUIElement) {
					if (model.getTitleControl() == null) {
						res = new SetTitleBarCenterViewCommand(model, (TitaniumUIBoundedElement)request.getNewObject());
					}
				}
			}
		}
		return res;
	}

}
