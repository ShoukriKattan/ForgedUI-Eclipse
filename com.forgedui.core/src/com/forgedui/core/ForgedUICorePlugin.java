package com.forgedui.core;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ForgedUICorePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.forgedui.core"; //$NON-NLS-1$

	public static final String CURRENT_VERSION = "1"; // Release version
	
	public static final Pattern JSS_NAME_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z_0-9\\.\\-]*");

	// The shared instance
	private static ForgedUICorePlugin plugin;

	private TitaniumProjectsManager titaniumProjectManager;

	/**
	 * The constructor
	 */
	public ForgedUICorePlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		try {
			if (this.titaniumProjectManager != null) {
				this.titaniumProjectManager.stop();
				this.titaniumProjectManager = null;
			}
		} finally {
			super.stop(context);
		}
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static ForgedUICorePlugin getDefault() {
		return plugin;
	}

	/**
	 * @param string
	 * @param e
	 */
	public static void logError(String s, Exception e) {
		System.err.println(s);
		e.printStackTrace();
	}

	/**
	 * 
	 * @param e
	 */
	public static void logError(Exception e) {
		logError(e.getLocalizedMessage(), e);
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static TitaniumProject getTitaniumProject(IProject project) {
		return getDefault().getTitaniumProjectsManager().getTitaniumProject(
				project);
	}

	private synchronized TitaniumProjectsManager getTitaniumProjectsManager() {
		if (this.titaniumProjectManager == null) {
			this.titaniumProjectManager = new TitaniumProjectsManager();
			this.titaniumProjectManager.start();
		}
		return this.titaniumProjectManager;
	}

}
