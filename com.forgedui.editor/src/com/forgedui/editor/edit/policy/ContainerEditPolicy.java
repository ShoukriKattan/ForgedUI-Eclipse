package com.forgedui.editor.edit.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.model.titanium.Toolbar;

/**
 * Edit policy for the containers.
 * 
 * @author Tareq Doufish
 *
 */
public class ContainerEditPolicy extends AddElementEditPolicy {

	public static final String KEY = "ContainerEditPolicy_KEY";
	
	/**
	 * Just to prevent some of the components from getting
	 * created twice at the system.
	 */
	protected Command getCreateCommand(CreateRequest request) {
		// And then passed those to the validate facility.
		Object newObject = request.getNewObject();
		Object container = getHost().getModel();
		
		if (!GUIEditorPlugin.getComponentValidator().validate(newObject, container))
			return null;
		
		return super.getCreateCommand(request);
	}
	
	@Override
	protected Object getConstraintFor(CreateRequest request) {
		Object newObject = request.getNewObject();
		Object container = getHost().getModel();
		Rectangle r = (Rectangle) super.getConstraintFor(request);
		if (newObject instanceof Toolbar){
			r.x = 0;//TODO set y to bottom
		}
		return r;
	}


}
