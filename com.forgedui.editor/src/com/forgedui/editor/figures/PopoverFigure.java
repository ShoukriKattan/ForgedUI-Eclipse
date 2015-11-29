/**
 * 
 */
package com.forgedui.editor.figures;

import java.io.File;

import org.eclipse.draw2d.AbstractBackground;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Zrieq
 *
 */
public class PopoverFigure extends TitaniumTextFigure {
	
	public static Insets INSETS = new Insets(30, 19, 24, 19);
	
	public PopoverFigure(){
		setText("");
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		Color blackest = new Color(Display.getCurrent(), 0, 0, 0);
		Color blacker = new Color(Display.getCurrent(), 30, 30, 30);
		Color grey = new Color(Display.getCurrent(), 94, 94, 94);
		Rectangle rect = new Rectangle(getBounds());
		
		graphics.setBackgroundColor(grey);
		graphics.setAlpha((int)(255 * 0.2));
		graphics.fillRoundRectangle(rect, 10, 10);
		rect.x = rect.x + 5; rect.y = rect.y + 5; rect.width = rect.width - 10;rect.height = rect.height - 10;    
		graphics.setAlpha((int)(255 * 0.25));
		graphics.fillRoundRectangle(rect, 10, 10);
		rect.x = rect.x + 5; rect.y = rect.y + 5; rect.width = rect.width - 10;rect.height = rect.height - 10;
		graphics.setAlpha((int)(255 * 0.30));
		graphics.fillRoundRectangle(rect, 10, 10);
		rect.x = rect.x + 5; rect.y = rect.y + 5; rect.width = rect.width - 10;rect.height = rect.height - 15;
		graphics.setAlpha((int)(255 * 0.35));
		graphics.fillRoundRectangle(rect, 14, 14);
		
		graphics.setAlpha(255);
		graphics.translate(getLocation());
		rect = new Rectangle(getBounds());
		rect = rect.crop(new Insets(10,15,20,15));
		rect.x = 15;rect.y = 10;
//		Image img = Drawer.getGradientRectangle(new PrecisionRectangle(rect), blackest.getRGB(), blacker.getRGB(), 10.0f, rect.height + 100 ,0);
//		graphics.drawImage(img, 15, 10);
//		img.dispose();
		graphics.setBackgroundColor(blackest);
		graphics.fillRoundRectangle(rect, 10, 10);
		int x = (rect.width/2) - 9 + 15;
		int y = rect.height + 10;
		graphics.fillPolygon(new int[] {x,y,x+18,y,x+18/2,y+10});
		graphics.restoreState();
		
		if (isOpaque())
			graphics.fillRectangle(rect);
		if (getBorder() instanceof AbstractBackground)
			((AbstractBackground) getBorder()).paintBackground(this, graphics,INSETS);
		
		paintTitaniumFigure(graphics);
	}
	
	private static final Rectangle PRIVATE_RECT = new Rectangle();
	
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty())
			return;

		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();
		if (useLocalCoordinates()) {
			graphics.translate(getBounds().x + getInsets().left, getBounds().y
					+ getInsets().top);
			if (!optimizeClip){
				Rectangle clientArea = getClientArea(PRIVATE_RECT);
				clientArea.crop(INSETS);
				graphics.clipRect(clientArea);
			}
			graphics.pushState();
			paintChildren(graphics);
			graphics.popState();
			graphics.restoreState();
		} else {
			if (optimizeClip)
				paintChildren(graphics);
			else {
				Rectangle clientArea = getClientArea(PRIVATE_RECT);
				clientArea.crop(INSETS);
				graphics.clipRect(clientArea);
				graphics.pushState();
				paintChildren(graphics);
				graphics.popState();
				graphics.restoreState();
			}
		}
	}
	
	private String strImage;
	
	private Image image;

	public void setImage(String imagePath) {
		if (Utils.safeNotEquals(strImage, imagePath)){
			if (image != null){
				image.dispose();
				image = null;
			}
			if (imagePath != null){
				image = Converter.getImageFromUrl(imagePath);
				if (image == null && !Converter.isStringUrl(imagePath)){
					File imageFile = new File(imagePath);
					if (imageFile.exists()){
						image = new Image(Display.getCurrent(), imagePath);
					}
				}
			}
			strImage = imagePath;
			setDirty(true);
		}
	}
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		paingImage(graphics);
		super.paintTitaniumFigure(graphics);
	}

	/**
	 * @param graphics
	 */
	private void paingImage(Graphics graphics) {
		if (image != null){
			graphics.drawImage(image,new Rectangle(image.getBounds()), getBounds());
		}
	}

	@Override
	protected Rectangle getTextHolderRectangle() {
		Rectangle src = super.getTextHolderRectangle();
		if (image != null){
			src.crop(new Insets(0, image.getImageData().width, 0, 0));
		}
		return src;
	}
	
	public void dispose() {
		super.dispose();
		if (image != null){
			image.dispose();
		}
	}
	
	protected void paintBorder(Graphics graphics) {
		if (getBorder() != null)
			getBorder().paint(this, graphics, INSETS);
	}
	
}
