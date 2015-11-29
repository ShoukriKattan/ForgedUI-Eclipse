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
public class IntegerPropertyDescriptor extends PropertyDescriptor {

	private boolean allowNull = false;
	
	/**
	 * @param id
	 * @param displayName
	 */
	public IntegerPropertyDescriptor(Object id, String displayName) {
		this(id, displayName, false);
	}
	
	public IntegerPropertyDescriptor(Object id, String displayName, boolean allowNull) {
		this(id, displayName, allowNull, false);
	}
	
	public IntegerPropertyDescriptor(Object id, String displayName, boolean allowNull, boolean signed) {
		super(id, displayName);
		setValidator(signed ? intValidator : uintValidator);
		this.allowNull = allowNull;
	}
	
    public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new TextCellEditor(parent);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }

	public ICellEditorValidator uintValidator = new ICellEditorValidator() {
		public String isValid(Object value) {
			if (allowNull && (value == null ||  value.toString().isEmpty())) return null;
			int intValue = -1;
			try {
				intValue = Integer.parseInt((String) value);
			} catch (NumberFormatException exc) {
				return "Not a number";
			}
			return (intValue >= 0) ? null : "Value must be >=  0";
		}
	};

	public ICellEditorValidator intValidator = new ICellEditorValidator() {
		public String isValid(Object value) {
			if (allowNull && (value == null ||  value.toString().isEmpty())) return null;
			try {
				Integer.parseInt((String) value);
			} catch (NumberFormatException exc) {
				return "Not a number";
			}
			return null;
		}
	};
	
}
