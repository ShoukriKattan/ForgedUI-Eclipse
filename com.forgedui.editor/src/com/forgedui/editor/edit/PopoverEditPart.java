// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.forgedui.editor.figures.TitaniumTextFigure;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.OptionDialog;
import com.forgedui.model.titanium.ipad.Popover;

/**
 * @author Zrieq
 *
 */
public class PopoverEditPart extends TitaniumContainerEditPart<Popover> {
	
	@Override
	protected void refreshVisuals() {
		TitaniumTextFigure figure = getFigure();
		
		super.refreshVisuals();
		
		if (figure.isDirty()){
			figure.repaint();
		}
	}
	
	public TitaniumTextFigure getFigure(){
		return (TitaniumTextFigure)super.getFigure();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}
	
	@Override
	protected void createEditPolicies() {
		// nothing
		super.createEditPolicies();
	}
	
	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		if(getParent()!=null){
			Dimension windowSize = getScreenBounds();
			bounds.x = (windowSize.width - bounds.width - bounds.x) / 2;
			bounds.y = (windowSize.height - bounds.height - bounds.y) / 2;
		}
		return bounds;
	}

}
