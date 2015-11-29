package com.forgedui.editor.edit;

import java.util.ArrayList;
import java.util.List;

import com.forgedui.editor.figures.TextAreaFigure;
import com.forgedui.editor.util.SWTWordWrap;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TextArea;
import com.forgedui.util.Utils;

/**
 * The edit part for the text area stuff.
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TextAreaEditPart extends TitaniumElementEditPart<TextArea> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public List<?> getModelChildren_() {
		List<Element> list = new ArrayList<Element>();
		TextArea model = getModel();
		if (model.getKeyboardToolbar() != null && model.getKeyboardToolbar().length > 0){
			for (int i = 0 ; i < model.getKeyboardToolbar().length ; i++) { 
				list.add(model.getKeyboardToolbar()[i]);
			}
		}
		return list;
	}
	
	@Override
	protected void refreshVisuals() {
		TextArea model = getModel();
		TextAreaFigure figure = (TextAreaFigure) getFigure();
		//((TextAreaFigure)getFigure()).setText(.getValue());
		int elementWidth = getBounds().width;
		elementWidth -= (int)(Utils.getFloat(model.getBorderWidth(), 1)*2);//1 is default for text area
		figure.setLines(SWTWordWrap.wrap(model.getValue(), elementWidth, figure.getFont_())
				.toArray(new String[0]));
		
		super.refreshVisuals();
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
