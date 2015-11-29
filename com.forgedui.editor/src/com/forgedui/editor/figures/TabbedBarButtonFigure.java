// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;




/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TabbedBarButtonFigure extends ButtonFigure {
	
	private Color fgColor;
	/**
	 * @param baseDoc
	 */
	public TabbedBarButtonFigure() {
		setText("Tab");
	}
	
	protected void paintString(Graphics graphics, String text, Rectangle parentRect) {
		Color oldFG = graphics.getForegroundColor(); 
		if(fgColor != null)
			graphics.setForegroundColor(fgColor);
		super.paintString(graphics, text, parentRect);
		graphics.setForegroundColor(oldFG);
	}

	public Color getFgColor() {
		return fgColor;
	}

	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}
	
}
