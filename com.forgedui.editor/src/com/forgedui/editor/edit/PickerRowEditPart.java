// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import com.forgedui.editor.defaults.IphoneDefaultsProvider;
import com.forgedui.editor.figures.PickerRowFigure;
import com.forgedui.editor.figures.TitaniumTextFigure.Alignments;
import com.forgedui.editor.images.TitaniumImages;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.Picker.PickerType;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class PickerRowEditPart extends TitaniumContainerEditPart<PickerRow> {
	
	static final FontData fontData = new FontData(IphoneDefaultsProvider.F_NAME, 25, SWT.NORMAL);
	
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
	protected void refreshVisuals() {
		PickerRow model = getModel();
		PickerRowFigure figure = (PickerRowFigure) getFigure();
		figure.setText(model.getTitle());
		figure.getBorder().setOpaque(getBackgroundColor() == null);
		figure.setTextHorisontalAlign(getTextAlign());
		
		if (model.getPlatform().isAndroid()
				&& getPickerType() == PickerType.PICKER_TYPE_PLAIN
				&& !Utils.getObject(
						getParent().getParent().getModel().getUseSpinner(), false)){
			if (!Utils.getBoolean(model.getSelected(), false)){
				figure.setRightImage(TitaniumImages.getImage(model.getPlatform(), TitaniumImages.CHECK_FALSE));
			} else {
				figure.setRightImage(TitaniumImages.getImage(model.getPlatform(), TitaniumImages.CHECK_TRUE));
			}
		} else {
			figure.setRightImage(null);
		}
		
		super.refreshVisuals();
		getFigure().revalidate();
	}
	
	@Override
	protected String getBackgroundColor() {
		if (Utils.getBoolean(getModel().getSelected(), false)
				&& getPickerType() == PickerType.PICKER_TYPE_PLAIN){
			if (getModel().getPlatform().isAndroid()){
				return Utils.getString(getModel().getBackgroundSelectedColor(), 
						getModel().getBackgroundColor());
			} else {
				return "#c9c9ff";
			}
			
		}
		return super.getBackgroundColor();
	}
	
	@Override
	public FontData getFontData() {
		if (!getModel().canEdit()){
			if ("+".equals(getModel().getTitle()) || "-".equals(getModel().getTitle()) ){
				return fontData;
			}
		}
		return super.getFontData();
	}

	protected Alignments getTextAlign() {
		switch (getPickerType()) {
		case PICKER_TYPE_DATE:
			return (getParent().getParent().useSpinner()) ? Alignments.center : Alignments.right;
		case PICKER_TYPE_COUNT_DOWN_TIMER:
		case PICKER_TYPE_PLAIN:
			return Alignments.left;
		default:
			return Alignments.center;
		}
	}

	protected PickerType getPickerType(){
		return Utils.getObject(
				getParent().getParent().getModel().getPickerType(),
				PickerType.PICKER_TYPE_PLAIN);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (Element.PROPERTY_SIZE.equals(evt.getPropertyName()) || Element.PROPERTY_LOCATION.equals(evt.getPropertyName())) {
			super.propertyChange(evt);
			getFigure().revalidate();
		} else {
			super.propertyChange(evt);
		}
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
	
	@Override
	public PickerColumnEditPart getParent() {
		return (PickerColumnEditPart) super.getParent();
	}
	
	protected boolean directEditingEnabled() { 
		return getParent().getParent().canEdit();
	}
}
