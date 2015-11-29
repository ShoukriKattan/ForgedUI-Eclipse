package com.forgedui.editor.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * A special border for the feedback elements of the parent.
 * 
 * This is because I don't want to use the XOR mode, because
 * its missing things up at our drawing...
 * 
 * @author Tareq Doufish
 *
 */
public class FeedbackBorder extends AbstractBorder {

	protected static Insets insets = new Insets(1, 1, 1, 1);
	
	/** Those are the colors for the allowed and the not allowed colors */
	private static final Color allowedColor = ColorConstants.green;
	
	public static final Color notAllowedColor = ColorConstants.red;
	
	private Color selectedColor = allowedColor;
	
	@Override
	public Insets getInsets(IFigure figure) {
		return insets;
	}

	@Override
	public void paint(IFigure figure, Graphics g, Insets in) {
		Rectangle r = figure.getBounds().getCropped(in);
		g.setLineStyle(Graphics.LINE_DASHDOT);
		g.setLineWidth(3);
		g.setForegroundColor(selectedColor);
		g.drawLine(r.x, r.y , r.right() , r.y );
		g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
		g.drawLine(r.x, r.y, r.x, r.bottom());
		g.drawLine(r.right(), r.bottom(), r.right() , r.y );
		r.crop(getInsets(figure));
	}
	
	public void changeAllowedColor(boolean allowed) { 
		if (allowed ) { 
			selectedColor = allowedColor;
		} else { 
			selectedColor = notAllowedColor;
		}
	}

}
