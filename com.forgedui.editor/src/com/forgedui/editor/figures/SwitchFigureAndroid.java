// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SwitchFigureAndroid extends SwitchFigure {
	
	// Those two styles are only for the android editor, and not for the ipad or iphone.
	private static final Color greenColor = new Color(null, 0xa2, 0xec, 0x42);
	
	private static final Color grayColor = new Color(null, 0xea, 0xea, 0xea);
	
	private Image checkTrue;
	
	private boolean toggle;
	
	public SwitchFigureAndroid(Image checkTrue){
		this.checkTrue = checkTrue;
	}
	
	public void setToggle(boolean toggle){
		this.toggle = toggle;
		if (this.toggle){
			setTextHorisontalAlign(Alignments.center);
			setTextVerticalAlign(Alignments.top);
		} else {
			setTextHorisontalAlign(Alignments.left);
			setTextVerticalAlign(Alignments.center);
		}
	}
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		this.paintString(graphics, getText(), getTextHolderRectangle());
		if (toggle){
			graphics.setBackgroundColor(isON() ? greenColor : grayColor);
			
			Rectangle bounds = getBounds();
			Rectangle value = new Rectangle(bounds.getCenter().x - getMargin()/2 - 30,
					bounds.bottom() - getMargin()*2 - 22,
					60, 7);
			graphics.fillRoundRectangle(value, 5, 5);
		} else if (isON()) {
			Rectangle bounds = getBounds();
			int side = getImageSide();
			Rectangle dest = new Rectangle(bounds.x, bounds.getCenter().y, side, side);
			dest.translate(getMargin(), -side/2);
			if (Utils.isEmpty(getText())){
				dest.x = bounds.getCenter().x - side/2;
			}
			
			graphics.drawImage(checkTrue, new Rectangle(checkTrue.getBounds()), dest);
		}
	}

	protected int getImageSide() {
		Rectangle bounds = getBounds();
		int side = Math.min(bounds.width, bounds.height);
		side = Math.min(side, checkTrue.getBounds().height);
		side = Math.max(1, side - 10);
		return side;
	}
	
	@Override
	protected Rectangle getTextHolderRectangle() {
		Rectangle r = getClientArea();
		if (useLocalCoordinates()){
			r.setLocation(getLocation().x + getInsets().left,
					getLocation().y + getInsets().top);
		}
		if (toggle){
			return r;
		} else {
			r.width -= getImageSide() + getMargin()*2;
			r.x += getImageSide() + getMargin()*2;
			return r;
		}
		
	}

	@Override
	public void dispose() {
		super.dispose();
		checkTrue.dispose();
	}

}
