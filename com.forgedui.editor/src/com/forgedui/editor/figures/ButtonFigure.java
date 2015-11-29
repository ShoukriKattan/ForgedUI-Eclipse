// LICENSE
package com.forgedui.editor.figures;

import java.io.File;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

import com.forgedui.editor.defaults.IDefaultsProvider;
import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class ButtonFigure extends TitaniumTextFigure {
	
	private String strImage;
	
	private Image image;

	public ButtonFigure() {
		setText("Button");
	}
	
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
			PrecisionRectangle tempRect = new PrecisionRectangle(getBounds().crop(insets));
			float r = getBorder().getActualBorderRadius(tempRect);
			// must use paths to process not integer values
			// the difference is visible with zoom
			graphics.setBackgroundColor(getBackgroundColor());
			float borderWidth = Math.min(Math.min(tempRect.height, tempRect.width)/2, getBorder().getBorderWidth());
			float internalRadius = Math.max(0, r - borderWidth);
			tempRect.shrink(borderWidth, borderWidth);
			Image roundedImage = getBorder().getRoundedImage(new Image(null, image.getImageData().scaledTo(tempRect.width, tempRect.height)),
					internalRadius);
			graphics.drawImage(roundedImage, tempRect.x, tempRect.y);
			roundedImage.dispose();
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

}
