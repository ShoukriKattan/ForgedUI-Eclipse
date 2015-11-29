/**
 * 
 */
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * @author shoukry
 * 
 */
public class IPhoneMobileScreenFigure extends ScreenFigure {

	private Color silver;
	private Color black;

	private Color cameraBlue;
	private Color cameraGray;
	private Color speakerExtGray;
	private Color speakerGray;
	private Color buttonExtGray;
	private Color buttonIntGray;

	public IPhoneMobileScreenFigure() {

		silver = new Color(Display.getCurrent(), 192, 192, 192);
		black = new Color(Display.getCurrent(), 0, 0, 0);
		cameraBlue = new Color(Display.getCurrent(), 47, 47, 79);
		cameraGray = new Color(Display.getCurrent(), 46, 46, 46);

		speakerExtGray = new Color(Display.getCurrent(), 38, 38, 38);
		speakerGray = new Color(Display.getCurrent(), 74, 74, 74);

		buttonExtGray = new Color(Display.getCurrent(), 38, 38, 38);
		buttonIntGray = new Color(Display.getCurrent(), 74, 74, 74);
		
		// bounds = new Rectangle(0, 0, 400, 800);

		// setBorder(new TitaniumBackgroundBorder(black, silver, 5, 50, true));

	}

	@Override
	protected void paintFigure(Graphics graphics) {

		Dimension dim = getResolution().getCopy();
		if (dim.width > dim.height){
			graphics.translate(0, dim.height + 60);
			graphics.rotate(-90);
			dim.transpose();
		}


		int height = dim.height;
		int width = dim.width;

		int fullDeviceWidth = 60 + width;
		int fullDeviceHeight = height + 220;

		int arcWidth = (int) ((double) height * (double) 0.15);
		graphics.setBackgroundColor(silver);
		graphics.fillRoundRectangle(new Rectangle(0, 0, fullDeviceWidth,
				fullDeviceHeight), arcWidth, arcWidth);

		int innerHeight = height + 210;
		int innerWidth = width + 50;

		graphics.setBackgroundColor(black);
		graphics.fillRoundRectangle(
				new Rectangle(5, 5, innerWidth, innerHeight), arcWidth,
				arcWidth);

		graphics.setBackgroundColor(new Color(Display.getCurrent(), 255, 255,
				255));
		graphics.fillRectangle(30, 105, width, height);

		// 80 is speaker width + 14 is camera width + 40 distance between
		int xSpeaker = Math.max(60,(fullDeviceWidth / 2) - ((94) / 2));
		int ySpeaker = 105 / 2;

		int xButton = (int) ((60. + width)
				/2 - 75. / 2);
		int yButton = 110 + height + 55 - 75 / 2;

		int sqrXOffset = 23;
		int sqrYOffset = 23;

		graphics.setBackgroundColor(cameraGray);
		graphics.fillArc(new Rectangle(xSpeaker - 40, ySpeaker, 14, 14), 0, 360);
		graphics.setBackgroundColor(cameraBlue);
		graphics.fillArc(new Rectangle(xSpeaker - 37, ySpeaker + 3, 8, 8), 0,
				360);

		graphics.setForegroundColor(speakerExtGray);
		graphics.drawRoundRectangle(new Rectangle(xSpeaker, ySpeaker, 80, 14),
				15, 15);

		graphics.setBackgroundColor(speakerGray);
		graphics.fillRoundRectangle(new Rectangle(xSpeaker + 6, ySpeaker + 2,
				70, 10), 15, 15);

		// Button external round
		graphics.setForegroundColor(buttonExtGray);
		graphics.drawArc(xButton, yButton, 75, 75, 0, 360);

		// in Button gray
		graphics.setForegroundColor(buttonIntGray);
		graphics.drawRoundRectangle(new Rectangle(xButton + sqrXOffset, yButton
				+ sqrYOffset, 30, 30), 10, 10);

		graphics.rotate(30);

		graphics.restoreState();
		
		super.paintFigure(graphics);
	}

	@Override
	protected Insets getPortraitInsets() {
		return new Insets(105, 30, 140, 30);
	}

}
