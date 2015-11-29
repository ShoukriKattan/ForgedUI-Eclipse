package com.forgedui.editor.preference;

import org.eclipse.jface.util.IPropertyChangeListener;

import com.forgedui.editor.GUIEditorPlugin;

/**
 * Simple constants for the preferences of the grid.
 * 
 * @author Tareq Doufish
 *
 */
public class EditorPreferences {
	//FIXME move grid properties here from plugin
	public static String GRID_WIDTH = "GridWidth";
	public static String GRID_HEIGHT = "GridHeight";
	static String PREFER_RELATIVE_BOUNDS = "PreferRelativeBounds";
	
	public static boolean PreferRelativeBounds;
	
	private static IPropertyChangeListener prefListener = new IPropertyChangeListener(){

		@Override
		public void propertyChange(
				org.eclipse.jface.util.PropertyChangeEvent event) {
			if (event.getProperty().equals(PREFER_RELATIVE_BOUNDS)){
				loadValues();
			}
		}
	
	};
	
	static {
		GUIEditorPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(prefListener);
		loadValues();
	}
	
	private static void loadValues() {
		PreferRelativeBounds = GUIEditorPlugin.getDefault()
			.getPreferenceStore()
			.getBoolean(PREFER_RELATIVE_BOUNDS);
	}
	
}
