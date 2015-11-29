// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPolicy;

import com.forgedui.editor.edit.policy.AddElementEditPolicy;
import com.forgedui.editor.edit.policy.TitleBarContainerEditPolicy;
import com.forgedui.editor.edit.policy.TitleBarEditPolicy;
import com.forgedui.editor.figures.TitleBarFigure;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.util.Converter;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitleBarEditPart extends ElementEditPart {

	private static final long serialVersionUID = 1L;
	

	@Override
	public List<?> getModelChildren_() {
		List<Element> list = new ArrayList<Element>();
		TitleBar model = getTitleBar();
		if (model.getLeftNavButton() != null){
			list.add(model.getLeftNavButton());
		}
		if (model.getRightNavButton() != null){
			list.add(model.getRightNavButton());
		}
		if (model.getTitleControl() != null){
			list.add(model.getTitleControl());
		}
		return list;
	}

	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(AddElementEditPolicy.KEY, new TitleBarEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new TitleBarContainerEditPolicy());	
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		final String propName = e.getPropertyName();
		if (propName.equals(TitleBar.PROPERTY_TITLEPROMPT) || propName.equals(TitleBar.PROPERTY_TITLEPROMPT_ID)) {
			getParent().refresh();
		} else {
			// Determine weather this is a visual propertey
			boolean isTypeVisiual = PropertyType.isVisualProperty(getModel().getClass().getName(), propName);
			if (TitleBar.PROPERTY_LEFT_NAV_BUTTON.equals(propName) || 
					TitleBar.PROPERTY_RIGHT_NAV_BUTTON.equals(propName) || 
					TitleBar.PROPERTY_TITLE_CONTROL.equals(propName)) {
				refreshChildren();
			} else if (isTypeVisiual){
				refreshVisuals();
			} else {
				super.propertyChange(e);
			}
		}
	}
	
	@Override
	protected void refreshVisuals() {
		TitleBar model = getTitleBar();
		TitleBarFigure figure = (TitleBarFigure)getFigure();
		
		figure.setText(getTitle());
		figure.setBarImage(Converter.getImageFullPath(model.getDiagram(), model.getBarImage()));
		figure.setPrompt(getTitlePrompt());
		figure.getBorder().setBackgroundColor(model.getBarColor());
		
		figure.setBackButtonTitle(model.getBackButtonTitle());
		figure.setBackButtonImage(Converter.getImageFullPath(
				model.getDiagram(), model.getBackButtonTitleImage()));

		TitaniumUIElement window = (TitaniumUIElement)model.getParent();
		if (window != null){
			figure.setFontData(Converter.getFontDataFromFont(window.getFont()));
			figure.setOpacity(window.getOpacity());
		}

		super.refreshVisuals();
		
		// Only refresh this if the properties are dirty and needs to be repaint.
		if (figure.isDirty()){
			figure.repaint();
		}
	}
	
	public String getTitle() {
		return ResourceHelper.getString(((TitleBar) getModel()).getTitle(), ((TitleBar) getModel()).getTitleid());
	}

	public String getTitlePrompt() {
		return ResourceHelper.getString(((TitleBar) getModel()).getTitlePrompt(),
				((TitleBar) getModel()).getTitlepromptid());
	}

	/*@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		if (getTitleBar().getTitlePrompt() != null){
			bounds.height += ScreenManager.TITLE_BAR_PROMPT_HEIGHT;
		}
		return bounds;
	}*/

	public TitleBar getTitleBar() {
		return (TitleBar)getModel();
	}

	protected boolean directEditingEnabled() { 
		return true;
	}
}

