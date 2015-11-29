// LICENSE
package com.forgedui.core;

import org.eclipse.osgi.util.NLS;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ProjectMessages extends NLS {
	
	private static final String BUNDLE_NAME = "com.forgedui.core.messages";//$NON-NLS-1$

	// ==============================================================================
	// New Titanium Project Wizards
	// ==============================================================================
	public static String NewProject_windowTitle;
	public static String NewProject_title;
	public static String NewProject_description;
	public static String NewProject_errorOpeningWindow;
	public static String NewProject_errorMessage;
	public static String NewProject_internalError;
	public static String NewProject_caseVariantExistsError;
	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, ProjectMessages.class);
	}
}
