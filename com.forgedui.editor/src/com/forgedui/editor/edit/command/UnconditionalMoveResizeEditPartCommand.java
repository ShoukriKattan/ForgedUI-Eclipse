// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Rectangle;

import com.forgedui.editor.edit.ElementEditPart;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class UnconditionalMoveResizeEditPartCommand extends
		MoveResizeEditPartCommand {

	/**
	 * @param elementEditPart
	 * @param newBounds
	 */
	public UnconditionalMoveResizeEditPartCommand(
			ElementEditPart elementEditPart, Rectangle newBounds) {
		super(elementEditPart, newBounds);
	}
	
	@Override
	public void redo() {
		elementEditPart.setModelDimension(newBounds.getSize());
		elementEditPart.setModelLocation(newBounds.getLocation());
	}

}
