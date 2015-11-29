package com.forgedui.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.properties.UndoablePropertySheetEntry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.forgedui.editor.actions.CopyElementAction;
import com.forgedui.editor.actions.DeleteAction;
import com.forgedui.editor.actions.PasteElementAction;
import com.forgedui.editor.dnd.MyTemplateTransferDropTargetListener;
import com.forgedui.editor.edit.DiagramEditPartFactory;
import com.forgedui.editor.edit.command.MoveElementCommand;
import com.forgedui.editor.edit.command.MoveElementCommand.MovementDirection;
import com.forgedui.editor.palette.GUIEditorPaletteFactory;
import com.forgedui.editor.preference.EditorPreferences;
import com.forgedui.editor.view.DiagramOutlinePage2;
import com.forgedui.model.Diagram;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.util.DiagramFileUtil;

public class GUIEditor extends GraphicalEditorWithFlyoutPalette {

	private PaletteRoot PALETTE_ROOT;

	private IContentOutlinePage outline = null;

	private Diagram diagram;

	private PropertySheetPage page;

	protected static final String PALETTE_DOCK_LOCATION = "Dock location"; //$NON-NLS-1$
	protected static final String PALETTE_SIZE = "Palette Size"; //$NON-NLS-1$
	protected static final String PALETTE_STATE = "Palette state"; //$NON-NLS-1$
	protected static final int DEFAULT_PALETTE_SIZE = 130;

	private ResourceTracker resourceListener = new ResourceTracker();
	private boolean editorSaving = false;

	static {
		GUIEditorPlugin.getDefault().getPreferenceStore()
				.setDefault(PALETTE_SIZE, DEFAULT_PALETTE_SIZE);
	}

	public GUIEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	protected void closeEditor(boolean save) {
		getSite().getPage().closeEditor(GUIEditor.this, save);
	}

	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	private ScrollingGraphicalViewer viewer;

	@Override
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		viewer = (ScrollingGraphicalViewer) getGraphicalViewer();

		ScalableFreeformRootEditPart root = new GridScalableFreeformRootEditPart();

		List<String> zoomLevels = new ArrayList<String>(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);

		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		getActionRegistry().registerAction(new ToggleGridAction(viewer));

		getSite().getKeyBindingService().registerAction(zoomIn);
		getSite().getKeyBindingService().registerAction(zoomOut);

		viewer.setRootEditPart(root);

		viewer.setEditPartFactory(new DiagramEditPartFactory());
		ContextMenuProvider provider = new GUIEditorContextMenuProvider(viewer,
				getActionRegistry());
		viewer.setContextMenu(provider);
		getSite().registerContextMenu(provider, viewer);

