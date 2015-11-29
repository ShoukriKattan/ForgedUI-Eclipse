//LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.forgedui.editor.util.ColorUtils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class PickerColumnBackgroundBorder extends TitaniumBackgroundBorder {

	/**
	 * @param defaultBackgroundColor
	 * @param defaultBorderColor
	 * @param borderWidth
	 * @param borderRadius
	 * @param showGradient
	 */
	public PickerColumnBackgroundBorder(Color defaultBackgroundColor,
			Color defaultBorderColor, float borderWidth, float borderRadius,
			boolean showGradient) {
		super(defaultBackgroundColor, defaultBorderColor, borderWidth, borderRadius,
				showGradient,null);
	}
	
	protected void showGradient(Graphics graphics, PrecisionRectangle tempRect,
			float internalRadius) {
		Color backgroundColor = getBackgroundColor();
		//top gradient
		Image gradientImage = Drawer.getGradientRectangle(
				tempRect, backgroundColor.getRGB(), ColorUtils.darker(ColorUtils.darker(ColorUtils.darker(
						backgroundColor.getRGB()))), internalRadius, 50, SWT.TOP);

		graphics.drawImage(gradientImage, tempRect.x, tempRect.y);
		gradientImage.dispose();
		
		//bottom gradient
		gradientImage = Drawer.getGradientRectangle(
				tempRect, ColorUtils.darker(ColorUtils.darker(ColorUtils.darker(
						backgroundColor.getRGB()))), backgroundColor.getRGB(), internalRadius, 50, SWT.BOTTOM);

		graphics.drawImage(gradientImage, tempRect.x, tempRect.bottom() - gradientImage.getBounds().height);
		gradientImage.dispose();
	}

}
