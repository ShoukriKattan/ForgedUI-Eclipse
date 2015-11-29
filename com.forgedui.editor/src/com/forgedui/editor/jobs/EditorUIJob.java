package com.forgedui.editor.jobs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.UIJob;

import com.forgedui.editor.GUIEditorPlugin;

public class EditorUIJob extends UIJob {

	private IEditorJob actionJob = null;

	/**
	 * @param name
	 */
	public EditorUIJob(IEditorJob job) {
		super(job.getJobName());
		this.actionJob = job;
	}

	public IStatus runInUIThread(IProgressMonitor monitor) {
		IStatus returnValue = Status.OK_STATUS;
		IStatus exceptionStatus = null;
		setPriority(Job.INTERACTIVE);
		monitor.beginTask(actionJob.getJobName(), actionJob.getTotalTime());
		try {
			this.actionJob.run(monitor);
		} catch (InvocationTargetException ex) {
			exceptionStatus = ErrorHandler.solveException(ex.getCause(),
					GUIEditorPlugin.PLUGIN_ID);
			returnValue = new MultiStatus(GUIEditorPlugin.PLUGIN_ID, 500,
					new IStatus[] { exceptionStatus }, ex.getMessage(), null);
		} catch (InterruptedException e) {
			exceptionStatus = ErrorHandler.solveException(e,
					GUIEditorPlugin.PLUGIN_ID);
			returnValue = new MultiStatus(GUIEditorPlugin.PLUGIN_ID, 500,
					new IStatus[] { exceptionStatus }, e.getMessage(), null);
		} finally {
			monitor.done();
		}
		if (monitor.isCanceled())
			returnValue = Status.CANCEL_STATUS;
		return returnValue;
	}

}
