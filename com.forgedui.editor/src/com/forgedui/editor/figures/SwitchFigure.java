package com.forgedui.editor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.forgedui.util.Utils;



/**
 * A figure for the switch element.
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SwitchFigure extends TitaniumTextFigure {
	
	private static RGB RECT_B_RGB = new RGB(0xFB, 0xFB, 0xFB);
	private static RGB RECT_T_RGB = new RGB(0xCC, 0xCC, 0xCC);
	
	private boolean value;
		
	public SwitchFigure() {
		setText("OFF");
	}
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		if (isON()){
			graphics.setForegroundColor(ColorConstants.white);
		}
		super.paintTitaniumFigure(graphics);
		Rectangle rect = getBounds().getCopy();
		//rect.shrink((int)getBorder().getBorderWidth(), (int)getBorder().getBorderWidth());
		if (isON()){
			rect.x = rect.right() - rect.height;
		}
		rect.width = rect.height;
		Image image = Drawer.getGradientRectangle(new PrecisionRectangle(rect.getCopy()),
				RECT_B_RGB, RECT_T_RGB, rect.height / 2, rect.height, rect.height);
		graphics.drawImage(image, rect.getLocation());
		image.dispose();
	}
	
	@Override
	protected Rectangle getTextHolderRectangle() {
		Rectangle halfRect = super.getTextHolderRectangle();
		if (!isON()){
			halfRect.x = halfRect.x + halfRect.height;
		}
		halfRect.width -= halfRect.height;
		return halfRect;
	}
	
	protected boolean isON(){
		return value;
	}
	
	public void setValue(boolean value){
		if (Utils.safeNotEquals(this.value, value)){
			this.value = value;
			setDirty(true);
		}
	}
	
	
}
