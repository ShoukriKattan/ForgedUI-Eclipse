// LICENSE
package com.forgedui.editor.edit.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.forgedui.editor.edit.ElementEditPart;
import com.forgedui.editor.edit.command.DeleteElementCommand;
import com.forgedui.editor.edit.command.DeleteTitaniumElementCommand;
import com.forgedui.editor.edit.command.DeleteTitleBarChildCommand;
import com.forgedui.editor.edit.command.DeleteTitleBarCommand;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Window;

/**
 * This edit policy enables the removal of a Shapes instance from its container.
 * 
 * @see ElementEditPart#createEditPolicies()
 * @author Dmitry {dmitry.grimm@gmail.com}
 */
public class DeleteElementEditPolicy extends ComponentEditPolicy {
	
	public static final String KEY = "DeleteElementEditPolicy";

	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Object parent = getHost().getParent().getModel();
		Object child = getHost().getModel();
		if (parent instanceof Window
				&& child instanceof TitleBar){
			return new DeleteTitleBarCommand((Window) parent,
					(TitleBar) child);
		} else if (parent instanceof TitleBar){
			return new DeleteTitleBarChildCommand((TitleBar) parent,
					(TitaniumUIBoundedElement) child);
		} else if (parent instanceof Container
				&& child instanceof Element) {
			return new DeleteElementCommand((Container) parent, (Element) child);
		} else if (parent instanceof TitaniumUIContainer
				&& child instanceof TitaniumUIElement) {
			return new DeleteTitaniumElementCommand((TitaniumUIContainer) parent,
					(TitaniumUIBoundedElement) child);
		}
		return super.createDeleteCommand(deleteRequest);
	}
}