package com.forgedui.editor.edit.policy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.graphics.Color;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.figures.FeedbackRectangle;
import com.forgedui.editor.figures.MobileScreenFigure;
import com.forgedui.editor.figures.ScreenFigure;
import com.forgedui.model.Element;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ContainerHighlightEditPolicy extends
		org.eclipse.gef.editpolicies.GraphicalEditPolicy {

	public final static Color backgroundGray = new Color(null, 200, 200, 200);
	
	// the allowed color when dropping item..
	public static final Color allowedColor = ColorConstants.green;
	
	// The not allowed color when dropping.
	public static final Color notAllowedColor = ColorConstants.red;

	private FeedbackRectangle sizeOnDropFeedback;

	public void eraseTargetFeedback(Request request) {
		if (sizeOnDropFeedback != null){
			removeFeedback(sizeOnDropFeedback);
			sizeOnDropFeedback = null;
		}
	}

	private IFigure getContainerFigure() {
		return ((GraphicalEditPart) getHost()).getFigure();
	}

	public EditPart getTargetEditPart(Request request) {
		return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ? getHost()
				: null;
	}

	protected void showHighlight(List<Element> children) {
		if (sizeOnDropFeedback == null) {
			sizeOnDropFeedback = new FeedbackRectangle();
//			FigureUtilities.makeGhostShape((Shape) sizeOnDropFeedback);
//			((Shape) sizeOnDropFeedback).setLineStyle(Graphics.LINE_DASHDOT);
//			((Shape) sizeOnDropFeedback).setLineWidth(2);
			sizeOnDropFeedback.setForegroundColor(ColorConstants.white);
			Rectangle r = new Rectangle(getContainerFigure().getBounds().getCopy().expand(new Insets(1,1,1,1)));
			//getContainerFigure().translateToAbsolute(t) doesn't work when scrolling
			translateToAbsolute(getContainerFigure(), r);
			sizeOnDropFeedback.setBounds(r);
		}
		
		boolean allowed = false;
		if (children .size() > 0) {
			allowed = true;
			EditPart parentEditPart = getHost();
			Object containerModel = parentEditPart.getModel();
			
			for (int i = 0; i < children.size(); i++) {
				Element toDropModel = children.get(i);
				if (!GUIEditorPlugin.getComponentValidator().validate(toDropModel, containerModel)) { 
					allowed = false;
					break;
				}
			}
		}
		
		changeFeedbackFigureColor(allowed);
		addFeedback(sizeOnDropFeedback);
	}
	
	/**
	 * This will change the feedback border color.
	 */
	public void changeFeedbackFigureColor(boolean allowed) { 
		if (sizeOnDropFeedback != null && allowed) { 
			sizeOnDropFeedback.setAllowed(true);
		} else if (sizeOnDropFeedback != null) { 
			sizeOnDropFeedback.setAllowed(false);
		}
	}
	
	public final void translateToAbsolute(IFigure child, Translatable t) {
		if (child != null) {
			child.getParent().translateToParent(t);
			if (!(child instanceof ScreenFigure)){
				translateToAbsolute(child.getParent(), t);
			}
		}
	}

	public void showTargetFeedback(Request request) {
		if (request.getType().equals(RequestConstants.REQ_MOVE)
				|| request.getType().equals(RequestConstants.REQ_ADD)
				//|| request.getType().equals(RequestConstants.REQ_CLONE)
				|| request.getType().equals(RequestConstants.REQ_CREATE)) {
			
			List<Element> children = new ArrayList<Element>();
			if (request instanceof ChangeBoundsRequest) { 
				ChangeBoundsRequest req = (ChangeBoundsRequest)request;
				List<?> editParts = req.getEditParts();
				for (Object editPart : editParts) {
					children.add((Element) ((EditPart)editPart).getModel());
				}
			} else if (request instanceof CreateRequest){
				children.add((Element) ((CreateRequest)request).getNewObject());
			} else {
				//what is this???
			}
			
			showHighlight(children);
		}
			
	}

	/**
	 * A fix to the scaling bug.
	 */
	protected IFigure getFeedbackLayer() {
		return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
	}
	
}
