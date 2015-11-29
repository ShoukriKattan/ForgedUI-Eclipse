package com.forgedui.editor.edit;

import com.forgedui.editor.figures.SliderFigure;
import com.forgedui.model.titanium.Slider;
import com.forgedui.util.Utils;

/**
 * Edit part for the slider component.
 * 
 * @author Tareq Doufish
 *
 */
public class SliderEditPart extends TitaniumElementEditPart<Slider> {
	

	@Override
	protected void refreshVisuals() {
		SliderFigure figure = (SliderFigure)getFigure();
		Slider model = getModel();
		
		double value = getDefaultValue();
		if (model.getMin() != null && model.getMax() != null){
			if (model.getMax() > model.getMin()){
				if (model.getValue() != null){
					value = (model.getValue() - model.getMin())/(model.getMax() - model.getMin());
				} else if (model.getMinRange() != null){
					value = (model.getMinRange() - model.getMin())/(model.getMax() - model.getMin());
				}
			}
		}	
		figure.setValue(value);
		
		super.refreshVisuals();
	}
	
	protected float getDefaultValue(){
		return getModel().getPlatform().isAndroid() ? 0 : 1;
	}
	
	@Override
	protected String getBackgroundColor() {
		return Utils.getBoolean(getModel().getEnabled(), true) ? getModel().getBackgroundColor()
					: Utils.getString(
							getModel().getBackgroundDisabledColor(),
							getModel().getBackgroundColor());

	}
	
	@Override
	protected String getBackgroundImage() {
		return Utils.getBoolean(getModel().getEnabled(), true) ? getModel().getBackgroundImage()
				: Utils.getString(
						getModel().getBackgroundDisabledImage(),
						getModel().getBackgroundImage());
	}

}
