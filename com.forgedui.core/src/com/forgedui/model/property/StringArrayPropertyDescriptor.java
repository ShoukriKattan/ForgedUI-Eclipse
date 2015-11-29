package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class StringArrayPropertyDescriptor extends PropertyDescriptor {
	
	private ILabelProvider arrayLabelProvider = new StringArrayLabelProvider();

	public StringArrayPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	@Override
	public ILabelProvider getLabelProvider() {
		return arrayLabelProvider;
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
		StringArrayCellEditor editor = new StringArrayCellEditor(parent);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}

}
