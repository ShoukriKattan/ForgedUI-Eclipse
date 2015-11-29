/**
 * 
 */
package com.forgedui.editor.generator;

import com.forgedui.editor.analytics.ReportingService;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.TitaniumUIBaseElement;

/**
 * @author shoukry
 * 
 */
public class CommonJSModuleCodeGenerator extends
		ObjectOrientedModuleCodeGenerator implements CodeGenerator {

	/**
	 * 
	 * @param logger
	 * @param reportingSvc
	 * @param supportJSS
	 */
	public CommonJSModuleCodeGenerator(Logger logger,
			ReportingService reportingSvc, boolean supportJSS) {
		super(logger, reportingSvc, supportJSS);

	}

	/**
	 * @see com.forgedui.editor.generator.CodeGenerator#generateCodeForModel(com.
	 *      forgedui.model.titanium.TitaniumUIContainer,
	 *      com.forgedui.model.titanium.Platform)
	 */
	@Override
	public void generateCodeForModel(TitaniumUIBaseElement rootElement,
			Platform platform) throws Exception {

		try {

			generatedCodeLines
					.add("//To start using this generated code .. simply require this file in your app.js");
			generatedCodeLines
					.add("//var window1ModuleDef= require('fui/window1').Window1;");

			generatedCodeLines
					.add("//You need to create an instance of this module  window1Module=new window1ModuleDef();");

			generatedCodeLines
					.add("//To create your generated window and get a reference to it do: ");
			generatedCodeLines
					.add("//window1Module.createAttachChWindow1(); This line instantiates the window and attaches all child components to it");

			generatedCodeLines
					.add("//window1Module.window1; this line returns the actual window instance");

			generatedCodeLines
					.add("//If a window has large views. Remember to destroy these views if they are no longer needed. window1Module.view=null");

			// Before we start we might want to link some definition files
			// .etc...

			generatedCodeLines.add("if(typeof FUI === \"undefined\"){FUI={};}");

			generatedCodeLines.add("(function(){");

			elementsStack.push(rootElement);

			// define the Window we are working with

			String moduleName = getModuleName(rootElement);

			generatedCodeLines.add("\tFUI." + moduleName + " = function(){};");

			String fqdn = "FUI." + moduleName + ".";

			// Now for other components generate functions
			while (!elementsStack.isEmpty()) {

				TitaniumUIBaseElement el = elementsStack.pop();

				generateCode((TitaniumUIBaseElement) el, platform.name()
						.toLowerCase(), fqdn);

			}

			generatedCodeLines.add("})();");

			// Do the exports piece
			generatedCodeLines.add("exports." + moduleName + "=FUI."
					+ moduleName);

			for (String varName : trackedVariables) {

				String variableQualifyingName = fqdn + "prototype.";

				generatedCodeLines.add(9, "\t" + variableQualifyingName
						+ varName + " = null ;");

			}

			// We are done ... lets send the statistics :

			doReporting(platform, "COMMON_JS_CODEGEN");

		} catch (Exception e) {

			logger.logError("Error while generating code", e);

			throw e;

		}

	}

}
