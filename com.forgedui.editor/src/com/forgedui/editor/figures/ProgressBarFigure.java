// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ProgressBarFigure extends TitaniumTextFigure {
	
	/**
	 * The value from 0 to 1
	 */
	private double value = 0.0f;
	
	private Color valueColor;
	
	public ProgressBarFigure(Color valueColor){
		this.valueColor = valueColor;
	}

	public void setValue(double value) {
		if (Utils.safeNotEquals(this.value, value)){
			this.value = value;
			setDirty(true);
		}
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		
		//this will draw main border
		super.paintFigure(graphics);
		//now we need to draw border with another color
		//to imitate value
		Rectangle bounds = getBounds().getCopy();
		bounds.width *= this.value;
		
		
		if (bounds.width > 0){
			int radius = Math.min(Math.min(bounds.height, bounds.width), (int)getBorder().getBorderRadius()*2);
			graphics.setBackgroundColor(valueColor);
			graphics.setForegroundColor(valueColor);
			graphics.fillRoundRectangle(bounds, radius, radius);
			graphics.drawRoundRectangle(bounds, radius, radius);
		}
	}


}
