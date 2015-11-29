package com.forgedui.editor.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.forgedui.editor.GUIEditorPlugin;

/**
 * Adding the default values of the editor grid.
 * 
 * @author Tareq Doufish
 *
 */
public class EditorPreferenceInitializer extends AbstractPreferenceInitializer{

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = GUIEditorPlugin.getDefault().getPreferenceStore();
		store.setDefault(EditorPreferences.GRID_WIDTH, 20);
		store.setDefault(EditorPreferences.GRID_HEIGHT, 20);
		store.setDefault(CodeGenerationPreferences.CODE_GENERATION_STYLE_PROPERTY_NAME, CodeGenerationPreferences.CODE_GEN_STYLE_VALUE_BASIC);
		store.setDefault(CodeGenerationPreferences.CODE_GENERATION_STYLE_SUPPORT_JSS_PROPERTY_NAME, false);
	}

}
