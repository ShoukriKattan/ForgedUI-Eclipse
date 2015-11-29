//LICENSE
package com.forgedui.model.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.forgedui.util.DateTimeLableProvider;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DateTimePropertyDescriptor extends PropertyDescriptor {
	
	private ILabelProvider dateTimeLabelProvider;
	private boolean showTime, showDate;

	/**
	 * @param id
	 * @param displayName
	 */
	public DateTimePropertyDescriptor(Object id, String displayName, boolean showTime, boolean showDate) {
		super(id, displayName);
		this.showTime = showTime;
		this.showDate = showDate;
		dateTimeLabelProvider = new DateTimeLableProvider(showTime, showDate);
	}
	
	@Override
	public ILabelProvider getLabelProvider() {
		return dateTimeLabelProvider;
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
		DateTimeCellEditor editor = new DateTimeCellEditor(parent, showTime, showDate);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}

}
