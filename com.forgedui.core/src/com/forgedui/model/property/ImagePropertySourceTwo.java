package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * This is just another way to implement
 * the image property descriptor only used
 * for the image view for the mean time...
 * 
 * @author Tareq Doufish
 *
 */
public class ImagePropertySourceTwo extends PropertyDescriptor {

	public ImagePropertySourceTwo(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// Just indicating that we are going to use URL as well for this item.
		CellEditor editor = new ImageCellEditor(parent,true);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}

}
