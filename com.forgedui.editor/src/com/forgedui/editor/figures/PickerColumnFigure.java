// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class PickerColumnFigure extends TitaniumFigure {
	
	private static final Insets insets = new Insets(0, 1, 0, 0);

	private ScrollPane scrollpane;
	
	private IFigure pane;
	
	private IFigure container;

	public PickerColumnFigure() {
		pane = new FreeformLayer();
		pane.setLayoutManager(new FreeformLayout());
		setLayoutManager(new StackLayout());
		container = new TitaniumFigure();
		container.setLayoutManager(new FreeformLayout());
		pane.add(container);
		
		scrollpane = new ScrollPane();
		scrollpane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.NEVER);
		scrollpane.setViewport(new FreeformViewport());
		scrollpane.setContents(pane);
		add(scrollpane);
	}
	
	public ScrollPane getScrollPane(){
		return scrollpane;
	}
	
	@Override
	public void setBorder(Border border) {
		getContentsPane().setBorder(border);
	}
	
	@Override
	public TitaniumBackgroundBorder getBorder() {
		return (TitaniumBackgroundBorder) getContentsPane().getBorder();
	}
	
	public IFigure getContainer() {
		return container;
	}
	
	public IFigure getContentsPane() {
		return pane;
	}
	
	@Override
	public Insets getInsets() {
		return insets;
	}
	
	class FlowLayout1 extends FlowLayout{

		@Override
		public void layout(IFigure parent) {
			super.layout(parent);
			Rectangle r = parent.getClientArea();
			
			int left = 0;
			IFigure f = null;
			for (int i = 0; i < parent.getChildren().size(); i++) {
				f = (IFigure) parent.getChildren().get(i);
				f.setLocation(r.getLocation().translate(left, 0));
				left += f.getSize().width;
				f.setSize(f.getSize().width, r.getSize().height);//set height
			}
			if (f != null && left < r.getSize().width){
				f.setSize(f.getSize().width + r.getSize().width - left, f.getSize().height);//set width to last column
			}
		}


		@Override
		protected Dimension calculatePreferredSize(IFigure container, int wHint,
				int hHint) {
			return getMaximumSize();
		}
		
		@Override
		public Object getConstraint(IFigure child) {
			return super.getConstraint(child);
		}
		
		protected Dimension getChildSize(IFigure child, int wHint, int hHint) {
			return child.getPreferredSize(wHint, hHint);
		}
		
	}

}
