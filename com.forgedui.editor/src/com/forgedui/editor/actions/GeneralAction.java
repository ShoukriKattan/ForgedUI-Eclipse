package com.forgedui.editor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class GeneralAction  implements IEditorActionDelegate {

	public GeneralAction() {
	}

	@Override
	public void run(IAction action) {
		// TODO: Kattan please remove this :).
		System.out.println("Runing the general action...");
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
	}

}
