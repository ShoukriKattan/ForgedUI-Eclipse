// LICENSE
package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class FloatPropertyDescriptor extends PropertyDescriptor {
	
	private boolean acceptNull;

	/**
	 * @param id
	 * @param displayName
	 */
	public FloatPropertyDescriptor(Object id, String displayName) {
		this(id, displayName, true);
	}
	
	public FloatPropertyDescriptor(Object id, String displayName, boolean acceptNull) {
		super(id, displayName);
		setValidator(floatValidator);//default validator
		this.acceptNull = acceptNull;
	}
	
    public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new TextCellEditor(parent);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }

	public ICellEditorValidator floatValidator = new ICellEditorValidator() {
		public String isValid(Object value) {
			if (value == null || (value != null && value.toString().isEmpty())){
				return acceptNull ? null : "Value must be set";
			}
			Float floatValue = null;
			try {
				floatValue = Float.parseFloat((String) value);
			} catch (NumberFormatException exc) {
				return "Not a number";
			}
			return (floatValue >= 0) ? null : "Value must be >=  0";
		}
	};
	
}
