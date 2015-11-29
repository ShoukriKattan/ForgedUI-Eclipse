package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * Boolean property descriptor to be used with the properties view.
 * 
 * @author Tareq Doufish
 *
 */
public class BooleanPropertyDescriptor extends PropertyDescriptor {
	
	private static final String DEFAULT = "<Default>";

	/** Weather this is just for the displaying or we will edit it */
	private boolean editable;
	
	/** 
	 * The default label provider for this item.
	 */
    private static final LabelProvider defaultLabelProvider = new LabelProvider() {
        @Override
        public String getText(Object o) {
        	if (o != null) {
	        	if (o.equals(new Integer(0))) { 
	        		return DEFAULT;
	        	} else if (o.equals(new Integer(1))) { 
	        		return "true";
	        	} else if (o.equals(new Integer(2))) { 
	        		return "false";
	        	}
        	}
        	return super.getText(o);
        }
    };
    
    public BooleanPropertyDescriptor(Object id, String displayName) {
        this(id, displayName, false);
    }

    public BooleanPropertyDescriptor(Object id, String displayName, boolean editable) {
        super(id, displayName);
        this.editable = editable;
        setLabelProvider(defaultLabelProvider);
    }
    
    public CellEditor createPropertyEditor(Composite parent) {
    	if (!this.editable){
    		return null;
    	}
        final ComboBoxCellEditor cellEditor = new ComboBoxCellEditor(parent,
        		new String[]{DEFAULT, Boolean.TRUE.toString(),Boolean.FALSE.toString()},
        		SWT.READ_ONLY);
        Control control = cellEditor.getControl();
        control.addMouseListener(new MouseAdapter() {
        	private final Integer ONE = new Integer(1);
        	private final Integer TWO = new Integer(2);
        	@Override
        	public void mouseDoubleClick(MouseEvent e) {
        		if (ONE.equals(cellEditor.getValue())){//true
        			cellEditor.setValue(2);//false
        		} else if (TWO.equals(cellEditor.getValue())){//false
        			cellEditor.setValue(1);//true
        		}
        		super.mouseDoubleClick(e);
        	}
		});
        if (getValidator() != null) {
            cellEditor.setValidator(getValidator());
        }
        return cellEditor;
    }

}
