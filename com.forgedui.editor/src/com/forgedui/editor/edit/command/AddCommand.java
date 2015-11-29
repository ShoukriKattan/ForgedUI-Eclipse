package com.forgedui.editor.edit.command;

import com.forgedui.model.Container;
import com.forgedui.model.Element;

/**
 * This command is used when moving the element
 * from one container to another.
 * 
 * @author Tareq Doufish
 *
 */
public class AddCommand extends org.eclipse.gef.commands.Command {

	private Element child;
	private Container parent;
	private int index = -1;

	public AddCommand() {
		super("Add element command");
	}

	public void execute() {
		if (!parent.getChildren().contains(child)) { 
			if (index < 0)
				parent.addChild(child);
			else
				parent.addChild(child, index);
		} 
	}

	public Container getParent() {
		return parent;
	}

	public void redo() {
		if (index < 0)
			parent.addChild(child);
		else
			parent.addChild(child, index);
	}

	public void setChild(Element subpart) {
		child = subpart;
	}

	public void setIndex(int i) {
		index = i;
	}

	public void setParent(Container newParent) {
		parent = newParent;
	}

	public void undo() {
		parent.removeChild(child);
	}

}
