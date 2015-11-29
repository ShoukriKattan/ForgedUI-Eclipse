// LICENSE
package com.forgedui.editor;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Provides context menu actions for the GUIEditor.
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 */
public class GUIEditorContextMenuProvider extends ContextMenuProvider {

	/** The editor's action registry. */
	private ActionRegistry actionRegistry;

	private IAction deleteActionItem;
	private IAction copyActionItem;
	private IAction pasteActionItem;
	
	/**
	 * Instantiate a new menu context provider for the specified EditPartViewer
	 * and ActionRegistry.
	 * 
	 * @param viewer
	 *            the editor's graphical viewer
	 * @param registry
	 *            the editor's action registry
	 * @throws IllegalArgumentException
	 *             if registry is <tt>null</tt>.
	 */
	public GUIEditorContextMenuProvider(EditPartViewer viewer,
			ActionRegistry registry) {
		super(viewer);
		if (registry == null) {
			throw new IllegalArgumentException();
		}
		actionRegistry = registry;
	}

	/**
	 * Called when the context menu is about to show. Actions, whose state is
	 * enabled, will appear in the context menu.
	 * 
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menu) {
		// Add standard action groups to the menu
		GEFActionConstants.addStandardActionGroups(menu);

		IAction action;

		action = actionRegistry.getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = actionRegistry.getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		// Adding the delete in a special way.
		deleteActionItem = actionRegistry.getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, deleteActionItem);
		
		// Adding the delete in a special way.
		copyActionItem = actionRegistry.getAction(ActionFactory.COPY.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, copyActionItem);
		// Adding the delete in a special way.
		pasteActionItem = actionRegistry.getAction(ActionFactory.PASTE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, pasteActionItem);
		
		// Add actions to the menu
//		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, // target group id
//				getAction(ActionFactory.UNDO.getId())); // action to add
//		menu.appendToGroup(GEFActionConstants.GROUP_UNDO,
//				getAction(ActionFactory.REDO.getId()));
//		menu.appendToGroup(GEFActionConstants.GROUP_EDIT,
//				getAction(ActionFactory.DELETE.getId()));
	}

//	private IAction getAction(String actionId) {
//		return actionRegistry.getAction(actionId);
//	}

}
