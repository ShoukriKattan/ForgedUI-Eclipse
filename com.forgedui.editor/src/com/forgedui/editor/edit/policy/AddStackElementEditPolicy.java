// LICENSE
package com.forgedui.editor.edit.policy;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;

import com.forgedui.editor.edit.ElementEditPart;
import com.forgedui.editor.edit.command.AddCommand;
import com.forgedui.editor.edit.command.AddElementToStackBetweenElements;
import com.forgedui.editor.edit.command.CloneCommand;
import com.forgedui.editor.figures.ElementFeedbackFigure;
import com.forgedui.editor.figures.MobileScreenFigure;
import com.forgedui.editor.figures.ScreenFigure;
import com.forgedui.model.Container;
import com.forgedui.model.Element;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddStackElementEditPolicy extends AbstractConstrainedLayoutEditPolicy {
	
	public static final String KEY = "AddStackElementEditPolicy";
	
	private IFigure targetFeedback;
	
	private Validator validator = new Validator() {
		@Override
		public boolean acceptAdd(EditPart parent, Object Child,
				Point point) {
			return parent.getModel().getClass() == Child.getClass();
		}
	};
	
	public AddStackElementEditPolicy(){}
	
	public AddStackElementEditPolicy(Validator validator){
		Assert.isNotNull(validator);
		this.validator = validator;
	}
	
	public Validator getValidator(){
		return validator;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (acceptCreate(request)){
			return new AddElementToStackBetweenElements((Element) getHost().getModel(),
					(Element)request.getNewObject(), 
					isInsertAfter(request));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Command getAddCommand(Request generic) {
		ChangeBoundsRequest request = (ChangeBoundsRequest) generic;
		if (acceptReorder(request)){
			List<EditPart> editParts = request.getEditParts();
			ElementEditPart movedElement = (ElementEditPart)editParts.get(0);
			Container parent = (Container) getHost().getParent().getModel();
			int siblingIndex = parent.getChildren().indexOf(getHost().getModel());
			int currentIndex = parent.getChildren().indexOf(movedElement.getModel());
			if (siblingIndex >= 0){
				if (isInsertAfter((DropRequest) request)) siblingIndex++;
				if (currentIndex <= siblingIndex) siblingIndex--;
				/*MoveChildCommand command = new MoveChildCommand(
						movedElement.getModel(), siblingIndex);*/
				AddCommand command = new AddCommand();
				command.setParent(parent);
				command.setChild(movedElement.getModel());
				command.setIndex(siblingIndex);
				return command;
			}
		}
		return super.getAddCommand(generic);
	}
	
	public boolean acceptCreate(CreateRequest request){
		return (validator.acceptAdd(getHost(), request.getNewObject(), getDropLocation(request)));
	}
	
	@SuppressWarnings("unchecked")
	public boolean acceptReorder(ChangeBoundsRequest request){
		List<EditPart> editParts = request.getEditParts();
		return ((editParts.size() == 1
				&& validator.acceptAdd(
						getHost(), editParts.get(0).getModel(), getDropLocation(request))
				&& (getHost().getParent() == editParts.get(0).getParent()//siblings
						&& getHost().getParent().getModel() instanceof Container)));
	}
	
	private Point getDropLocation(DropRequest request){
		Point where = request.getLocation().getCopy();
		IFigure figure = getLayoutContainer();
		figure.translateToRelative(where);
		figure.translateFromParent(where);
		where.translate(getLayoutOrigin().getNegated());
		return where;
	}

	private boolean isInsertAfter(DropRequest request) {
		ElementEditPart ep = (ElementEditPart) getHost();
		Point where = getDropLocation(request);
		Rectangle parentBounds = ep.getBounds();
		return (parentBounds.height / 2) < where.y;
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
	//	System.out.println(request);
		if (REQ_ADD.equals(request.getType())
				|| REQ_CLONE.equals(request.getType())
				|| REQ_MOVE.equals(request.getType())
				|| REQ_RESIZE_CHILDREN.equals(request.getType())
				|| REQ_CREATE.equals(request.getType()))
			showLayoutTargetFeedback(request);

		if (REQ_CREATE.equals(request.getType())) {
			CreateRequest createReq = (CreateRequest) request;
			showSizeOnDropFeedback(createReq);
		}
	}

	/**
	 * A fix to the scaling bug.
	 */
	protected IFigure getFeedbackLayer() {
		return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
	}

	private IFigure getContainerFigure() {
		return ((GraphicalEditPart) getHost()).getFigure();
	}

	public EditPart getTargetEditPart(Request request) {
		return (request.getType().equals(RequestConstants.REQ_CREATE)
			|| request.getType().equals(RequestConstants.REQ_MOVE)
			|| request.getType().equals(RequestConstants.REQ_ORPHAN))
				? getHost()	: null;
	}

	@Override
	protected void showLayoutTargetFeedback(Request request) {
		eraseLayoutTargetFeedback(request);
		if (request instanceof ChangeBoundsRequest || (request instanceof CreateRequest
				&& acceptCreate((CreateRequest) request))){
			targetFeedback = new RectangleFigure();
			Rectangle parentBounds = ((ElementEditPart)getHost()).getBounds();
			translateToAbsolute(getContainerFigure(), parentBounds);
			Rectangle lineBounds = parentBounds.getCopy();
			lineBounds.height = 4;
			lineBounds.y--;
			targetFeedback.setForegroundColor(ColorConstants.green);
			targetFeedback.setBackgroundColor(ColorConstants.green);
			if (isInsertAfter((DropRequest) request)){
				lineBounds.y += parentBounds.height;
			}
			targetFeedback.setBounds(lineBounds);
			addFeedback(targetFeedback);
		} else if (request instanceof ChangeBoundsRequest){
			
		}
		
	}
	
	@Override
	protected void eraseLayoutTargetFeedback(Request request) {
		if (targetFeedback != null){
			try{
				removeFeedback(targetFeedback);
			} catch (Exception e){
				
			}
			targetFeedback = null;
		}
	}
	
	@Override
	public void eraseTargetFeedback(Request request) {
		super.eraseTargetFeedback(request);
		if (REQ_CREATE.equals(request.getType()))
			eraseLayoutTargetFeedback(request);
	}

	public final void translateToAbsolute(IFigure child, Translatable t) {
		if (child != null) {
			child.getParent().translateToParent(t);
			if (!(child instanceof ScreenFigure)){
				translateToAbsolute(child.getParent(), t);
			}
		}
	}
	
	
	public static interface Validator {
		
		public boolean acceptAdd(EditPart parent, Object child, Point dropPoint);
		
	}

}
