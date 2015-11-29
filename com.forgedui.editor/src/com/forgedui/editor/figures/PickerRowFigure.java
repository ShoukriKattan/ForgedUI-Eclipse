// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import com.forgedui.util.Utils;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class PickerRowFigure extends TitaniumTextFigure {
	
	private static final Dimension rightImageDimension = new Dimension(22, 22);
	
	private Image rightImage;
	
	@Override
	protected void paintTitaniumFigure(Graphics graphics) {
		super.paintTitaniumFigure(graphics);
		this.paintRightImage(graphics);
	}

	protected void paintRightImage(Graphics graphics) {
		if (rightImage != null){
			Rectangle clientArea = getClientArea();
			clientArea.setLocation(getLocation());
			graphics.clipRect(clientArea);
			
			int dy = (clientArea.height - rightImageDimension.height) /2;
			Rectangle dest = new Rectangle(
					new Point(clientArea.right() - rightImageDimension.width - 10,
					clientArea.y + dy),
					rightImageDimension
					);
			
			Rectangle src = new Rectangle(0, 0,
					rightImage.getImageData().width, rightImage.getImageData().height);
			graphics.drawImage(rightImage, src, dest);
		}
	}

	public void setRightImage(Image rightImage) {
		if (Utils.safeNotEquals(this.rightImage, rightImage)){
			this.rightImage = rightImage;
			setDirty(true);
		}
	}
	
	@Override
	public void setBounds(Rectangle rect) {
		// TODO Auto-generated method stub
		super.setBounds(rect);
	}

}
