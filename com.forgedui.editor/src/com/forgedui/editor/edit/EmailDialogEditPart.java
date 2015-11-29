// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

import com.forgedui.editor.figures.EmailDialogFigure;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.model.titanium.EmailDialog;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class EmailDialogEditPart extends ElementEditPart {

	@Override
	public Rectangle getBounds() {
		return ((GraphicalEditPart)getParent()).getFigure().getClientArea();
	}
	
	@Override
	public EmailDialog getModel() {
		return (EmailDialog) super.getModel();
	}
	
	private String convertToString(String[] arr){
		if (arr != null && arr.length > 0 ){
			StringBuilder sb = new StringBuilder();
			for (String string : arr) {
				sb.append( string + ", ");
			}
			sb.delete(sb.length() - 2, sb.length() - 1);
			return sb.toString();
		}
		return null;
	}
	
	@Override
	protected void refreshVisuals() {
		EmailDialogFigure figure = (EmailDialogFigure)getFigure();
		EmailDialog model = getModel();
	
		figure.setBarColor(model.getBarColor());
		figure.setTo(convertToString(model.getToRecipients()));
		figure.setCc(convertToString(model.getCcRecipients()));
		figure.setBcc(convertToString(model.getBccRecipients()));
		figure.setSubject(model.getSubject());
		figure.setMessage(model.getMessageBody());
		super.refreshVisuals();
		if (figure.isDirty()){
			figure.repaint();
		}
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
