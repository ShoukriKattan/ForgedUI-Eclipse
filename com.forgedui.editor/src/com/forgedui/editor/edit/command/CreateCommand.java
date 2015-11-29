/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.forgedui.editor.edit.command;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.model.Container;
import com.forgedui.model.Diagram;
import com.forgedui.model.Element;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.Dialog;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;

public class CreateCommand extends org.eclipse.gef.commands.Command {

	private TitaniumUIBoundedElement child;
	private Rectangle rect;
	private Element parent;
	private int index = -1;

	public CreateCommand() {
		super("ZRIEQ");
	}

	public boolean canExecute() {
		return child != null
				&& parent != null
				&& parent instanceof Container
				&& !(parent instanceof Diagram)
				&& !(parent instanceof Dialog)
				&& !(parent instanceof Screen)
				&& !(GUIEditorPlugin.getComponentValidator().isAncestor(child, parent))
				&& GUIEditorPlugin.getComponentValidator().validate(child,parent);
	}

	public void execute() {
		redo();
	}

	private Insets getInsets() {
		return new Insets();
	}

	public void redo() {
		if (rect != null) {
			Insets expansion = getInsets();
			if (!rect.isEmpty())
				rect.expand(expansion);
			else {
				rect.x -= expansion.left;
				rect.y -= expansion.top;
			}
			child.setLocation(rect.getLocation());
//			if (!rect.isEmpty())
//				child.setSize(rect.getSize());
		}
		((Container)parent).addChild(child, index);
		child.initDefaults();
	}

	public void setChild(TitaniumUIBoundedElement subpart) {
		child = subpart;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setLocation(Rectangle r) {
		rect = r;
	}

	public void setParent(Element newParent) {
		parent = newParent;
	}

	public void undo() {
		((Container)parent).removeChild(child);
	}
}
