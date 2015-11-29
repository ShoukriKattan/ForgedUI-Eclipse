package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import com.forgedui.editor.figures.TextFieldFigure;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TextField;
import com.forgedui.util.Utils;

/**
 * Text field edit part.
 * @author Dmitry Grimm
 *
 */
public class TextFieldEditPart extends TitaniumElementEditPart<TextField> {

	private static final long serialVersionUID = 1L;

	@Override
	public List<?> getModelChildren_() {
		List<Element> list = new ArrayList<Element>();
		TextField model = getModel();
		if (model.getLeftButton() != null){
			list.add(model.getLeftButton());
		}
		if (model.getRightButton() != null){
			list.add(model.getRightButton());
		}
		if (model.getKeyboardToolbar() != null && model.getKeyboardToolbar().length > 0){
			for (int i = 0 ; i < model.getKeyboardToolbar().length ; i++) { 
				list.add(model.getKeyboardToolbar()[i]);
			}
		}
		return list;
	}
	
	@Override
	protected void refreshVisuals() {
		((TextFieldFigure)getFigure()).setText(getModel().getHintText());
		
		super.refreshVisuals();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (TextField.PRO_LEFT_BUTTON.equals(e.getPropertyName())
				|| TextField.PRO_RIGHT_BUTTON.equals(e.getPropertyName())
				|| TextField.PRO_TOOLBAR.equals(e.getPropertyName())){
			refreshChildren();
		} else {
			super.propertyChange(e);
		}
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
