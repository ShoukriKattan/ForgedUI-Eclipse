// LICENSE
package com.forgedui.model.property;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * Sets value to null on Backspace press
 *
 */
public abstract class NullabelDialogCellEditor extends DialogCellEditor {
	
	private Button browseButton;

	public NullabelDialogCellEditor() {

	}

	/**
	 * @param parent
	 */
	public NullabelDialogCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public NullabelDialogCellEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	protected Button createButton(Composite parent) {
		browseButton = super.createButton(parent);
		browseButton.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.BS || e.character == SWT.DEL){
					changeToNull();
				}
			}
		});
		return browseButton;
	}
	
	public void changeToNull() {
		super.doSetValue((String) null);
		this.focusLost();
	}

}
