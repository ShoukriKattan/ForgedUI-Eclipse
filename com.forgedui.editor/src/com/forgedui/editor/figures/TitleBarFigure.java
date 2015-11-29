// LICENSE
package com.forgedui.editor.figures;

import java.io.File;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

import com.forgedui.editor.util.ColorUtils;
import com.forgedui.util.Converter;
import com.forgedui.util.ScreenManager;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitleBarFigure extends TitaniumTextFigure {
		
	private static final int BUTTON_WIDTH = 40;
	
	private String prompt;
	
	private String backButtonTitle;
	
	private String strBarImage;
	
	private Image barImage;
	
	private String strBackButtonImage;
	
	private Image backButtonImage;
	
	public TitleBarFigure(){

	}
	
	@Override
	public Insets getInsets() {
		if (Utils.isNotEmpty(prompt)){
			return new Insets(getBounds().height/3, 0, 0, 0);
		}
		return super.getInsets();
	}
	
	public void setPrompt(String prompt) {
		if (Utils.safeNotEquals(this.prompt, prompt)){
			this.prompt = prompt;
			setDirty(true);
		}
	}
	
	/**
	 * @param backButtonTitle the backButtonTitle to set
	 */
	public void setBackButtonTitle(String backButtonTitle) {
		if (Utils.safeNotEquals(this.backButtonTitle, backButtonTitle)){
			this.backButtonTitle = backButtonTitle;
			setDirty(true);
		}
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

	public void setBackButtonImage(String imagePath) {
		if (Utils.safeNotEquals(strBackButtonImage, imagePath)){
			if (backButtonImage != null){
				backButtonImage.dispose();
				backButtonImage = null;
			}
			if (imagePath != null){
				backButtonImage = Converter.getImageFromUrl(imagePath);
				if (backButtonImage == null && !Converter.isStringUrl(imagePath)){
					File imageFile = new File(imagePath);
					if (imageFile.exists()){
						backButtonImage = new Image(Display.getCurrent(), imagePath);
					}
				}
			}
			strBackButtonImage = imagePath;
			setDirty(true);
		}
	}
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		Rectangle barBounds = getBounds().getCopy();
		if (Utils.isNotEmpty(prompt)){
			Rectangle promptBounds = barBounds.getCopy();
			promptBounds.height = barBounds.height / 3;
			setTextHorisontalAlign(Alignments.center);
			graphics.setForegroundColor(ColorConstants.black);
			paintString(graphics, prompt, promptBounds);
			graphics.restoreState();
			barBounds.y += barBounds.height / 3;
			barBounds.height = 2*barBounds.height / 3;
		}
		//graphics.setBackgroundColor(getBarColor());
		//graphics.fillRectangle(barBounds);
		
		barBounds.crop(new Insets(ScreenManager.TITLE_BAR_BUTTONS_INSET));
		if (barImage == null){
			paintString(graphics, getText(), barBounds);
		} else {
			Rectangle imageRect = barBounds.getCopy();
			imageRect.shrink(0, getMargin());
			imageRect.shrink((imageRect.width - imageRect.height)/2, 0);
			Rectangle src = new Rectangle(0, 0,
					barImage.getImageData().width, barImage.getImageData().height);
			graphics.drawImage(barImage, src, imageRect);
		}
		
		if (backButtonImage != null || backButtonTitle != null ){
			Rectangle backBounds = barBounds.getCopy();
			backBounds.x = getMargin() + BUTTON_WIDTH/2;
			int archWidth = backBounds.height/2;
			int textXDelta = 0;
			if (backButtonTitle != null){
				Dimension d = FigureUtilities.getStringExtents(backButtonTitle, getFont_());
				textXDelta = archWidth - d.height / 2;
				backBounds.width = Math.max(d.width - textXDelta + 4, BUTTON_WIDTH);
			} else {
				backBounds.width = BUTTON_WIDTH;
			}
			Path buttonPath = new Path(null);
			buttonPath.moveTo(backBounds.x - archWidth, backBounds.getCenter().y);
			buttonPath.lineTo(backBounds.x, backBounds.y);
			//buttonPath.lineTo(backBounds.right(), backBounds.y);
			int buttonsInset = ScreenManager.TITLE_BAR_BUTTONS_INSET*2;
			buttonPath.addArc(backBounds.right() - buttonsInset, backBounds.y,
					buttonsInset, buttonsInset, 90, -90);
			//buttonPath.lineTo(backBounds.right(), backBounds.bottom());
			buttonPath.addArc(backBounds.right() - buttonsInset, backBounds.bottom() - buttonsInset,
					buttonsInset, buttonsInset, 0, -90);
			buttonPath.lineTo(backBounds.x, backBounds.bottom());
			buttonPath.lineTo(backBounds.x - archWidth, backBounds.getCenter().y);
			
			Color darker1 = new Color(null, ColorUtils.darker(
					getBorder().getBackgroundColor().getRGB()));
			graphics.setBackgroundColor(darker1);
			graphics.fillPath(buttonPath);
						
			if (backButtonImage == null){
				paintString(graphics, backButtonTitle, backBounds.getExpanded(new Insets(0, textXDelta, 0, 0)));
			} else {
				Rectangle src = new Rectangle(0, 0,
						backButtonImage.getImageData().width, backButtonImage.getImageData().height);
				graphics.drawImage(backButtonImage, src, backBounds);
			}
			graphics.setForegroundColor(ColorConstants.black);
			graphics.drawPath(buttonPath);
			darker1.dispose();
		}
		
		

		setDirty(false);
	}
	
	public void dispose() {
		super.dispose();
		/*if (userBarColor != null){
			userBarColor.dispose();
		}*/
		if (barImage != null){
			barImage.dispose();
		}
		if (backButtonImage != null){
			backButtonImage.dispose();
		}
	}
	
}
