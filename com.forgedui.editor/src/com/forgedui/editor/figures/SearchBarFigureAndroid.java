// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SearchBarFigureAndroid extends SearchBarFigure {

	private Image cancelImage;
	
	/**
	 * @param defaultBarColor
	 * @param searchImage
	 */
	public SearchBarFigureAndroid(Color defaultBarColor, Image cancel) {
		super(defaultBarColor, null);
		cancelImage = cancel;
	}
	
	@Override
	protected int getMaxHeight() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected int getCancelButtonWidth() {
		return cancelImage.getBounds().width;
	}
	
	@Override
	protected int getMinimumBarRadius() {
		return 3;
	}
	
	@Override
	protected void paintCancel(Graphics graphics) {
		Rectangle barBounds = getBounds().getCopy();
		barBounds.crop(new Insets(getMargin()));
		Point imagePoint = new Point(barBounds.getRight().x, barBounds.getCenter().y);
		imagePoint.translate(-getCancelButtonWidth(), -cancelImage.getBounds().height / 2);
		graphics.drawImage(cancelImage, imagePoint);
	}

}
