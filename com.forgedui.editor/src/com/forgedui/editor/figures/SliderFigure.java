package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.forgedui.util.Utils;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SliderFigure extends TitaniumTextFigure {
	
	/**
	 * The value from 0 to 1
	 */
	private double value = 1f;
	
	private Image leftTrackImage, rightTrackImage;
	
	protected static final int sliderWidth = 20;
	
	protected static final int topMargin = 4;
	
	protected Insets borderInsets = new Insets(topMargin,sliderWidth / 2,
			topMargin, sliderWidth / 2);
	
	private Color valueColor;
	
	public SliderFigure(Color valueColor){
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
		getBorder().paintBackground(this, graphics,	borderInsets);

		//now we need to draw border with another color
		//to imitate value
		Rectangle bounds = getBounds().getCopy().crop(borderInsets);
		bounds.width *= this.value;
		//bounds.shrink(0, borderInsets.top);
		
		int radius = Math.min(Math.min(bounds.height, bounds.width), (int)getBorder().getBorderRadius()*2);
		graphics.setBackgroundColor(valueColor);
		graphics.fillRoundRectangle(bounds, radius, radius);
		graphics.drawRoundRectangle(bounds, radius, radius);
		
		graphics.setBackgroundColor(getBorder().getBackgroundColor());
		
		drawCircle(graphics, bounds);
		
	}
	
	protected void drawCircle(Graphics graphics, Rectangle bounds){
		//draw circle
		int x = Math.min(Math.max(bounds.right() - sliderWidth / 2, 0), getBounds().right() - sliderWidth);
		graphics.fillArc(x, getBounds().y, sliderWidth,
				getBounds().height, 0, 360);
		graphics.drawArc(x, getBounds().y, sliderWidth,
				getBounds().height, 0, 360);
	}


}