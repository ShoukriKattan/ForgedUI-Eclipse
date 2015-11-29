// LICENSE
package com.forgedui.editor.edit.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.edit.command.AddTitleBarCommand;
import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Toolbar;
import com.forgedui.model.titanium.Window;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class WindowAddElementEditPolicy extends AddElementEditPolicy {
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// And then passed those to the validate facility.
		Object newObject = request.getNewObject();
		Object container = getHost().getModel();
		if (!GUIEditorPlugin.getComponentValidator().validate(newObject, container))
			return null;
		
		if (request.getNewObject() instanceof TitleBar
				&& getHost().getModel() instanceof Window) {
			TitleBar titleBar = (TitleBar) request.getNewObject();
			Window window = (Window)getHost().getModel();
			if (window.getTitleBar() == null){
				return new AddTitleBarCommand(window,
						titleBar, 
						new Rectangle(0, 0, window.getDimension().width, -1));
			}
		} else if (request.getNewObject() instanceof TitaniumUIBaseElement
				&& getHost().getModel() instanceof TitaniumUIContainer) {
			return super.getCreateCommand(request);
		}
		return null;
	}
	
	@Override
	protected Object getConstraintFor(CreateRequest request) {
		Object newObject = request.getNewObject();
		Rectangle r = (Rectangle) super.getConstraintFor(request);
		if (newObject instanceof Toolbar){
			r.x = 0;
			//TODO set y
		}
		return r;
	}


}
