//LICENSE
package com.forgedui.model.property;

import java.util.Calendar;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.forgedui.util.DateTimeLableProvider;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DateTimeCellEditor extends NullabelDialogCellEditor {
	
	private ILabelProvider dateTimeLabelProvider;
	private boolean showTime, showDate;
	
	private Label defaultLabel;
	
	public DateTimeCellEditor(Composite parent, boolean showTime, boolean showDate) {
		super(parent);
		this.showTime = showTime;
		this.showDate = showDate;
		this.dateTimeLabelProvider = new DateTimeLableProvider(showTime, showDate);
	}
	
	@Override
	protected Control createContents(Composite cell) {
		defaultLabel = (Label) super.createContents(cell);
		return defaultLabel;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		Calendar currentValue = null;
		if (getValue() != null && getValue() instanceof Calendar) { 
			currentValue = (Calendar) getValue();
		}
		DateTimeDialog dialog = new DateTimeDialog(getControl().getShell(), currentValue, showTime, showDate);
		if (dialog.open() == Window.OK){
			return dialog.getSelectedDateTime();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
	 */
	@Override
	protected void updateContents(Object value) {
		if (defaultLabel == null) {
			return;
		}

        String text = "";//$NON-NLS-1$
        if (value != null) {
			text = dateTimeLabelProvider.getText(value);
			if (text == null) text = "";
		}
        defaultLabel.setText(text);
	}

}
