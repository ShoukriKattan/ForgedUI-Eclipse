// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.forgedui.editor.edit.ElementEditPart;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class MoveResizeEditPartCommand extends Command {
	
	/** Stores the new size and location. */
	protected final Rectangle newBounds;
	/** Stores the old size and location. */
	private Rectangle oldBounds;

	/** Element to manipulate. */
	protected final ElementEditPart elementEditPart;

	/**
	 * Create a command that can resize and/or move an element.
	 * 
	 * @param element
	 *            the element to manipulate
	 * @param req
	 *            the move and resize request
	 * @param newBounds
	 *            the new size and location
	 * @throws IllegalArgumentException
	 *             if any of the parameters is null
	 */
	public MoveResizeEditPartCommand(ElementEditPart elementEditPart, Rectangle newBounds) {
		if (elementEditPart == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.elementEditPart = elementEditPart;
		this.newBounds = newBounds.getCopy();
		setLabel("move / resize");
	}

	public void execute() {
		oldBounds = elementEditPart.getBounds();
		redo();
	}

	public void redo() {
		elementEditPart.setModelLocation(newBounds.getLocation());
		if (newBounds.width >= 0 && newBounds.height > 0){
			elementEditPart.setModelDimension(newBounds.getSize());
		}
	}

	public void undo() {
		elementEditPart.setVisualLocation(oldBounds.getLocation());
		elementEditPart.setVisualDimension(oldBounds.getSize());
	}
}