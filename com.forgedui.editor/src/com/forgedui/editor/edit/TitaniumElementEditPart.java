// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.swt.graphics.FontData;

import com.forgedui.editor.figures.TitaniumFigure;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.util.Converter;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TitaniumElementEditPart<S extends TitaniumUIElement> extends ElementEditPart {
	
	@SuppressWarnings("unchecked")
	@Override
	public S getModel() {
		return (S) super.getModel();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ElementEditPart> getChildren() {
		return super.getChildren();
	}
	
	@Override
	public TitaniumFigure getFigure() {
		return (TitaniumFigure) super.getFigure();
	}
	
	@Override
	protected void refreshVisuals() {
		TitaniumFigure figure = getFigure();
		S model = getModel();

		if (figure.getBorder() != null){
			figure.getBorder().setBackgroundColor(getBackgroundColor());
			figure.getBorder().setBorderColor(model.getBorderColor());
			figure.getBorder().setBorderWidth(model.getBorderWidth());
			figure.getBorder().setBorderRadius(model.getBorderRadius());
			figure.getBorder().setBackgroundImage(Converter.getImageFullPath(
					model.getDiagram(), getBackgroundImage()));

		}
		
		figure.setFontData(getFontData());
		figure.setOpacity(model.getOpacity());

		
		super.refreshVisuals();
		
		// Only refresh this if the properties are dirty and needs to be repaint.
		if (figure.isDirty()){
			figure.repaint();
		}
	}

	public FontData getFontData() {
		return Converter.getFontDataFromFont(getModel().getFont());
	}
	
	protected String getBackgroundColor(){
		return getModel().getBackgroundColor();
	}
	
	protected String getBackgroundImage(){
		return getModel().getBackgroundImage();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean isTypeVisiual = PropertyType.isVisualProperty(getModel().getClass().getName(), evt.getPropertyName());
		if (isTypeVisiual){
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}
	
}
