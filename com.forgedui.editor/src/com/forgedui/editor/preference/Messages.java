// LICENSE
package com.forgedui.editor.preference;

import org.eclipse.osgi.util.NLS;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.forgedui.editor.preference.messages"; //$NON-NLS-1$
	public static String EditorPropertiesPreferencePage_Prefer_Relative_Bounds;
	public static String CodeGenerationPropertiesPreferencePage_Code_Generation_Style;
	public static String CodeGenerationPropertiesPreferencePage_Code_Generation_Style_BASIC;
	public static String CodeGenerationPropertiesPreferencePage_Code_Generation_Style_OOModule;
	public static String CodeGenerationPropertiesPreferencePage_Code_Generation_Style_COMMONJS;
	public static String CodeGenerationPropertiesPreferencePage_Code_Generation_SUPPORT_JSS;

	public static String CodeGen_NonLicensed_Msg_title;
	public static String CodeGen_NonLicensed_Msg_txt;

	public static String CodeGen_Communication_LicensingServer_Failed_title;
	public static String CodeGen_Communication_LicensingServer_Failed_txt;

	public static String CodeGen_Communication_AccountError_title;
	public static String CodeGen_Communication_AccountError_txt;

	public static String CodeGen_Communication_ServerError_title;
	public static String CodeGen_Communication_ServerError_txt;

	public static String CodeGen_Communication_GeneralError_title;
	public static String CodeGen_Communication_GeneralError_txt;

	public static String CodeGen_Demo_Note_title;
	public static String CodeGen_Demo_Note_txt;

	public static String CodeGen_Gens_Exceeded_title;
	public static String CodeGen_Gens_Exceeded_txt;

	public static String Activation_Success_Full_title;

	public static String Activation_Success_Full_txt;
	

	public static String Activation_Failed_License_Tampered_title;
	public static String Activation_Failed_License_Tampered_txt;

	public static String Activation_Failed_ServerCommunication_title;
	public static String Activation_Failed_ServerCommunication_txt;

	public static String Activation_Failed_InvalidActivation_title;
	public static String Activation_Failed_InvalidActivation_txt;

	public static String Activation_Failed_GeneralError_title;
	public static String Activation_Failed_GeneralError_txt;

	public static String Activation_Failed_Wrong_Bad_Code_title;
	public static String Activation_Failed_Wrong_Bad_Code_txt;

	public static String Activation_Validation_Error_title;
	public static String Activation_Validation_Error_txt;

	public static String Activation_General_Error_title;
	public static String Activation_General_Error_txt;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
