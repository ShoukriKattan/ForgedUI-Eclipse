// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumTextFigure extends TitaniumFigure {
	
	public static enum Alignments {
		left, center, right, top, bottom
	}

	private String text;
	
	private String[] lines;
	
	private Alignments vAlign = Alignments.center;
	
	private Alignments hAlign = Alignments.center;
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		paintTitaniumFigure(graphics);
	}
	
	protected void paintTitaniumFigure(Graphics graphics){
		this.paintString(graphics, this.text, getTextHolderRectangle());
		this.paintLines(graphics, this.lines, getLinesHolderRectangle());
		this.paintBorder(graphics);
	}
	
	protected void paintString(Graphics graphics, String text, Rectangle parentRect) {
		Drawer.drawString(graphics, text, parentRect, hAlign, vAlign, getMargin());
	}
	
	protected void paintLines(Graphics graphics, String[] text, Rectangle parentRect) {
		if (text != null && text.length > 0){
			Alignments prevAlignments = vAlign;
			int oneLineHeight = getMargin()
				+ FigureUtilities.getStringExtents("X", graphics.getFont()).height;
			int newHeight = oneLineHeight*text.length + getMargin();
			
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
				parentRect.height = (parentRect.height / oneLineHeight)*oneLineHeight + getMargin();
			}
			graphics.pushState();
			graphics.clipRect(parentRect);
			
			vAlign = Alignments.top;
			int y = parentRect.y;
			for (int i = 0; y < parentRect.bottom() && i < text.length; i++) {
				Rectangle lineRect = new Rectangle(parentRect.x, y, parentRect.width,
						oneLineHeight + getMargin());
				paintString(graphics, text[i], lineRect);
				y += oneLineHeight;
			}
			vAlign = prevAlignments;
			graphics.popState();
		}
	}
	
	protected Rectangle getTextHolderRectangle(){
		Rectangle r = getClientArea();
		if (useLocalCoordinates()){
			r.setLocation(getLocation().x + getInsets().left,
					getLocation().y + getInsets().top);
		}
		return r;
	}
	
	protected Rectangle getLinesHolderRectangle(){
		Rectangle r = getClientArea();
		if (useLocalCoordinates()){
			r.setLocation(getBounds().getLocation());
		}
		return r;
	}
	
	protected Point getTextLocation(Rectangle r, Dimension textSize, Alignments h, Alignments v){
		Rectangle textBounds = r.getCopy();
		int dx = (textBounds.width - textSize.width) / 2;
		int dy = (textBounds.height - textSize.height) / 2;
		switch (h) {
		case left:
			if (textBounds.width > textSize.width){
				textBounds.translate(Math.min(dx, getMargin()), 0);
			}
			break;
		case right:
			textBounds.translate(2*dx, 0);
			if (textBounds.width > textSize.width){
				textBounds.translate(-Math.min(dx, getMargin()), 0);
			}
			break;
		case center:
			textBounds.translate(dx, 0);
			break;
		}
		switch (v) {
		case top:
			if (textBounds.height > textSize.height){
				textBounds.translate(0, Math.min(dy, getMargin()));
			}
			break;
		case bottom:
			textBounds.translate(0, 2*dy);
			if (textBounds.height > textSize.height){
				textBounds.translate(0, -Math.min(dy, getMargin()));
			}
			break;
		case center:
			textBounds.translate(0, dy);
			break;
		}
		return textBounds.getLocation();
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		if (Utils.safeNotEquals(this.text, text)){
			this.text = text;
			setDirty(true);
		}
	}
	
	/**
	 * @param text the text to set
	 */
	public void setLines(String[] text) {
		if (Utils.safeNotEquals(this.lines, text)){
			this.lines = text;
			setDirty(true);
		}
	}
	
	public String[] getLines() {
		return lines;
	}

	public void setTextHorisontalAlign(Alignments hAlign) {
		if (Utils.safeNotEquals(this.hAlign, hAlign)){
			if (hAlign == null){
				this.hAlign = Alignments.center;
			} else {
				this.hAlign = hAlign;
			}
			setDirty(true);
		}
	}
	
	public void setTextVerticalAlign(Alignments vAlign) {
		if (Utils.safeNotEquals(this.vAlign, vAlign)){
			if (vAlign == null){
				this.vAlign = Alignments.center;
			} else {
				this.vAlign = vAlign;
			}
			setDirty(true);
		}
	}

}
