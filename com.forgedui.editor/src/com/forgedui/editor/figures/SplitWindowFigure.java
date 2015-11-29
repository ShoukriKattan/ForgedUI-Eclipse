/**
 * 
 */
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author Zrieq
 *
 */
public class SplitWindowFigure extends TitaniumTextFigure {
	
	private boolean showMasterInPortrait;
	
	@Override
	protected void paintFigure(Graphics graphics) {
		Dimension dim = getSize().getCopy();
		
		int height = dim.height;
		int width = dim.width;
		
		int arcWidth = 10;
		
		Color grey = new Color(Display.getCurrent(), 204, 204, 204);
		Color greyer = new Color(Display.getCurrent(), 160, 160, 160);
		Color greyest = new Color(Display.getCurrent(), 80, 80, 80);
		Color white = new Color(Display.getCurrent(), 255, 255,255);
		
		Rectangle rect = new Rectangle(0, 0, width, 22);
		
		if(width < height && !showMasterInPortrait){
			Image img = Drawer.getGradientRectangle(new PrecisionRectangle(rect), greyer.getRGB(), white.getRGB(), 10.0f, SWT.TOP);
			
			graphics.setBackgroundColor(white);
			graphics.fillRoundRectangle(new Rectangle(0, 0, width, height), arcWidth,arcWidth);
			graphics.setBackgroundColor(grey);
			graphics.drawImage(img, 0, 0);
			graphics.restoreState();
			
			graphics.setForegroundColor(greyest);
			FontData[] oldFontData = graphics.getFont().getFontData();
			oldFontData[0].setHeight(8);
			oldFontData[0].setStyle(SWT.BOLD);
			Font smallerFont = new Font(graphics.getFont().getDevice(), oldFontData);
			graphics.setFont(smallerFont);
			paintString(graphics, "Detail", rect);
			
			rect = new Rectangle(3, 3, 70, 14);
			img = Drawer.getGradientRectangle(new PrecisionRectangle(rect), greyest.getRGB(), greyer.getRGB(), 5.0f, 0);
			graphics.drawImage(img, 3, 3);
			
			graphics.setForegroundColor(grey);
			oldFontData = graphics.getFont().getFontData();
			oldFontData[0].setHeight(5);
			oldFontData[0].setStyle(SWT.BOLD);
			smallerFont = new Font(graphics.getFont().getDevice(), oldFontData);
			graphics.setFont(smallerFont);
			paintString(graphics, "Master View List", rect.setX(2));
		}else{
			int masterWidth = (int) ((float)width * 0.4f);
			int detailWidth = width - masterWidth ;
			
			rect = new Rectangle(0, 0, masterWidth, 22);
			Image img = Drawer.getGradientRectangle(new PrecisionRectangle(rect), greyer.getRGB(), white.getRGB(), 5.0f, SWT.TOP);
			
			graphics.setBackgroundColor(white);
			graphics.fillRoundRectangle(new Rectangle(0, 0, masterWidth, height), arcWidth,arcWidth);
			graphics.setBackgroundColor(grey);
			graphics.drawImage(img, 0, 0);
			graphics.restoreState();
			
			graphics.setForegroundColor(greyest);
			FontData[] oldFontData = graphics.getFont().getFontData();
			oldFontData[0].setHeight(8);
			oldFontData[0].setStyle(SWT.BOLD);
			Font smallerFont = new Font(graphics.getFont().getDevice(), oldFontData);
			graphics.setFont(smallerFont);
			paintString(graphics, "Master", rect);
			
			rect = new Rectangle(masterWidth, 0, detailWidth, 22);
			img = Drawer.getGradientRectangle(new PrecisionRectangle(rect), greyer.getRGB(), white.getRGB(), 5.0f, SWT.TOP);
			
			graphics.setBackgroundColor(white);
			graphics.fillRoundRectangle(new Rectangle(masterWidth, 0, detailWidth, height), arcWidth,arcWidth);
			graphics.setBackgroundColor(grey);
			graphics.drawImage(img, masterWidth, 0);
			graphics.restoreState();
			
			graphics.setForegroundColor(greyest);
			oldFontData = graphics.getFont().getFontData();
			oldFontData[0].setHeight(8);
			oldFontData[0].setStyle(SWT.BOLD);
			smallerFont = new Font(graphics.getFont().getDevice(), oldFontData);
			graphics.setFont(smallerFont);
			paintString(graphics, "Detail", rect);
			
			graphics.setBackgroundColor(greyest);
			graphics.fillRectangle(masterWidth, 3, 1, height);
		}
	}

	public boolean isShowMasterInPortrait() {
		return showMasterInPortrait;
	}

	public void setShowMasterInPortrait(boolean showMasterInPortrait) {
		this.showMasterInPortrait = showMasterInPortrait;
	}
}
