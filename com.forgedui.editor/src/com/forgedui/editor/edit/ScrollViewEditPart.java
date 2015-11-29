// LICENSE
package com.forgedui.editor.edit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.MouseWheelHelper;
import org.eclipse.gef.editparts.ViewportAutoexposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gef.editparts.ViewportMouseWheelHelper;

import com.forgedui.editor.figures.ScrollViewFigure;
import com.forgedui.model.titanium.ScrollView;
import com.forgedui.model.titanium.ScrollView.ScrollType;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ScrollViewEditPart extends TitaniumContainerEditPart<ScrollView> {
	
	@Override
	protected void refreshVisuals() {
		ScrollViewFigure figure = (ScrollViewFigure)getFigure();
		ScrollView model = getModel();
		Dimension size = model.getDimension();
		Float fVal = model.getContentWidth();
		int contentWidth = fVal != null ? fVal.intValue() : size.width;
		fVal = model.getContentHeight();
		int contentHeight = fVal != null ? fVal.intValue() : size.height;
		//float scale = model.getZoomScale() == null ? 1 : model.getZoomScale();
		Dimension contentSize = new Dimension(contentWidth, contentHeight);
		figure.setViewPreferredSize(contentSize);
		figure.setSize(size);
		figure.setLocation(model.getLocation());
		Boolean h = model.getShowHorizontalScrollIndicator();
		Boolean v = model.getShowVerticalScrollIndicator();
		if (ScrollType.horizontal == model.getScrollType()){
			v = false;
		}  else if (ScrollType.vertical == model.getScrollType()){
			h = false;
		}
		figure.setScrollBarVisibility(h, v);
		
		super.refreshVisuals();
	}
	
	@Override
	public IFigure getContentPane() {
		return ((ScrollViewFigure) getFigure()).getContentsPane();
	}
	
	private IFigure getContainer(){
		return ((ScrollViewFigure) getFigure()).getContainer();
	}
	
	//*************** use getContainer() instead of getContentPane()
	
	/*@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		getContainer().add(child, index);
	}
	
	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		getContainer().remove(child);
	}*/
	
	@Override
	protected void reorderChild(EditPart child, int index) {
		IFigure childFigure = ((GraphicalEditPart) child).getFigure();
		LayoutManager layout = getContainer().getLayoutManager();
		Object constraint = null;
		if (layout != null)
			constraint = layout.getConstraint(childFigure);

		super.reorderChild(child, index);
		setLayoutConstraint(child, childFigure, constraint);
	}
	
	public Object getAdapter(Class key) {
		if (key == AutoexposeHelper.class)
			return new ViewportAutoexposeHelper(this);
		if (key == ExposeHelper.class)
			return new ViewportExposeHelper(this);
		if (key == MouseWheelHelper.class)
			return new ViewportMouseWheelHelper(this);
		return super.getAdapter(key);
	}
	
}
