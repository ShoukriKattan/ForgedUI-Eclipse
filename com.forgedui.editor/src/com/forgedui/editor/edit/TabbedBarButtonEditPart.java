// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Color;

import com.forgedui.editor.edit.policy.ElementDirectEditPolicy;
import com.forgedui.editor.figures.TabbedBarButtonFigure;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.model.titanium.Toolbar;
import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TabbedBarButtonEditPart extends ElementEditPart {
		
	@Override
	protected void refreshVisuals() {
		TabbedBarButtonFigure figure = (TabbedBarButtonFigure)getFigure();
		TabbedBarButton model = getTabbedBarButton();
		
		figure.getBorder().setDrawLeftSide(model.isFirst());
		figure.getBorder().setDrawRightSide(model.isLast());
		figure.getBorder().setBorderRadius(10f);
		
		if (Utils.getBoolean(model.getSelected(), false)){
			figure.setFgColor(new Color(null,255,255,255));
			if(model.getParent().getParent() instanceof Toolbar)
				figure.getBorder().setBackgroundColor("0x4C6D9A");//202060
			else
				figure.getBorder().setBackgroundColor("0x10398C");
		} else {
			if(model.getParent().getParent() instanceof Toolbar){
				figure.setFgColor(new Color(null,255,255,255));
				figure.getBorder().setBackgroundColor("0x6A7E9B");//0x2020A0
			}else
				figure.getBorder().setBackgroundColor(null);
		}
		
		figure.getBorder().setBackgroundImage(Converter.getImageFullPath(model.getDiagram(), model.getImage()));
//		figure.setImage(Converter.getImageFullPath(model.getDiagram(), model.getImage()));
		
		figure.setText(model.getTitle());

		super.refreshVisuals();
		
		// Only refresh this if the properties are dirty and needs to be repaint.
		if (figure.isDirty()){
			figure.repaint();
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (TabbedBarButton.TITLE_PROP.equals(evt.getPropertyName())
				|| TabbedBarButton.SELECTED_PROP.equals(evt.getPropertyName())
				|| TabbedBarButton.IMAGE_PROP.equals(evt.getPropertyName()))	{
			refreshVisuals();//Calculate name property
		} else if (TabbedBarButton.WIDTH_PROP.equals(evt.getPropertyName())){
			getParent().refresh();
		} else {
			super.propertyChange(evt);
		}
	}
	
	@Override
	protected void createEditPolicies() {
		// Just adding the direct edit policy here.
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ElementDirectEditPolicy());
	}
	
	protected boolean directEditingEnabled() { 
		return true;
	}
	
	public TabbedBarButton getTabbedBarButton() {
		return (TabbedBarButton)getModel();
	}

}
