// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.gef.commands.Command;

import com.forgedui.model.Container;
import com.forgedui.model.Element;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DeleteElementCommand extends Command {

	private Element child;
	private Container parent;
	private int index = -1;

	public DeleteElementCommand(Container parent, Element child) {
		this.parent = parent;
		this.child = child;
	}

	public void execute() {
		index = parent.getChildren().indexOf(child);
		parent.removeChild(child);
	}

	public void redo() {
		execute();
	}

	public void undo() {
		parent.addChild(child, index);
	}

}
