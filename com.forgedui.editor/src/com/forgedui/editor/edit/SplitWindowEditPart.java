// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.forgedui.editor.figures.SplitWindowFigure;
import com.forgedui.editor.figures.TitaniumTextFigure;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.OptionDialog;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.model.titanium.ipad.Popover;
import com.forgedui.model.titanium.ipad.SplitWindow;

/**
 * @author Zrieq
 *
 */
public class SplitWindowEditPart extends ContainerEditPart {
	
	protected static final int TOP_GAP = 45;
	protected static final int BUTTONS_GAP = 10;

	@Override
	protected void refreshVisuals() {
		TitaniumTextFigure figure = getFigure();
		figure.setText("");
		if(((SplitWindow)getModel()).getShowMasterInPortrait() != null){
			((SplitWindowFigure)figure).setShowMasterInPortrait(((SplitWindow)getModel()).getShowMasterInPortrait());
			figure.setDirty(true);
		}
		
		super.refreshVisuals();
		
		if (figure.isDirty()){
			figure.repaint();
		}
	}
	
	public TitaniumTextFigure getFigure(){
		return (TitaniumTextFigure)super.getFigure();
	}
	
	protected int getTopGap(){
		return TOP_GAP;
	}
	

//	@Override
//	public Rectangle getBounds() {
//		Element e = (Element) getModel();
//		int height = getTopGap() + (getDialogButtonSize(e).height + getButtonsGap())
//			* getModelChildren().size();
//		Dimension size = new Dimension(getScreenBounds().width, height);
//		Point location = new Point(0, getScreenBounds().height - size.height);
//		return getSupport().modelToView(new Rectangle(location, size));
//	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (SplitWindow.PROPERTY_SHOW_MASTER_IN_PORTRAIT.equals(evt.getPropertyName()))	{
			refreshVisuals();
		}else
			super.propertyChange(evt);
	}
	
	@Override
	protected void createEditPolicies() {
		// nothing
		//super.createEditPolicies();
	}

}
