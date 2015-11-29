package com.forgedui.editor.edit.command;

import java.util.List;

import org.eclipse.gef.commands.Command;

import com.forgedui.model.Container;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;

/**
 * This command is created once we try to move a component
 * from one container to another. Please see EditorContainerEditPolicy
 * 
 * @author Tareq Doufish
 *
 */
public class TitaniumOrphanChildCommand extends Command {

	private String t,l,b,r,w,h;
	private Container container;
	private TitaniumUIBoundedElement element;
	private int index;

	public TitaniumOrphanChildCommand() {
	}

	public void execute() {
		List<?> children = container.getChildren();
		index = children.indexOf(element);
		t = element.getTop();
		l = element.getLeft();
		b = element.getBottom();
		r = element.getRight();
		w = element.getWidth();
		h = element.getHeight();
		container.removeChild(element);
	}

	public void redo() {
		container.removeChild(element);
	}

	public void setChild(TitaniumUIBoundedElement element) {
		this.element = element;
	}

	public void setParent(Container parent) {
		container = parent;
	}

	public void undo() {
		element.setTop(t);
		element.setLeft(l);
		element.setBottom(b);
		element.setRight(r);
		element.setWidth(w);
		element.setHeight(h);
		container.addChild(element, index);
	}

}