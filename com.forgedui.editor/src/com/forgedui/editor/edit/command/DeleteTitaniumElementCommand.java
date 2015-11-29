// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.gef.commands.Command;

import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.model.titanium.TitaniumUIContainer;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DeleteTitaniumElementCommand extends Command {

	private TitaniumUIBaseElement child;
	private TitaniumUIContainer parent;
	private int index = -1;

	public DeleteTitaniumElementCommand(TitaniumUIContainer parent, TitaniumUIBaseElement child) {
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
