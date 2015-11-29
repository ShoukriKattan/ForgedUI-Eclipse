// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SliderFigureAndroid extends SliderFigure {

	/**
	 * @param valueColor
	 */
	public SliderFigureAndroid(Color valueColor) {
		super(valueColor);
		setValue(0f);
		borderInsets = new Insets(topMargin,sliderWidth / 4,
				topMargin, sliderWidth / 4);
	}
	
	@Override
	protected void drawCircle(Graphics graphics, Rectangle bounds) {
		//draw circle
		int x = Math.min(Math.max(bounds.right() - sliderWidth / 4, 0), getBounds().right() - sliderWidth/2);
		graphics.fillRoundRectangle(new Rectangle(x, getBounds().y, sliderWidth / 2 + 1,
				getBounds().height), 5, 5);
		graphics.drawRoundRectangle(new Rectangle(x, getBounds().y, sliderWidth / 2,
				getBounds().height - 1), 5, 5);
	}

}
