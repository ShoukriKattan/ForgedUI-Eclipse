// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.forgedui.editor.util.ColorUtils;
import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SearchBarFigure extends TitaniumTextFigure{
	
	private static final int BUTTON_WIDTH = 65;
	
	private static final int MAX_HEIGHT = 43;
	
	private String prompt;
	
	private boolean showCancelButton;
	
	private String strBarColor;
	
	private Color userBarColor;
	
	private Color defaultBarColor;
	
	private Image searchImage;
	
	public SearchBarFigure(Color defaultBarColor, Image searchImage){
		this.defaultBarColor = defaultBarColor;
		if (searchImage != null){
			this.searchImage = new Image(null, searchImage, SWT.IMAGE_GRAY);
		}
		
	}
	
	public void setBarColor(String color) {
		if (Utils.safeNotEquals(strBarColor, color)){
			if (strBarColor != null){
				userBarColor.dispose();
			}
			userBarColor = (color == null)  ? null
				: Converter.getColorFromHexa(color);
			strBarColor = color;
			setDirty(true);
		}
	}
	
	protected Color getBarColor(){
		return userBarColor != null ? userBarColor : defaultBarColor;
	}
	
	public void setShowCancelButton(boolean showCancelButton) {
		if (this.showCancelButton != showCancelButton){
			this.showCancelButton = showCancelButton;
			setDirty(true);
		}
	}
	
	public void setPrompt(String prompt) {
		if (Utils.safeNotEquals(this.prompt, prompt)){
			this.prompt = prompt;
			setDirty(true);
		}
	}
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		graphics.setForegroundColor(ColorConstants.white);
		Rectangle barBounds = getBounds().getCopy();
		graphics.setBackgroundColor(getBarColor());
		graphics.fillRectangle(barBounds);
		
		barBounds.crop(new Insets(getMargin()));
		if (barBounds.height > getMaxHeight()){
			int dy = (barBounds.height - getMaxHeight())/2;
			barBounds.crop(new Insets(dy,0,dy,0));
		}

		if (showCancelButton){
			paintCancel(graphics);
			barBounds.width -= getCancelButtonWidth() + getMargin();
		}
		int radius = Math.min(getMinimumBarRadius(), Math.min(barBounds.height, barBounds.width));
		graphics.setBackgroundColor(ColorConstants.white);
		graphics.fillRoundRectangle(barBounds, radius, radius);

		Rectangle dest = barBounds.getCropped(new Insets(2,2,2,2));
		if (searchImage != null){
			dest.width = dest.height;
			graphics.drawImage(searchImage, new Rectangle(searchImage.getBounds()), dest);
		} else {
			dest.width = 0;
		}
		if (Utils.isNotEmpty(prompt)){
			graphics.setForegroundColor(getBarColor());
			setTextHorisontalAlign(Alignments.center);
			setTextVerticalAlign(Alignments.top);
			paintString(graphics, prompt, barBounds.getCropped(new Insets(0,dest.width + 4,2,2)));
		}
		if (Utils.isNotEmpty(getText())){
			graphics.setForegroundColor(getBarColor());
			setTextHorisontalAlign(Alignments.left);
			setTextVerticalAlign(Alignments.center);
			paintString(graphics, getText(), barBounds.getCropped(new Insets(0,dest.width + 4,2,2)));
		}

		setDirty(false);
	}

	/**
	 * @return
	 */
	protected int getMinimumBarRadius() {
		return 30;
	}

	/**
	 * @return
	 */
	protected int getCancelButtonWidth() {
		return BUTTON_WIDTH;
	}
	
	protected void paintCancel(Graphics graphics){
		Rectangle barBounds = getBounds().getCopy();
		barBounds.crop(new Insets(getMargin()));
		if (barBounds.height > getMaxHeight()){
			int dy = (barBounds.height - getMaxHeight())/2;
			barBounds.crop(new Insets(dy,0,dy,0));
		}
		
		Rectangle cancelBounds = barBounds.getCopy();
		cancelBounds.x = cancelBounds.right() - getCancelButtonWidth();
		cancelBounds.width = getCancelButtonWidth();
		
		Color cancelColor = new Color(null,
				ColorUtils.darker(
						ColorUtils.darker(
								graphics.getBackgroundColor().getRGB())));
		graphics.setBackgroundColor(cancelColor);
		int radius = Math.min(15, cancelBounds.height);
		graphics.fillRoundRectangle(cancelBounds, radius, radius);
		graphics.drawRoundRectangle(cancelBounds, radius, radius);
		graphics.setForegroundColor(ColorConstants.white);
		paintString(graphics, "Cancel", cancelBounds);
		cancelColor.dispose();
	}

	/**
	 * @return
	 */
	protected int getMaxHeight() {
		return MAX_HEIGHT;
	}
	
	public void dispose() {
		super.dispose();
		if (userBarColor != null){
			userBarColor.dispose();
		}
		if (searchImage != null){
			searchImage.dispose();
		}
		
	}

}
