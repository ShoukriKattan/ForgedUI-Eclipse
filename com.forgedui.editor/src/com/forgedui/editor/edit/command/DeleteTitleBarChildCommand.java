// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.gef.commands.Command;

import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.TitleBar;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DeleteTitleBarChildCommand extends Command {

	protected TitaniumUIBoundedElement child;
	protected TitleBar parent;
	private int element = 0;
	
	public DeleteTitleBarChildCommand(TitleBar parent, TitaniumUIBoundedElement child) {
		this.parent = parent;
		this.child = child;
		setLabel("Delete title bar");
	}
	
	/**
	 * Can execute if all the necessary information has been provided. 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return child != null && parent != null
			&& (parent.getLeftNavButton() == child
				|| parent.getRightNavButton() == child
				|| parent.getTitleControl() == child);
	}

	public void execute() {
		if	(parent.getLeftNavButton() == child){
			element = -1;
		} else if (parent.getRightNavButton() == child){
			element = 1;
		} else if(parent.getTitleControl() == child){
			element = 2;
		}else{
			element = 0;
		}
		redo();
	}

	public void redo() {
		if	(element == -1){
			parent.setLeftNavButton(null);
		} else if (element == 1){
			parent.setRightNavButton(null);
		} else{
			parent.setTitleControl(null);
		}
	}

	public void undo() {
		if	(element == -1){
			parent.setLeftNavButton((Button) child);
		} else if (element == 1){
			parent.setRightNavButton((Button) child);
		} else {
			parent.setTitleControl(child);
		} 
	}
	
}
