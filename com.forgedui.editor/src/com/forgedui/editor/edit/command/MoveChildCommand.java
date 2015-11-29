package com.forgedui.editor.edit.command;

import org.eclipse.gef.commands.Command;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.model.Container;
import com.forgedui.model.Element;

public class MoveChildCommand extends Command {
	private Element child;
	private int index = -1;
	private int oldIndex = 0;

	public MoveChildCommand(Element child, int index) {
		this.child = child;
		this.index = index;
		setLabel("Move element command");
	}
	
	@Override
	public boolean canExecute() {
		return child.getParent() instanceof Container;
	}

	public void execute() {
		Container parent = (Container)child.getParent();
		oldIndex = parent.getChildren().indexOf(child);
		redo();
		
	}

	public void redo() {
		Container parent = (Container)child.getParent();
		parent.removeChild(child);
		parent.addChild(child, index);
	}

	public void undo() {
		Container parent = (Container)child.getParent();
		parent.removeChild(child);
		parent.addChild(child, oldIndex);
	}

}
