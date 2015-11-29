// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class IPadCarrierFigure extends CarrierFigure {
	
	protected static final int IMAGE_WIDTH = 18;
	
	private static final String CARRIER = "Carrier";

	private static Color background = new Color(null, 0, 0, 0);

	@Override
	public Color getBackgroundColor_() {
		return background;
	}
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		setTextHorisontalAlign(Alignments.center);
		Color oldFG = graphics.getForegroundColor();
		graphics.setForegroundColor(new Color(null, 204, 204, 204));
		this.paintString(graphics, getTime(), getBounds());
		setTextHorisontalAlign(Alignments.left);
		this.paintString(graphics, CARRIER, getBounds());
		graphics.setForegroundColor(oldFG);
		this.paintLeftImage(graphics);
		this.paintRightImage(graphics);
	}
	
	protected void paintLeftImage(Graphics graphics) {
		if (signalImage != null){
			Rectangle dest = super.getTextHolderRectangle();
			Dimension textSize = FigureUtilities.getStringExtents(CARRIER, graphics.getFont());
			dest.translate(textSize.width + getMargin()*2,
					(dest.height - IMAGE_SIZE) / 2);
			dest.setSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
			graphics.drawImage(signalImage, new Rectangle(0, 0, signalImage.getImageData().width,
					signalImage.getImageData().height), dest);
		}
	}
	
	protected void paintRightImage(Graphics graphics) {
		if (batteryImage != null){
			Rectangle dest = super.getTextHolderRectangle();
			dest.translate(dest.width - IMAGE_WIDTH - getMargin(),
					(dest.height - IMAGE_SIZE) / 2);
			dest.setSize(new Dimension(IMAGE_WIDTH, IMAGE_SIZE));
			graphics.drawImage(batteryImage, new Rectangle(0, 0, batteryImage.getImageData().width,
							batteryImage.getImageData().height), dest);
		}
	}

}
