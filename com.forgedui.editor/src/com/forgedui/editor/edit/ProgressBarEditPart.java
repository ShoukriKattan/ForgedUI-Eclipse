// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;

import com.forgedui.editor.figures.ProgressBarFigure;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.model.titanium.ProgressBar;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ProgressBarEditPart extends TitaniumElementEditPart<ProgressBar> {
	
	@Override
	protected void refreshVisuals() {
		ProgressBarFigure figure = (ProgressBarFigure)getFigure();
		ProgressBar model = getModel();
		
		if (model.getMin() != null && model.getMax() != null && model.getValue() != null){
			figure.setValue((model.getValue() - model.getMin())/(model.getMax() - model.getMin()));
		} else {
			figure.setValue(0);
		}
		
		figure.setFontColor(model.getColor());
		figure.setText(model.getMessage());
		
		super.refreshVisuals();
		
		/*if (figure.isDirty()){
			figure.repaint();
		}*/
	}
	
	/*@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean isTypeVisiual = PropertyType.isVisualProperty(getModel().getClass().getName(), evt.getPropertyName());
		if (isTypeVisiual){
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}*/


}
