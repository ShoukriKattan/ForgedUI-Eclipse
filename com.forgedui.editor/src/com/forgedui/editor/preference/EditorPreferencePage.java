package com.forgedui.editor.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.forgedui.editor.GUIEditorPlugin;

/**
 * The main preference page for the editor.
 * 
 * @author Tareq Doufish
 * 
 */
public class EditorPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new RowLayout());

		String text = "Welcome to ForgedUI the simple Drag and Drop UI Builder for Titanium";

		Label mainLabel = new Label(container, SWT.NONE);
		mainLabel.setText(text);
		return container;
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(GUIEditorPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * For loading the defaults here.
	 */
	protected void performDefaults() {
	}

	/**
	 * For performing the ok, once a user is done with his/her changes.
	 */
	public boolean performOk() {
		return super.performOk();
	}

}
