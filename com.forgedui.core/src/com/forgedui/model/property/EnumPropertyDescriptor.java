// LICENSE
package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.ComboBoxLabelProvider;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class EnumPropertyDescriptor extends PropertyDescriptor {
	
	private String[] labels;
	
	/**
	 * @param id
	 * @param displayName
	 * @param enumm
	 */
	public EnumPropertyDescriptor(Object id, String displayName, Class enumm) {
		super(id, displayName);
		labels = getLabels(enumm);
	}
	
    /**
	 * @param enumm
	 * @return
	 */
	protected String[] getLabels(Class enumm) {
		Object[] constants = getEnumConstants(enumm);
		String[] labels = new String[constants.length + 1];
		labels[0] = "<Default>";
		for (int i = 0; i < constants.length; i++) {
			Object constant = constants[i];
			labels[i + 1] = constant.toString();
		}
		return labels;
	}
	
	protected Object[] getEnumConstants(Class enumm){
		return enumm.getEnumConstants();
	}

	public CellEditor createPropertyEditor(Composite parent) {
    	 CellEditor editor = new ComboBoxCellEditor(parent, labels,
                 SWT.READ_ONLY);
         if (getValidator() != null) {
 			editor.setValidator(getValidator());
 		}
         return editor;
    }
	
	public ILabelProvider getLabelProvider() {
		if (isLabelProviderSet()) {
			return super.getLabelProvider();
		}
		return new ComboBoxLabelProvider(labels);
	}
	
}
