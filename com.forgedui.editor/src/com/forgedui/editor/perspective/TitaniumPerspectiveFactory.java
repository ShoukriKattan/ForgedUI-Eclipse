package com.forgedui.editor.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * @author Vitali Y.
 */
public class TitaniumPerspectiveFactory implements IPerspectiveFactory {
	
	public static final String ID_FOLDER_TOP_LEFT = "topLeft";
	public static final String ID_FOLDER_BOTTOM_LEFT = "bottomLeft";
	public static final String ID_FOLDER_BOTTOM = "bottom";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		
		final String editorArea = layout.getEditorArea();

		// Top left:
		IFolderLayout topLeft = layout.createFolder(ID_FOLDER_TOP_LEFT, IPageLayout.LEFT, 0.25f, editorArea);
		topLeft.addView(IPageLayout.ID_RES_NAV);
		//topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER);
		
		// Bottom left: Outline view
		IFolderLayout bottomLeft = layout.createFolder(ID_FOLDER_BOTTOM_LEFT, IPageLayout.BOTTOM, 0.50f, ID_FOLDER_TOP_LEFT);
		bottomLeft.addView(IPageLayout.ID_OUTLINE);

		// Bottom: Property Sheet view
		IFolderLayout bottom = layout.createFolder(ID_FOLDER_BOTTOM, IPageLayout.BOTTOM, 0.75f, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);

		layout.setEditorAreaVisible(true);
	}

}
