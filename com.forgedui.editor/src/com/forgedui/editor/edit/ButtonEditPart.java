// LICENSE
package com.forgedui.editor.edit;

import org.eclipse.swt.graphics.RGB;

import com.forgedui.editor.figures.ButtonFigure;
import com.forgedui.editor.util.ColorUtils;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ButtonEditPart extends TitaniumElementEditPart<Button> {
	
	@Override
	protected void refreshVisuals() {
		ButtonFigure figure = (ButtonFigure)getFigure();
		Button model = getModel();
	
		figure.setFontColor(getFontColor());
		figure.setText(getTitle());
		figure.setImage(Converter.getImageFullPath(model.getDiagram(), model.getImage()));
				
		super.refreshVisuals();
	}
	
	public String getFontColor(){
		if (getModel().getColor() == null && getModel().getParent() instanceof TitleBar){
			return Converter.getHexColorValue(getDefaults().getForegroundColor(
					getModel().getParent()));
		}
		return getModel().getColor();
	}
	
	@Override
	protected String getBackgroundColor() {
		if (getModel().getBackgroundColor() == null && getModel().getParent() instanceof TitleBar){
			RGB darker = ColorUtils.darker(getDefaults().getBackgroundColor(
					getModel().getParent()).getRGB());
			return Converter.getHexColorValue(darker);
		} else {
			return Utils.getBoolean(getModel().getEnabled(), true) ? getModel().getBackgroundColor()
						: Utils.getString(
								getModel().getBackgroundDisabledColor(),
								getModel().getBackgroundColor());
		}
	}
	
	@Override
	protected String getBackgroundImage() {
		return Utils.getBoolean(getModel().getEnabled(), true) ? getModel().getBackgroundImage()
				: Utils.getString(
						getModel().getBackgroundDisabledImage(),
						getModel().getBackgroundImage());
	}

	protected boolean directEditingEnabled() { 
		return true;
	}

	private String getTitle() {
		return ResourceHelper.getString(((Button) getModel()).getTitle(), ((Button) getModel()).getTitleid());
	}

}
