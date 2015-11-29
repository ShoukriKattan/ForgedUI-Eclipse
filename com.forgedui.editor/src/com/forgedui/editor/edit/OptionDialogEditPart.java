// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.forgedui.editor.figures.TitaniumTextFigure;
import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.OptionDialog;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class OptionDialogEditPart extends ContainerEditPart {
	
	protected static final int TOP_GAP = 45;
	protected static final int BUTTONS_GAP = 10;

	@Override
	protected void refreshVisuals() {
		TitaniumTextFigure figure = getFigure();
		figure.setText(getTitle());
		
		super.refreshVisuals();
		
		if (figure.isDirty()){
			figure.repaint();
		}
	}
	
	protected String getTitle(){
		return ResourceHelper.getString(
				((OptionDialog)getModel()).getTitle(),
				((OptionDialog)getModel()).getTitleid());
	}
	
	public TitaniumTextFigure getFigure(){
		return (TitaniumTextFigure)super.getFigure();
	}
	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		List<?> children = getChildren();

		Element e = (Element) getModel();
		Dimension buttonSize = getDialogButtonSize(e);
		for (int i = 0; i < children.size(); i++) {
			Point location = new Point(
					(getScreenBounds().width - buttonSize.width)/2,
					getTopGap() + i *( buttonSize.height + getButtonsGap()));
			//Point viewLocation = ((Element)getModel()).getSupport().modelToView(location);
			OptionDialogButtonEditPart editPart = (OptionDialogButtonEditPart) children.get(i);
			editPart.getFigure().setBounds(getSupport().modelToView(new Rectangle (location,buttonSize)));
			editPart.refresh();
		}
	}
	
	protected int getTopGap(){
		return TOP_GAP;
	}
	
	protected int getButtonsGap(){
		return getModel().getPlatform().isAndroid() ? 1 : BUTTONS_GAP;
	}

	@Override
	public Rectangle getBounds() {
		Element e = (Element) getModel();
		int height = getTopGap() + (getDialogButtonSize(e).height + getButtonsGap())
			* getModelChildren().size();
		Dimension size = new Dimension(getScreenBounds().width, height);
		Point location = new Point(0, getScreenBounds().height - size.height);
		//location.translate(getScreenBounds().getLocation());
		/*TabGroup tg = ((Element)getModel()).getDiagram().getTabGroupDiagram();
		if (tg != null){
			location.translate(0, -ScreenManager.getTabGroupSize(e).height);
		}*/
		return getSupport().modelToView(new Rectangle(location, size));
	}
	
	protected Dimension getDialogButtonSize(Element e){
		if (getModel().getPlatform().isAndroid()){
			return new Dimension((int)(e.getResolution().width - 4), 48);
		} else {
			return new Dimension((int)(e.getResolution().width*0.85), 48);
		}
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (Container.PROPERTY_CHILDREN.equals(evt.getPropertyName())
				|| OptionDialog.CANCEL_PROP.equals(evt.getPropertyName())
				|| OptionDialog.DESTRUCTIVE_PROP.equals(evt.getPropertyName())
				|| OptionDialog.SELECTED_PROP.equals(evt.getPropertyName())){
			refresh();
		} else if (OptionDialog.TITLE_PROP.equals(evt.getPropertyName())
				|| OptionDialog.TITLE_ID_PROP.equals(evt.getPropertyName())){
			refreshVisuals();//Calculate name property
		} else {
			super.propertyChange(evt);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.forgedui.editor.edit.ContainerEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// nothing
		//super.createEditPolicies();
	}

}
