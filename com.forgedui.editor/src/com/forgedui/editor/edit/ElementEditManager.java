package com.forgedui.editor.edit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.forgedui.editor.edit.direct.DirectEditingManager;

/**
 * The element edit manager.
 * 
 * @author Tareq Doufish
 *
 */
public class ElementEditManager extends DirectEditManager {

	public ElementEditManager(GraphicalEditPart source,
			CellEditorLocator locator) {
		super(source, null, locator);
	}

	/**
	 * Creating a single cell text editor.
	 */
	protected CellEditor createCellEditorOn(Composite composite) {
		return new TextCellEditor(composite, SWT.SINGLE | SWT.WRAP);
	}

	protected void initCellEditor() {
		GraphicalEditPart part = getEditPart();
		ElementEditPart elementEditPart = null;
		if (part instanceof ElementEditPart) { 
			elementEditPart = (ElementEditPart)part;
			Object model = elementEditPart.getModel();
			IFigure figure = elementEditPart.getFigure();
			String editValue = DirectEditingManager. getElementDirectText(model);
			if (editValue != null) {
				getCellEditor().setValue(editValue);
				getCellEditor().getControl().setFont(figure.getFont());
			} else { 
				getCellEditor().setValue("");
			}
		}
	}
}
