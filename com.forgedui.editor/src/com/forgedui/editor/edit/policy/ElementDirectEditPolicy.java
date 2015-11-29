package com.forgedui.editor.edit.policy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.forgedui.editor.actions.EditingCommand;
import com.forgedui.editor.edit.ElementEditPart;

/**
 * Direct editing policy for the elements.
 * 
 * @author Tareq Doufish
 *
 */
public class ElementDirectEditPolicy extends DirectEditPolicy {

	protected Command getDirectEditCommand(DirectEditRequest edit) {
		EditPart part = getHost();
		if (part instanceof ElementEditPart) {
			String labelText = (String) edit.getCellEditor().getValue();
			ElementEditPart editPart = (ElementEditPart) getHost();
			Object model = editPart.getModel();
			EditingCommand command = new EditingCommand(
					model, labelText);
			return command;
		}
		return null;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest arg0) {
	}
}
