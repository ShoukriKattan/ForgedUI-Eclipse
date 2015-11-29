// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

import com.forgedui.editor.figures.TitaniumTextFigure.Alignments;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TableViewFigure extends TitaniumFigure {

	private String headerTitle;
	
	private String footerTitle;
	
	private boolean hasHeaderView, hasFooterView;
	
	private Color headerColor;
	
	public TableViewFigure(Color headerColor){
		this.headerColor = headerColor;
		setMargin(5);
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		Rectangle bounds = getBounds();
		graphics.setBackgroundColor(headerColor);
		float bw =  getBorder().getBorderWidth();
		if (hasHeader()){
			PrecisionRectangle top = new PrecisionRectangle();
			top.setX(bounds.x + bw);
			top.setY(bounds.y + bw);
			top.setWidth(bounds.width - 2*bw);
			top.setHeight(getTitleHeight());
			//Path topPath = Drawer.getPartlyRoundedRectangle(top, getBorder().getBorderRadius() - bw, true, true, false, false);
			Path topPath = Drawer.getTopRoundedRectangle(top, getBorder().getActualBorderRadius(getBounds()) - bw);
			graphics.fillPath(topPath);
		}
		if (footerTitle != null || hasFooterView){
			PrecisionRectangle bottom = new PrecisionRectangle();
			bottom.setX(bounds.x + bw);
			bottom.setY(bounds.bottom() - bw - getTitleHeight());
			bottom.setWidth(bounds.width - 2*bw);
			bottom.setHeight(getTitleHeight());
			//Path bottomPath = Drawer.getPartlyRoundedRectangle(bottom, getBorder().getBorderRadius() - bw, false, false, true, true);
			Path bottomPath = Drawer.getBottomRoundedRectangle(bottom, getBorder().getActualBorderRadius(getBounds()) - bw);
			graphics.fillPath(bottomPath);
		}
		graphics.restoreState();
		this.paintString(graphics, headerTitle, true);
		this.paintString(graphics, footerTitle, false);
	}

	public boolean hasHeader() {
		return headerTitle != null || hasHeaderView;
	}
	
	public int getTitleHeight(){
		if (getFont_() != null){
			return FigureUtilities.getStringExtents("Some Text", getFont_()).height;
		} else {
			return FigureUtilities.getStringExtents("Some Text", Display.getCurrent().getSystemFont()).height;
		}
	}
	
	@Override
	public Insets getInsets() {
		Insets insets = new Insets();
		int h = getTitleHeight();
		if (headerTitle != null) insets.top = h;
		if (footerTitle != null) insets.bottom = h;
		return super.getInsets().getAdded(insets);
	}
	
	protected void paintString(Graphics graphics, String text, boolean top) {
		if (text != null && text.length() > 0){
			Rectangle textBounds = getBounds().getCopy();
			textBounds.shrink((int)(getBorder().getBorderWidth() + getMargin() + getBorder().getActualBorderRadius(getBounds())/3),
					(int)getBorder().getBorderWidth());
			textBounds.height = getTitleHeight();
			
			if (!top){
				textBounds.y = getBounds().bottom() - getTitleHeight();
			}
			
			Drawer.drawString(graphics, text, textBounds, Alignments.left, Alignments.center);
		}
	}
	
	public void setHeaderTitle(String name){
		if (Utils.safeNotEquals(this.headerTitle, name)){
			this.headerTitle = name;
			setDirty(true);
		}
	}
	
	public void setFooterTitle(String name){
		if (Utils.safeNotEquals(this.footerTitle, name)){
			this.footerTitle = name;
			setDirty(true);
		}
	}
	
	//resolve header view problems
	private static final Point PRIVATE_POINT = new Point();
	private static final Rectangle PRIVATE_RECT = new Rectangle();
	
	private Rectangle getFullArea(){
		PRIVATE_RECT.setBounds(getBounds());
		PRIVATE_RECT.crop(getBorder().getInsets(this));
		PRIVATE_RECT.setLocation(0, 0);
		if (hasHeader())
			PRIVATE_RECT.setLocation(0, -getTitleHeight());
		return PRIVATE_RECT;
	}
	
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty())
			return;

		graphics.translate(getBounds().x + getInsets().left, getBounds().y
				+ getInsets().top);

		graphics.clipRect(getFullArea());
		graphics.pushState();
		paintChildren(graphics);
		graphics.popState();
		graphics.restoreState();
	}
	
	protected IFigure findDescendantAtExcluding(int x, int y, TreeSearch search) {
		PRIVATE_POINT.setLocation(x, y);
		translateFromParent(PRIVATE_POINT);
		if (!getFullArea().contains(PRIVATE_POINT))
			return null;

		x = PRIVATE_POINT.x;
		y = PRIVATE_POINT.y;
		IFigure fig;
		for (int i = getChildren().size(); i > 0;) {
			i--;
			fig = (IFigure) getChildren().get(i);
			if (fig.isVisible()) {
				fig = fig.findFigureAt(x, y, search);
				if (fig != null)
					return fig;
			}
		}
		// No descendants were found
		return null;
	}

	public void setHasHeaderView(boolean hasHeaderView) {
		this.hasHeaderView = hasHeaderView;
	}
	
	public void setHasFooterView(boolean hasFooterView) {
		this.hasFooterView = hasFooterView;
	}
	
	public int getHeaderHeight(){
		return hasHeader() ? getTitleHeight() : 0;
	}
	
	public int getFooterHeight(){
		return footerTitle != null || hasFooterView ? getTitleHeight() : 0;
	}


}
