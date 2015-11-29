package com.forgedui.editor.preference;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.common.ActivationService;
import com.forgedui.editor.exception.BadActivationCodeException;
import com.forgedui.editor.exception.InvalidActivationCodeException;
import com.forgedui.editor.exception.InvalidActivationException;
import com.forgedui.editor.exception.LicenseTamperedException;
import com.forgedui.editor.exception.ServerCommunicationException;

/**
 * The main preference page for the license
 * 
 * @author Tareq Doufish
 * 
 */
public class LicensePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Text activationValue;

	@Override
	protected Control createContents(final Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		container.setLayout(layout);

		Composite activationCodeContainer = container;

		Label activationLabel = new Label(activationCodeContainer, SWT.NONE);
		activationLabel.setText("Activation Code: ");

		this.activationValue = new Text(activationCodeContainer, SWT.BORDER
				| SWT.WRAP);

		GridData activationLayoutData = new GridData();
		activationLayoutData.horizontalAlignment = GridData.FILL;
		activationLayoutData.grabExcessHorizontalSpace = true;
		this.activationValue.setLayoutData(activationLayoutData);

		Button activationButton = new Button(activationCodeContainer, NONE);
		activationButton.setText("Activate");

		activationButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String activationText = LicensePreferencePage.this.activationValue.getText();

				if (activationText == null || activationText.length() == 0) {

					MessageDialog.openError(getShell(),
							Messages.Activation_Validation_Error_title,
							Messages.Activation_Validation_Error_txt);

					return;
				}

				try {
					activatePluginLicense(activationText.trim());
				} catch (Exception ee) {

					MessageDialog.openError(getShell(),
							Messages.Activation_General_Error_title,
							Messages.Activation_General_Error_txt + " ERROR: ["
									+ ee.getMessage() + "]");

					return;

				}
				// Validate make sure activation text has text
				// Also make sure its valid and works for activation
				// Is there a way to notify main page that the situation
				// changed?

			}

		});

		// createFeedbackComposite(parent);

		return container;
	}

	public void activatePluginLicense(String licenseCode)
			throws BadActivationCodeException {

		try {

			ActivationService.getInstance().activatePluginLicense(licenseCode);

			String stdTitle = Messages.Activation_Success_Full_title;

			String stdMsg = Messages.Activation_Success_Full_txt;

			MessageDialog.openInformation(getShell(), stdTitle, stdMsg);

		} catch (LicenseTamperedException e) {

			MessageDialog.openError(getShell(),

			Messages.Activation_Failed_License_Tampered_title,
					Messages.Activation_Failed_License_Tampered_txt);

		} catch (ServerCommunicationException e) {

			MessageDialog.openError(getShell(),
					Messages.Activation_Failed_ServerCommunication_title,
					Messages.Activation_Failed_ServerCommunication_txt

			);
		} catch (InvalidActivationException e) {

			MessageDialog.openError(getShell(),
					Messages.Activation_Failed_InvalidActivation_title, e
							.getMessage() != null ? e.getMessage()
							: Messages.Activation_Failed_InvalidActivation_txt);
		}

		catch (InvalidActivationCodeException e) {
			MessageDialog.openError(getShell(), Messages.Activation_Failed_Wrong_Bad_Code_title,
					Messages.Activation_Failed_Wrong_Bad_Code_txt);
			
			
		} catch (Exception e) {
			MessageDialog.openError(
					getShell(),

					Messages.Activation_Failed_GeneralError_title,
					Messages.Activation_Failed_GeneralError_txt + "["
							+ e.getMessage() + "]");
		}
		return;

	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(GUIEditorPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * For loading the defaults here.
	 */
	@Override
	protected void performDefaults() {

		ActivationService.getInstance().clearActivation();

	}

	/**
	 * For performing the ok, once a user is done with his/her changes.
	 */
	@Override
	public boolean performOk() {
		return super.performOk();
	}

}
