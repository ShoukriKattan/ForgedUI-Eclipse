// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ScrollableViewFigure extends TitaniumFigure {
	
	private String strPagingControlColor;
	
	private Color userPagingControlColor;
	
	protected Color defaultPagingControlColor = ColorConstants.black;
	
	protected int pagesCount;
	
	protected int currentPage;
	
	protected int pagingControlHeight;

	public void setPagingControlColor(String color) {
		if (Utils.safeNotEquals(strPagingControlColor, color)){
			if (strPagingControlColor != null){
				userPagingControlColor.dispose();
			}
			userPagingControlColor = (color == null)  ? null
				: Converter.getColorFromHexa(color);
			strPagingControlColor = color;
			setDirty(true);
		}
	}
	
	protected Color getPagingControlColor(){
		return userPagingControlColor != null ? userPagingControlColor : defaultPagingControlColor;
	}
	
	public void setPagingControlHeight(int height) {
		if (Utils.safeNotEquals(this.pagingControlHeight, height)){
			this.pagingControlHeight = height;
			setInsets(new Insets(0,0,pagingControlHeight,0));
			setDirty(true);
		}
	}
	
	/**
	 * @param pagesCount the pagesCount to set
	 */
	public void setPagesCount(int viewsNumber) {
		this.pagesCount = viewsNumber;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int selectedNumber) {
		this.currentPage = selectedNumber;
	}
	
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		paintPagingControl(graphics);
	}

	protected void paintPagingControl(Graphics graphics) {
		if (pagingControlHeight > 0){
			graphics.setBackgroundColor(getPagingControlColor());
			
			Rectangle r = getBounds().getCopy();
			r.y = r.bottom() - pagingControlHeight;
			r.height = pagingControlHeight;
			graphics.fillRectangle(r);
			
			graphics.setBackgroundColor(ColorConstants.white);
			int diametr = Math.min(pagingControlHeight, 10);
			
			int width = diametr*(pagesCount*2 - 1);
			
			int x = r.getCenter().x - width / 2;
			for (int i = 0; i < pagesCount; i++) {
				//draw dot at the bottom
				graphics.fillArc(x,
						r.getCenter().y - diametr / 2, diametr, diametr,
						0, 360);
				x += diametr*2;
			}
			
			if (currentPage >= 0){
				graphics.setBackgroundColor(ColorConstants.gray);
				x = r.getCenter().x - width / 2;
				x += diametr*2*currentPage;
				graphics.fillArc(x,
						r.getCenter().y - diametr / 2, diametr, diametr,
						0, 360);
			}
			
		}
	}
	
	public void dispose() {
		super.dispose();
		if (userPagingControlColor != null){
			userPagingControlColor.dispose();
		}
	}



	
}