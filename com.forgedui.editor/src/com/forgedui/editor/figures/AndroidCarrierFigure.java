// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AndroidCarrierFigure extends CarrierFigure {
	
	private static Color background = ColorConstants.black;
	
	private int time_width;
	
	Image signal3G;

	public AndroidCarrierFigure(){
		setTextHorisontalAlign(Alignments.right);
	}
	
	@Override
	public Color getBackgroundColor_() {
		return background;
	}

	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		String time = getTime();
		time_width = FigureUtilities
			.getStringExtents(time, graphics.getFont()).width;
		graphics.setForegroundColor(ColorConstants.white);
		this.paintString(graphics, time, getBounds());
		this.paintRightImage(graphics);
		this.paintSignalImage(graphics);
		this.paintSignal3GImage(graphics);
	}
	
	protected void paintRightImage(Graphics graphics) {
		if (batteryImage != null){
			Rectangle dest = super.getTextHolderRectangle();
			dest.translate(dest.width - IMAGE_SIZE
					- 2*getMargin() - time_width,
					(dest.height - IMAGE_SIZE) / 2);
			dest.setSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
			graphics.drawImage(batteryImage, new Rectangle(0, 0, batteryImage.getImageData().width,
							batteryImage.getImageData().height), dest);
		}
	}
	
	protected void paintSignalImage(Graphics graphics) {
		if (signalImage != null){
			Rectangle dest = super.getTextHolderRectangle();
			dest.translate(dest.width - IMAGE_SIZE*2
					- 3*getMargin() - time_width,
					(dest.height - IMAGE_SIZE) / 2);
			dest.setSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
			graphics.drawImage(signalImage, new Rectangle(0, 0, signalImage.getImageData().width,
					signalImage.getImageData().height), dest);
		}
	}
	
	protected void paintSignal3GImage(Graphics graphics) {
		if (signal3G != null){
			Rectangle dest = super.getTextHolderRectangle();
			dest.translate(dest.width - IMAGE_SIZE*3
					- 4*getMargin() - time_width,
					(dest.height - IMAGE_SIZE) / 2);
			dest.setSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
			graphics.drawImage(signal3G, new Rectangle(0, 0, signal3G.getImageData().width,
					signal3G.getImageData().height), dest);
		}
	}

	public Image getSignal3G() {
		return signal3G;
	}

	public void setSignal3G(Image signal3g) {
		signal3G = signal3g;
	}
	
}
