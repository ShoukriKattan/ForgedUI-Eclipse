// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Window;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddTitleBarCommand extends Command {

	protected TitleBar child;
	protected Window parent;
	protected Rectangle bounds;
	
	public AddTitleBarCommand(Window parent, TitleBar child, Rectangle bounds) {
		this.parent = parent;
		this.child = child;
		this.bounds = bounds;
		setLabel("Title bar creation");
	}
	
	/**
	 * Can execute if all the necessary information has been provided. 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return child != null && parent != null && bounds != null
			&& parent.getTitleBar() == null;
	}

	public void execute() {
		child.setLocation(bounds.getLocation());
		Dimension size = bounds.getSize();
		//if (size.width > 0 && size.height > 0)
		//	child.setDimension(size);
		redo();
	}

	public void redo() {
		parent.setTitleBar(child);
		//not possible to add the same child 2 times.
	}

	public void undo() {
		parent.setTitleBar(null);
	}
	
}
