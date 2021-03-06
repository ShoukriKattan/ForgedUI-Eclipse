// LICENSE
package com.forgedui.editor.view;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class DiagramOutlinePage extends Page implements IContentOutlinePage {

	/** the control of the overview */
	private Canvas overview;
	/** the root edit part */
	private ScalableFreeformRootEditPart rootEditPart;
	/** the thumbnail */
	private Thumbnail thumbnail;

	/* *
	 * Creates a new OverviewOutlinePage instance.
	 * 
	 * @param rootEditPart the root edit part to show the overview from
	 */
	public DiagramOutlinePage(ScalableFreeformRootEditPart rootEditPart) {
		super();
		this.rootEditPart = rootEditPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ISelectionProvider#addSelectionChangedListener
	 * (ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		// create canvas and lws
		overview = new Canvas(parent, SWT.NONE);
		LightweightSystem lws = new LightweightSystem(overview);
		// create thumbnail
		thumbnail = new ScrollableThumbnail((Viewport) rootEditPart.getFigure());
		thumbnail.setBorder(new MarginBorder(3));
		thumbnail.setSource(rootEditPart
				.getLayer(LayerConstants.PRINTABLE_LAYERS));
		lws.setContents(thumbnail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		if (null != thumbnail)
			thumbnail.deactivate();
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() {
		return overview;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		return StructuredSelection.EMPTY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ISelectionProvider#removeSelectionChangedListener
	 * (ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() {
		if (getControl() != null)
			getControl().setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ISelectionProvider#setSelection(ISelection)
	 */
	public void setSelection(ISelection selection) {}

}
