// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.edit.command.AddToTableViewElementCommand;
import com.forgedui.editor.edit.policy.ContainerEditPolicy;
import com.forgedui.editor.figures.TableViewFigure;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TableViewEditPart extends TitaniumContainerEditPart<TableView> {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<?> getModelChildren_() {
		List list = new ArrayList(super.getModelChildren_());
		TableView model = (TableView)getModel();
		if (model.getHeaderView() != null){
			list.add(model.getHeaderView());
		}
		if (model.getFooterView() != null){
			list.add(model.getFooterView());
		}
		if ((model.getSearchHidden() == null
				|| !model.getSearchHidden())
				&& model.getSearch() != null){
			list.add(model.getSearch());
		}
		return list;
	}
	
	/**
	 * Making sure to refresh things visual.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		final String propName = evt.getPropertyName();
		if (TableView.PROP_HEADER_VIEW.equals(propName)
				|| TableView.PROP_FOOTER_VIEW.equals(propName)
				|| TableView.PROP_SEARCH_VIEW.equals(propName)
				|| TableView.PROP_SEARCH_VIEW_HIDDEN.equals(propName)
				|| TableView.PROP_MIN_ROW_HEIGHT.equals(propName)
				|| TableView.PROP_MAX_ROW_HEIGHT.equals(propName)
				) {
			refresh();
		} else {
			super.propertyChange(evt);
		}
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(ContainerEditPolicy.KEY, new TableViewEditPolicy());
	}

	@Override
	protected void refreshVisuals() {
		TableView model = (TableView)getModel();
		TableViewFigure figure = (TableViewFigure)getFigure();

		figure.setHeaderTitle(model.getHeaderTitle());
		figure.setFooterTitle(model.getFooterTitle());
		
		figure.setHasHeaderView(model.getHeaderView() != null);
		figure.setHasFooterView(model.getFooterView() != null);
		
		super.refreshVisuals();
	}
	
}

class TableViewEditPolicy extends ContainerEditPolicy {
	
	protected Command getCreateCommand(CreateRequest request) {
		// And then passed those to the validate facility.
		Object newObject = request.getNewObject();
		Object container = getHost().getModel();
		
		if (!GUIEditorPlugin.getComponentValidator().validate(newObject, container))
			return null;
		
		if (!(newObject instanceof TableViewRow) && 
				!(newObject instanceof TableViewSection) &&
				newObject instanceof TitaniumUIElement){
			Rectangle r = (Rectangle)getConstraintFor(request);
			if (r != null){
				TitaniumUIBoundedElement child = (TitaniumUIBoundedElement) newObject;
				if (container instanceof TableView){
					TableView view = (TableView) getHost().getModel();
					if (child instanceof SearchBar && view.getSearch() == null){
						return new AddToTableViewElementCommand(view, child, r, true);
					} else if (GUIEditorPlugin.getComponentValidator().isView(child)){
						if (r.y <= view.getDimension().height / 2){
							if (view.getHeaderView() == null){
								return new AddToTableViewElementCommand(view, child, r, true);
							}
						} else if (view.getFooterView() == null){
							return new AddToTableViewElementCommand(view, child, r, false);
						}
					}
					return null;//Can't drop
				}
			}			
		}
		
		return super.getCreateCommand(request);
	}
	
	/*@Override
	protected Object getConstraintFor(CreateRequest request) {
		Rectangle r = (Rectangle) super.getConstraintFor(request);
		r.x = 0;
		return r;
	}*/
	
}
