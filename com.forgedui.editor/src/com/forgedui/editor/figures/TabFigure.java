// LICENSE
package com.forgedui.editor.figures;

import java.io.File;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.forgedui.util.Converter;
import com.forgedui.util.Utils;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TabFigure extends TitaniumTextFigure {
	
	private static final Dimension barImageDimension = new Dimension(30, 30);
	
	private String strBarImage;
	
	private Image barImage;
	
	private String badge;
	
	private boolean active;

	public TabFigure() {
		setText("...");
		setTextVerticalAlign(Alignments.bottom);
		setMargin(0);
	}
	
	public void setBarImage(String imagePath) {
		if (Utils.safeNotEquals(strBarImage, imagePath)){
			if (barImage != null){
				barImage.dispose();
				barImage = null;
			}
			if (imagePath != null){
				barImage = Converter.getImageFromUrl(imagePath);
				if (barImage == null && !Converter.isStringUrl(imagePath)){
					File imageFile = new File(imagePath);
					if (imageFile.exists()){
						barImage = new Image(Display.getCurrent(), imagePath);
					}
				}
			}
			strBarImage = imagePath;
			setDirty(true);
		}
	}
	
	public void setBadge(String badge){
		if (Utils.safeNotEquals(this.badge, badge)){
			this.badge = badge;
			setDirty(true);
		}
	}
	
	
	public void setActive(boolean active){
		this.active = active;
		getBorder().setOpaque(active);
	}
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		Rectangle imageRect = super.getTextHolderRectangle();
		int dx = (imageRect.width - barImageDimension.width)/2;
		imageRect.shrink(dx, 2);
		imageRect.height = barImageDimension.height;

		if (barImage != null){
			Image changedImage = null; 
			if (!active){
				changedImage = new Image(null, barImage, SWT.IMAGE_GRAY);
			} else {
				Image tempImage = new Image(null, barImage, SWT.IMAGE_GRAY);
				RGB maskRGB = new RGB(46, 133, 222);
				int max = Math.max(maskRGB.red, Math.max(maskRGB.green, maskRGB.blue));
				ImageData id = tempImage.getImageData();
				for (int i = 0; i < id.height; i++) {
					for (int j = 0; j < id.width; j++) {
						int pixel = id.getPixel(j, i);
						int red = (pixel & id.palette.redMask) >> 16;
						int green = (pixel & id.palette.greenMask) >> 8;
						int blue = pixel & id.palette.blueMask;
						//System.out.println(new RGB(red, green, blue));
						RGB rgb = new RGB(maskRGB.red*red/max,
								maskRGB.green*green/max,
								maskRGB.blue*blue/max);
						id.setPixel(j, i, id.palette.getPixel(rgb));
					}
				}
				changedImage = new Image(null, id);
				tempImage.dispose();
			}
			
			Rectangle src = new Rectangle(0, 0,
					changedImage.getImageData().width, barImage.getImageData().height);
			graphics.clipRect(imageRect);
			graphics.drawImage(changedImage, src, imageRect);
			graphics.restoreState();
			changedImage.dispose();
		}
		super.paintTitaniumFigure(graphics);
		imageRect.expand(14, 4);
		Drawer.drawBadge(graphics, imageRect, badge);
	}
	
	@Override
	protected Point getTextLocation(Rectangle r, Dimension textSize,
			Alignments h, Alignments v) {
		// there are always some pixels at the bottom
		return super.getTextLocation(r, textSize, h, v).getTranslated(0, 3);
	}
	
	public void dispose() {
		super.dispose();
		if (barImage != null){
			barImage.dispose();
		}
	}
	
}
