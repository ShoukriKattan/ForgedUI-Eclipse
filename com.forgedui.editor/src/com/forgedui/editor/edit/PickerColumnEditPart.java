// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.MouseWheelHelper;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.ViewportAutoexposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gef.editparts.ViewportMouseWheelHelper;
import org.eclipse.swt.graphics.Color;

import com.forgedui.editor.edit.policy.ChangeConstraintEditPolicy;
import com.forgedui.editor.figures.PickerColumnFigure;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * As children can't be added not a container
 */
public class PickerColumnEditPart extends TitaniumElementEditPart<PickerColumn> {
	
	@Override
	protected IFigure createFigure() {
		Dimension dimension = getModel().getDimension();
		IFigure figure = super.createFigure();
		if (dimension.width <= 0 && dimension.height <= 0){
			getModel().setDimension(getSupport().viewToModel(figure.getSize()));
		}
		return figure;
	}
	
	@Override
	public List<?> getModelChildren() {
		//no zIndex compare!!!
		return getModelChildren_();
	}
	
	@Override
	protected List<?> getModelChildren_(){
		return getModel().getChildren();
	}
	
	@Override
	protected void refreshVisuals() {
		Color c = ((PickerEditPart)getParent()).getFigure().getBorder().getBackgroundColor();
		//getFigure().getContainer().setBackgroundColor(ColorConstants.red);
		getFigure().setBackgroundColor(c);
		//getFigure().getContentsPane().setBackgroundColor(ColorConstants.blue);
		super.refreshVisuals();
	}
	
	@Override
	public PickerColumnFigure getFigure() {
		return (PickerColumnFigure) super.getFigure();
	}
	
	@Override
	protected void refreshChildren() {
		PickerColumn picker = getModel();
		
		Rectangle visualBounds = getFigure().getClientArea();
		Dimension d = getSupport().viewToModel(visualBounds.getSize());
		Point location = getSupport().viewToModel(visualBounds.getLocation());
		
		int left = location.x;
		int top = location.y;
		for (int i = 0; i < picker.getChildren().size(); i++) {
			Dimension defSize = getDefaults().getSize(picker.getChildren().get(i));
			Dimension childD = new Dimension();
			childD.width = d.width;
			childD.height = defSize.height;
			picker.getChildren().get(i).setDimension(childD);
			picker.getChildren().get(i).setLocation(new Point(left, top));
			top+=childD.height;
		}
		super.refreshChildren();
		List<?> children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			EditPart editPart = (EditPart) children.get(i);
			editPart.refresh();
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (Element.PROPERTY_SIZE.equals(evt.getPropertyName()) || Element.PROPERTY_LOCATION.equals(evt.getPropertyName())) {
			//refreshVisuals();
			//((GraphicalEditPart)getParent()).getFigure().revalidate();
			((GraphicalEditPart)getParent()).refresh();
		} else if (Container.PROPERTY_CHILDREN.equals(evt.getPropertyName())){
			refreshChildren();
		//	((GraphicalEditPart)getParent()).getFigure().revalidate();
		} else {
			super.propertyChange(evt);
		}
	}
	
	@Override
	public IFigure getContentPane() {
		return getFigure().getContentsPane();
	}
	
	private IFigure getContainer(){
		return getFigure().getContainer();
	}
	
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_OPEN) {
			Picker picker = (Picker) getModel().getParent();
			if (picker.getPlatform().isAndroid()){
				picker.setExpanded(!picker.isExpanded());
			}//ignore
		} else {
			super.performRequest(request);
		}
	}

	@Override
	public PickerEditPart getParent() {
		return (PickerEditPart) super.getParent();
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		//this is necessary to make children able to be selected
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ChangeConstraintEditPolicy());
	}

	@Override
	public boolean isSelectable() {
		return getModel().canEdit();
	}
	
	@Override
	public void setSelected(int value) {
		if (!isSelectable()){
			if (EditPart.SELECTED_NONE == value)
				super.setSelected(value);
		} else {
			super.setSelected(value);
		}
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
