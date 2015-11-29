// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

import com.forgedui.editor.edit.policy.ElementDirectEditPolicy;
import com.forgedui.editor.figures.PickerRowFigure;
import com.forgedui.editor.figures.TitaniumTextFigure;
import com.forgedui.editor.images.TitaniumImages;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.OptionDialog;
import com.forgedui.model.titanium.OptionDialogButton;
import com.forgedui.util.Converter;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class OptionDialogButtonEditPart extends ElementEditPart {
	
	@Override
	protected void refreshVisuals() {
		TitaniumTextFigure figure = (TitaniumTextFigure)getFigure();
		OptionDialogButton model = getModel();
		figure.setText(model.getTitle());
		if (figure.getBorder() != null){
			figure.getBorder().setBackgroundColor(getBackgroundColor());
		}
		
		if (figure instanceof PickerRowFigure) {
			PickerRowFigure pickerRowFigure = (PickerRowFigure) figure;
			String imageKey = isSelected() ? TitaniumImages.CHECK_TRUE : TitaniumImages.CHECK_FALSE;
			Image image = TitaniumImages.getImage(model.getPlatform(), imageKey);
			pickerRowFigure.setRightImage(image);
		}
		
		super.refreshVisuals();
		if (figure.isDirty())
			figure.repaint();
	}
	
	@Override
	public OptionDialogButton getModel() {
		return (OptionDialogButton)super.getModel();
	}
	
	protected String getBackgroundColor() {
		Element parent = getModel().getParent();
		OptionDialog dialog = (OptionDialog)parent;
		if (dialog.getOptionDialogButton(
				dialog.getCancel()) == getModel() && !getModel().getPlatform().isAndroid()){
			return "#111111";
		} else if (dialog.getOptionDialogButton(
				dialog.getDesctructive()) == getModel()){
			return Converter.getHexColorValue(getDefaults().getAdditionColor(getModel()));
		} else if (isSelected()){
			return "#c9c9ff";
		}
		return null;//standard uses default
	}
	
	public boolean isSelected(){
		OptionDialog dialog = (OptionDialog)getModel().getParent();
		return dialog.getOptionDialogButton(
				dialog.getSelected()) == getModel();
	}
		
	
	/**
	 * Prevent figure bound s override
	 */
	@Override
	public Rectangle getBounds() {
		return getFigure().getBounds();
	}

	@Override
	protected void createEditPolicies() {
		// Only install the direct editing for this item.
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new ElementDirectEditPolicy());
	}
	
	protected boolean directEditingEnabled() { 
		return true;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean isTypeVisiual = PropertyType.isVisualProperty(getModel().getClass().getName(), evt.getPropertyName());
		if (isTypeVisiual){
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}

}
