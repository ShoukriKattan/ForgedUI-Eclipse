// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class RoundedBorder extends AbstractBorder {
		
	private int defaultWidth;
	private int defaultRadius;
	private Color defaultColor;
	
	private Float width;
	private Float radius;
	private Color color;
	
	private Insets insets = new Insets(0);
	
	public RoundedBorder(Color color, int width, int radius) {
		Assert.isNotNull(color);
		defaultColor = color;
		defaultWidth = width;
		defaultRadius = radius;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color != null ? color : defaultColor;
	}
	
	public void setWidth(Float width) {
		this.width = width;
	}
	
	public float getWidth() {
		return width != null ? width : defaultWidth;
	}
	
	public void setRadius(Float radius) {
		this.radius = radius;
	}
	
	public int getRadius() {
		return radius != null ? radius.intValue() : defaultRadius;
	}

	@Override
	public Insets getInsets(IFigure figure) {
		return insets;
	}
	
	@Override
	public boolean isOpaque() {
		return true;
	}
	
	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		if (getWidth() > 0){
			graphics.setForegroundColor(getColor());
			graphics.setBackgroundColor(getColor());
			tempRect.setBounds(getPaintRectangle(figure, insets));
			int r = getRadius()*2;
			int width = (int)getWidth();
			if (r == 0){//workaround for line width problem
				/* paint rectangles doesn't work for 1 pixel width with zoom*/
				Path border = new Path(null);
				border.moveTo(tempRect.x, tempRect.y);
				border.lineTo(tempRect.right(), tempRect.y);
				border.lineTo(tempRect.right(), tempRect.bottom());
				border.lineTo(tempRect.x, tempRect.bottom());
				border.lineTo(tempRect.x, tempRect.y);
				border.lineTo(tempRect.x + width, tempRect.y + width);
				border.lineTo(tempRect.x + width, tempRect.bottom() - width);
				border.lineTo(tempRect.right() - width, tempRect.bottom() - width);
				border.lineTo(tempRect.right() - width, tempRect.y + width);
				border.lineTo(tempRect.x + width, tempRect.y + width);

				graphics.fillPath(border);
			} else {
				float externalWidth = Math.min(getRadius(), Math.min(tempRect.width, tempRect.height)) * 2;
				float internalWidth = Math.max(externalWidth - getWidth() * 2, 0);
				Path leftTop = new Path(null);
				leftTop.addArc(tempRect.x, tempRect.y, externalWidth, externalWidth, 90, 90);
				leftTop.addArc(tempRect.x + getWidth(), tempRect.y + getWidth(),
						internalWidth, internalWidth, 180, -90);
				
				Path rightTop = new Path(null);
				rightTop.addArc(tempRect.right() - externalWidth, tempRect.y, externalWidth, externalWidth, 0, 90);
				rightTop.addArc(tempRect.right() - internalWidth - getWidth(), tempRect.y + getWidth(),
						internalWidth, internalWidth, 90, -90);
				
				Path leftBottom = new Path(null);
				leftBottom.addArc(tempRect.x, tempRect.bottom() - externalWidth, externalWidth, externalWidth, 180, 90);
				leftBottom.addArc(tempRect.x + getWidth(), tempRect.bottom() - internalWidth - getWidth(),
						internalWidth, internalWidth, 270, -90);
				
				Path rightBottom = new Path(null);
				rightBottom.addArc(tempRect.right() - externalWidth, tempRect.bottom()- externalWidth,
						externalWidth, externalWidth, 0, -90);
				rightBottom.addArc(tempRect.right() - internalWidth - getWidth(), tempRect.bottom() - internalWidth- getWidth(),
						internalWidth, internalWidth, 270, 90);
				
				graphics.fillPath(leftTop);
				graphics.fillPath(rightTop);
				graphics.fillPath(leftBottom);
				graphics.fillPath(rightBottom);
				
				Path left = new Path(null);
				left.addRectangle(tempRect.x, tempRect.y + externalWidth / 2 , width, tempRect.height - externalWidth);

				Path right = new Path(null);
				right.addRectangle(tempRect.right() - width, tempRect.y + externalWidth / 2 , width, tempRect.height - externalWidth);
				
				Path top = new Path(null);
				top.addRectangle(tempRect.x + externalWidth / 2, tempRect.y, tempRect.width - externalWidth, width);
				
				Path bottom = new Path(null);
				bottom.addRectangle(tempRect.x + externalWidth / 2, tempRect.bottom() - width, tempRect.width - externalWidth, width);
			
				graphics.fillPath(left);
				graphics.fillPath(right);
				graphics.fillPath(top);
				graphics.fillPath(bottom);
			}
		}
	}

}
