package com.forgedui.model.property;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class StringArrayCellEditor extends DialogCellEditor {

	private ILabelProvider arrayLabelProvider;
	private Label defaultLabel;
	
	public StringArrayCellEditor(Composite parent) {
		super(parent);
		this.arrayLabelProvider = new StringArrayLabelProvider();
	}
	
	@Override
	protected Control createContents(Composite cell) {
		defaultLabel = (Label) super.createContents(cell);
		return defaultLabel;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		String[] currentValue = null;
		if (getValue() != null && String[].class.equals(getValue().getClass())) { 
			currentValue = (String[]) getValue();
		}
		StringListDialog dialog = new StringListDialog(getControl().getShell(), currentValue);
		if (dialog.open() == Window.OK){
			return dialog.getValue();
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
			text = arrayLabelProvider.getText(value);
			if (text == null) text = "";
		}
        defaultLabel.setText(text);
	}

}
