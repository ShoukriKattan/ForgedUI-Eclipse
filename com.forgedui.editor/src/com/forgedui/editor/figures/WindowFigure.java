// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class WindowFigure extends TitaniumFigure {
	
	private int titleBarHeight = 0;

	public WindowFigure() {
		
	}
	
	public void setTitleBarHeight(int titleBarHeight){
		this.titleBarHeight = titleBarHeight;
	}
	
	public int getTitleBarHeight(){
		return this.titleBarHeight;
	}
	
	@Override
	public Insets getInsets() {
		return super.getInsets().getAdded(new Insets(titleBarHeight, 0,0,0));
	}

	@Override
	public Rectangle getClientArea(Rectangle rect) {
		rect.setBounds(getBounds());
		rect.crop(super.getInsets());
		rect.setLocation(0, -titleBarHeight);
		return rect;
	}

}
