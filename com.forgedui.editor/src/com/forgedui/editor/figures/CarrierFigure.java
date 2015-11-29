// LICENSE
package com.forgedui.editor.figures;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.forgedui.util.ScreenManager;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public abstract class CarrierFigure extends TitaniumTextFigure {
		
	protected static final int IMAGE_SIZE = 16;
	
	Image signalImage, batteryImage;
	
	public CarrierFigure(){
		setBorder(new TitaniumBackgroundBorder(
				getBackgroundColor_(),
				ColorConstants.black, 0, 1, true));
		
		setSize(new Dimension(0, ScreenManager.CarrierHeight));
	}
	
	public abstract Color getBackgroundColor_();
	
	public String getTime(){
		return new SimpleDateFormat("h:mm a")
			.format(Calendar.getInstance().getTime());
	}

	public Image getSignalImage() {
		return signalImage;
	}

	public void setSignalImage(Image signalImage) {
		this.signalImage = signalImage;
	}

	public Image getBatteryImage() {
		return batteryImage;
	}

	public void setBatteryImage(Image batteryImage) {
		this.batteryImage = batteryImage;
	}
	
	
}
