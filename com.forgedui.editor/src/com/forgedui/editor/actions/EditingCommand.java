package com.forgedui.editor.actions;

import org.eclipse.gef.commands.Command;

import com.forgedui.editor.edit.direct.DirectEditingManager;

/**
 * Doing a command for setting the value of the element text.
 * @author Tareq Doufish
 *
 */
public class EditingCommand extends Command {

	private String newValue, oldValue;
	private Object model;

	public EditingCommand(Object model, String newValue) {
		this.model = model;
		if (newValue != null)
			this.newValue = newValue;
		else
			this.newValue = "";
	}

	public void execute() {
		String oldValue = DirectEditingManager.getOldValue(model);
		this.oldValue = oldValue;
		DirectEditingManager.setElementDirectText(model, newValue);
	}

	public void undo() {
		DirectEditingManager.setElementDirectText(model, oldValue);
	}

}
