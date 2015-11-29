// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ScrollViewFigure extends TitaniumFigure {
	
	private ScrollPane scrollpane;
	
	private IFigure pane;
	
	private IFigure container;

	public ScrollViewFigure() {
		pane = new FreeformLayer();
		pane.setLayoutManager(new FreeformLayout());
		setLayoutManager(new StackLayout());
		container = new TitaniumFigure();
		container.setLayoutManager(new FreeformLayout());
		pane.add(container);
		
		scrollpane = new ScrollPane();
		scrollpane.setScrollBarVisibility(ScrollPane.AUTOMATIC);
		scrollpane.setViewport(new FreeformViewport());
		scrollpane.setContents(pane);
		add(scrollpane);
	}
	
	@Override
	public void setBorder(Border border) {
		getContentsPane().setBorder(border);
	}
	
	@Override
	public TitaniumBackgroundBorder getBorder() {
		return (TitaniumBackgroundBorder) getContentsPane().getBorder();
	}
	
	public void setViewPreferredSize(Dimension size) {
		pane.setPreferredSize(size);
		container.setSize(size);
		pane.revalidate();
	}
	
	public IFigure getContainer() {
		return container;
	}
	
	public IFigure getContentsPane() {
		return pane;
	}
	
	public void setScrollBarVisibility(Boolean h, Boolean v){
		scrollpane.setHorizontalScrollBarVisibility(getScrollVisibility(h));
		scrollpane.setVerticalScrollBarVisibility(getScrollVisibility(v));
		pane.revalidate();
	}
	
	private int getScrollVisibility(Boolean h){
		return h == null ? ScrollPane.AUTOMATIC :
			(h ? ScrollPane.ALWAYS : ScrollPane.NEVER);
	}

}
