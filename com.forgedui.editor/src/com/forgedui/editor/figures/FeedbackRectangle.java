package com.forgedui.editor.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;

/**
 * Feedback rectangle will draw the border 
 * when you try to drag and drop components 
 * at the system, mainly will work with the containers.
 * 
 * @author Tareq Doufish
 *
 */
public class FeedbackRectangle extends Figure {

	private FeedbackBorder border = new FeedbackBorder();
	
	/**
	 * We will be just painting the borders here.
	 */
	protected void paintFigure(Graphics g) {
		border.paint(this, g, new Insets());
	}
	
	public void setAllowed(boolean allowed) { 
		border.changeAllowedColor(allowed);
	}
	
}
