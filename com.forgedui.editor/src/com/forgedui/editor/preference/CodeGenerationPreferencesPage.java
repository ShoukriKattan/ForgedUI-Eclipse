/**
 * 
 */
package com.forgedui.editor.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.forgedui.editor.GUIEditorPlugin;

/**
 * @author shoukry
 * 
 */
public class CodeGenerationPreferencesPage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Combo codeGenerationStyleCombo;
	private Button supportJSS;

	public CodeGenerationPreferencesPage() {
		super();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.CodeGenerationPropertiesPreferencePage_Code_Generation_Style);

		codeGenerationStyleCombo = new Combo(container, SWT.VERTICAL
				| SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);

		codeGenerationStyleCombo
				.add(Messages.CodeGenerationPropertiesPreferencePage_Code_Generation_Style_BASIC,
						CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_BASIC);
		codeGenerationStyleCombo
				.add(Messages.CodeGenerationPropertiesPreferencePage_Code_Generation_Style_OOModule,
						CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_OO_MODULE);
		codeGenerationStyleCombo
				.add(Messages.CodeGenerationPropertiesPreferencePage_Code_Generation_Style_COMMONJS,
						CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_COMMON_JS);

		supportJSS = new Button(container, SWT.CHECK);
		supportJSS
				.setText(Messages.CodeGenerationPropertiesPreferencePage_Code_Generation_SUPPORT_JSS);

		GridData data = new GridData();
		data.horizontalSpan = 2;
		supportJSS.setLayoutData(data);

		loadValues();

		return container;
	}

	public void loadValues() {
		IPreferenceStore store = getPreferenceStore();

		int styleName = store
				.getInt(CodeGenerationPreferences.CODE_GENERATION_STYLE_PROPERTY_NAME);

		codeGenerationStyleCombo.select(styleName);

		supportJSS
				.setSelection(store
						.getBoolean(CodeGenerationPreferences.CODE_GENERATION_STYLE_SUPPORT_JSS_PROPERTY_NAME));

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

		store.setValue(
				CodeGenerationPreferences.CODE_GENERATION_STYLE_PROPERTY_NAME,
				CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_BASIC);

		codeGenerationStyleCombo
				.select(CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_BASIC);

		store.setValue(
				CodeGenerationPreferences.CODE_GENERATION_STYLE_SUPPORT_JSS_PROPERTY_NAME,
				false);

		supportJSS.setSelection(false);

	}

	public boolean performOk() {
		IPreferenceStore store = getPreferenceStore();

		store.setValue(
				CodeGenerationPreferences.CODE_GENERATION_STYLE_PROPERTY_NAME,
				codeGenerationStyleCombo.getSelectionIndex());

		store.setValue(
				CodeGenerationPreferences.CODE_GENERATION_STYLE_SUPPORT_JSS_PROPERTY_NAME,
				supportJSS.getSelection());

		return super.performOk();
	}
}
