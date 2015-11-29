// LICENSE
package com.forgedui.model.property;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ColorCellEditor extends org.eclipse.jface.viewers.ColorCellEditor {

	private Button browseButton;
	
	/**
	 * @param parent
	 */
	public ColorCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Button createButton(Composite parent) {
		browseButton = super.createButton(parent);
		browseButton.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.BS){
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
