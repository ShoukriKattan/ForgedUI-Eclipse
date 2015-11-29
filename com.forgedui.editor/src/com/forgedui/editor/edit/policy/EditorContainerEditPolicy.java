package com.forgedui.editor.edit.policy;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.forgedui.editor.edit.TabbedBarButtonEditPart;
import com.forgedui.editor.edit.command.OrphanChildCommand;
import com.forgedui.editor.edit.command.TitaniumOrphanChildCommand;
import com.forgedui.model.Container;
import com.forgedui.model.ElementImpl;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;

/**
 * The container edit policy for the editor.
 * @author Tareq Doufish
 *
 */
public class EditorContainerEditPolicy extends ContainerEditPolicy {

	public static final String KEY = "ContainerEditPolicy";
	
	/**
	 * Just to prevent some of the components from getting
	 * created twice at the system.
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}
	@SuppressWarnings("unchecked")
	public Command getOrphanChildrenCommand(GroupRequest request) {
		List<EditPart> parts = request.getEditParts();
		for (int i = 0; i < parts.size(); i++) {
			EditPart part = parts.get(i);
			//System.out.println("THe part is something here..." + part);
			// We will just return and do nothing at all.
			if (part instanceof TabbedBarButtonEditPart) { 
				return null;
			}
		}
		
		CompoundCommand result = new CompoundCommand("Moving from one part to another");
		for (int i = 0; i < parts.size(); i++) {
			if (parts.get(i).getModel() instanceof TitaniumUIBoundedElement) {
				TitaniumOrphanChildCommand c = new TitaniumOrphanChildCommand();
				c.setChild((TitaniumUIBoundedElement)parts.get(i).getModel());
				c.setParent((Container) getHost().getModel());
				result.add(c);
			} else {
				OrphanChildCommand orphan = new OrphanChildCommand();
				orphan.setChild((ElementImpl)parts.get(i).getModel());
				orphan.setParent((Container) getHost().getModel());
				result.add(orphan);
			}
			
		}
		return result.unwrap();
	}
	
}
