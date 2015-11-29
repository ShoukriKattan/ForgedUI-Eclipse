package com.forgedui.editor.edit.policy;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.forgedui.editor.edit.TitleBarEditPart;
import com.forgedui.editor.edit.command.DeleteTitleBarChildCommand;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;

public class TitleBarContainerEditPolicy extends ContainerEditPolicy {

	public static final String KEY = "ContainerEditPolicy";
	
	/**
	 * Just to prevent some of the components from getting
	 * created twice at the system.
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}
	
	public Command getOrphanChildrenCommand(GroupRequest request) {
		TitleBarEditPart host = (TitleBarEditPart)getHost();
		List parts = request.getEditParts();
		CompoundCommand result = new CompoundCommand("Moving from title bar part to another");
		for (int i = 0; i < parts.size(); i++) {
			final EditPart ep = (EditPart)parts.get(i);
			if (ep.getModel() instanceof TitaniumUIElement) {
				DeleteTitleBarChildCommand dtbc = new DeleteTitleBarChildCommand(host.getTitleBar(), (TitaniumUIBoundedElement)ep.getModel());
				result.add(dtbc);
			}
		}
		return result.unwrap();
	}
	
}
