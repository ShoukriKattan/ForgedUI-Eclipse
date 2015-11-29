// LICENSE
package com.forgedui.editor.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;

import com.forgedui.editor.GUIEditor;
import com.forgedui.editor.GUIEditorContextMenuProvider;
import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.dnd.MyTemplateTransferDropTargetListener;
import com.forgedui.editor.edit.tree.DiagramTreePartFactory;
import com.forgedui.model.Diagram;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class DiagramOutlinePage2 extends ContentOutlinePage
	implements IAdaptable {

	private PageBook pageBook;
	private Control outline;
	private Canvas overview;
	private IAction showOutlineAction, showOverviewAction;
	static final int ID_OUTLINE  = 0;
	static final int ID_OVERVIEW = 1;
	private Thumbnail thumbnail;
	private Diagram diagram;
	private RootEditPart rep;
	//private EditDomain editDomain;
	private GUIEditor editor;
	
	public DiagramOutlinePage2(GUIEditor editor,EditPartViewer viewer, Diagram diagram, RootEditPart rep){
		super(viewer);
		this.rep = rep;
		this.diagram = diagram;
		this.editor = editor;
	}
	
	protected void configureOutlineViewer(){
		getViewer().setEditDomain(editor.getEditorEditDomain());
		getViewer().setEditPartFactory(new DiagramTreePartFactory());
		ContextMenuProvider cmProvider = new GUIEditorContextMenuProvider(
				getViewer(), editor.getEditorActionRegistry());
		getViewer().setContextMenu(cmProvider);
		getSite().registerContextMenu(
				"com.forgedui.editor.view.outline.contextmenu",
				cmProvider, getSite().getSelectionProvider());
		// Making this view as a selection provider to the workbench.
		editor.getEditorSelectionSynchronizer().addViewer(getViewer());
		getViewer().addDropTargetListener((TransferDropTargetListener)
			new MyTemplateTransferDropTargetListener(getViewer(), diagram));
		
		IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
		showOutlineAction = new Action() {
			public void run() {
				showPage(ID_OUTLINE);
			}
		};
		showOutlineAction.setImageDescriptor(GUIEditorPlugin.getImageDescriptor("icons/outline.gif")); //$NON-NLS-1$
		showOutlineAction.setToolTipText("Show Outline");
		tbm.add(showOutlineAction);
		showOverviewAction = new Action() {
			public void run() {
				showPage(ID_OVERVIEW);
			}
		};
		showOverviewAction.setImageDescriptor(GUIEditorPlugin.getImageDescriptor("icons/overview.gif")); //$NON-NLS-1$
		showOverviewAction.setToolTipText("Show Overview");
		tbm.add(showOverviewAction);
		showPage(ID_OUTLINE);
	}
	
	public void createControl(Composite parent){
		// initialize outline viewer with model
		pageBook = new PageBook(parent, SWT.NONE);
		outline = getViewer().createControl(pageBook);
		overview = new Canvas(pageBook, SWT.NONE);
		pageBook.showPage(outline);
		
		configureOutlineViewer();
		
		initializeOutlineViewer();
	}
	
	public void dispose(){
		//unhookOutlineViewer();
		if (thumbnail != null) {
			thumbnail.deactivate();
			thumbnail = null;
		}
		super.dispose();
	}
	
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class)
			return getViewer().getProperty(ZoomManager.class.toString());
		return null;
	}

	public Control getControl() {
		return pageBook;
	}

	protected void initializeOutlineViewer(){
		setContents(diagram);
	}
	
	protected void initializeOverview() {
		LightweightSystem lws = new LightweightSystem(overview);
		if (rep instanceof ScalableFreeformRootEditPart) {
			ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart)rep;
			thumbnail = new ScrollableThumbnail((Viewport)root.getFigure());
			thumbnail.setBorder(new MarginBorder(3));
			thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);
		}
	}
	
	public void setContents(Object contents) {
		getViewer().setContents(contents);
	}
	
	protected void showPage(int id) {
		if (id == ID_OUTLINE) {
			showOutlineAction.setChecked(true);
			showOverviewAction.setChecked(false);
			pageBook.showPage(outline);
			if (thumbnail != null)
				thumbnail.setVisible(false);
		} else if (id == ID_OVERVIEW) {
			if (thumbnail == null)
				initializeOverview();
			showOutlineAction.setChecked(false);
			showOverviewAction.setChecked(true);
			pageBook.showPage(overview);
			thumbnail.setVisible(true);
		}
	}
	
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		ActionRegistry registry = editor.getEditorActionRegistry();
		IActionBars bars = pageSite.getActionBars();
		String id = ActionFactory.UNDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = ActionFactory.REDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = ActionFactory.DELETE.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		bars.setGlobalActionHandler(ActionFactory.COPY.getId(),
				registry.getAction(ActionFactory.COPY.getId()));
		bars.setGlobalActionHandler(ActionFactory.PASTE.getId(),
				registry.getAction(ActionFactory.PASTE.getId()));

	}
	
}
