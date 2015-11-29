// LICENSE
package com.forgedui.editor.figures;

import java.io.File;

import org.eclipse.draw2d.AbstractBackground;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.forgedui.editor.util.ColorUtils;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumBackgroundBorder extends AbstractBackground {
	
	private String strBackgroundColor;
	
	private Color backgroundColor;
	
	private String strBorderColor;
	
	private Color borderColor;
	
	private Float borderWidth, borderRadius;
	
	private float defaultBorderWidth, defaultBorderRadius;
	
	private Color defaultBorderColor;
	
	private Color defaultBackgroundColor;
	
	private String strBackgroundImage;
	
	private Image backgroundImage;
	
	private boolean opaque;
	
	private boolean showGradient = true;
	
	private boolean isDirty = false;
	
	private boolean drawLeftSide = true;
	
	private boolean drawRightSide = true;
	
	private Element model;
	
	public TitaniumBackgroundBorder(Color defaultBackgroundColor, Color defaultBorderColor,
			float borderWidth, float borderRadius, boolean showGradient){
		this.defaultBackgroundColor = defaultBackgroundColor;
		this.defaultBorderColor = defaultBorderColor;
		this.defaultBorderWidth = borderWidth;
		this.defaultBorderRadius = borderRadius;
		this.showGradient = showGradient;
	}
	
	public TitaniumBackgroundBorder(Color defaultBackgroundColor, Color defaultBorderColor,
			float borderWidth, float borderRadius, boolean showGradient,Element model){
		this.defaultBackgroundColor = defaultBackgroundColor;
		this.defaultBorderColor = defaultBorderColor;
		this.defaultBorderWidth = borderWidth;
		this.defaultBorderRadius = borderRadius;
		this.showGradient = showGradient;
		this.model = model;
	}
	
	public void setOpaque(boolean opaque){
		if (this.opaque !=  opaque){
			this.opaque = opaque;
			setDirty(true);
		}
	}
	
	public void setShowGradient(boolean showGradient){
		if (this.showGradient != showGradient){
			this.showGradient = showGradient;
			setDirty(true);
		}
	}
	
	public boolean isOpaque() {
		return opaque;
	}
	
	public void setBackgroundColor(String color){
		if (Utils.safeNotEquals(strBackgroundColor, color)){
			if (backgroundColor != null){
				backgroundColor.dispose();
			}
			backgroundColor = (color == null)  ? null
				: Converter.getColorFromHexa(color);
			strBackgroundColor = color;
			setDirty(true);
		}
	}
	
	public Color getBackgroundColor() {
		return backgroundColor != null ? backgroundColor : defaultBackgroundColor;
	}
	
	public void setBorderColor(String color){
		if (Utils.safeNotEquals(strBorderColor, color)){
			if (borderColor != null){
				borderColor.dispose();
			}
			borderColor = (color == null)  ? null
				: Converter.getColorFromHexa(color);
			strBorderColor = color;
			setDirty(true);
		}
	}
	
	public Color getBorderColor() {
		return borderColor != null ? borderColor : defaultBorderColor;
	}
	
	public void setBorderWidth(Float width) {
		if (Utils.safeNotEquals(borderWidth, width)){
			this.borderWidth = width;
			setDirty(true);
		}
	}
	
	public float getBorderWidth() {
		return borderWidth != null ? borderWidth : defaultBorderWidth;
	}
	
	public void setBorderRadius(Float radius) {
		if (Utils.safeNotEquals(borderRadius, radius)){
			this.borderRadius = radius;
			setDirty(true);
		}
	}
	
	public float getBorderRadius() {
		return borderRadius != null ? borderRadius : defaultBorderRadius;
	}
	
	public void setBackgroundImage(String imagePath) {
		if (Utils.safeNotEquals(strBackgroundImage, imagePath)){
				if (backgroundImage != null){
					backgroundImage.dispose();
					backgroundImage = null;
				}
				if (imagePath != null){
					backgroundImage = Converter.getImageFromUrl(imagePath);
					if (backgroundImage == null && !Converter.isStringUrl(imagePath)){
						File imageFile = new File(imagePath);
						if (imageFile.exists()){
							backgroundImage = new Image(Display.getCurrent(), imagePath);
						}
					}
				}
				strBackgroundImage = imagePath;
				setDirty(true);
			}
	}
	
	public Insets getInsets(IFigure figure) {
		return new Insets((int)getBorderWidth());
	}
	
	public void setDirty(boolean isDirty){
		this.isDirty = isDirty;
	}
	
	public boolean isDirty(){
		return this.isDirty;
	}
	
	@Override
	public void paintBackground(IFigure figure, Graphics graphics, Insets insets) {
		PrecisionRectangle tempRect = new PrecisionRectangle(getPaintRectangle(figure, insets));
		float r = getActualBorderRadius(tempRect);
		// must use paths to process not integer values
		// the difference is visible with zoom
		graphics.setBackgroundColor(getBackgroundColor());
		Path extPath = Drawer.getRoundedRectangle(tempRect, r, drawLeftSide , drawRightSide);
		float borderWidth = Math.min(Math.min(tempRect.height, tempRect.width)/2, getBorderWidth());
		float internalRadius = Math.max(0, r - borderWidth);
		tempRect.shrink(borderWidth, borderWidth);
		
		
		if (!isOpaque()){
			graphics.fillPath(extPath);
			if (tempRect.height > 0 && tempRect.width > 0){
				if (backgroundImage != null){
					Image roundedImage = getRoundedImage(new Image(null, backgroundImage.getImageData().scaledTo(tempRect.width, tempRect.height)),
							internalRadius);
//					Image roundedImage = getRoundedImage(new Image(null,backgroundImage.getImageData()),
//							internalRadius);
//					graphics.drawImage(roundedImage, tempRect.x, tempRect.y);
					graphics.drawImage(roundedImage, new Rectangle(roundedImage.getBounds()),new Rectangle(tempRect.x,tempRect.y,tempRect.width,tempRect.height));
//					graphics.drawImage(backgroundImage, new Rectangle(backgroundImage.getBounds()),new Rectangle(tempRect.x,tempRect.y,tempRect.width,tempRect.height));
					roundedImage.dispose();
				} else {
					graphics.setBackgroundColor(getBackgroundColor());
					graphics.fillPath(Drawer.getRoundedRectangle(tempRect, internalRadius, drawLeftSide , drawRightSide));

					if (showGradient){
						showGradient(graphics, tempRect, internalRadius);
					}
				}
			}
		} else {
			if (borderWidth > 0){
				extPath.addPath(Drawer.getRoundedRectangle(tempRect, internalRadius, drawLeftSide , drawRightSide));
				graphics.fillPath(extPath);//this will draw only border
			}
			if (tempRect.height > 0 && tempRect.width > 0 && backgroundImage != null){
				Image roundedImage = getRoundedImage(new Image(null, backgroundImage.getImageData().scaledTo(tempRect.width, tempRect.height)),
						internalRadius);
				graphics.drawImage(roundedImage, tempRect.x, tempRect.y);
				roundedImage.dispose();
			}
		}
		
		
		setDirty(false);
	}

	protected void showGradient(Graphics graphics, PrecisionRectangle tempRect,
			float internalRadius) {
		Image gradientImage = Drawer.getGradientRectangle(tempRect, getBackgroundColor().getRGB(), ColorUtils.lighter(ColorUtils.lighter(
				getBackgroundColor().getRGB())), internalRadius, SWT.TOP,Drawer.MAXIMUM_GRADIENT_HEIGHT,drawLeftSide,drawRightSide);
		graphics.drawImage(gradientImage, tempRect.x, tempRect.y);
		gradientImage.dispose();
	}

	/**
	 * @param tempRect
	 * @return
	 */
	public float getActualBorderRadius(Rectangle tempRect) {
		return  Math.min(Math.min(tempRect.height, tempRect.width)/2, getBorderRadius());
	}
	
	protected Image getRoundedImage(Image roundedImage, float internalRadius){
		ImageData iData = roundedImage.getImageData();
		boolean isTransparent = iData.getTransparencyType() != SWT.TRANSPARENCY_NONE;
		if (!isTransparent){
			//here is a big problem with transparent zones on transparent images
			//if we change transparent pixel, so have to create new image
			Image newImage = new Image(null, roundedImage.getBounds());
			GC gc = new GC(newImage);
			SWTGraphics swtGraphics = new SWTGraphics(gc);
			swtGraphics.drawImage(roundedImage, 0, 0);
			
			RGB transparentRGB = new RGB(1,2,3);
			Color transparent = new Color(null, transparentRGB);
			swtGraphics.setBackgroundColor(transparent);
			
			Path transparentCorners = new Path(Display.getCurrent());
			Rectangle bounds = new Rectangle(0, 0, iData.width, iData.height);
			transparentCorners.addRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
			transparentCorners.addPath(Drawer.getRoundedRectangle(new PrecisionRectangle(bounds), internalRadius, drawLeftSide , drawRightSide));
			swtGraphics.fillPath(transparentCorners);
			
			iData = newImage.getImageData();
			iData.transparentPixel = iData.palette.getPixel(transparentRGB);
			
			newImage.dispose();
			transparent.dispose();
			swtGraphics.dispose();
			gc.dispose();
		} else {
			//this method has not antialiasing effect
			//that is why it is used only for transparent images
			int transparentPixel = iData.transparentPixel;

			for (int i = 0; i < internalRadius; i++) {
				for (int j = i;j < internalRadius; j++) {
					if ((i-internalRadius)*(i-internalRadius) + (j-internalRadius)*(j-internalRadius)
							> internalRadius*internalRadius){
						if ( i < iData.width && j < iData.height) iData.setPixel(i, j, transparentPixel);
						if ( j < iData.width && i < iData.height) iData.setPixel(j, i, transparentPixel);
						
						if ( iData.width - 1 - i > 0 && j < iData.height) iData.setPixel(iData.width - 1 - i, j, transparentPixel);
						if ( iData.width - 1 - j > 0 && i < iData.height) iData.setPixel(iData.width - 1 - j, i, transparentPixel);
						
						iData.setPixel(iData.width - 1 - i,iData.height - 1 - j, transparentPixel);
						iData.setPixel(iData.width - 1 - j, iData.height - 1 - i, transparentPixel);
						
						iData.setPixel(i,iData.height - 1 - j, transparentPixel);
						iData.setPixel(j, iData.height - 1 - i, transparentPixel);
					}
				}
			}
		}
		roundedImage.dispose();
		return new Image(null, iData);
		//return roundedImage;
	}
	

	
	/*private Image getGradientImage(PrecisionRectangle tempRect, float internalRadius){
		PrecisionRectangle gradientRect = tempRect.getPreciseCopy();
		gradientRect.height = tempRect.height / 4;
		
		Image gradientImage = new Image(Display.getCurrent(),
				gradientRect.width,
				gradientRect.height);
		GC gc = new GC(gradientImage);
		SWTGraphics swtGraphics = new SWTGraphics(gc);
		swtGraphics.setForegroundColor(ColorConstants.gray);
		swtGraphics.setBackgroundColor(getBackgroundColor());
		swtGraphics.translate((float)-tempRect.preciseX,
				(float)-tempRect.preciseY);
		
		swtGraphics.fillGradient(gradientRect, true);
		swtGraphics.setBackgroundColor(null);

		ImageData iData = gradientImage.getImageData();
		RGB transparentRGB = new RGB(1, 1, 1);
		iData.transparentPixel = iData.palette.getPixel(transparentRGB);
		int greenPixel = iData.transparentPixel;

		for (int i = 0; i < internalRadius; i++) {
			for (int j = i;j < internalRadius; j++) {
				if ((i-internalRadius)*(i-internalRadius) + (j-internalRadius)*(j-internalRadius)
						> internalRadius*internalRadius){
					if ( i < iData.width && j < iData.height) iData.setPixel(i, j, greenPixel);
					if ( j < iData.width && i < iData.height) iData.setPixel(j, i, greenPixel);
					
					if ( iData.width - 1 - i > 0 && j < iData.height) iData.setPixel(iData.width - 1 - i, j, greenPixel);
					if ( iData.width - 1 - j > 0 && i < iData.height) iData.setPixel(iData.width - 1 - j, i, greenPixel);
					
//					iData.setPixel(iData.width - 1 - i,iData.height - 1 - j, greenPixel);
//					iData.setPixel(iData.width - 1 - j, iData.height - 1 - i, greenPixel);
//					
//					iData.setPixel(i,iData.height - 1 - j, greenPixel);
//					iData.setPixel(j, iData.height - 1 - i, greenPixel);
				}
			}
		}
		swtGraphics.dispose();
		gc.dispose();
		gradientImage.dispose();
		
		return new Image(Display.getCurrent(), iData);
	}*/
	

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		if (getWidth() > 0){
			graphics.setForegroundColor(getColor());
			graphics.setBackgroundColor(getColor());
			tempRect.setBounds(getPaintRectangle(figure, insets));
			int r = getRadius()*2;
			int width = (int)getWidth();
			if (r == 0 || (model instanceof TabbedBarButton && !getDrawLeftSide() && !getDrawRightSide())){//workaround for line width problem
				/* paint rectangles doesn't work for 1 pixel width with zoom*/
				Path border = new Path(null);
				border.moveTo(tempRect.x, tempRect.y);
				border.lineTo(tempRect.right(), tempRect.y);
				border.lineTo(tempRect.right(), tempRect.bottom());
				border.lineTo(tempRect.x, tempRect.bottom());
				border.lineTo(tempRect.x, tempRect.y);
				border.lineTo(tempRect.x + width, tempRect.y + width);
				border.lineTo(tempRect.x + width, tempRect.bottom() - width);
				border.lineTo(tempRect.right() - width, tempRect.bottom() - width);
				border.lineTo(tempRect.right() - width, tempRect.y + width);
				border.lineTo(tempRect.x + width, tempRect.y + width);

				graphics.fillPath(border);
			} else {
				float externalWidth = Math.min(getRadius(), Math.min(tempRect.width, tempRect.height)) * 2;
				float internalWidth = Math.max(externalWidth - getWidth() * 2, 0);
				
				Path leftTop = new Path(null);
				if(getDrawLeftSide()){
					leftTop.addArc(tempRect.x, tempRect.y, externalWidth, externalWidth, 90, 90);
					leftTop.addArc(tempRect.x + getWidth(), tempRect.y + getWidth(),
							internalWidth, internalWidth, 180, -90);
				}
				
				Path rightTop = new Path(null);
				if(getDrawRightSide()){
					rightTop.addArc(tempRect.right() - externalWidth, tempRect.y, externalWidth, externalWidth, 0, 90);
					rightTop.addArc(tempRect.right() - internalWidth - getWidth(), tempRect.y + getWidth(),
							internalWidth, internalWidth, 90, -90);
				}
				
				Path leftBottom = new Path(null);
				if(getDrawLeftSide()){
					leftBottom.addArc(tempRect.x, tempRect.bottom() - externalWidth, externalWidth, externalWidth, 180, 90);
					leftBottom.addArc(tempRect.x + getWidth(), tempRect.bottom() - internalWidth - getWidth(),
							internalWidth, internalWidth, 270, -90);
				}
				
				Path rightBottom = new Path(null);
				if(getDrawRightSide()){
					rightBottom.addArc(tempRect.right() - externalWidth, tempRect.bottom()- externalWidth,
							externalWidth, externalWidth, 0, -90);
					rightBottom.addArc(tempRect.right() - internalWidth - getWidth(), tempRect.bottom() - internalWidth- getWidth(),
							internalWidth, internalWidth, 270, 90);
				}
				
				graphics.fillPath(leftTop);
				graphics.fillPath(rightTop);
				graphics.fillPath(leftBottom);
				graphics.fillPath(rightBottom);
				
				Path left = new Path(null);
				if(getDrawLeftSide()){
					left.addRectangle(tempRect.x, tempRect.y + externalWidth / 2 , width, tempRect.height - externalWidth);
				}else{
					left.addRectangle(tempRect.x, tempRect.y, width, tempRect.height);
				}
				
				Path right = new Path(null);
				if(getDrawRightSide()){
					right.addRectangle(tempRect.right() - width, tempRect.y + externalWidth / 2 , width, tempRect.height - externalWidth);
				}else{
					right.addRectangle(tempRect.right() - width, tempRect.y, width, tempRect.height);
				}
				
				Path top = new Path(null);
				if(getDrawLeftSide() && getDrawRightSide()){
					top.addRectangle(tempRect.x + externalWidth / 2, tempRect.y, tempRect.width - externalWidth, width);
				}else if(getDrawLeftSide()){
					top.addRectangle(tempRect.x + externalWidth / 2, tempRect.y, tempRect.width - externalWidth/2, width);
				}else if(getDrawRightSide()){
					top.addRectangle(tempRect.x, tempRect.y, tempRect.width - externalWidth/2, width);
				}else{
					top.addRectangle(tempRect.x, tempRect.y, tempRect.width, width);
				}
					
				Path bottom = new Path(null);
				if(getDrawLeftSide() && getDrawRightSide()){
					bottom.addRectangle(tempRect.x + externalWidth / 2, tempRect.bottom() - width, tempRect.width - externalWidth, width);
				}else if(getDrawLeftSide()){
					bottom.addRectangle(tempRect.x + externalWidth / 2, tempRect.bottom() - width, tempRect.width - externalWidth/2, width);
				}else if(getDrawRightSide()){
					bottom.addRectangle(tempRect.x, tempRect.bottom() - width, tempRect.width - externalWidth/2, width);
				}else{
					bottom.addRectangle(tempRect.x, tempRect.bottom() - width, tempRect.width, width);
				}
				
				graphics.fillPath(left);
				graphics.fillPath(right);
				graphics.fillPath(top);
				graphics.fillPath(bottom);
			}
		}
	}
	
	private boolean getDrawLeftSide() {
		return drawLeftSide;
	}
	
	public void setDrawLeftSide(boolean drawLeftSide) {
		this.drawLeftSide = drawLeftSide;
	}
	
	private boolean getDrawRightSide() {
		return drawRightSide;
	}

	public void setDrawRightSide(boolean drawRightSide) {
		this.drawRightSide = drawRightSide;
	}
	
	public synchronized void dispose() {
		if (backgroundColor != null && !backgroundColor.isDisposed()){
			backgroundColor.dispose();
		}
		if (borderColor != null && !borderColor.isDisposed()){
			borderColor.dispose();
		}		
	}
	
	public Color getColor() {
		return borderColor != null ? borderColor : defaultBorderColor;
	}
	
	public float getWidth() {
		return borderWidth != null ? borderWidth : defaultBorderWidth;
	}
	
	public int getRadius() {
		return borderRadius != null ? borderRadius.intValue() : (int)defaultBorderRadius;
	}
}
