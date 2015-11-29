// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitleBar;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SetTitleBarCenterViewCommand extends Command {

	private TitaniumUIBoundedElement center;
	private TitleBar parent;
	
	public SetTitleBarCenterViewCommand(TitleBar parent, TitaniumUIBoundedElement center) {
		this.parent = parent;
		this.center = center;
		setLabel("Add Title Button");
	}
	
	/**
	 * Can execute if all the necessary information has been provided. 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return center != null && parent != null;
	}

	public void execute() {
		//center.setDimension(new Dimension(20, 20));
		String prompt = ResourceHelper.getString(parent.getTitlePrompt(),
				parent.getTitlepromptid());
		if (prompt == null) {
			prompt = "";
		}
		Dimension dim = parent.getDimension();
		center.setLocation(new Point(dim.width / 3, 1));
		center.setDimension(dim.getCopy().scale(1.0 / 3.0, prompt.length() == 0 ? 1.0 : 1.0 / 2.0));
		parent.setTitleControl(center);
	}

	public void redo() {
		//do nothing
	}

	public void undo() {
		parent.setTitleControl(null);
	}
	
}
