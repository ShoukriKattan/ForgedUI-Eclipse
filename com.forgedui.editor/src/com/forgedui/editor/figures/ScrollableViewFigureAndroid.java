// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ScrollableViewFigureAndroid extends ScrollableViewFigure {
	
	protected void paintPagingControl(Graphics graphics) {
		if (pagingControlHeight > 0){
			graphics.setBackgroundColor(getPagingControlColor());
			
			if (currentPage > 0){
				paintLeftArrow(graphics);
			}
			
			if (currentPage < pagesCount - 1){
				paintRightArrow(graphics);
			}
		}
	}
	
	protected void paintLeftArrow(Graphics graphics){
		int width = pagingControlHeight / 4;
		Rectangle r = getBounds().getCopy();
		Point start = new Point(r.getRight().x - 10, r.getCenter().y);
		Path triangle = new Path(null);
		triangle.moveTo(start.x, start.y);
		triangle.lineTo(start.x - width, start.y - pagingControlHeight/2);
		triangle.lineTo(start.x - width, start.y + pagingControlHeight/2);
		graphics.fillPath(triangle);
	}
	
	protected void paintRightArrow(Graphics graphics){
		int width = pagingControlHeight / 4;
		Rectangle r = getBounds().getCopy();
		Point start = new Point(r.x + 10, r.getCenter().y);
		Path triangle = new Path(null);
		triangle.moveTo(start.x, start.y);
		triangle.lineTo(start.x + width, start.y - pagingControlHeight/2);
		triangle.lineTo(start.x + width, start.y + pagingControlHeight/2);
		graphics.fillPath(triangle);
	}
	
	public void setPagingControlHeight(int height) {
		if (Utils.safeNotEquals(this.pagingControlHeight, height)){
			this.pagingControlHeight = height;//don't add insets
			setDirty(true);
		}
	}

}
