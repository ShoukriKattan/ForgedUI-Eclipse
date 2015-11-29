package com.forgedui.editor.edit.policy;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.swt.graphics.Color;

import com.forgedui.editor.figures.TabbedBarButtonFigure;
import com.forgedui.model.titanium.TabbedBarButton;

/**
 * A policy that will create the feedback item of the figure only 
 * used with the tabbed stuff.
 * 
 * @author Tareq Doufish
 *
 */
public class TabbedBarFeedbackEditPolicy extends ResizableEditPolicy {

	protected IFigure createDragSourceFeedbackFigure() {
		IFigure figure = createFigure((GraphicalEditPart) getHost(), null);
		
		figure.setBounds(getInitialFeedbackBounds());
		
		addFeedback(figure);
		return figure;
	}
	
	protected IFigure createFigure(GraphicalEditPart part, IFigure parent) {
		IFigure child = getCustomFeedbackFigure(part.getModel());

		if (parent != null)
			parent.add(child);

		Rectangle childBounds = part.getFigure().getBounds().getCopy();

		if (childBounds != null)
			child.setBounds(childBounds);

		return child;
	}
	
	public final static Color ghostFillColor = new Color(null, 31, 31, 31);
	
	protected IFigure getCustomFeedbackFigure(Object modelPart) {
		IFigure figure = null;
		if (modelPart instanceof TabbedBarButton) {
			figure = new RectangleFigure();
			((RectangleFigure) figure).setXOR(true);
			((RectangleFigure) figure).setFill(true);
			figure.setBackgroundColor(ghostFillColor);
			figure.setForegroundColor(ColorConstants.white);
		}

		return figure;
	}
	
	protected Rectangle getInitialFeedbackBounds() {
		Rectangle bounds = getHostFigure().getBounds().getCopy();
		if (getHostFigure() instanceof TabbedBarButtonFigure) {
			// This is another workaround...
			if (bounds.x > ( getHostFigure().getParent().getBounds().x + 10) ) { 
				bounds.x -= getHostFigure().getBounds().x;
			}
			bounds.width = getHostFigure().getParent().getBounds().width;
			bounds.height = getHostFigure().getParent().getBounds().height;
		}
		return bounds;
	}
}
