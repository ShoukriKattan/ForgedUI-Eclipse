// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddToTableSectionElementCommand extends Command {

	protected TableViewSection parent;
	protected TitaniumUIBoundedElement child;
	protected Point location;
	protected boolean isHeader;
	
	public AddToTableSectionElementCommand(TableViewSection parent, TitaniumUIBoundedElement child,
			Point location, boolean isHeader) {
		this.parent = parent;
		this.child = child;
		this.location = location;
		this.isHeader = isHeader;
		setLabel(isHeader ? "Header creation" : "Footer creation");
	}
	
	/**
	 * Can execute if all the necessary information has been provided. 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return child != null && parent != null;
	}

	public void execute() {
		if (isHeader){
			parent.setHeaderView(child);
		} else {
			parent.setFooterView(child);
		}
		child.setLeft(""+location.x);
		child.setTop(""+location.y);
	}

	public void redo() {
		//not possible to add the same child 2 times.
	}

	public void undo() {
		if (isHeader){
			parent.setHeaderView(null);
		} else {
			parent.setFooterView(null);
		}
	}
	
}
