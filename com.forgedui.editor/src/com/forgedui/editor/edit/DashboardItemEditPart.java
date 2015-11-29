// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPart;

import com.forgedui.editor.figures.DashboardItemFigure;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.model.titanium.DashboardItem;
import com.forgedui.util.Converter;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DashboardItemEditPart extends ElementEditPart {
	
	@Override
	protected void refreshVisuals() {
		DashboardItemFigure figure = (DashboardItemFigure)getFigure();
		DashboardItem model = (DashboardItem)getModel();
		
		String badge = model.getBadge() != null ? model.getBadge().toString() : null;
		figure.setText(badge);
		
		figure.getBorder().setBackgroundImage(Converter.getImageFullPath(
				model.getDiagram(), model.getImage()));
		
		if (figure.isDirty()){
			figure.repaint();
		}
		
		//figure.setBounds(getBounds());
	}
	

	@Override
	public void setSelected(int value) {
		if (EditPart.SELECTED_PRIMARY == value){
			((DashboardViewEditPart)getParent()).changePageTo((DashboardItem)getModel());
		}
		super.setSelected(value);
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
