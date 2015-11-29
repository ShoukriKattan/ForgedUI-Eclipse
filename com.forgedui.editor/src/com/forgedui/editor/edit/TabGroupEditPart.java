// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import com.forgedui.editor.edit.policy.DeleteElementEditPolicy;
import com.forgedui.model.titanium.TabGroup;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TabGroupEditPart extends TitaniumElementEditPart<TabGroup> {

	private static final long serialVersionUID = 1L;
	
	/*@Override
	protected IFigure createFigure() {
		IFigure figure = super.createFigure();
		Element element = (Element)getModel();
		element.setDimension(ScreenManager.getTabGroupSize(element));
		Point location = element.getPlatform().isAndroid()
			? new Point(0, 0) : new Point(0, getScreenBounds().height
				- element.getDimension().height);
		//at the bottom of screen window
		element.setLocation(location);
		return figure;
	}*/
	
	@Override
	protected List<?> getModelChildren_() {
		return ((TabGroup)getModel()).getTabs();
	}
	
	/* moved to size calculator
	@Override
	protected void refreshChildren() {
		TabGroup tg = (TabGroup) getModel();
		int visibleTabs = Math.min(tg.getTabNumber(), 5);
		int heingtInsets = getSupport().viewToModel(new Point(
				getFigure().getInsets().top + getFigure().getInsets().bottom, 0)).x;
		Dimension d = tg.getDimension().scale(1.0/visibleTabs, 1);
		d.height -= heingtInsets;
		
		for (int i = 0; i < visibleTabs; i++) {
			tg.getTabs().get(i).setDimension(d);
			tg.getTabs().get(i).setLocation(new Point((int)(i*tg.getDimension().width/visibleTabs), 0));
		}
		for (int i = 5; i < tg.getTabNumber(); i++) {
			tg.getTabs().get(i).setDimension(new Dimension(0, d.height));
			tg.getTabs().get(i).setLocation(new Point(tg.getDimension().width, 0));
		}
		super.refreshChildren();
	}*/
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (TabGroup.TAB_PROP.equals(evt.getPropertyName())){
			refreshChildren();
		} else {
			super.propertyChange(evt);
		}
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(DeleteElementEditPolicy.KEY, new DeleteElementEditPolicy());
	}
	
}
