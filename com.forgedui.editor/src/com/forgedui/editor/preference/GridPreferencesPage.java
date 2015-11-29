package com.forgedui.editor.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.forgedui.editor.GUIEditorPlugin;

/**
 * Preference page for the grid with and the height.
 * 
 * @author Tareq Doufish
 *
 */
public class GridPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

	/** The width and the height of the grid component */
	private Text width;
	private Text height;
	
	public GridPreferencesPage() { 
		super();
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		container.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL));
		
		Label label = new Label(container, SWT.NONE);
		label.setText("Grid width:");
		width = new Text(container,  SWT.BORDER |  SWT.WRAP);
		
		
		label = new Label(container, SWT.NONE);
		label.setText("Grid height:");
		height = new Text(container,  SWT.BORDER |  SWT.WRAP);
		
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		
		width.setLayoutData(data);
		height.setLayoutData(data);
		
		loadValues();
		
		return container;
	}
	
	public void loadValues() { 
		IPreferenceStore store = getPreferenceStore();
		width.setText("" + store.getInt(EditorPreferences.GRID_WIDTH));
		height.setText("" + store.getInt(EditorPreferences.GRID_HEIGHT));
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
		width.setText(store.getDefaultInt(EditorPreferences.GRID_WIDTH) + "");
		height.setText(store.getDefaultInt(EditorPreferences.GRID_HEIGHT) + "");
	}
	
	public boolean performOk() {
		IPreferenceStore store = getPreferenceStore();
		String widthString = width.getText();
		String heightString = height.getText();
		int widthValue = 10;
		int heightValue = 10;
		try { 
			widthValue = Integer.parseInt(widthString);
		} catch (Exception e) {
			widthValue = store.getInt(EditorPreferences.GRID_WIDTH);
		}
		
		try { 
			heightValue = Integer.parseInt(heightString);
		} catch (Exception e) {
			heightValue = store.getInt(EditorPreferences.GRID_HEIGHT);
		}
		
		store.setValue(EditorPreferences.GRID_WIDTH, widthValue);
		store.setValue(EditorPreferences.GRID_HEIGHT, heightValue);
		return super.performOk();
	}
}
