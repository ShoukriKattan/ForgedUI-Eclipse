// LICENSE
package com.forgedui.editor.wizard;

import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.forgedui.model.titanium.AlertDialog;
import com.forgedui.model.titanium.Dialog;
import com.forgedui.model.titanium.EmailDialog;
import com.forgedui.model.titanium.OptionDialog;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DialogTypeWizardPage extends WizardPage {
	
	protected static final int HORIZONTAL_GAP = 8;
	
	private String dialogType = null;

	/**
	 * @param pageName
	 */
	protected DialogTypeWizardPage(String pageName) {
		super(pageName);
		setDescription("Select Dialog Type");
	}


	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite topLevel = new Composite(parent, SWT.NONE);
		topLevel.setLayout(new GridLayout());
		topLevel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));
		Font font = parent.getFont();
		topLevel.setFont(font);
		
		RadioGroupFieldEditor rgfe = new RadioGroupFieldEditor("DialogTypeWizardPage",
				"Select dialog", 1,
				new String[][] {
					{"Option Dialog", "option"},
					{"Alert Dialog", "alert"},
					{"Email dialog", "email"}
				},
				topLevel);
		
		rgfe.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				dialogType = (String) event.getNewValue();
				DialogTypeWizardPage.this.setPageComplete(true);
			}
		});
		
		Composite radioBoxControl = rgfe.getRadioBoxControl(topLevel);
		((Button)radioBoxControl.getChildren()[0]).setSelection(true);
		dialogType = "option";

		setControl(topLevel);
	}
	
	public String getDialogType(){
		return dialogType;
	}
	
	public Dialog getDialog(){
		if ("option".equals(getDialogType())){
			return new OptionDialog();
		} else if ("alert".equals(getDialogType())){
			return new AlertDialog();
		} else if ("email".equals(getDialogType())){
			return new EmailDialog();
		}
		return null;
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return getDialogType() != null;
	}

}
