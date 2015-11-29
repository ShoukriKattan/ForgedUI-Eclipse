package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public abstract class MobileScreenFigure extends TitaniumFigure {

	private Dimension resolution;
	
	protected abstract Insets getPortraitInsets();
	
	protected Insets getLandscapeInsets(){
		return new Insets(
				getPortraitInsets().right,
				getPortraitInsets().top,
				getPortraitInsets().left,
				getPortraitInsets().bottom);
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setAntialias(SWT.ON);
		super.paint(graphics);
	}

	public void setResolution(Dimension resolution) {
		this.resolution = resolution;
		if (resolution.height > resolution.width){
			setInsets(getPortraitInsets());
		} else {
			setInsets(getLandscapeInsets());
		}
	}

	public Dimension getResolution() {
		return resolution;
	}

	@Override
	public Rectangle getBounds() {

		Dimension d = getResolution();

		int deviceHeight = d.height + insets.getHeight();
		int deviceWidth = d.width + insets.getWidth();

		/*if (deviceHeight < deviceWidth) {
			return new Rectangle(0, 0, deviceHeight, deviceWidth);
		}*/

		return new Rectangle(0, 0, deviceWidth, deviceHeight);

	}
	
	@Override
	protected void paintChildren(Graphics graphics) {
		graphics.clipRect(getClientArea(new Rectangle()));
		super.paintChildren(graphics);
	}

}
