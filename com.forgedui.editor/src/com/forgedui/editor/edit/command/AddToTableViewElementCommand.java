// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class AddToTableViewElementCommand extends Command {

	protected TableView parent;
	protected TitaniumUIBoundedElement child;
	protected Rectangle bounds;
	protected boolean isHeader;
	
	public AddToTableViewElementCommand(TableView parent, TitaniumUIBoundedElement child,
			Rectangle bounds, boolean isHeader) {
		this.parent = parent;
		this.child = child;
		this.bounds = bounds;
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
		if (child instanceof SearchBar
				&& parent.getSearch() == null){
			parent.setSearch((SearchBar) child);
			child.setLocation(bounds.getLocation());
		} else if (isHeader){
			child.setLocation(new Point(0, 0));
			parent.setHeaderView(child);
		} else {
			child.setLocation(bounds.getLocation());
			parent.setFooterView(child);
		}
	}

	public void redo() {
		//not possible to add the same child 2 times.
	}

	public void undo() {
		if (child == parent.getSearch()){
			parent.setSearch(null);
		} else if (isHeader){
			parent.setHeaderView(null);
		} else {
			parent.setFooterView(null);
		}
	}
	
}
