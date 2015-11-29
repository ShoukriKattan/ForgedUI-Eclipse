// LICENSE
package com.forgedui.editor.figures;

import java.io.File;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.forgedui.util.Converter;
import com.forgedui.util.Utils;



/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TableViewRowFigure extends TitaniumTextFigure {
	
	private static final Dimension hasImageDimension = new Dimension(22, 22);
	
	private String strLeftImage;
	
	private Image leftImage;
	
	private String strRightImage;
	
	private Image rightImage;

	private Image hasImage = null;

	public TableViewRowFigure(){
		setText("Row");
		setTextHorisontalAlign(Alignments.left);
		//setInsets(new Insets(0, 10, 0, 0));
	}

	public void setHasImage(Image hasImage) {
		if (Utils.safeNotEquals(this.hasImage, hasImage)){
			this.hasImage = hasImage;
			setDirty(true);
		}
	}
	
	public void setLeftImage(String imagePath) {
		if (Utils.safeNotEquals(strLeftImage, imagePath)){
			if (leftImage != null){
				leftImage.dispose();
				leftImage = null;
			}
			if (imagePath != null){
				leftImage = Converter.getImageFromUrl(imagePath);
				if (leftImage == null && !Converter.isStringUrl(imagePath)){
					File imageFile = new File(imagePath);
					if (imageFile.exists()){
						leftImage = new Image(Display.getCurrent(), imagePath);
					}
				}
			}
			strLeftImage = imagePath;
			setDirty(true);
		}
	}
	
	public void setRightImage(String imagePath) {
		if (Utils.safeNotEquals(strRightImage, imagePath)){
			if (rightImage != null){
				rightImage.dispose();
				rightImage = null;
			}
			if (imagePath != null){
				rightImage = Converter.getImageFromUrl(imagePath);
				if (rightImage == null && !Converter.isStringUrl(imagePath)){
					File imageFile = new File(imagePath);
					if (imageFile.exists()){
						rightImage = new Image(Display.getCurrent(), imagePath);
					}
				}
			}
			strRightImage = imagePath;
			setDirty(true);
		}
	}

	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		super.paintTitaniumFigure(graphics);
		this.paintHasImage(graphics);
		this.paintLeftImage(graphics);
		this.paintRightImage(graphics);
	}

	protected void paintHasImage(Graphics graphics) {
		if (hasImage != null){
			Rectangle clientArea = getClientArea();
			clientArea.setLocation(getLocation());
			graphics.clipRect(clientArea);
			
			int dy = (clientArea.height - hasImageDimension.height) /2;
			Rectangle dest = new Rectangle(
					new Point(clientArea.right() - hasImageDimension.width - 10,
					clientArea.y + dy),
					hasImageDimension
					);
			
			Rectangle src = new Rectangle(0, 0,
					hasImage.getImageData().width, hasImage.getImageData().height);
			graphics.drawImage(hasImage, src, dest);
		}
	}

	protected void paintLeftImage(Graphics graphics) {
		if (leftImage != null){
			Rectangle src = super.getTextHolderRectangle();
			src.translate(0, (src.height - leftImage.getImageData().height) / 2);
			graphics.drawImage(leftImage, src.getLocation());
		}
	}
	
	protected void paintRightImage(Graphics graphics) {
		if (rightImage != null){
			Rectangle src = super.getTextHolderRectangle();
			src.translate(src.width - rightImage.getImageData().width,
					(src.height - rightImage.getImageData().height) / 2);
			graphics.drawImage(rightImage, src.getLocation());
		}
	}
	
	@Override
	protected Rectangle getTextHolderRectangle() {
		Insets insets = new Insets(0);
		if (leftImage != null){
			insets.left = leftImage.getImageData().width;
		}
		if (rightImage != null){
			insets.right = rightImage.getImageData().width;
		}
		return super.getTextHolderRectangle().getCropped(insets);
	}
	
	public void dispose() {
		super.dispose();
		if (leftImage != null){
			leftImage.dispose();
		}
		if (rightImage != null){
			rightImage.dispose();
		}
	}

}
