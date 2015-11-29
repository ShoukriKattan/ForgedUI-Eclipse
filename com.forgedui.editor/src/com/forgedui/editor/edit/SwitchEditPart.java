package com.forgedui.editor.edit;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import com.forgedui.editor.figures.SwitchFigure;
import com.forgedui.editor.figures.SwitchFigureAndroid;
import com.forgedui.model.titanium.Switch;
import com.forgedui.model.titanium.Switch.SwitchStyle;
import com.forgedui.util.Utils;

/**
 * Edit part for the switch component.
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SwitchEditPart extends TitaniumElementEditPart<Switch> {
	
	private static String onColor = "0x1a4791";
	private static String checkColor = "0x3a3a3a";
	
	@Override
	protected void refreshVisuals() {
		SwitchFigure figure = (SwitchFigure)getFigure();
		Switch model = getModel();
		boolean value = Utils.getBoolean(model.getValue(), false);
		figure.setValue(value);
		
		String title = value ? "ON" : "OFF";
		if (model.getPlatform().isAndroid()) {
			((SwitchFigureAndroid)figure).setToggle(isToggle());
			
			if (!isToggle()){
				if (Utils.isNotEmpty(model.getTitle())){
					title = model.getTitle();
				} else {
					title = null;
				}
			} else {
				if (!value){
					if ( Utils.isNotEmpty(model.getTitleOff())){
						title = model.getTitleOff();
					}
				} else {//ON
					if (Utils.isNotEmpty(model.getTitleOn())) { 
						title = model.getTitleOn();
					}
				}
			}
		}
		
		figure.setText(title);
		
		super.refreshVisuals();
	}

	public boolean isToggle() {
		SwitchStyle style = Utils.getObject(getModel().getStyle(),
				SwitchStyle.SWITCH_STYLE_TOGGLEBUTTON);
		boolean isToggle = style == SwitchStyle.SWITCH_STYLE_TOGGLEBUTTON;
		return isToggle;
	}
	
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_OPEN) {
			boolean value = Utils.getBoolean(getModel().getValue(), false);
			getModel().setValue(!value);//change to opposite
		} else {
			super.performRequest(request);
		}
	}
	
	@Override
	protected String getBackgroundColor() {
		String bgColor =
			Utils.getBoolean(getModel().getEnabled(), true) ? getModel().getBackgroundColor()
					: Utils.getString(
							getModel().getBackgroundDisabledColor(),
							getModel().getBackgroundColor());
		if (bgColor == null){
			if (getModel().getPlatform().isAndroid()){
				if (!isToggle()){
					return checkColor;
				}
			} else {
				if (Utils.getBoolean(getModel().getValue(), false)){
					return onColor;
				}
			}
		}
		return bgColor;
	}
	
	@Override
	protected String getBackgroundImage() {
		return Utils.getBoolean(getModel().getEnabled(), true) ? getModel().getBackgroundImage()
				: Utils.getString(
						getModel().getBackgroundDisabledImage(),
						getModel().getBackgroundImage());
	}
}
