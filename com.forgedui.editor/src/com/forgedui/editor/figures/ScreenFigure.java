// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.forgedui.util.ScreenManager;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public abstract class ScreenFigure extends TitaniumImageFigure {
	
	private CarrierFigure cf;
	
	public ScreenFigure() {
	}
	
	/**
	 * @param baseDoc
	 */
	public ScreenFigure(Image image) {
		super(image);
	}
	
	public void setCarrierFigure(CarrierFigure cf){
		this.cf = cf;
	}
	
	@Override
	public Insets getInsets() {
		if (cf != null){
			return insets.getAdded(new Insets(ScreenManager.CarrierHeight,0,0,0));
		}
		return insets;
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		Rectangle r = getWindowRect();
		r.translate(insets.left, insets.top);
		if (cf != null){
			cf.setLocation(r.getLocation());
			cf.setSize(r.width, ScreenManager.CarrierHeight);
			cf.paintFigure(graphics);
		}
	}
	
	public void setSignalImage(Image image){
		if (cf != null){
			cf.signalImage = image;
		}
	}
	
	public void setSignal3GImage(Image image){
		if (cf != null){
			((AndroidCarrierFigure)cf).signal3G = image;
		}
	}
	
	public void setBatteryImage(Image image){
		if (cf != null){
			cf.batteryImage = image;
		}
	}
	
	protected void paintChildren(Graphics graphics) {
		graphics.clipRect(getWindowRect());
		super.paintChildren(graphics);
	}
	
	protected Rectangle getWindowRect(){
		return new Rectangle(getClientArea(new Rectangle()));
	}
	
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
	
}
