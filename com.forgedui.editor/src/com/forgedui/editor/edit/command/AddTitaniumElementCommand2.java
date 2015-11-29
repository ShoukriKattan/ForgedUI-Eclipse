// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.forgedui.editor.edit.ElementEditPart;
import com.forgedui.editor.preference.EditorPreferences;
import com.forgedui.editor.util.BoundsHelper;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddTitaniumElementCommand2 extends Command {

	private TitaniumUIBoundedElement child;
	private ElementEditPart parent;
	private Rectangle bounds;
	
	public AddTitaniumElementCommand2(ElementEditPart parent, TitaniumUIBoundedElement child, Rectangle bounds) {
		Assert.isTrue(parent.getModel() instanceof Container);
		this.parent = parent;
		this.child = child;
		this.bounds = bounds;
		setLabel("Element creation");
	}
	
	/**
	 * Can execute if all the necessary information has been provided. 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return child != null && parent != null;
	}

	public void execute() {
		if (bounds != null){
			BoundsHelper.setLocation(parent, child, bounds.getLocation());
			Dimension size = bounds.getSize();
			if (size.width > 0 && size.height > 0){
				//otherwise will not be able to convert to percents
				child.setWidth(null);
				child.setHeight(null);
				BoundsHelper.setDimension(parent, child, bounds.getSize());
			} else if (EditorPreferences.PreferRelativeBounds){
				//otherwise will not be able to convert to percents
				Dimension d = new Dimension(Integer.parseInt(child.getWidth()),
					Integer.parseInt(child.getHeight()));
				child.setWidth(null);
				child.setHeight(null);
				BoundsHelper.setDimension(parent, child, d);
			}
		} else {
			child.setLeft(null);
			child.setRight(null);
			child.setTop(null);
			child.setBottom(null);
			child.setWidth(null);
			child.setHeight(null);
		}
		getParent().addChild(child);
		child.initDefaults();
	}

	public void redo() {
		//not possible to add the same child 2 times.
		//parent.addChild(child);
		getParent().addChild(child);
	}

	public void undo() {
		getParent().removeChild(child);
	}
	

	public Container getParent() {
		return (Container)parent.getModel();
	}
	
	public Element getChild(){
		return this.child;
	}

}
