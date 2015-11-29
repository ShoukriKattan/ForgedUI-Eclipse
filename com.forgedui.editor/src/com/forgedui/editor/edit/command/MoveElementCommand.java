package com.forgedui.editor.edit.command;

import java.util.ArrayList;

import org.eclipse.gef.commands.Command;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.Dialog;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.ipad.Popover;

/**
 * 
 * @author Zrieq
 *
 */
public class MoveElementCommand extends Command {
	
	public static final int MOVE_STEP_1 = 1;
	public static final int MOVE_STEP_10 = 10;
	
	private MovementDirection moveDirection;
	private int moveStep;
	private ArrayList<TitaniumUIBoundedElement> list = new ArrayList<TitaniumUIBoundedElement>();
	
	public MoveElementCommand (MovementDirection moveDirection, int moveStep){
		this.moveDirection = moveDirection;
		this.moveStep = moveStep;
	}
	
	public MoveElementCommand (ArrayList<TitaniumUIBoundedElement> list,MovementDirection moveDirection, int moveStep){
		this.moveDirection = moveDirection;
		this.moveStep = moveStep;
		this.list = list;
	}
	
	@Override
	public boolean canExecute() {
		for (TitaniumUIBoundedElement element : list)
			if(!isMoveableElement(element))
				return false;
		return true;
	}

	private boolean isMoveableElement(TitaniumUIBoundedElement moveableElement) {
		if ((moveableElement instanceof Picker && ((Picker)moveableElement).isExpanded()) 
				|| moveableElement instanceof PickerColumn
				|| moveableElement instanceof PickerRow
				|| moveableElement instanceof TableViewRow
				|| moveableElement instanceof TableViewSection
				|| isOnDialog(moveableElement))
				return false;
			return true;
	}
	
	private boolean isOnDialog(Element element){
		if(element == null)
			return false;
		else if(!(element instanceof Popover) && element instanceof Dialog)
			return true;
		else
			return isOnDialog(element.getParent()); 
	}
	
	@Override
	public void execute() {
		if (!canExecute())
			return;
		redo();
	}

	@Override
	public void redo() {
		for (TitaniumUIBoundedElement element : list){
			if(moveDirection == MovementDirection.RIGHT)
				element.setLeft(element.getLeft()== null?0:Integer.parseInt(element.getLeft()) + moveStep);
			else if(moveDirection == MovementDirection.DOWN)
				element.setTop(element.getTop()== null?0:Integer.parseInt(element.getTop()) + moveStep);
			else if(moveDirection == MovementDirection.LEFT)
				element.setLeft(element.getLeft()== null?0:Integer.parseInt(element.getLeft()) - moveStep);
			else 
				element.setTop(element.getTop()== null?0:Integer.parseInt(element.getTop()) - moveStep);
		}
	}

	@Override
	public void undo() {
		for (TitaniumUIBoundedElement element : list){
			if(moveDirection == MovementDirection.RIGHT)
				element.setLeft(element.getLeft()== null?0:Integer.parseInt(element.getLeft()) - moveStep);
			else if(moveDirection == MovementDirection.DOWN)
				element.setTop(element.getTop()== null?0:Integer.parseInt(element.getTop()) - moveStep);
			else if(moveDirection == MovementDirection.LEFT)
				element.setLeft(element.getLeft()== null?0:Integer.parseInt(element.getLeft()) + moveStep);
			else 
				element.setTop(element.getTop()== null?0:Integer.parseInt(element.getTop()) + moveStep);
		}
	}
	
	public boolean addElement(TitaniumUIBoundedElement node) {
		if (!list.contains(node)) {
			return list.add(node);
		}
		return false;
	}
	
	public static enum MovementDirection{
		UP("_UP_"),DOWN("_DOWN_"),LEFT("_LEFT_"),RIGHT("_RIGHT_");
		
		private String value;
		
		private MovementDirection(String value){
			this.value = value;
		}
		
		public String toString() {
			return value;
		};
	}
}