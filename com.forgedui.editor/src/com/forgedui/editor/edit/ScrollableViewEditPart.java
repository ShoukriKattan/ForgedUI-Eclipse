// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;

import com.forgedui.editor.figures.ScrollableViewFigure;
import com.forgedui.model.Container;
import com.forgedui.model.titanium.ScrollableView;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ScrollableViewEditPart extends TitaniumContainerEditPart<ScrollableView> {
	
	@Override
	protected List<?> getModelChildren_() {
		ScrollableView view = (ScrollableView)getModel();
		List<?> children = view.getChildren();
		if (view.getCurrentPage() != null && view.getCurrentPage()  >= 0
				/*&& chchildren.contains(o)*/){
			return Collections.singletonList(children.get(view.getCurrentPage()));
		}
		return Collections.emptyList();
	}
	
	@Override
	protected IFigure createFigure() {
		IFigure figure = super.createFigure();
		figure.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent me) {}
			
			@Override
			public void mousePressed(MouseEvent me) {
				//TODO select view if we clicked on "dot"
				//System.out.println("Pressed: " + me.getLocation());
			}
			
			@Override
			public void mouseDoubleClicked(MouseEvent me) {}
		});
		return figure;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (ScrollableView.PROP_CURRENT_PAGE.equals(e.getPropertyName())
				|| Container.PROPERTY_CHILDREN.equals(e.getPropertyName())){
			refresh();
		} else {
			super.propertyChange(e);
		}
	}
	
	@Override
	protected void refreshVisuals() {
		ScrollableViewFigure figure = (ScrollableViewFigure)getFigure();
		ScrollableView model = getModel();
		
		figure.setPagingControlColor(model.getPagingControlColor());
		figure.setPagingControlHeight(Utils.getBoolean(model.getShowPagingControl(), true)
				? (int)Utils.getFloat(model.getPagingControlHeight(), 40) : 0);
		
		figure.setPagesCount(model.getChildren().size());
		figure.setCurrentPage(Utils.getInt(model.getCurrentPage(), -1));
		
		super.refreshVisuals();
	}
	
}
