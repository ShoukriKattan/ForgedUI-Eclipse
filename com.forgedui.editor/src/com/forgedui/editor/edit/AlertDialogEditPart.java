// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Font;

import com.forgedui.editor.edit.policy.ElementDirectEditPolicy;
import com.forgedui.editor.figures.AlertDialogFigure;
import com.forgedui.editor.figures.TitaniumTextFigure;
import com.forgedui.editor.figures.TitaniumTextFigure.Alignments;
import com.forgedui.editor.util.PropertyType;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.editor.util.SWTWordWrap;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.AlertDialog;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AlertDialogEditPart extends ContainerEditPart {
	
	private static final int BUTTONS_GAP = 10;
	
	private static final float WIDTH_PERCENT = 0.94f;
	
	@Override
	protected void refreshVisuals() {
		AlertDialogFigure figure = (AlertDialogFigure)getFigure();
		AlertDialog model = (AlertDialog)getModel();
		
		if (model.getPlatform().isAndroid()){
			figure.setTextHorisontalAlign(Alignments.left);
		}
		
		figure.setText(model.getTitle());
		figure.setLines(getExplanations(getMessage()));
		figure.setOkText(getOkText());
		
		super.refreshVisuals();
		
		if (figure.isDirty()){
			figure.repaint();
		}
	}

	@Override
	public AlertDialog getModel() {
		return (AlertDialog) super.getModel();
	}
	
	protected String getTitle(){
		return ResourceHelper.getString(
				getModel().getTitle(),
				getModel().getTitleid());
	}
	
	protected String getOkText(){
		if (getModel().getChildren().size() == 0){
			String ok = ResourceHelper.getString(getModel().getOk(),
					getModel().getOkid());
			//"Ok" button on IPhone by default
			return ok != null ? ok :
				getModel().getPlatform().isIPhone() ? "Ok" : null;
		}
		return null;
	}
	
	public TitaniumTextFigure getFigure(){
		return (TitaniumTextFigure)super.getFigure();
	}

	private int getGap(){
		return getFigure().getMargin();
	}
	
	private int getRowHeight(){
		return  FigureUtilities.getStringExtents("T", getFigure().getFont_()).height;
	}
	
	private int getDialogWidth(){
		return (int)(getScreenBounds().width*WIDTH_PERCENT);
	}

	protected int getTopInset(){
		int height = getGap() + getRowHeight() + 5;//for title
		String[] explanations = getExplanations(getMessage());
		height += (getGap() + getRowHeight())*explanations.length
			+ BUTTONS_GAP/2;//add 5 pixels between buttons and text
		return height;
	}

	private Font getFont_(){
		return ((TitaniumTextFigure)getFigure()).getFont_();
	}
	
	/**
	 * This will return the wrapped text.
	 * @param explanation
	 * @return
	 */
	protected String[] getExplanations(String explanation){
		if (explanation == null || explanation.trim().length() == 0){
			return new String[0];
		}
		return SWTWordWrap.wrap(
				explanation, getDialogWidth() - 2*
				(getFigure().getMargin() + (int)getFigure().getBorder().getBorderWidth()),
				getFigure().getFont_()).toArray(new String[0]);
	}
	
	protected int getMaxButtonOnOneLine(){
		return getModel().getPlatform().isAndroid() ? 3 : 2;
	}

	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		List<?> children = getChildren();

		Element e = (Element) getModel();
		Dimension buttonSize = getAlertDialogButtonSize(e);
		if (isVerticalLayout()){
			for (int i = 0; i < children.size(); i++) {
				Point location = new Point(
						(getDialogWidth() - buttonSize.width)/2,
						getTopInset() + i *( buttonSize.height + BUTTONS_GAP));
				OptionDialogButtonEditPart editPart = (OptionDialogButtonEditPart) children.get(i);
				editPart.getFigure().setBounds(getSupport().modelToView(
						new Rectangle (location, buttonSize)));
				editPart.refresh();
			}
		} else {
			int gap = getGap();
			// Temp workaround by Shoukri K.        Added if else..Refresh children should not work at all if there is no children.
			Dimension size = new Dimension((getDialogWidth() -(children.size() + 1)*gap) / (children.size()<=0?1: children.size()),buttonSize.height);
			Point location = new Point(gap,	getTopInset());
			for (int i = 0; i < children.size(); i++) {
				OptionDialogButtonEditPart editPart = (OptionDialogButtonEditPart) children.get(i);
				editPart.getFigure().setBounds(getSupport().modelToView(new Rectangle (location, size)));
				editPart.refresh();
				
				location = location.getCopy().getTranslated(gap + size.width, 0);
			}
		}
	}

	public boolean isVerticalLayout() {
		boolean verticalLayout = getChildren().size() != 2;
		if (verticalLayout && getModel().getPlatform().isAndroid()){
			verticalLayout = getChildren().size() > 3;
		}
		return verticalLayout;
	}
	
	@Override
	public Rectangle getBounds() {
		Element e = (Element) getModel();
		int buttonRows = getOkText() != null ? 1 : 0;
		if (getModelChildren().size() > 0){
			buttonRows = getModel().getPlatform().isAndroid()
					? (getModelChildren().size() < 4 ? 1 : getModelChildren().size())
					: (getModelChildren().size() < 3 ? 1 : getModelChildren().size());
		}
		int height = getTopInset() + (getAlertDialogButtonSize(e).height + BUTTONS_GAP)
			* buttonRows;
		Dimension size = new Dimension(getDialogWidth(), height);
		Point location = new Point(getScreenBounds().width*(1-WIDTH_PERCENT)/2,
				(getScreenBounds().height - size.height) / 2);
		//location.translate(getScreenBounds().getLocation());
		return getSupport().modelToView(new Rectangle(location, size));
	}
	
	public static Dimension getAlertDialogButtonSize(Element e){
		return new Dimension((int)(e.getResolution().width*0.6), 45);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean isTypeVisiual = PropertyType.isVisualProperty(getModel().getClass().getName(), evt.getPropertyName());
		if (Container.PROPERTY_CHILDREN.equals(evt.getPropertyName())
				|| AlertDialog.CANCEL_PROP.equals(evt.getPropertyName())
				|| evt.getPropertyName().equals(AlertDialog.MESSAGE_PROP)){
			refresh();
		} else if(isTypeVisiual) {
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}
	
	@Override
	protected void createEditPolicies() {
		// Only install the edit policy for the direct editing.
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new ElementDirectEditPolicy());
	}

	
	protected boolean directEditingEnabled() { 
		return true;
	}

	public String getMessage() {
		return ResourceHelper.getString(((AlertDialog) getModel()).getMessage(),
				((AlertDialog) getModel()).getMessageId());
	}
}
