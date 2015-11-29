package com.forgedui.editor.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.forgedui.editor.GUIEditor;
import com.forgedui.editor.edit.ContainerEditPart;
import com.forgedui.editor.edit.TitaniumContainerEditPart;
import com.forgedui.editor.edit.command.PasteElementCommand;
import com.forgedui.editor.edit.tree.TitaniumContainerTreeEditPart;
import com.forgedui.model.Container;

/**
 * 
 * @author Zrieq
 * 
 */
public class PasteElementAction extends SelectionAction {

	public PasteElementAction(IWorkbenchPart part) {
		super(part);
		// force calculateEnabled() to be called in every context
		setLazyEnablementCalculation(true);
	}

	protected void init() {
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setText("Paste");
		setId(ActionFactory.PASTE.getId());
		setHoverImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(false);
	}

	private Command createPasteCommand() {
		List selectedObjects = getSelectedObjects();
		if(selectedObjects != null && selectedObjects.size() > 0 && selectedObjects.get(0) != null && selectedObjects.get(0) instanceof AbstractEditPart)
			return new PasteElementCommand(((AbstractEditPart)selectedObjects.get(0)),(GUIEditor)getWorkbenchPart());
		return null;
	}

	@Override
	protected boolean calculateEnabled() {
		Command command = createPasteCommand();
		return command != null && command.canExecute();
	}

	@Override
	public void run() {
		Command command = createPasteCommand();
		if (command != null && command.canExecute())
			execute(command);
	}

}
