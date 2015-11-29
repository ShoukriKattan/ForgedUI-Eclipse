package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.FigureUtilities;

import com.forgedui.editor.figures.LabelFigure;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.editor.util.SWTWordWrap;
import com.forgedui.model.titanium.Label;
import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * The label edit part.
 * 
 * @author Tareq Doufish
 *
 */
public class LabelEditPart extends TitaniumElementEditPart<Label> {
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean isTypeVisiual = PropertyType.isVisualProperty(getModel().getClass().getName(), evt.getPropertyName());
		if (isTypeVisiual){
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}
	
	@Override
	protected void refreshVisuals() {
		LabelFigure figure = (LabelFigure)getFigure();
		Label model = getModel();
		
		figure.setFontData(Converter.getFontDataFromFont(model.getFont()));
		String modelText = getText();
		int width = modelText == null ? 0 : FigureUtilities.getStringExtents(modelText, figure.getFont_()).width;
		
		int elementWidth = getBounds().width;
		elementWidth -= (int)(Utils.getFloat(model.getBorderWidth(), 0)*2);//0 is default for label
		
		if (Utils.getBoolean(model.getWordWrap(), false)){
			figure.setLines(SWTWordWrap.wrap(modelText, elementWidth, figure.getFont_())
					.toArray(new String[0]));
			figure.setText(null);
		} else {
			boolean defaultEllipsize = getModel().getPlatform().isIPhone();
			if (Utils.getBoolean(model.getEllipsize(), defaultEllipsize) && Utils.isNotEmpty(modelText)) {
				if (elementWidth > 0 && elementWidth < width) { 
					StringBuffer textString = new StringBuffer(modelText + "...");
					while (elementWidth < width && textString.length() > 3) {
						textString = textString.delete(textString.length() - 4, textString.length() - 3);
						width = FigureUtilities.getStringExtents(textString.toString(), figure.getFont_()).width;
					}
					figure.setText(textString.toString());
				}
			} else {
				figure.setText(modelText);
			}
			figure.setLines(null);
		}
		
		// For the text align
		figure.setTextAlign(model.getTextAlign());
		figure.setFontColor(model.getColor());
		
		figure.getBorder().setOpaque(getBackgroundColor() == null && getBackgroundImage() == null);
		
		super.refreshVisuals();
	}

	protected boolean directEditingEnabled() { 
		return true;
	}
	
	private String getText() {
		return ResourceHelper.getString(((Label) getModel()).getText(),
				((Label) getModel()).getTextid());
	}

}
