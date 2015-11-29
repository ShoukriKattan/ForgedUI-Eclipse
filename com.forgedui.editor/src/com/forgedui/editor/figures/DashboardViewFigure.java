// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DashboardViewFigure extends TitaniumFigure {
	
	private static final int ARCH_RADIUS = 5;
	
	private int pagesCount;
	
	private int currentPage;

	public DashboardViewFigure() {
		setLayoutManager(new DashboardViewLayout());
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		paintPagingControl(graphics);
	}

	protected void paintPagingControl(Graphics graphics){
		int diametr = ARCH_RADIUS * 2;
		int width = diametr * 2 * pagesCount - diametr;
		Point leftTop = new Point(getBounds().getCenter().x - width / 2,
				getBounds().bottom() - ARCH_RADIUS * 3);
		
		for (int i = 0; i < pagesCount; i++) {
			graphics.setBackgroundColor(currentPage == i ?
					ColorConstants.black : ColorConstants.lightGray);

			graphics.fillArc(leftTop.x,	leftTop.y, diametr,
					diametr, 0, 360);
			leftTop.translate(diametr*2, 0);
		}
	}
	
	public void setPagesCount(int pagesCount) {
		if (Utils.safeNotEquals(this.pagesCount, pagesCount)){
			this.pagesCount = pagesCount;
			setDirty(true);
		}
	}

	public void setCurrentPage(int currentPage) {
		if (Utils.safeNotEquals(this.currentPage, currentPage)){
			this.currentPage = currentPage;
			setDirty(true);
		}
	}

	@Override
	public void invalidate() {
		if (getLayoutManager() != null){
			getLayoutManager().invalidate();
			getLayoutManager().layout(this);
		}
		
		setValid(false);
	}

}

class DashboardViewLayout extends AbstractLayout {
	
	private static final Dimension CHILD_SIZE = new Dimension(75, 85);

	@Override
	public void layout(IFigure parent) {
		Point offset = parent.getClientArea().getLocation();
		Dimension pSize = parent.getClientArea().getSize();
		int wGap = (pSize.width - 3*CHILD_SIZE.width) / 4;
		int hGap = (pSize.height - 3*CHILD_SIZE.height) / 4;
		wGap = wGap > 0 ? wGap : 0;
		hGap = hGap > 0 ? hGap : 0;
		
		for (int i = 0; i < parent.getChildren().size(); i++) {
			int indexOnPage = i % 9;
			IFigure f = (IFigure) parent.getChildren().get(i);
			Rectangle bounds = new Rectangle(offset.getTranslated(wGap, hGap), CHILD_SIZE);
			Point pos = new Point(indexOnPage % 3 *(CHILD_SIZE.width + wGap),
								indexOnPage / 3 *(CHILD_SIZE.height + hGap));
			bounds = bounds.getTranslated(pos);
			f.setBounds(bounds);
		}
	}

	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint,
			int hHint) {
		return new Dimension(CHILD_SIZE.width*3 + 20, CHILD_SIZE.height*3 + 20);
	}

}