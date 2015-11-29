package com.forgedui.model.property;

/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * A Generic property descriptor for numbers.
 * <p>
 * It will use the NumberCellEditor and Label provider. These editors and
 * providers display and format the numbers in locale format.
 */
public class NumberPropertyDescriptor extends PropertyDescriptor {

    // Need only one, they are not descriptor specific.
    protected static final NumberLabelProvider labelProvider = new NumberLabelProvider();

    private int numberType;

    private boolean allowNull;
    
    public NumberPropertyDescriptor(Object propertyID, String propertyDisplayname, boolean allowNull) {
        this(propertyID, propertyDisplayname, NumberCellEditor.NUMBER, allowNull);
    }

    public NumberPropertyDescriptor(Object id, String displayName, int numberType, boolean allowNull) {
        super(id, displayName);
        this.numberType = numberType;
        this.allowNull = allowNull;
        // This can be overridden by setting a different value after creation.
        setLabelProvider(labelProvider);
    }

    public NumberPropertyDescriptor(Object id, String displayName, int numberType, boolean allowNull, ICellEditorValidator validator) {
    	this(id, displayName, numberType, allowNull);
    	setValidator(validator);
    }

    @Override
    public CellEditor createPropertyEditor(Composite parent) {
        NumberCellEditor editor = new NumberCellEditor(parent, this.allowNull);
        editor.setType(this.numberType);
        ICellEditorValidator v = getValidator();
        if (v != null) {
            editor.setValidator(v);
        }
        return editor;
    }

    public void setNumberType(int numberType) {
        this.numberType = numberType;
    }

}