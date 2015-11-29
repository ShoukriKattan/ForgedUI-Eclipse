// LICENSE
package com.forgedui.editor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.forgedui.editor.GUIEditor;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ExportToImageActionDelegate implements IEditorActionDelegate {

	public static final String ID = "com.forgedui.editor.actions.ExportToImageAction";

	private IEditorPart editor;

	@Override
	public void run(IAction action) {
		if (editor instanceof GUIEditor) {
			GUIEditor guiEditor = (GUIEditor) editor;
			ImageSaveUtil.save(editor, guiEditor.getGraphicalViewer());
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editor = targetEditor;
	}	

}
