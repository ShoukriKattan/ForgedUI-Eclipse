// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
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

import com.forgedui.editor.figures.TitaniumTextFigure.Alignments;
import com.forgedui.editor.util.ColorUtils;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class Drawer {
	
	private static final int TEXT_MARGIN = 3;
	
	public static final int MAXIMUM_GRADIENT_HEIGHT = 20;
		
	public static void drawBadge(Graphics graphics, Rectangle r, String text){
		if (Utils.isNotEmpty(text)){
			graphics.setBackgroundColor(ColorConstants.white);
			Dimension textSize = FigureUtilities.getStringExtents(text, graphics.getFont());
			int h = textSize.height;
			int w = Math.max(h, textSize.width + 8);
			int radius = Math.min(w, h);
			graphics.fillRoundRectangle(new Rectangle(r.right()-w-2, r.y+2, w, h), radius, radius);
			graphics.setBackgroundColor(ColorConstants.red);
			Rectangle textRect = new Rectangle(r.right()-w-1, r.y + 3,w-2, h-2);
			radius = Math.min(textRect.width, textRect.height);
			graphics.fillRoundRectangle(textRect, radius, radius);
			drawString(graphics, text, textRect);
		}
	}
	
	public static void drawString(Graphics graphics, String text, Rectangle parentRect) {
		drawString(graphics, text, parentRect, Alignments.center, Alignments.center);
	}
	
	public static void drawString(Graphics graphics, String text, Rectangle parentRect,
			Alignments hAlign, Alignments vAlign) {
		drawString(graphics, text, parentRect, hAlign, vAlign, TEXT_MARGIN);
	}
	
	public static void drawString(Graphics graphics, String text, Rectangle parentRect,
			Alignments hAlign, Alignments vAlign, int textMargin) {
		if (text != null && text.length() > 0){
			graphics.pushState();
			Dimension textSize = FigureUtilities.getStringExtents(text, graphics.getFont());
			Point textLocation = getTextLocation(parentRect,
					textSize, hAlign, vAlign, textMargin);
			graphics.clipRect(parentRect);
			graphics.drawString(text, textLocation);
			graphics.popState();
		}
	}
	
	public static void paintLines(Graphics graphics, String[] text, Rectangle parentRect,
			Alignments hAlign, Alignments vAlign, int textMargin) {
		if (text != null && text.length > 0){
			int oneLineHeight = textMargin
				+ FigureUtilities.getStringExtents("X", graphics.getFont()).height;
			int newHeight = oneLineHeight*text.length + textMargin;
			
			int visibleLines = text.length;
			if (newHeight < parentRect.height){
				if (vAlign == Alignments.center) {
						parentRect.shrink(0, (parentRect.height - newHeight)/2);
				} else if (vAlign == Alignments.bottom) {
						parentRect.y += parentRect.height - newHeight;
						parentRect.height = newHeight;
				}
			} else {
				visibleLines = Math.min(visibleLines, text.length);
				parentRect.height = (parentRect.height / oneLineHeight)*oneLineHeight + textMargin;
			}
			graphics.clipRect(parentRect);
			
			vAlign = Alignments.top;
			int y = parentRect.y;
			for (int i = 0; y < parentRect.bottom() && i < text.length; i++) {
				Rectangle lineRect = new Rectangle(parentRect.x, y, parentRect.width,
						oneLineHeight + textMargin);
				drawString(graphics, text[i], lineRect, hAlign, Alignments.top);
				y += oneLineHeight;
			}
			graphics.restoreState();
		}
	}
	
	public static Point getTextLocation(Rectangle r, Dimension textSize,
			Alignments h, Alignments v, int textMargin){
		Rectangle textBounds = r.getCopy();
		int dx = (textBounds.width - textSize.width) / 2;
		int dy = (textBounds.height - textSize.height) / 2;
		switch (h) {
		case left:
			if (textBounds.width > textSize.width){
				textBounds.translate(Math.min(dx, textMargin), 0);
			}
			break;
		case right:
			textBounds.translate(2*dx, 0);
			if (textBounds.width > textSize.width){
				textBounds.translate(-Math.min(dx, textMargin), 0);
			}
			break;
		case center:
			textBounds.translate(dx, 0);
			break;
		}
		switch (v) {
		case top:
			if (textBounds.height > textSize.height){
				textBounds.translate(0, Math.min(dy, textMargin));
			}
			break;
		case bottom:
			textBounds.translate(0, 2*dy);
			if (textBounds.height > textSize.height){
				textBounds.translate(0, -Math.min(dy, textMargin));
			}
			break;
		case center:
			textBounds.translate(0, dy);
			break;
		}
		return textBounds.getLocation();
	}
	
	public static Image getGradientRectangle(PrecisionRectangle tempRect, RGB bottomRGB, RGB topRGB,
			float internalRadius, int maxGradientHeight, int roundedPlace){
		return Drawer.getGradientRectangle(tempRect, bottomRGB, topRGB, internalRadius, maxGradientHeight, roundedPlace, true, true);
	}
	
	//rounded is SWT.TOP, SWT.BOTTOM, SWT.CENTER
	public static Image getGradientRectangle(PrecisionRectangle tempRect, RGB bottomRGB, RGB topRGB,
			float internalRadius, int maxGradientHeight, int roundedPlace,boolean drawLeftSide,boolean drawRightSide){
		Color bottomColor = new Color(null, bottomRGB);
		Color topColor = new Color(null, topRGB);
		
		PrecisionRectangle gradientRect = tempRect.getPreciseCopy();
		//gradientRect.height = tempRect.height / 2;
		if (gradientRect.height == 0) gradientRect.height = 1;
		if (gradientRect.height > maxGradientHeight )
			gradientRect.setHeight(maxGradientHeight);
		
		Image gradientImage = new Image(Display.getCurrent(), gradientRect.width, gradientRect.height);
		GC gc = new GC(gradientImage);
		SWTGraphics swtGraphics = new SWTGraphics(gc);
		swtGraphics.setForegroundColor(topColor);
		swtGraphics.setBackgroundColor(bottomColor);
		swtGraphics.translate((float)-tempRect.preciseX,
				(float)-tempRect.preciseY);
		
		swtGraphics.fillGradient(gradientRect, true);
		
		//using this color allows us to remove black arch at the corners
		Color transparent = (roundedPlace == SWT.TOP) ? topColor : bottomColor;
		RGB transparentRGB = transparent.getRGB();
		swtGraphics.setBackgroundColor(transparent);

		Path transparentCorners = new Path(Display.getCurrent());
		transparentCorners.addRectangle(gradientRect.x, gradientRect.y, gradientRect.width, gradientRect.height);
		switch (roundedPlace) {
		case SWT.TOP:
			transparentCorners.addPath(getTopRoundedRectangle(gradientRect, internalRadius,drawLeftSide,drawRightSide));
			break;
		case SWT.BOTTOM:
			transparentCorners.addPath(getBottomRoundedRectangle(gradientRect, internalRadius,drawLeftSide,drawRightSide));
			break;
		default:
			transparentCorners.addPath(getRoundedRectangle(gradientRect, internalRadius,drawLeftSide,drawRightSide));
		}
		
		swtGraphics.fillPath(transparentCorners);
		//for debug reason
		/*switch (roundedPlace) {
		case SWT.TOP:
			swtGraphics.drawPath(getTopRoundedRectangle(gradientRect, internalRadius));
			break;
		case SWT.BOTTOM:
			swtGraphics.drawPath(getBottomRoundedRectangle(gradientRect, internalRadius));
			break;
		default:
			swtGraphics.drawPath(getRoundedRectangle(gradientRect, internalRadius));
		}*/
		
		
		ImageData iData = gradientImage.getImageData();
		iData.transparentPixel = iData.palette.getPixel(transparentRGB);
		
		swtGraphics.dispose();
		gc.dispose();
		gradientImage.dispose();
		bottomColor.dispose();
		topColor.dispose();
		
		return new Image(Display.getCurrent(), iData);
	}
	
	public static Image getGradientRectangle(PrecisionRectangle tempRect, RGB bottomRGB, RGB topRGB,
			float internalRadius, int roundedPlace){
		return getGradientRectangle(tempRect, bottomRGB, topRGB, internalRadius, MAXIMUM_GRADIENT_HEIGHT, roundedPlace);
	}
	
	public static Image getGradientRectangle(PrecisionRectangle tempRect, Color backgroundColor, float internalRadius){
		return getGradientRectangle(tempRect, backgroundColor.getRGB(), ColorUtils.lighter(ColorUtils.lighter(
				backgroundColor.getRGB())), internalRadius, SWT.TOP);
	}
	
	public static Path getTopRoundedRectangle(PrecisionRectangle tempRect, float radius){
		return Drawer.getTopRoundedRectangle(tempRect, radius, true, true);
	}
	
	public static Path getTopRoundedRectangle(PrecisionRectangle tempRect, float radius,boolean drawLeftSide,boolean drawRightSide){
		if (radius <= 0) return getRoundedRectangle(tempRect, radius,drawLeftSide,drawRightSide);
		float width = radius * 2;
		float dy = radius - tempRect.height;
		float dx = radius - (float) Math.sqrt(radius*radius - dy*dy);
		float alpha =  dy > 0 ? (float)(Math.acos(dy/radius)*180/Math.PI) : 90;
		Path p = new Path(null);
		float x = (float) tempRect.preciseX;
		float y = (float) tempRect.preciseY;
		float right = (float) tempRect.preciseRight();
		float bottom = (float) tempRect.preciseBottom();
		
		if (dy < 0){
			p.moveTo(x, y + radius);
		} else {
			p.moveTo(x + dx, y - dy);
		}
		p.addArc(x, y, width, width, 90 + alpha, -alpha);
		p.addArc(right-width, y, width, width, 90, -alpha);
		
		if (dy < 0){
			p.lineTo(right, bottom);//rightBottom
			p.lineTo(x, bottom);//leftBottom
		}
				
		return p;
	}
	
	public static Path getBottomRoundedRectangle(PrecisionRectangle tempRect, float radius){
		return Drawer.getBottomRoundedRectangle(tempRect, radius, true, true);
	}
	
	public static Path getBottomRoundedRectangle(PrecisionRectangle tempRect, float radius,boolean drawLeftSide,boolean drawRightSide){
		if (radius <= 0) return getRoundedRectangle(tempRect, radius,drawLeftSide,drawRightSide);
		float width = radius * 2;
		float dy = radius - tempRect.height;
		float dx = radius - (float) Math.sqrt(radius*radius - dy*dy);
		float alpha =  dy > 0 ? (float)(Math.acos(dy/radius)*180/Math.PI) : 90;
		Path p = new Path(null);
		float x = (float) tempRect.preciseX;
		float y = (float) tempRect.preciseY;
		float right = (float) tempRect.preciseRight();
		float bottom = (float) tempRect.preciseBottom();
		
		if (dy < 0){
			p.moveTo(x, bottom - radius);
		} else {
			p.moveTo(x + dx, y);
		}
		p.addArc(x, bottom - 2*radius, width, width, 270 - alpha, alpha);
		p.addArc(right-width, bottom - 2*radius, width, width, 270, alpha);
		
		if (dy < 0){
			p.lineTo(right, y);//rightTop
			p.lineTo(x, y);//leftTop
		}
				
		return p;
	}
	
	public static Path getRoundedRectangle(PrecisionRectangle tempRect, float radius){
		return getRoundedRectangle(tempRect, radius, true, true);
	}
	
	public static Path getRoundedRectangle(PrecisionRectangle tempRect, float radius, boolean drawLeftSide, boolean drawRightSide){
		if (radius <= 0){
			//this is a fix for Windows OS
			Path p = new Path(null);
			p.addRectangle((float)tempRect.preciseX, (float)tempRect.preciseY,
					(float)tempRect.preciseWidth, (float)tempRect.preciseHeight);
			return p;
		}
		float width = radius * 2;
		Path p = new Path(null);
		float x = (float) tempRect.preciseX;
		float y = (float) tempRect.preciseY;
		float right = (float) tempRect.preciseRight();
		float bottom = (float) tempRect.preciseBottom();
		if((drawLeftSide && drawRightSide)){
			p.moveTo(x, y + radius);//leftTop
			p.addArc(x, y, width, width, 180, -90);
			p.addArc(right - width, y, width, width, 90, -90);
			p.addArc(right - width, bottom- width,width, width, 0, -90);
			p.addArc(x, bottom - width,width, width, 270, -90);
		}else if(drawLeftSide){
			p.moveTo(x, y + radius);//leftTop
			p.addArc(x, y, width, width, 180, -90);
			p.lineTo(right, y);
			p.lineTo(right,bottom);
			p.addArc(x, bottom - width,width, width, 270, -90);
		}else if(drawRightSide){
			p.moveTo(x, y);//leftTop
			p.lineTo(x,y - width);
			p.addArc(right - width, y, width, width, 90, -90);
			p.addArc(right - width, bottom- width,width, width, 0, -90);
			p.lineTo(x,right - width);
		}else{
			p.moveTo(x, y);
			p.lineTo(right,y);
			p.lineTo(right,bottom);
			p.lineTo(x,bottom);
			p.lineTo(x,y);
		}
		
		return p;
	}
}