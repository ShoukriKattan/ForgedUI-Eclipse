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
public class AndroidMobileScreenFigure extends ScreenFigure {

	private Color speakerExtGray;
	private Color speakerGray;
	private Color whiteColor;
	private Color black;
	private Color creameColor;
	private Color deviceColor;

	public AndroidMobileScreenFigure() {

		whiteColor = new Color(Display.getCurrent(), 255, 255, 255);
		speakerGray = new Color(Display.getCurrent(), 46, 46, 46);
		black = new Color(Display.getCurrent(), 0, 0, 0);
		creameColor = new Color(Display.getCurrent(), 232, 232, 232);
		deviceColor = new Color(Display.getCurrent(), 92, 64, 51);
		speakerExtGray = new Color(Display.getCurrent(), 38, 38, 38);
		speakerGray = new Color(Display.getCurrent(), 74, 74, 74);
		
	}

	@Override
	protected void paintFigure(Graphics graphics) {

		Dimension dim = getResolution().getCopy();
		
		if (dim.width > dim.height){
			graphics.translate(0, dim.height + 80);
			graphics.rotate(-90);
			dim.transpose();
		}

		int height = dim.height;
		int width = dim.width;

		int beginPointX = 0;
		int beginPointY = 0;
		int deviceHeight = height + 220;
		int deviceWidth = width + 80;

		int beginInnerFrameX = 0 + 10;
		int beginInnerFrameY = 0 + 30;
		int innerFrameHeight = deviceHeight - 100;
		int innerFrameWidth = deviceWidth - 20;

		int beginWindowX = 0 + 40;
		int beginWindowY = 0 + 80;

		graphics.setBackgroundColor(deviceColor);
		graphics.fillRoundRectangle(new Rectangle(beginPointX, beginPointY,
				deviceWidth, deviceHeight), 300, 120);

		graphics.setBackgroundColor(black);
		graphics.fillRoundRectangle(new Rectangle(beginInnerFrameX,
				beginInnerFrameY, innerFrameWidth, innerFrameHeight), 180, 100);

		graphics.setBackgroundColor(whiteColor);
		graphics.fillRectangle(beginWindowX, beginWindowY, width, height);

		drawDeviceSpeaker(graphics, deviceWidth);

		drawFunctionButtons(graphics, height, width, beginWindowX, beginWindowY);

		drawDeviceButton(graphics, deviceHeight, deviceWidth);

		graphics.restoreState();
		
		super.paintFigure(graphics);
	}

	protected void drawFunctionButtons(Graphics graphics, int height,
			int width, int beginWindowX, int beginWindowY) {
		int iconsYLocation = beginWindowY + height + 20;

		int homeIconWidth = 18;
		int menuIconWidth = 20;
		int searchIcon = 17;
		int backIcon = 28;

		int distanceBetween = (width - (homeIconWidth + menuIconWidth
				+ searchIcon + backIcon)) / 3;

		int iconsXLocation = beginWindowX;

		drawBackIcon(graphics, iconsXLocation, iconsYLocation);
		drawMenuIcon(graphics, iconsXLocation + backIcon + distanceBetween,
				iconsYLocation);
		drawHomeIcon(graphics, iconsXLocation + backIcon + menuIconWidth
				+ distanceBetween + distanceBetween, iconsYLocation);
		drawSearchIcon(graphics, iconsXLocation + backIcon + menuIconWidth
				+ homeIconWidth + distanceBetween + distanceBetween
				+ distanceBetween, iconsYLocation);
	}

	protected void drawDeviceSpeaker(Graphics graphics, int deviceWidth) {
		int widthSpeacker = Math.min(180, deviceWidth / 2);
		int xSpeaker = (deviceWidth - widthSpeacker) / 2;
		int ySpeaker = 15;

		graphics.setForegroundColor(black);
		graphics.setLineWidth(2);
		graphics.drawRoundRectangle(new Rectangle(xSpeaker, ySpeaker, widthSpeacker, 10),
				10, 10);
		graphics.setBackgroundColor(speakerGray);
		graphics.fillRoundRectangle(new Rectangle(xSpeaker, ySpeaker, widthSpeacker, 10),
				10, 10);
	}

	protected void drawDeviceButton(Graphics graphics, int deviceHeight,
			int deviceWidth) {
		int beginBlackButtonX = (deviceWidth / 2) - 25;
		int beginBlackButtonY = deviceHeight - 35 - 25;

		int beginCremeButtonX = (deviceWidth / 2) - 20;

		int beginCremeButtonY = deviceHeight - 35 - 20;

		int beginWhiteButtonX = (deviceWidth / 2) - 18;

		int beginWhiteButtonY = deviceHeight - 35 - 18;

		graphics.setForegroundColor(black);
		graphics.setLineWidth(6);
		graphics.drawArc(beginBlackButtonX, beginBlackButtonY, 50, 50, 0, 360);

		graphics.setForegroundColor(creameColor);
		graphics.setLineWidth(6);
		graphics.drawArc(beginCremeButtonX, beginCremeButtonY, 40, 40, 0, 360);

		graphics.setBackgroundColor(whiteColor);
		graphics.fillArc(beginWhiteButtonX, beginWhiteButtonY, 36, 36, 0, 360);
	}

	protected void drawSearchIcon(Graphics graphics, int searchIconX,
			int searchIconY) {

		graphics.setForegroundColor(speakerExtGray);
		graphics.setLineWidth(2);
		graphics.drawArc(new Rectangle(searchIconX, searchIconY + 3, 11, 11),
				0, 360);
		graphics.drawLine(searchIconX + 11, searchIconY + 12, searchIconX + 17,
				searchIconY + 18);
	}

	protected void drawBackIcon(Graphics graphics, int beginX, int beginY) {

		graphics.setForegroundColor(speakerExtGray);
		graphics.setLineWidth(2);
		graphics.drawArc(beginX - 11, beginY + 10, 30, 15, 0, 90);
		graphics.drawLine(beginX, beginY + 10, beginX + 7, beginY + 3);
		graphics.drawLine(beginX, beginY + 10, beginX + 7, beginY + 17);
	}

	protected void drawMenuIcon(Graphics graphics, int xMenu, int yMenu) {

		graphics.setForegroundColor(speakerExtGray);
		graphics.setLineWidth(2);
		graphics.drawLine(xMenu, yMenu + 5, xMenu + 20, yMenu + 5);
		graphics.drawLine(xMenu + 6, yMenu + 9, xMenu + 20, yMenu + 9);
		graphics.drawLine(xMenu + 6, yMenu + 13, xMenu + 20, yMenu + 13);
		graphics.drawLine(xMenu + 6, yMenu + 17, xMenu + 20, yMenu + 17);
	}

	protected void drawHomeIcon(Graphics graphics, int homeX, int homeY) {

		graphics.setForegroundColor(speakerExtGray);
		graphics.setLineWidth(2);
		// Floor
		graphics.drawLine(homeX + 4, homeY + 18, homeX + 14, homeY + 18);

		// Walls
		graphics.drawLine(homeX + 4, homeY + 18, homeX + 4, homeY + 13);
		graphics.drawLine(homeX + 14, homeY + 18, homeX + 14, homeY + 13);

		// Shimney
		graphics.drawLine(homeX + 14, homeY + 9, homeX + 14, homeY + 4);

		// House roof
		graphics.drawLine(homeX + 9, homeY + 4, homeX + 18, homeY + 14);
		graphics.drawLine(homeX + 9, homeY + 4, homeX, homeY + 14);
	}

	@Override
	protected Insets getPortraitInsets() {
		return new Insets(80, 40, 140, 40);
	}

}
