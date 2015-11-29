// LICENSE
package com.forgedui.editor.edit.policy;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.widgets.ToolBar;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.edit.ButtonEditPart;
import com.forgedui.editor.edit.ElementEditPart;
import com.forgedui.editor.edit.PickerColumnEditPart;
import com.forgedui.editor.edit.PickerEditPart;
import com.forgedui.editor.edit.PickerRowEditPart;
import com.forgedui.editor.edit.PopoverEditPart;
import com.forgedui.editor.edit.ScreenEditPart;
import com.forgedui.editor.edit.TabGroupEditPart;
import com.forgedui.editor.edit.TabbedBarButtonEditPart;
import com.forgedui.editor.edit.TableViewSectionEditPart;
import com.forgedui.editor.edit.TitleBarEditPart;
import com.forgedui.editor.edit.WindowEditPart;
import com.forgedui.editor.edit.command.AddCommand;
import com.forgedui.editor.edit.command.MoveResizeEditPartCommand;
import com.forgedui.editor.edit.command.UnconditionalMoveResizeEditPartCommand;
import com.forgedui.editor.figures.PopoverFigure;
import com.forgedui.editor.figures.WindowFigure;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.TabbedBar;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public abstract class AbstractConstrainedLayoutEditPolicy extends ConstrainedLayoutEditPolicy {
	
	private static final Dimension DEFAULT_SIZE = new Dimension(-1, -1);
	
	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
			EditPart child, Object constraint) {
		/*if (child instanceof PickerColumnEditPart
				|| child instanceof PickerRowEditPart){
			return null;
		}*/
		if (child instanceof PickerEditPart){
			/*if (!((Picker)child.getModel()).isExpanded()){
				Rectangle noResize = (Rectangle) constraint;
				noResize.setSize(-1, -1);
				return new MoveResizeEditPartCommand((ElementEditPart) child, noResize);
			} else {
				return null;
			}*/
			if (((Picker)child.getModel()).isExpanded()){
				return null;
			}
		}
		if (child instanceof PickerColumnEditPart){
			if (!((PickerColumn)child.getModel()).canEdit()) return null;
		}
		if (child instanceof PickerRowEditPart){
			if (!((PickerRow)child.getModel()).canEdit()) return null;
		}
		if (child instanceof ElementEditPart && constraint instanceof Rectangle
				&& !(child instanceof ScreenEditPart)
				&& !(child instanceof TabGroupEditPart)
				&& !(child instanceof TitleBarEditPart)
				&& !(child instanceof TableViewSectionEditPart)
				&& !(child instanceof ButtonEditPart && (child.getParent() instanceof TitleBarEditPart))) {
			// return a command that can move and/or resize a Element
			return new MoveResizeEditPartCommand((ElementEditPart) child,
					(Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, child, constraint);
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
	
	protected Command createAddCommand(Request request, EditPart childEditPart,
			Object constraint) {
		
		// The object that will host this item.
		Object hostObject = getHost().getModel();
		Object droppedObject = childEditPart.getModel();
		// We will only allow to move to containers.
		if (!(hostObject instanceof Container))
			return null;
		
		// We will also have to apply the rules of the components at the move of the item
		// here as well
		if (!GUIEditorPlugin.getComponentValidator().validate(droppedObject, hostObject))
			return null;
		
		Element part = (Element) childEditPart.getModel();
		Rectangle rect = (Rectangle) constraint;
		AddCommand addCommand = new AddCommand();
		addCommand.setParent((Container) getHost().getModel());
		addCommand.setChild(part);
		UnconditionalMoveResizeEditPartCommand setConstraint = new UnconditionalMoveResizeEditPartCommand((ElementEditPart) childEditPart,rect);
		return addCommand.chain(setConstraint);
	}

	protected Command getAddCommand(Request generic) {
		ChangeBoundsRequest request = (ChangeBoundsRequest) generic;
		List<?> editParts = request.getEditParts();
		CompoundCommand command = new CompoundCommand();
		GraphicalEditPart childPart;
		Rectangle r;
		Object constraint;

		for (int i = 0; i < editParts.size(); i++) {
			childPart = (GraphicalEditPart) editParts.get(i);
			r = childPart.getFigure().getBounds().getCopy();
			// convert r to absolute from childpart figure
			childPart.getFigure().translateToAbsolute(r);
			r = request.getTransformedRectangle(r);
			// convert this figure to relative
			getLayoutContainer().translateToRelative(r);
			getLayoutContainer().translateFromParent(r);
			r.translate(getLayoutOrigin().getNegated());
			constraint = getConstraintFor(r);
			if (childPart instanceof TabbedBarButtonEditPart) { 
				// PS: This is a work around for now. We have to think of another
				// way, but I can't seem to figure it out.
				Command moveCommand = TabbedBarButtonEditPolicy.createMoveCommand(childPart, generic);
				command.add(moveCommand);
			} else { 
				command.add(createAddCommand(generic, childPart,
						translateToModelConstraint(constraint)));
			}
		}
		if (command.isEmpty()){
			return null;//don't bock other commands with UnexecutableCommand
		}
		return command.unwrap();
	}
	
	/**
	 * Creating the edit policy for the move and the rezise of the 
	 * children
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new ChildElementResizableEditPolicy();
	}
	
	@Override
	protected Object getConstraintFor(CreateRequest request) {
		Rectangle r = (Rectangle) super.getConstraintFor(request);
		if (request.getNewObject() instanceof Element) {
			if(getHost() instanceof PopoverEditPart){
				Rectangle rect = new Rectangle(getHostFigure().getBounds());
				rect.x = 0;
				rect.y = 0;
				rect.crop(PopoverFigure.INSETS);
				return rect;
			}else if (ComponentValidator.isFullScreenView(request.getNewObject())){
				return null;//set all to null
			} else if (/*request.getNewObject() instanceof ProgressBar
					|| request.getNewObject() instanceof Slider
					||*/ request.getNewObject() instanceof SearchBar
					|| request.getNewObject() instanceof ToolBar
					|| request.getNewObject() instanceof ButtonBar
					|| request.getNewObject() instanceof TabbedBar){
				r.x = 0;
			}
			//necessary for titlebar delta
			if (getHost() instanceof WindowEditPart){
				r.y -= ((WindowFigure)((GraphicalEditPart) getHost()).getFigure()).getTitleBarHeight();
			}
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
			if (child.getParent() instanceof WindowEditPart){
				r.y -= ((WindowFigure)((GraphicalEditPart) child.getParent()).getFigure()).getTitleBarHeight();
			}
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
