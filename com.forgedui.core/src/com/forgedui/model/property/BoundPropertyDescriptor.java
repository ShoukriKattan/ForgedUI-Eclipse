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
public class BoundPropertyDescriptor extends PropertyDescriptor {
	
	private boolean allowNegative = false;
	
	public BoundPropertyDescriptor(Object id, String displayName) {
		this(id, displayName, true);
	}
	
	public BoundPropertyDescriptor(Object id, String displayName, boolean allowNegative) {
		super(id, displayName);
		setValidator(boundValidator);
		this.allowNegative = allowNegative;
	}
	
    public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new TextCellEditor(parent);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }

	public ICellEditorValidator boundValidator = new ICellEditorValidator() {
		public String isValid(Object value) {
			if (value == null ||  value.toString().isEmpty()) return null;
			//not supported for now
			//if ("auto".equalsIgnoreCase(value)) return null;
			String strValue = (String)value;
			boolean canBeNegative = allowNegative;
			if (strValue.endsWith("%")){
				strValue = strValue.substring(0, strValue.length() - 1);
				canBeNegative = false;
			}
			float floatValue = -1;
			try {
				floatValue = Float.parseFloat((String) strValue);
			} catch (NumberFormatException exc) {
				return "Not a number or percent";
			}
			return (!canBeNegative && floatValue < 0) ? "Value must be >= 0" : null;
		}
	};
	
}
