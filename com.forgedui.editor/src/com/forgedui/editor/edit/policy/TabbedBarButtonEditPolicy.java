package com.forgedui.editor.edit.policy;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.forgedui.editor.edit.ButtonBarEditPart;
import com.forgedui.editor.edit.ElementEditPart;
import com.forgedui.editor.edit.TabbedBarButtonEditPart;
import com.forgedui.editor.edit.TabbedBarEditPart;
import com.forgedui.editor.edit.command.UnconditionalMoveResizeEditPartCommand;
import com.forgedui.model.Element;

/**
 * This edit policy will try to move the items of the tabbed bar edit part.
 * 
 * @author Tareq Doufish
 *
 */
public class TabbedBarButtonEditPolicy extends ConstrainedLayoutEditPolicy {
	
	private static final Dimension DEFAULT_SIZE = new Dimension(-1, -1);
	
	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
			EditPart child, Object constraint) {
		return createMoveCommand((GraphicalEditPart) child, request);
	}
	
	/**
	 * This is a very special case for the move of the tabbed 
	 * button element.
	 * @param childPart
	 * @param generic
	 * @return
	 */
	public static Command createMoveCommand(GraphicalEditPart childPart,Request generic) {
		if(generic instanceof ChangeBoundsRequest) { 
			ChangeBoundsRequest request = (ChangeBoundsRequest) generic;
			TabbedBarButtonEditPart childEditPart = ((TabbedBarButtonEditPart)childPart);
			ElementEditPart parentEditPartObject = (ElementEditPart) childEditPart.getParent();
			Rectangle parentBounds = null;
			Element parentElement = null;
			if (parentEditPartObject instanceof TabbedBarEditPart) { 
				TabbedBarEditPart parentEditPart = ((TabbedBarEditPart)parentEditPartObject);
				parentElement = (Element) parentEditPart.getModel();
				parentBounds = parentEditPart.getBounds().getCopy();
			} else if (parentEditPartObject instanceof ButtonBarEditPart) { 
				ButtonBarEditPart parentEditPart = ((ButtonBarEditPart)parentEditPartObject);
				parentElement = (Element) parentEditPart.getModel();
				parentBounds = parentEditPart.getBounds().getCopy();
			}
			if (parentElement != null && parentBounds != null) {
				Point delta = request.getMoveDelta().getCopy();
				delta = parentElement.getSupport().viewToModel(delta);
				Point newLocation = parentEditPartObject.getBounds().getLocation().getTranslated(delta);
				Rectangle newBounds = new Rectangle(newLocation, parentEditPartObject.getBounds().getSize());
				UnconditionalMoveResizeEditPartCommand comm = new UnconditionalMoveResizeEditPartCommand(
						parentEditPartObject, newBounds);
				return comm;
			}
		}
		return null;
	}
	
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new TabbedBarFeedbackEditPolicy();
	}
	
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		return null;
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	@Override
	protected Object getConstraintFor(Point p) {
		return new Rectangle(p, DEFAULT_SIZE);
	}

	@Override
	protected Object getConstraintFor(Rectangle r) {
		return new Rectangle(r);
	}
	
	@Override
	protected Object getConstraintFor(CreateRequest request) {
		Rectangle r = (Rectangle) super.getConstraintFor(request);
		if (getHost().getModel() instanceof Element) {
			Element e = (Element) getHost().getModel();
			return e.getSupport().viewToModel(r);
		}
		return r;
	}
	
	@Override
	protected Object getConstraintFor(ChangeBoundsRequest request,
			GraphicalEditPart child) {
		Rectangle r = (Rectangle) super.getConstraintFor(request, child);
		if (getHost().getModel() instanceof Element) {
			Element e = (Element) getHost().getModel();
			return e.getSupport().viewToModel(r);
		}
		return r;
	}
	
	@Override
	protected Object getConstraintForClone(GraphicalEditPart part,
			ChangeBoundsRequest request) {
		Rectangle r = (Rectangle) super.getConstraintForClone(part, request);
		if (getHost().getModel() instanceof Element) {
			Element e = (Element) getHost().getModel();
			return e.getSupport().viewToModel(r);
		}
		return r;
	}

}
