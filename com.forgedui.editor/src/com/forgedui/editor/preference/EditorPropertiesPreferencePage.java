package com.forgedui.editor.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.forgedui.editor.GUIEditorPlugin;

public class EditorPropertiesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	
	private Button preferRelatives;

	public EditorPropertiesPreferencePage() {
		super();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		preferRelatives = new Button(container, SWT.CHECK);
		preferRelatives.setText(Messages.EditorPropertiesPreferencePage_Prefer_Relative_Bounds);
		
		GridData data = new GridData();
		data.horizontalSpan = 2;
		preferRelatives.setLayoutData(data);
		
		loadValues();
		
		return container;
	}
	
	public void loadValues() { 
		IPreferenceStore store = getPreferenceStore();
		preferRelatives.setSelection(
				store.getBoolean(EditorPreferences.PREFER_RELATIVE_BOUNDS));
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(GUIEditorPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * For loading the defaults here.
	 */
	protected void performDefaults() {
		IPreferenceStore store = getPreferenceStore();
		preferRelatives.setSelection(store.getDefaultBoolean(EditorPreferences.PREFER_RELATIVE_BOUNDS));
	}
	
	public boolean performOk() {
		IPreferenceStore store = getPreferenceStore();
		store.setValue(EditorPreferences.PREFER_RELATIVE_BOUNDS, preferRelatives.getSelection());
		return super.performOk();
	}

}