		viewer.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, getDefaultDim());
		GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(viewer){
			@Override
			public boolean keyPressed(KeyEvent event) {
				int moveStep = MoveElementCommand.MOVE_STEP_10;
				switch (event.stateMask) {
					case SWT.CTRL:
					case SWT.COMMAND:{
						moveStep = MoveElementCommand.MOVE_STEP_1; 
					}
				}
				
				switch (event.keyCode) {
					case SWT.ARROW_LEFT:
						return performAction(MovementDirection.LEFT,moveStep);
					case SWT.ARROW_RIGHT:
						return performAction(MovementDirection.RIGHT,moveStep);
					case SWT.ARROW_UP:
						return performAction(MovementDirection.UP,moveStep);
					case SWT.ARROW_DOWN:
						return performAction(MovementDirection.DOWN,moveStep);
					default:
						return super.keyPressed(event);
				}
			}
			
			private boolean performAction(MovementDirection moveDirection, int moveStep) {
				Command cmd = createMoveCommand(getViewer().getSelectedEditParts(), moveDirection, moveStep);
				if (cmd != null && cmd.canExecute()) {
					getCommandStack().execute(cmd);
					return true;
				}
				return false;
			}
			
			private Command createMoveCommand(List<Object> selectedObjects,MovementDirection moveDirection,int moveStep) {
				if (selectedObjects == null || selectedObjects.isEmpty()) {
					return null;
				}
				
				MoveElementCommand cmd = new MoveElementCommand(moveDirection, moveStep);
				Iterator<Object> it = selectedObjects.iterator();
				while (it.hasNext()) {
					Object next = it.next();
					if(next instanceof EditPart){
						EditPart ep = (EditPart) next;
						Element node = (Element) ep.getModel();
						if (!(node instanceof TitaniumUIBoundedElement))
							return null;
						cmd.addElement((TitaniumUIBoundedElement)node);
					}else
						return null;
				}
				return cmd;
			}
		};
		graphicalViewerKeyHandler.put(KeyStroke.getPressed(SWT.BS, 0),getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		viewer.setKeyHandler(graphicalViewerKeyHandler.setParent(viewer.getKeyHandler()));
		
		addPreferencesPropertyListener();
	}

	private Dimension getDefaultDim() {
		IPreferenceStore store = GUIEditorPlugin.getDefault()
				.getPreferenceStore();
		Dimension dimension = new Dimension();
		float widthValue = store.getFloat(EditorPreferences.GRID_WIDTH);
		dimension.width = (int) widthValue;
		float heightValue = store.getFloat(EditorPreferences.GRID_HEIGHT);
		dimension.height = (int) heightValue;
		return dimension;
	}

	/**
	 * Adding the property change for the grid.
	 */
	public void addPreferencesPropertyListener() {
		final IPreferenceStore store = GUIEditorPlugin.getDefault()
				.getPreferenceStore();
		store.addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				Dimension newDim = new Dimension();
				if (event.getProperty().equals(EditorPreferences.GRID_WIDTH)) {
					float widthValue = store
							.getFloat(EditorPreferences.GRID_WIDTH);
					newDim.width = (int) widthValue;
				} else if (event.getProperty().equals(
						EditorPreferences.GRID_HEIGHT)) {
					float heightValue = store
							.getFloat(EditorPreferences.GRID_HEIGHT);
					newDim.height = (int) heightValue;
				}
				viewer.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, newDim);
			}
		});
	}

	/**
	 * Optimized to use Apache commons IO . Cleaner and fasters impl.
	 * 
	 * @param os
	 * @throws IOException
	 */
	protected void writeToOutputStream(OutputStream os) throws IOException {

		IOUtils.copy(DiagramFileUtil.getDiagramContents(getDiagram()), os);
		os.flush();

	}

	// Need to expose those methods for the outline view.
	public ActionRegistry getEditorActionRegistry() {
		return getActionRegistry();
	}

	public SelectionSynchronizer getEditorSelectionSynchronizer() {
		return getSelectionSynchronizer();
	}

	public DefaultEditDomain getEditorEditDomain() {
		return getEditDomain();
	}

	// End of methods exposure.

	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
						viewer));
			}

		};
	}

	public void doSave(final IProgressMonitor progressMonitor) {
		editorSaving = true;
		SafeRunner.run(new SafeRunnable() {
			public void run() throws Exception {
				// saveProperties();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				writeToOutputStream(out);
				IFile file = ((IFileEditorInput) getEditorInput()).getFile();
				file.setContents(new ByteArrayInputStream(out.toByteArray()),
						true, false, progressMonitor);
				getCommandStack().markSaveLocation();
			}
		});
		editorSaving = false;
	}

	public void doSaveAs() {
		SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow()
				.getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		IPath path = dialog.getResult();

		if (path != null) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			final IFile file = workspace.getRoot().getFile(path);

			if (!file.exists()) {
				WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
					public void execute(final IProgressMonitor monitor) {
						// saveProperties();
						try {
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							writeToOutputStream(out);
							file.create(
									new ByteArrayInputStream(out.toByteArray()),
									true, monitor);
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				try {
					new ProgressMonitorDialog(getSite().getWorkbenchWindow()
							.getShell()).run(false, true, op);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			getCommandStack().markSaveLocation();
		}
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
		if (type == org.eclipse.ui.views.properties.IPropertySheetPage.class) {
			page = new PropertySheetPage();
			page.setRootEntry(new UndoablePropertySheetEntry(getCommandStack()));
			return page;
		}
		if (type == IContentOutlinePage.class) {
			return getOutlinePage();
		} else if (type == ZoomManager.class)
			return getGraphicalViewer().getProperty(
					ZoomManager.class.toString());

		return super.getAdapter(type);
	}

	protected Diagram getDiagram() {
		return diagram;
	}

	public PropertySheetPage getPropertySheetPage() {
		return page;
	}

	/**
	 * We might have to pass in the platform that we are dealing with at the
	 * mean time.
	 */
	protected PaletteRoot getPaletteRoot() {
		return new PaletteRoot();
	}

	/**
	 * Because we have to pass in the platform from the diagram...
	 * 
	 * @return
	 */
	private PaletteRoot createPlatformPalette() {
		if (PALETTE_ROOT == null) {
			PALETTE_ROOT = GUIEditorPaletteFactory.createPalette(diagram);
		}
		return PALETTE_ROOT;
	}

	public void createPalette() {
		getEditDomain().setPaletteRoot(createPlatformPalette());
	}

	public void gotoMarker(IMarker marker) {
	}

	public IFile getFile() {
		return ((IFileEditorInput) getEditorInput()).getFile();
	}

	protected void handleActivationChanged(Event event) {
		IAction copy = null;
		if (event.type == SWT.Deactivate)
			copy = getActionRegistry().getAction(ActionFactory.COPY.getId());
		if (getEditorSite().getActionBars().getGlobalActionHandler(
				ActionFactory.COPY.getId()) != copy) {
			getEditorSite().getActionBars().setGlobalActionHandler(
					ActionFactory.COPY.getId(), copy);
			getEditorSite().getActionBars().updateActionBars();
		}
	}

	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(getDiagram());
//		getGraphicalViewer()
//		.addDropTargetListener(
//				(TransferDropTargetListener) new TemplateTransferDropTargetListener(
//						getGraphicalViewer()));
		getGraphicalViewer()
		.addDropTargetListener(
				(TransferDropTargetListener) new MyTemplateTransferDropTargetListener(
						getGraphicalViewer(), getDiagram()));
//		getGraphicalViewer().setSelection(new StructuredSelection(elements))
		/*
		 * getGraphicalViewer().addDropTargetListener((TransferDropTargetListener
		 * ) new TextTransferDropTargetListener( getGraphicalViewer(),
		 * TextTransfer.getInstance()));
		 */
	}

	protected FigureCanvas getEditor() {
		return (FigureCanvas) getGraphicalViewer().getControl();
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	protected void setInput(IEditorInput input) {
		superSetInput(input);
		try {
			setDiagram(DiagramFileUtil.getDiagram(getFile()));
		} catch (Exception e) {

			getSite().getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					MessageDialog.openWarning(getSite().getShell(), "Incompatible FUI file",
							"The file you are trying to open is incompatile with the current ForgedUI version. This means you used version 1.1 or below to generate this FUI file. We no longer support files generated using this version.");
				}
			});

			throw new RuntimeException("Can't open file", e);

		}
	}

	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;

		// After this we will try to re create the palette.
		this.createPalette();
	}

	/**
	 * Once its disposed we shall close active editor.
	 */
	public void dispose() {
		// Also removes the resource listener
		// when we dispose this editor.
		((IFileEditorInput) getEditorInput()).getFile().getWorkspace()
				.removeResourceChangeListener(resourceListener);
		super.dispose();
	}

	protected IContentOutlinePage getOutlinePage() {
		if (null == outline && null != getGraphicalViewer()) {
			TreeViewer viewer = new TreeViewer();
			viewer.setEditDomain(getEditDomain());
			outline = new DiagramOutlinePage2(this, viewer, getDiagram(),
					getGraphicalViewer().getRootEditPart());
		}
		return outline;
	}

	protected void superSetInput(IEditorInput input) {
		// The workspace never changes for an editor. So, removing and re-adding
		// the
		// resourceListener is not necessary. But it is being done here for the
		// sake
		// of proper implementation. Plus, the resourceListener needs to be
		// added
		// to the workspace the first time around.
		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().removeResourceChangeListener(resourceListener);
		}

		super.setInput(input);

		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().addResourceChangeListener(resourceListener);
			setPartName(getFile().getName());
		}
	}

	// PS: Totally taken from the example of the logic editor.
	// This class listens to changes to the file system in the workspace, and
	// makes changes accordingly.
	// 1) An open, saved file gets kd -> close the editor
	// 2) An open file gets renamed or moved -> change the editor's input
	// accordingly
	class ResourceTracker implements IResourceChangeListener,
			IResourceDeltaVisitor {
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			try {
				if (delta != null)
					delta.accept(this);
			} catch (CoreException exception) {
				// What should be done here?
			}
		}

		public boolean visit(IResourceDelta delta) {
			if (delta == null
					|| !delta.getResource().equals(
							((IFileEditorInput) getEditorInput()).getFile()))
				return true;

			if (delta.getKind() == IResourceDelta.REMOVED) {
				Display display = getSite().getShell().getDisplay();
				if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) { // if
																			// the
																			// file
																			// was
																			// deleted
					// NOTE: The case where an open, unsaved file is deleted is
					// being handled by the
					// PartListener added to the Workbench in the initialize()
					// method.
					display.asyncExec(new Runnable() {
						public void run() {
							// if (!isDirty())
							closeEditor(false);
						}
					});
				} else { // else if it was moved or renamed
					final IFile newFile = ResourcesPlugin.getWorkspace()
							.getRoot().getFile(delta.getMovedToPath());
					display.asyncExec(new Runnable() {
						public void run() {
							superSetInput(new FileEditorInput(newFile));
						}
					});
				}
			} else if (delta.getKind() == IResourceDelta.CHANGED) {
				if (!editorSaving) {
					// the file was overwritten somehow (could have been
					// replaced by another
					// version in the respository)
					final IFile newFile = ResourcesPlugin.getWorkspace()
							.getRoot().getFile(delta.getFullPath());
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {
						public void run() {
							setInput(new FileEditorInput(newFile));
							getCommandStack().flush();
						}
					});
				}
			}
			return false;
		}
	}

	@Override
	protected void createActions() {
		super.createActions();
		getSelectionActions().remove(ActionFactory.DELETE.getId());
		//
		ActionRegistry registry = getActionRegistry();
		//
		IAction action = new DeleteAction((IWorkbenchPart) this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new CopyElementAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new PasteElementAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

}
