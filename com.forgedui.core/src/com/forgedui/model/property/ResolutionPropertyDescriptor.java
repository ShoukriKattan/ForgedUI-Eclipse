// LICENSE
package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.forgedui.model.titanium.Platform;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ResolutionPropertyDescriptor extends PropertyDescriptor {
	
	private Platform platform;

	public ResolutionPropertyDescriptor(Platform platform,
			String displayName) {
		super(displayName, displayName);
		this.platform = platform;
		setLabelProvider(new ResolutionLabelProvider());
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new ResolutionCellEditor(parent, platform);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }

}
