// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddTitaniumElementCommand extends Command {

	private TitaniumUIBoundedElement child;
	private Container parent;
	private Rectangle bounds;
	
	public AddTitaniumElementCommand(Container parent, TitaniumUIBoundedElement child, Rectangle bounds) {
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
		return child != null && parent != null;
	}

	public void execute() {
		if (bounds != null){
			child.setLeft(bounds.getLocation().x);
			child.setTop(bounds.getLocation().y);
			Dimension size = bounds.getSize();
			if (size.width > 0 && size.height > 0){
				child.setWidth(size.width);
				child.setHeight(size.height);
			}
		} else {
			child.setLeft(null);
			child.setRight(null);
			child.setTop(null);
			child.setBottom(null);
			child.setWidth(null);
			child.setHeight(null);
		}
		parent.addChild(child);
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
