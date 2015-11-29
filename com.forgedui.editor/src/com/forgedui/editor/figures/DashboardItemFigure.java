// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DashboardItemFigure extends ButtonFigure {
	
	private static final Insets borderInsets = new Insets(0, 0, 0, 0);
	
	public DashboardItemFigure(){
		setText("");
	}
	
	protected void paintFigure(Graphics graphics) {
		getBorder().paintBackground(this, graphics,	borderInsets);
		Drawer.drawBadge(graphics, getBounds(), getText());
	}
}
