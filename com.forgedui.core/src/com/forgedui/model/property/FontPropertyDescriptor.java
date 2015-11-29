package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * Font property descriptor.
 * 
 * @author Tareq Doufish
 *
 */
public class FontPropertyDescriptor extends PropertyDescriptor {

	/** 
	 * The default label provider for this item.
	 */
    private static final LabelProvider defaultLabelProvider = new LabelProvider() {
        @Override
        public String getText(Object o) {
    		return super.getText(o);
        }
    };
    
    public FontPropertyDescriptor(Object id, String displayName) {
        super(id, displayName);
        setLabelProvider(defaultLabelProvider);
    }
    
    public CellEditor createPropertyEditor(Composite parent) {
        CellEditor cellEditor = new FontDialogCellEditor(parent);
        if (getValidator() != null) {
            cellEditor.setValidator(getValidator());
        }
        return cellEditor;
    }


}
