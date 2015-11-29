// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.ElementImpl;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddElementCommand extends Command {

	private ElementImpl child;
	private Container parent;
	private Rectangle bounds;
	
	public AddElementCommand(Container parent, ElementImpl child, Rectangle bounds) {
		this.parent = parent;
		this.child = child;
		this.bounds = bounds;
		setLabel("Element creation");
	}
	
	/**
	 * Can execute if all the necessary information has been provided. 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return child != null && parent != null && bounds != null;
	}

	public void execute() {
		child.setLocation(bounds.getLocation());
		Dimension size = bounds.getSize();
		if (size.width > 0 && size.height > 0)
			child.setDimension(size);
		parent.addChild(child);
		child.initDefaults();
	}

	public void redo() {
		//not possible to add the same child 2 times.
		//parent.addChild(child);
	}

	public void undo() {
		parent.removeChild(child);
	}
	

	public Container getParent() {
		return parent;
	}
	
	public Element getChild(){
		return this.child;
	}

}
