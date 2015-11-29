// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;

import com.forgedui.editor.edit.policy.AddStackElementEditPolicy;
import com.forgedui.editor.figures.TableViewRowFigure;
import com.forgedui.editor.images.TitaniumImages;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.util.Converter;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TableViewRowEditPart extends TitaniumContainerEditPart<TableViewRow> {
	
	private static final int INDENT = 15;

	@Override
	protected void refreshVisuals() {
		TableViewRow model = getModel();
		TableViewRowFigure figure = (TableViewRowFigure) getFigure();
		
		//from appcelerator: the title cell value. do not specify if using views as children of the row
		figure.setText(getModelChildren().size() == 0 ? model.getTitle() : null);		

		//int indentation = (model.getIndentionLevel() == null ? 0 : INDENT*model.getIndentionLevel()) ;
		//figure.getFigureProperties().setPropertyValue(SVGConstants.ATTR_X, "" + indentation, SVGFigureProperty.TYPE_ATTRIBUTE,figure.getManagedElement());

		switch (getHasOrdered()) {
		case 1:
			figure.setHasImage(TitaniumImages.getImage(model.getPlatform(), TitaniumImages.HAS_CHILD));
			break;
		case 2:
			figure.setHasImage(TitaniumImages.getImage(model.getPlatform(), TitaniumImages.HAS_DETAIL));
			break;
		case 3:
			figure.setHasImage(TitaniumImages.getImage(model.getPlatform(), TitaniumImages.HAS_CHECK));
			break;
		default:
			figure.setHasImage(null);
		}
		
		figure.setLeftImage(Converter.getImageFullPath(model.getDiagram(), model.getLeftImage()));
		figure.setRightImage(Converter.getImageFullPath(model.getDiagram(), model.getRightImage()));
		figure.setFontColor(model.getColor());
		
		super.refreshVisuals();
	}
	
	/*
	 * IPhone: hasChild, hasDetail then hasCheck.
	 * Android: hasChild then hasCheck. (note hasDetail is IPhone/IPad only) 
	 */
	protected int getHasOrdered(){
		return Utils.getBoolean(getModel().getHasChild(), false) ? 1:
				Utils.getBoolean(getModel().getHasDetail(), false) ? 2:
				 Utils.getBoolean(getModel().getHasCheck(), false) ? 3 : -1;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (Element.PROPERTY_SIZE.equals(prop) || Element.PROPERTY_LOCATION.equals(prop)) {
			if (getParent() instanceof TableViewSectionEditPart) {
				((TableViewSectionEditPart) getParent()).updateHeight();
			}
			refresh();
			getParent().refresh();
		}
		super.propertyChange(e);
	}

	protected boolean directEditingEnabled() { 
		return true;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(AddStackElementEditPolicy.KEY, new AddStackElementEditPolicy(
				new AddStackElementEditPolicy.Validator() {					
					@Override
					public boolean acceptAdd(EditPart parent, Object child,
							Point p) {
						if (child instanceof TableViewSection){
							if (parent.getModel() instanceof TableViewRow){
								return !(parent.getParent().getModel() instanceof TableViewSection);
							}
						}
						return child instanceof TableViewRow || child instanceof TableViewSection;
					}
				}));
		super.createEditPolicies();
	}
	
	@Override
	public String toString() {
		return getModel().getTitle();
	}
}
