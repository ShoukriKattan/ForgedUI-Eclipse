// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumFigure extends Figure {
	
	private FontData fontData;
	
	private Font userFont;
	
	private Font defaultFont;
	
	private String strFontColor = null;
	
	private Color userFontColor = null;
	
	private Color defaultFontColor = null;
	
	private Float opacity;
	
	private boolean isDirty;
	
	protected Insets insets = NO_INSETS;
	
	/**
	 * some magrin value for common uses
	 */
	private int margin = 3;

	public TitaniumFigure() {

	}
	
	@Override
	public TitaniumBackgroundBorder getBorder() {
		return (TitaniumBackgroundBorder) super.getBorder();
	}
	
	public void setDefaultFont(Font defaultFont) {
		this.defaultFont = defaultFont;
	}
	
	public void setDefaultFontColor(Color defaultFontColor) {
		this.defaultFontColor = defaultFontColor;
	}
	
	public void setFontColor(String color) {
		if (Utils.safeNotEquals(strFontColor, color)){
			if (userFontColor != null){
				userFontColor.dispose();
			}
			userFontColor = (color == null)  ? null
				: Converter.getColorFromHexa(color);
			strFontColor = color;
			setDirty(true);
		}
	}
	
	protected Color getFontColor(){
		return userFontColor != null ? userFontColor : defaultFontColor;
	}
	
	@Override
	public void setBorder(Border border) {
		if (border != null){
			Assert.isTrue(border instanceof TitaniumBackgroundBorder,
					"Only TitaniumBackgroundBorder can be set in TitaniumFigure!");
		}
		super.setBorder(border);
	}
	
	public void setFontData(FontData fontData){
		if (Utils.safeNotEquals(this.fontData, fontData)){
			this.fontData = fontData;
			if (userFont != null){
				userFont.dispose();
			}
			userFont = fontData == null ? null
					: new Font(null, fontData);
			setDirty(true);
		}
	}
	
	public Font getFont_(){
		return (userFont != null) ? userFont : defaultFont;
	}

	public void setOpacity(Float opacity) {
		if (Utils.safeNotEquals(this.opacity, opacity)){
			this.opacity = opacity;
			setDirty(true);
		}
	}
	
	private int getAlpha(){
		return opacity == null ? 255 : (int)(255*opacity);
	}
	
	public void setDirty(boolean isDirty){
		this.isDirty = isDirty;
	}
	
	public boolean isDirty(){
		return this.isDirty || (getBorder() != null ? getBorder().isDirty() : false);
	}
	
	@Override
	public void paint(Graphics graphics) {
		if (getFont_() != null) setFont(getFont_());
		if (getFontColor() != null) setForegroundColor(getFontColor());
		graphics.setAlpha(getAlpha());
		super.paint(graphics);
		setDirty(false);
	}
	
	protected boolean useLocalCoordinates() {
		return true;
	}
	
	public void dispose() {
		if (getBorder() != null){
			getBorder().dispose();
		}
		if (userFont != null){
			userFont.dispose();
		}
		if (userFontColor != null){
			userFontColor.dispose();
		}
	}

	/**
	 * @param insets the insets to set
	 */
	public void setInsets(Insets insets) {
		this.insets = insets;
	}

	/**
	 * @return the insets
	 */
	public Insets getInsets() {
		if (getBorder() != null)
			return getBorder().getInsets(this).add(insets);
		return insets;
	}

	/**
	 * @param margin the margin to set
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	/**
	 * @return the margin
	 */
	public int getMargin() {
		return margin;
	}

}
