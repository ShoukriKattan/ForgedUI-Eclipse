// LICENSE
package com.forgedui.editor.edit.policy;

import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.forgedui.editor.edit.ElementEditPart;
import com.forgedui.editor.edit.command.AddElementCommand;
import com.forgedui.editor.edit.command.AddTitaniumElementCommand2;
import com.forgedui.editor.edit.command.CloneCommand;
import com.forgedui.editor.figures.ElementFeedbackFigure;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.ElementImpl;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddElementEditPolicy extends AbstractConstrainedLayoutEditPolicy {
	
	public static final String KEY = "AddElementEditPolicy";

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (getHost().getModel() instanceof Container){
			Container container = (Container) getHost().getModel();
			if (request.getNewObject() instanceof TitaniumUIBoundedElement) {
				TitaniumUIBoundedElement element = (TitaniumUIBoundedElement)request.getNewObject();
				return new AddTitaniumElementCommand2((ElementEditPart) getHost(),
					element, 
					(Rectangle)getConstraintFor(request));
			} else if (request.getNewObject() instanceof ElementImpl) {
				ElementImpl element = (ElementImpl)request.getNewObject();
				return new AddElementCommand(container,
					element, 
					(Rectangle)getConstraintFor(request));
			}
		}
		return null;
	}
	
	protected Command getCloneCommand(ChangeBoundsRequest request) {
		if (getHost().getModel() instanceof Container){
			CloneCommand clone = new CloneCommand();
			clone.setParent((Container)getHost().getModel());
			
			Iterator<?> i = request.getEditParts().iterator();
			GraphicalEditPart currPart = null;
			
			while (i.hasNext()) {
				currPart = (GraphicalEditPart)i.next();
				clone.addElement((Element)currPart.getModel(), 
						(Rectangle)getConstraintForClone(currPart, request));
			}
			
			return clone;
		}
		return null;
	}

	protected IFigure getCustomFeedbackFigure(Object obj) {
		if (obj instanceof Element) {
			Element element = (Element) obj;
			element.setParent((Element) getHost().getModel());
			return ElementFeedbackFigure.getElementFeedBack(element);
		}
		return null;
	}
	
	@Override
	protected IFigure createSizeOnDropFeedback(CreateRequest createRequest) {
		IFigure figure = getCustomFeedbackFigure(createRequest.getNewObject());
		
		if (figure != null){
			addFeedback(figure);
		}
		
		return figure;
	}
	
	protected void showSizeOnDropFeedback(CreateRequest request) {
		Point p = new Point(request.getLocation().getCopy());
		IFigure feedback = getSizeOnDropFeedback(request);
		feedback.translateToRelative(p);
		feedback.setBounds(new Rectangle(p, feedback.getSize())
				.expand(getCreationFeedbackOffset(request)));
	}

	public void showTargetFeedback(Request request) {
		if (REQ_ADD.equals(request.getType())
				|| REQ_CLONE.equals(request.getType())
				|| REQ_MOVE.equals(request.getType())
				|| REQ_RESIZE_CHILDREN.equals(request.getType())
				|| REQ_CREATE.equals(request.getType()))
			showLayoutTargetFeedback(request);

		if (REQ_CREATE.equals(request.getType())) {
			CreateRequest createReq = (CreateRequest) request;
			//if (createReq.getSize() != null)
				showSizeOnDropFeedback(createReq);
		}
	}

	/**
	 * A fix to the scaling bug.
	 */
	protected IFigure getFeedbackLayer() {
		return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
	}
}
