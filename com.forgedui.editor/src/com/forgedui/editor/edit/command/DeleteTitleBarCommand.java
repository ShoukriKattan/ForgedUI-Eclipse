// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.gef.commands.Command;

import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Window;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DeleteTitleBarCommand extends Command {

	protected TitleBar child;
	protected Window parent;
	
	public DeleteTitleBarCommand(Window parent, TitleBar child) {
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
			&& parent.getTitleBar() == child;
	}

	public void execute() {
		parent.setTitleBar(null);
	}

	public void redo() {
		//not possible to add the same child 2 times.
	}

	public void undo() {
		parent.setTitleBar(child);
	}
	
}