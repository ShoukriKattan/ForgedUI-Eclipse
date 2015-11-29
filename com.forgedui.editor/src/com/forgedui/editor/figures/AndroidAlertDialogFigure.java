// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AndroidAlertDialogFigure extends AlertDialogFigure {
	
	/**
	 * @param okButton
	 */
	public AndroidAlertDialogFigure(DialogOptionFigure okButton) {
		super(okButton);
	}

	@Override
	public void paintTitle(Graphics graphics) {
		if (Utils.isNotEmpty(getText())){
			graphics.pushState();
			Color bgColor = graphics.getBackgroundColor(); 
			graphics.setBackgroundColor(ColorConstants.white);
			int titleHeight = FigureUtilities.getStringExtents(getText(), titleFont).height;
			Rectangle titleRect = getTextHolderRectangle();
			Rectangle imRect = new Rectangle(titleRect.x + getMargin(),
					titleRect.y + getMargin(), titleHeight,	titleHeight);
			graphics.fillArc(imRect, 0, 360);
			
			graphics.setBackgroundColor(bgColor);
			imRect.shrink(4, 4);
			graphics.fillArc(imRect, 0, 360);
			
			graphics.setBackgroundColor(ColorConstants.white);
			graphics.fillPolygon(new int[]{imRect.x + imRect.width / 6,
					imRect.y + imRect.width / 3,
					imRect.x + imRect.width - imRect.width/ 6,
					imRect.y + imRect.width / 3,
					imRect.x + imRect.width/ 2,
					imRect.y + imRect.width});
			
			graphics.setBackgroundColor(ColorConstants.gray);
			graphics.drawLine(titleRect.x + 50, titleRect.y + titleHeight + getMargin() + 1,
					titleRect.right() - 50, titleRect.y + titleHeight + getMargin() + 1);
			graphics.popState();
			titleRect.x += titleHeight + getMargin()*2;
			titleRect.width -= titleHeight + getMargin()*3;
			paintString(graphics, getText(), titleRect);
			
		}
	}

}
