// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.forgedui.editor.edit.command.AddToTableSectionElementCommand;
import com.forgedui.editor.edit.policy.AddStackElementEditPolicy;
import com.forgedui.editor.edit.policy.ComponentValidator;
import com.forgedui.editor.edit.policy.ContainerEditPolicy;
import com.forgedui.editor.edit.policy.ContainerHighlightEditPolicy;
import com.forgedui.editor.edit.policy.NullEditPolicy;
import com.forgedui.editor.figures.TableViewSectionFigure;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TableViewSectionEditPart extends TitaniumContainerEditPart<TableViewSection> {
	
	private AddStackElementEditPolicy stackEditPolicy;
	
	private AddStackElementEditPolicy.Validator validator =
		new AddStackElementEditPolicy.Validator() {					
			@Override
			public boolean acceptAdd(EditPart parent, Object child,
					Point p) {
				Dimension d = ((ElementEditPart)parent).getBounds().getSize();
				if (child instanceof TableViewRow || child instanceof TableViewSection){
					TableViewSection section = (TableViewSection) parent.getModel();
					if (Utils.isNotEmpty(section.getHeaderTitle()) || section.getHeaderView() != null){
						return p.y <= -10 || (d.height - p.y) < 30;
					} else {
						return p.y <= 10 || (d.height - p.y) < 10;
					}
				}
				return false;
			}
		};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<?> getModelChildren_() {
		List list = new ArrayList(super.getModelChildren_());
		TableViewSection model = getModel();
		if (model.getHeaderView() != null){
			list.add(model.getHeaderView());
		}
		if (model.getFooterView() != null){
			list.add(model.getFooterView());
		}
		return list;
	}
	
	protected void refreshVisuals() {
		TableViewSection model = getModel();
		TableViewSectionFigure figure = (TableViewSectionFigure) getFigure();
		
		figure.setHeaderTitle(model.getHeaderTitle());
		figure.setFooterTitle(model.getFooterTitle());
		
		figure.setHasHeaderView(model.getHeaderView() != null);
		figure.setHasFooterView(model.getFooterView() != null);
				
		super.refreshVisuals();
	}
	
	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		bounds.x = 0;
		bounds.width = getFigure().getParent().getClientArea().width;
		return bounds;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (Element.PROPERTY_SIZE.equals(prop) || Element.PROPERTY_LOCATION.equals(prop)) {
			if (getParent() instanceof TableViewSectionEditPart) {
				((TableViewSectionEditPart) getParent()).updateHeight();
			}
			getParent().refresh();
		} else if (TableViewSection.PROP_HEADER_VIEW.equals(prop)
				|| TableViewSection.PROP_FOOTER_VIEW.equals(prop)
				|| TableViewSection.PROP_HEADER_TITLE.equals(prop)
				|| TableViewSection.PROP_FOOTER_TITLE.equals(prop)){
			refresh();
			updateHeight();
		} else {
			super.propertyChange(e);
		}
	}

	@Override
	protected void addChild(EditPart child, int index) {
		super.addChild(child, index);
		updateHeight();
	}
	
	@Override
	protected void removeChild(EditPart child) {
		super.removeChild(child);
		updateHeight();
	}
	
	protected void updateHeight() {
		EditPart editPart;
		Map modelToEditPart = new HashMap();
		List children = getChildren();

		for (int i = 0; i < children.size(); i++) {
			editPart = (EditPart) children.get(i);
			modelToEditPart.put(editPart.getModel(), editPart);
		}
		
		int childrenHeight = 0;
		
		for (TitaniumUIBaseElement child : getModel().getChildren()) {//rows and sections only
			ElementEditPart elEP = (ElementEditPart)modelToEditPart.get(child);
			if (elEP != null){
				childrenHeight += elEP.getBounds().height;
			}
		}
		float height = 2*Utils.getFloat(getModel().getBorderWidth(),
				getDefaults().getBorderWidth(getModel()));
		TableViewSectionFigure sectionFigure = (TableViewSectionFigure)getFigure();
		height += sectionFigure.getHeaderHeight() + sectionFigure.getFooterHeight();
		getModel().setHeight(getSupport().viewToModel((int)height + childrenHeight));
	}

	//FIXME the UGLY implementation. Should be changed when we have more time
	@Override
	protected void createEditPolicies() {
		stackEditPolicy = new AddStackElementEditPolicy(validator);
		installEditPolicy(AddStackElementEditPolicy.KEY, stackEditPolicy);
		super.createEditPolicies();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy(){
			@Override
			public void showTargetFeedback(Request request) {
				if (request instanceof CreateRequest) {
					CreateRequest createRequest = (CreateRequest) request;
					if (stackEditPolicy.acceptCreate(createRequest)){
						eraseTargetFeedback(createRequest);
						return;//this will be handled by the stackEditPolicy
					}
				}
				super.showTargetFeedback(request);
			}
		});
		installEditPolicy(ContainerEditPolicy.KEY, new TableViewSectionEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, NullEditPolicy.getInstance());
	}
	
	class TableViewSectionEditPolicy extends ContainerEditPolicy {
		
		@Override
		protected Command createAddCommand(Request request,
				EditPart childEditPart, Object constraint) {
			if (request instanceof ChangeBoundsRequest) {
				ChangeBoundsRequest changeBounds = (ChangeBoundsRequest) request;
				if (stackEditPolicy.acceptReorder(changeBounds)){
					return null;//this will be handled by the stackEditPolicy
				}
			}
			return super.createAddCommand(request, childEditPart, constraint);
		}
		
		@Override
		public EditPart getTargetEditPart(Request request) {
			if (request instanceof CreateRequest) {
				CreateRequest createRequest = (CreateRequest) request;
				if (stackEditPolicy.acceptCreate(createRequest)){
					return null;//this will be handled by the stackEditPolicy
				}
			}
			
			return super.getTargetEditPart(request);
		}
		
		protected Command getCreateCommand(CreateRequest request) {
			if (stackEditPolicy.acceptCreate(request)){
				return null;//this will be handled by the stackEditPolicy
			}
			// And then passed those to the validate facility.
			Object newObject = request.getNewObject();
			Object container = getHost().getModel();
			
			if (!(newObject instanceof TableViewRow) && newObject instanceof TitaniumUIElement){
				Point dropLocation = getDropLocation(request);
				TitaniumUIBoundedElement child = (TitaniumUIBoundedElement) newObject;
				if (container instanceof TableViewSection && ComponentValidator.isView(child)){
					TableViewSectionEditPart ep = (TableViewSectionEditPart)getHost();
					TableViewSection section = ep.getModel();
					AddToTableSectionElementCommand addCommand = null;
					TableViewSectionFigure figure = (TableViewSectionFigure)ep.getFigure();
					if (dropLocation.y <= figure.getSize().height / 2){
						if (section.getHeaderView() == null){
							//FIXME ugly solution for header
							dropLocation = new Point(0, -figure.getTitleHeight());
							return new AddToTableSectionElementCommand(section, child,
									ep.getSupport().viewToModel(dropLocation), true);
						}
					} else if (section.getFooterView() == null){
						//FIXME ugly solution for header
						dropLocation = new Point(0, figure.getSize().height - figure.getTitleHeight()*2);
						return addCommand = new AddToTableSectionElementCommand(section, child,
								ep.getSupport().viewToModel(dropLocation), false);
					}
					return null;//Can't drop to the title!
				}
			}
			
			return super.getCreateCommand(request);
		}
		
		@Override
		protected Object getConstraintFor(CreateRequest request) {
			Rectangle r = (Rectangle) super.getConstraintFor(request);
			r.x = 0;
			return r;
		}
		
		protected Point getDropLocation(CreateRequest request) {
			IFigure figure = getLayoutContainer();

			Point where = request.getLocation().getCopy();
			Dimension size = request.getSize();

			figure.translateToRelative(where);
			figure.translateFromParent(where);
			where.translate(getLayoutOrigin().getNegated());
			return where;
		}
		
	}


}

