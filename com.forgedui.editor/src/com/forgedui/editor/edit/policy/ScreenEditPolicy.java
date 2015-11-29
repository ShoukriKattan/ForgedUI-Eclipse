package com.forgedui.editor.edit.policy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.forgedui.model.titanium.Window;

public class ScreenEditPolicy extends AbstractConstrainedLayoutEditPolicy {
	
	public static final String KEY = "ScreenEditPolicy";

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		//nothing to drop
		return null;
	}
	
	@Override
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (child.getModel() instanceof Window){
			return super.createChangeConstraintCommand(request, child, constraint);
		}
		return null;
	}


}
