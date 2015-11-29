// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.gef.commands.Command;

import com.forgedui.model.Container;
import com.forgedui.model.Element;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddElementToStackBetweenElements extends Command {
	
	private Element parent, child;
	private boolean after;
	
	public AddElementToStackBetweenElements(Element parent, Element child, boolean after){
		this.parent = parent;
		this.child = child;
		this.after = after;
	}
	
	@Override
	public boolean canExecute() {
		return (parent.getParent() instanceof Container)
			&& ((Container)parent.getParent()).getChildren().contains(parent);
	}
	
	@Override
	public void execute() {
		int index = ((Container)parent.getParent()).getChildren().indexOf(parent);
		if (after) index++;
		((Container)parent.getParent()).addChild(child, index);
	}
	
	@Override
	public void undo() {
		((Container)parent.getParent()).removeChild(child);
	}

}
