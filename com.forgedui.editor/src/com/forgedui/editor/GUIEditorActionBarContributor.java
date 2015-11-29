// LICENSE
package com.forgedui.editor;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

/**
 * Contributes actions to a toolbar. This class is tied to the editor in the
 * definition of editor-extension (see plugin.xml).
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 */
@SuppressWarnings("restriction")
public class GUIEditorActionBarContributor extends ActionBarContributor {

	/**
	 * Create actions managed by this contributor.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		
		IWorkbenchWindow iww = getPage().getWorkbenchWindow();
		addRetargetAction((RetargetAction)ActionFactory.COPY.create(iww));
		addRetargetAction((RetargetAction)ActionFactory.PASTE.create(iww));
		
		// Registering the zooming actions at the menu bar.
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
		
		addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY,
			GEFMessages.ToggleGrid_Label, IAction.AS_CHECK_BOX));
	}

	/**
	 * Add actions to the given toolbar.
	 * 
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.COPY.getId()));
		toolBarManager.add(getAction(ActionFactory.PASTE.getId()));
		
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));

		// Adding the zooming action for the editor.
		String[] zoomStrings = new String[] { ZoomManager.FIT_ALL,
				ZoomManager.FIT_WIDTH, ZoomManager.FIT_HEIGHT };
		ZoomComboContributionItem zoomActionItem = new ZoomComboContributionItem(getPage(), zoomStrings);
		toolBarManager.add(zoomActionItem);
		
		//toolBarManager.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
	}

	/**
	 * Just to make the zooming actions for the editor via the context menu.
	 */
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		// TODO Auto-generated method stub
		super.contributeToMenu(menuManager);
		
		// Adding the zooming to the workbench while our editor is active.
		MenuManager zoomMenu = new MenuManager("Zoom");
		IAction zoomInAction = getAction(GEFActionConstants.ZOOM_IN);
		IAction zoomOutAction = getAction(GEFActionConstants.ZOOM_OUT);
		if (zoomInAction != null && zoomOutAction != null) {
			zoomMenu.add(zoomInAction);
			zoomMenu.add(zoomOutAction);
			menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT,zoomMenu);
		}
		
		MenuManager mnu = new MenuManager ("view");
		mnu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT, mnu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {
		// currently none
	}

}