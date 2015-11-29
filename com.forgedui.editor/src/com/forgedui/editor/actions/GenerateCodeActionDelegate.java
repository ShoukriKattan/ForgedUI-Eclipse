package com.forgedui.editor.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.progress.UIJob;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.common.ActivationService;
import com.forgedui.editor.exception.NotActivatedException;
import com.forgedui.editor.exception.ServerCommunicationException;
import com.forgedui.editor.exception.ServerErrorException;
import com.forgedui.editor.exception.ServerResponseTamperedException;
import com.forgedui.editor.jobs.CodeGenJob;
import com.forgedui.editor.jobs.EditorUIJob;
import com.forgedui.editor.preference.Messages;

public class GenerateCodeActionDelegate implements IEditorActionDelegate,
		IObjectActionDelegate {

	private ISelection selection;

	private IEditorPart editor;

	public GenerateCodeActionDelegate() {
	}

	@Override
	public void run(IAction action) {

		GUIEditorPlugin.info("Starting code generation");

		Shell mainShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		Long remaining = 0L;

		try {
			remaining = ActivationService.getInstance().connectAndInc();

		} catch (ServerCommunicationException e) {

			MessageDialog.openWarning(mainShell, Messages.CodeGen_Communication_LicensingServer_Failed_title,
					Messages.CodeGen_Communication_LicensingServer_Failed_txt);

			return;

		} catch (ServerErrorException e) {

			MessageDialog.openWarning(mainShell, Messages.CodeGen_Communication_AccountError_title,
					Messages.CodeGen_Communication_AccountError_txt);

			return;
		} catch (ServerResponseTamperedException e) {

			MessageDialog.openWarning(mainShell, Messages.CodeGen_Communication_ServerError_title,
					Messages.CodeGen_Communication_LicensingServer_Failed_txt);

			return;
		} catch (NotActivatedException e) {

			MessageDialog.openWarning(mainShell, Messages.CodeGen_NonLicensed_Msg_title,
					Messages.CodeGen_NonLicensed_Msg_txt);

			return;

		} catch (Exception e) {

			MessageDialog.openWarning(mainShell, Messages.CodeGen_Communication_GeneralError_title,
					Messages.CodeGen_Communication_GeneralError_txt + "ERROR: [" + e.getMessage() + "]");

			return;
		}

		if (remaining != ActivationService.UNLIMITED_CODE_GENS) {
			if (remaining == 0) {
				MessageDialog.openWarning(mainShell, Messages.CodeGen_Gens_Exceeded_title,
						Messages.CodeGen_Gens_Exceeded_txt);

				return;
			} else if (remaining < 11) {
				MessageDialog.openWarning(mainShell, Messages.CodeGen_Demo_Note_title,
						Messages.CodeGen_Demo_Note_txt.replaceAll("#noMessages#", "" + (remaining - 1)));
			}
		}

		if (this.editor != null) {
			processFile(((IFileEditorInput) this.editor.getEditorInput()).getFile());
		} else if (this.selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) this.selection).iterator(); it
					.hasNext();) {
				Object element = it.next();
				IFile file = null;
				if (element instanceof IFile) {
					file = (IFile) element;
				} else if (element instanceof IContainer) {
					processContainer((IContainer) element);
				} else if (element instanceof IAdaptable) {
					file = (IFile) ((IAdaptable) element)
							.getAdapter(IFile.class);
					if (file == null) {
						processContainer((IContainer) ((IAdaptable) element)
								.getAdapter(IContainer.class));
					}
				}
				processFile(file);
			}
		}
	}

	protected void processContainer(IContainer container) {
		if (container != null) {
			IResource[] resources;
			try {
				resources = container.members();
				for (IResource iResource : resources) {
					if (iResource instanceof IContainer) {
						processContainer((IContainer) iResource);
					} else if (iResource instanceof IFile) {
						processFile((IFile) iResource);
					}
				}
			} catch (CoreException e) {
				GUIEditorPlugin.logError(e.getMessage(), e);
			}
		}
	}

	protected void processFile(IFile file) {

		if (file != null
				&& file.getFileExtension().equalsIgnoreCase(
						GUIEditorPlugin.FORGED_UI_EXTENSION)) {

			if (GUIEditorPlugin.getDefault().getWorkbench()
					.saveAllEditors(true)) {
				// Do the start of the job here.
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				IProgressService progressService = PlatformUI.getWorkbench()
						.getProgressService();
				CodeGenJob job = new CodeGenJob(file);
				UIJob runJob = new EditorUIJob(job);
				progressService.showInDialog(shell, runJob);
				// runJob.setRule(ISchedulingRule);
				runJob.schedule();

			}
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

}
