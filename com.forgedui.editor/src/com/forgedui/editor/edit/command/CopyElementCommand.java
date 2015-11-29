package com.forgedui.editor.edit.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import com.forgedui.model.Diagram;
import com.forgedui.model.Element;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.Dialog;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Window;

/**
 * 
 * @author Zrieq
 *
 */
public class CopyElementCommand extends Command {
	protected ArrayList<Element> list = new ArrayList<Element>();

	public boolean addElement(Element node) {
		if (!list.contains(node)) {
			return list.add(node);
		}
		return false;
	}

	@Override
	public boolean canExecute() {
		if (list == null || list.isEmpty())
			return false;
		Iterator<Element> it = list.iterator();
		while (it.hasNext()) {
			if (!isCopyableNode(it.next()))
				return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if (canExecute())
			Clipboard.getDefault().setContents(list);
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	public boolean isCopyableNode(Element node) {
		if(node instanceof Screen || node instanceof Diagram || node instanceof Window || node instanceof Dialog || node instanceof TitleBar)
			return false;
		return true;
	}
}
