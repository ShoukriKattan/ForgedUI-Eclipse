package com.forgedui.editor.jobs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ErrorHandler {

	public static void showErrorDialog(final Shell currentShell,
			final String message, final IStatus status) {
		if (currentShell == null) {
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
				public void run() {
					ErrorDialog.openError(PlatformUI.getWorkbench()
							.getDisplay().getActiveShell(),
							"Error", message, status, IStatus.ERROR); //$NON-NLS-1$
				}
			});
		} else {
			ErrorDialog.openError(currentShell, "Error", message, status); //$NON-NLS-1$
		}
	}

	public static IStatus solveException(Throwable e, String pluginId) {
		IStatus returnValue = null;
		if (e instanceof CoreException) {
			returnValue = ((CoreException) e).getStatus();

		} else if (e instanceof InvocationTargetException) {
			Status status = new Status(IStatus.ERROR, pluginId, 500, e
					.getMessage(), e.getCause());
			returnValue = solveException(new CoreException(status), pluginId);

		} else {
			if (e.getMessage() != null) {
				returnValue = new Status(IStatus.ERROR, pluginId, 500, e
						.getMessage(), e);
			} else {
				returnValue = new Status(IStatus.ERROR, pluginId, 500,
						"UNKNOWN ERROR", e); //$NON-NLS-1$
			}
		}
		return returnValue;

	}

}