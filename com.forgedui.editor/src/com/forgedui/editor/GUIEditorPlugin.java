// LICENSE
package com.forgedui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.forgedui.editor.analytics.ReportingService;
import com.forgedui.editor.analytics.ReportingServiceImpl;
import com.forgedui.editor.common.ActivationService;
import com.forgedui.editor.edit.policy.ComponentValidator;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 */
public class GUIEditorPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.forgedui.editor"; //$NON-NLS-1$
	// Titanium default perspective ID
	public static final String PERSPECTIVE_ID = "com.forgedui.editor.perspective.titanium.ID"; //$NON-NLS-1$

	public static final String FORGED_UI_EXTENSION = "fui";

	// The shared instance
	private static GUIEditorPlugin plugin;

	private static ComponentValidator componentsValidator;

	private ReportingServiceImpl reportingService;

	private Job reportingFileUploadJob;

	/**
	 * The constructor
	 */
	public GUIEditorPlugin() {

	}

	protected void initReportingService() {

		reportingService = new ReportingServiceImpl();

		reportingFileUploadJob = new Job("FUIReporting") {

			@Override
			protected IStatus run(IProgressMonitor arg0) {

				System.out.println("Upload reporting job .. started");
				reportingService.uploadReports();
				System.out.println("Upload reporting job ... Ended");
				long delay = 3 * 60 * 60 * 1000;

				// delay =10000;
				schedule(delay);

				return Status.OK_STATUS;
			}
		};

		// First 10 seconds .. and then later every 3 hours
		reportingFileUploadJob.schedule(10000);
	}

	public ReportingService getReportingService() {
		return reportingService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {

		getWorkbench().getPerspectiveRegistry().setDefaultPerspective(
				PERSPECTIVE_ID);

		super.start(context);

		// If everything is ok .. check local DB for feature

		plugin = this;

		// resetPrefs();

		initReportingService();

		ActivationService.start(getLog());

		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static GUIEditorPlugin getDefault() {
		return plugin;
	}

	/**
	 * Added to make Junit testing easy
	 * 
	 * @param plugin
	 */
	public static void setPlugin(GUIEditorPlugin plugin) {
		GUIEditorPlugin.plugin = plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * @param string
	 * @param e
	 */
	public static void logError(String s, Exception e) {

		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, s, e));

	}

	public static void info(String info) {
		getDefault().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, info));
	}

	public static ComponentValidator getComponentValidator() {
		if (componentsValidator == null)
			return componentsValidator = new ComponentValidator();
		return componentsValidator;
	}

	public static void main(String[] args) {

		
		
		
		

	}
}
