// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import com.forgedui.editor.edit.policy.ChangeConstraintEditPolicy;
import com.forgedui.editor.figures.PickerFigure;
import com.forgedui.editor.util.BoundsHelper;
import com.forgedui.model.Container;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.Picker.PickerType;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.util.DateTimeLableProvider;
import com.forgedui.util.Utils;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class PickerEditPart extends ElementEditPart {

	private DateTimeLableProvider labelProvider = new DateTimeLableProvider(false, false);
	
	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		List<?> children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			EditPart editPart = (EditPart) children.get(i);
			editPart.refresh();
		}
		if (getSelection() >= 0){
			GraphicalEditPart colEP = (GraphicalEditPart) children.get(0);
			Rectangle r1 = colEP.getFigure().getBounds();
			int rowHeinght = getOneRowHeight();
			getFigure().getSelection().setBounds(new Rectangle(
					r1.x, r1.y + getSelection()*rowHeinght,
					getExpandedBounds().width - r1.x*2, rowHeinght
			));
		} else {
			getFigure().getSelection().setBounds(new Rectangle());
		}
	}
	
	public int getOneRowHeight(){
		return getDefaults().getSize(new PickerRow(), getSupport()).height;
	}
	
	protected int getSelection(){
		if (!getModel().canEdit() && getModel().isExpanded() &&getChildren().size() > 0){
				return 2;			
		}
		return -1;
	}
	
	@Override
	protected void refreshVisuals() {
		PickerFigure figure = getFigure();
		Picker model = getModel();
		figure.setExpanded(model.isExpanded());
		figure.setType(model.getPickerType());
		figure.setValue(getCollapsedString());
		if (model.isExpanded()){
			figure.setBounds(getExpandedBounds());
		} else {
			figure.setBounds(getBounds());
		}
		
		if (figure.isDirty()){
			figure.repaint();
		}
	}
	
	protected String getCollapsedString(){
		Picker model = getModel();
		if (model.getPlatform().isAndroid()){
			if (PickerType.PICKER_TYPE_PLAIN == getPickerType()){
				if (model.getChildren().size() > 0){
					PickerColumn column = model.getChildren().get(0);
					for (PickerRow row : column.getChildren()) {
						//return title of first selected row
						if (Utils.getBoolean(row.getSelected(), false) && 
								Utils.getBoolean(row.getVisible(), true)
								) return row.getTitle();
					}
				}
			} else {
				labelProvider.setShowDate(model.getPickerType() == PickerType.PICKER_TYPE_DATE);
				labelProvider.setShowTime(model.getPickerType() == PickerType.PICKER_TYPE_TIME);
				labelProvider.setFormat24(Utils.getBoolean(getModel().getFormat24(), false));
				return labelProvider.getText(model.getValue());
			}
		}
		return "<Empty>";
	}
	
	@Override
	protected List<?> getModelChildren_(){
		Container model = (Container)getModel();
		return model.getChildren();
	}
	
	protected Rectangle getExpandedBounds(){
		if (getModel().getPlatform().isAndroid()
				&& getPickerType() == PickerType.PICKER_TYPE_PLAIN
				&& useSpinner() && getModel().getVisibleItems() != null){
			int height = getModel().getVisibleItems()*getOneRowHeight();
			height += BoundsHelper.PICKER_COLUMN_MARGIN*2;
			Dimension d = new Dimension(getModel().getDimension().width, height);
			return new Rectangle(getModel().getLocation(), d);
		}
		Dimension size = getSupport().modelToView(getScreenBounds());
		Dimension d = getDefaults().getSize(getModel());
		int dy = (size.height - d.height)/2;
		Rectangle bounds = new Rectangle(0, dy,
				size.width, d.height);
		return bounds;
	}
	
	@Override
	protected List<?> getModelChildren() {
		if (getModel().isExpanded()){
			//no zIndex compare!!!
			return getModelChildren_();
		} else{
			return Collections.EMPTY_LIST;
		}
	}
	
	@Override
	public PickerFigure getFigure() {
		return (PickerFigure)super.getFigure();
	}
	
	@Override
	public Picker getModel() {
		return (Picker) super.getModel();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		//FIXME react on useSpinner/format24/value/countDown duration change!
		if (Picker.VISIBLE_ITEMS.equals(prop)){
			//PickerUtil.changePickerType(this, (PickerType)evt.getOldValue());
			refresh();
		} else if (Picker.PICKER_TYPE.equals(prop)){
			PickerType old = (PickerType)evt.getOldValue();
			if (Utils.getObject(old, PickerType.PICKER_TYPE_PLAIN) != getPickerType()
					|| Picker.VISIBLE_ITEMS.equals(prop)){
				PickerUtil.changePickerType(this, (PickerType)evt.getOldValue());
			}
			refreshVisuals();
		} else if (Picker.VALUE.equals(prop) ){
			if (PickerType.PICKER_TYPE_DATE == getPickerType()
					|| PickerType.PICKER_TYPE_TIME == getPickerType()){
				PickerUtil.changePickerType(this, getModel().getPickerType());
			}
			refreshVisuals();
		} else if (Picker.EXPAND_COLLAPSE.equals(prop)) {
			//this is necessary to reorder children: make expanded picker
			//appear at the top
			ScreenEditPart parent = getScreenEditPart();
			if (getModel().isExpanded()){
				getViewer().deselect(this);
			}
			if (parent != null)
				parent.refresh();
		} else if (Picker.USE_SPINNER.equals(prop)) {
			boolean useSpinner = useSpinner();
			if (PickerType.PICKER_TYPE_PLAIN ==	getPickerType()){
				if (!useSpinner && getModel().getPlatform().isAndroid()){
					getModel().setColumnCount(1);//only 1 column is allowed
				}
				boolean oldUseSpinner = Utils.getBoolean((Boolean) evt.getOldValue(), false);
				if (oldUseSpinner != useSpinner){
					refresh();
				}
			} else if (getModel().getPlatform().isAndroid() 
					&& PickerType.PICKER_TYPE_DATE == getPickerType()
					|| PickerType.PICKER_TYPE_TIME == getPickerType()){
				PickerUtil.changePickerType(this, getModel().getPickerType());
			}
		} else if (Picker.FORMAT24.equals(prop)) {
			if (PickerType.PICKER_TYPE_TIME == getModel().getPickerType()){
				PickerUtil.changePickerType(this, getModel().getPickerType());
			}
			refreshVisuals();
		} else if (Container.PROPERTY_CHILDREN.equals(prop)){
			refreshChildren();
		} else {
			super.propertyChange(evt);
		}
	}

	public boolean useSpinner() {
		return Utils.getBoolean(getModel().getUseSpinner(), false);
	}
	
	public boolean canEdit(){
		return PickerType.PICKER_TYPE_PLAIN == getPickerType();
	}

	protected PickerType getPickerType() {
		return Utils.getObject(getModel().getPickerType(), PickerType.PICKER_TYPE_PLAIN);
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		//this is necessary to make children able to be selected
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ChangeConstraintEditPolicy());
	}
	
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_OPEN) {
			Picker picker = getModel();
			if (picker.getPlatform().isAndroid()){
				picker.setExpanded(!picker.isExpanded());
			}//ignore
		} else {
			super.performRequest(request);
		}
	}

}
