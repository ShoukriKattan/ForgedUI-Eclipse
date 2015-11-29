package com.forgedui.editor.edit.command;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.forgedui.model.Container;
import com.forgedui.model.ElementImpl;

/**
 * This command is created once we try to move a component
 * from one container to another. Please see EditorContainerEditPolicy
 * 
 * @author Tareq Doufish
 *
 */
public class OrphanChildCommand extends Command {

	private Rectangle oldBounds;
	private Container container;
	private ElementImpl element;
	private int index;

	public OrphanChildCommand() {
	}

	public void execute() {
		List<?> children = container.getChildren();
		index = children.indexOf(element);
		oldBounds = new Rectangle(element.getLocation(), element.getDimension());
		container.removeChild(element);
	}

	public void redo() {
		container.removeChild(element);
	}

	public void setChild(ElementImpl element) {
		this.element = element;
	}

	public void setParent(Container parent) {
		container = parent;
	}

	public void undo() {
		element.setLocation(oldBounds.getLocation());
		container.addChild(element, index);
	}

}