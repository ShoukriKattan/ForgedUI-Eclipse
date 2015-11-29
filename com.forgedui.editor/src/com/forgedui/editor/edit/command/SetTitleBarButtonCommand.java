// LICENSE
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;

import com.forgedui.editor.util.ResourceHelper;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.PlatformSupport;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.util.ScreenManager;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class SetTitleBarButtonCommand extends Command {

	private Button child;
	private TitleBar parent;
	private boolean leftButton;
	
	public SetTitleBarButtonCommand(TitleBar parent, Button child, boolean leftButton) {
		this.parent = parent;
		this.child = child;
		this.leftButton = leftButton;
		setLabel("Add Title Button");
	}
	
	/**
	 * Can execute if all the necessary information has been provided. 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return child != null && parent != null;
	}

	public void execute() {
		//button size should be defined here and do not overridden later
		// or we should use a special layout
		String prompt = ResourceHelper.getString(parent.getTitlePrompt(),
				parent.getTitlepromptid());
		if (prompt == null) {
			prompt = "";
		}
		Dimension dim = parent.getDimension();
		Dimension oneThird = dim.getCopy().scale(0.9 / 3, prompt.length() == 0 ? 1.0 : 1.0 / 2);
		PlatformSupport support = new PlatformSupport(parent.getPlatform(), parent.getResolution());
		int inset = support.viewToModel(ScreenManager.TITLE_BAR_BUTTONS_INSET);//add some insets
		oneThird.expand(-inset*2, -inset*2);
		child.setWidth(oneThird.width);
		child.setHeight(oneThird.height);
		if (leftButton){
			child.setLeft(inset);
			child.setTop(inset);
			parent.setLeftNavButton(child);
		} else {
			child.setLeft(parent.getDimension().width - oneThird.width - inset);
			child.setTop(parent.getLocation().y + inset);
			parent.setRightNavButton(child);
		}
	}

	public void redo() {
		//do nothing
	}

	public void undo() {
		if (leftButton){
			parent.setLeftNavButton(null);
		} else {
			parent.setRightNavButton(null);
		}
	}
	
}
