package com.forgedui.editor.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * @author Vitali Y.
 */
public class DeleteAction extends org.eclipse.gef.ui.actions.DeleteAction {

	public DeleteAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	public void run() {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		boolean ret = MessageDialog.openQuestion(shell, "Delete Confirmation",
				"Are you sure you want to delete the element?");
		if (ret) {
			super.run();
		}
	}

}
