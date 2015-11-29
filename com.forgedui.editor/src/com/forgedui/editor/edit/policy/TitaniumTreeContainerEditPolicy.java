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
package com.forgedui.editor.edit.policy;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.TreeContainerEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.widgets.TreeItem;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.edit.command.CreateCommand;
import com.forgedui.editor.edit.command.MoveChildCommand;
import com.forgedui.editor.edit.tree.ElementTreeEditPart;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;

public class TitaniumTreeContainerEditPolicy extends TreeContainerEditPolicy {

	protected Command createCreateCommand(TitaniumUIBoundedElement child, Rectangle r,
			int index, String label) {
		CreateCommand cmd = new CreateCommand();
		Rectangle rect;
		if (r == null) {
			rect = new Rectangle();
			rect.setSize(new Dimension(-1, -1));
		} else {
			rect = r;
		}
		cmd.setLocation(rect);
		cmd.setParent((Element) getHost().getModel());
		cmd.setChild(child);
		cmd.setLabel(label);
		if (index >= 0)
			cmd.setIndex(index);
		return cmd;
	}

	protected Command getAddCommand(ChangeBoundsRequest request) {
		CompoundCommand command = new CompoundCommand();
		List editparts = request.getEditParts();
		int index = findIndexOfTreeItemAt(request.getLocation());

		for (int i = 0; i < editparts.size(); i++) {
			EditPart child = (EditPart) editparts.get(i);
			if (GUIEditorPlugin.getComponentValidator().isAncestor((Element)child.getModel(),(Element)getHost().getModel()))
				command.add(UnexecutableCommand.INSTANCE);
			else {
				TitaniumUIBoundedElement childModel = (TitaniumUIBoundedElement) child.getModel();
				command.add(createCreateCommand(childModel,
						new Rectangle(new org.eclipse.draw2d.geometry.Point(),
								new Dimension(-1, -1)), index,
						"Reparent LogicSubpart"));//$NON-NLS-1$
			}
		}
		return command;
	}

	protected Command getCreateCommand(CreateRequest request) {
		TitaniumUIBoundedElement child = (TitaniumUIBoundedElement) request.getNewObject();
		int index = findIndexOfTreeItemAt(request.getLocation());
		return createCreateCommand(child, null, index, "Create LogicSubpart");//$NON-NLS-1$
	}

	protected Command getMoveChildrenCommand(ChangeBoundsRequest request) {
		CompoundCommand command = new CompoundCommand();
		List editparts = request.getEditParts();
		List children = getHost().getChildren();
		int newIndex = findIndexOfTreeItemAt(request.getLocation());
		for (int i = 0; i < editparts.size(); i++) {
			EditPart child = (EditPart) editparts.get(i);
			int tempIndex = newIndex;
			int oldIndex = children.indexOf(child);
			if (tempIndex == -1 || oldIndex == tempIndex || oldIndex + 1 == tempIndex) {
				command.add(UnexecutableCommand.INSTANCE);
				return command;
			} else if (oldIndex <= tempIndex) {
				tempIndex--;
			}
			command.add(new MoveChildCommand((Element) child.getModel(),tempIndex));
		}
		return command;
	}

}
