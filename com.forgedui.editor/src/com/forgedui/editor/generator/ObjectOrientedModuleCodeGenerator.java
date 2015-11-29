/**
 * 
 */
package com.forgedui.editor.generator;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONWriter;

import com.forgedui.editor.analytics.ReportingService;
import com.forgedui.model.titanium.AlertDialog;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.OptionDialog;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.TabbedBar;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.annotations.Composite;
import com.forgedui.model.titanium.annotations.EnumValues;
import com.forgedui.model.titanium.annotations.Reference;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @author shoukry
 * 
 * 
 * 
 *         TODO: Handle window case and properties of the title bar which need
 *         to be merged into window
 * 
 * 
 *         When do we generate app.js and link its windows, etc...
 * 
 *         TODO: Support className property .. Dmitry to also support that
 * 
 */
public class ObjectOrientedModuleCodeGenerator extends
		AbstractTitaniumCodeGenerator implements CodeGenerator {

	/**
	 * 
	 */
	public ObjectOrientedModuleCodeGenerator(Logger logger,
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
					.add("//To start using this generated code .. simply include this file in your app.js");
			generatedCodeLines
					.add("//Ti.include(\"your generated window file.js goes here\");");
			generatedCodeLines
					.add("//To create your generated window and get a reference to it do: ");
			generatedCodeLines
					.add("//var module = new FUI.window1(); This line instantiates the module ");
			generatedCodeLines
					.add("//module.createAttachChWindow1(); This line instantiates the window and attaches all child components to it");

			generatedCodeLines
					.add("//module.window1; this line returns the actual window instance");

			generatedCodeLines
					.add("//If a window has large views. Remember to destroy these views if they are no longer needed. FUI.windowModule.view=null");

			// Before we start we might want to link some definition files
			// .etc...

			// (function(){})()

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

			for (String varName : trackedVariables) {

				String variableQualifyingName = fqdn + "prototype.";

				generatedCodeLines.add(10, "\t" + variableQualifyingName
						+ varName + " = null ;");

			}

			doReporting(platform, "OO_CODE_GEN");

		} catch (Exception e) {

			logger.logError("Error while generating code", e);

			throw e;

		}

	}

	/**
	 * 
	 * TODO: app.js selection strategy... How do we choose what is app.js
	 * 
	 * NOTE : Anything which should have a default value The default value
	 * should be assigned in the class upon instantiation. It should be
	 * recursive as its a tree:
	 */
	protected void generateCode(TitaniumUIBaseElement element,

	String forPlatform, String fqdn) {

		// Logging

		String classs = element.getClass().toString();
		Long current = stats.get(classs) != null ? stats.get(classs) + 1 : 0;

		stats.put(classs, current);
		// adding the stat

		// This element (Window , panel, etc...) name
		String thisVariableName = getVariableNameForElement(element);

		trackedVariables.add(thisVariableName);

		String exposedCreationMethod = getExposedCreationMethod(element);

		String thisObjectTitaniumCreationMethod = getTitaniumObjectCreationMethod(element);

		// String forPlatform = "iphone";

		generatedCodeLines.add("\t" + fqdn + "prototype."
				+ exposedCreationMethod + " = function(){");

		// get all those properties and types

		// create the element then find the fields and attach them.

		StringWriter sw = new StringWriter();

		JSONWriter propWriter = new JSONWriter(sw);

		// You need to begin object properties and write them one by one. But if

		generateElementProperties(element, forPlatform, propWriter, fqdn);

		generatedCodeLines
				.add("\t\tthis." + thisVariableName + " = "
						+ thisObjectTitaniumCreationMethod + "("
						+ sw.toString() + ");");

		generatedCodeLines.add("\t};");

		createAttachFunction(element, fqdn);

	}

	protected void createAttachFunction(TitaniumUIBaseElement element,
			String fqdn) {

		boolean handled = false;

		if (!handled
				&& (element instanceof TabbedBar
						|| element instanceof ButtonBar || element instanceof TableView)) {

			handled = true;
		}

		if (element instanceof OptionDialog || element instanceof AlertDialog) {

			// To do, add options here

			handled = true;
		}

		// If we dont have a table view ... do we have a normal container?
		// A normal container is a view and a tableViewRow .. which can have
		// other elements
		if (!handled
				&& (element instanceof TitaniumUIContainer || element instanceof TabGroup)) {

			String parentVarName = getVariableNameForElement(element);

			// Create the attachment function
			String attachFunctionName = getExposedAttachMethodName(element);

			generatedCodeLines.add("\t " + fqdn + "prototype."
					+ attachFunctionName + " = function(){");

			String exposedCreationMethod = getExposedCreationMethod(element);

			generatedCodeLines.add("\t\tthis." + exposedCreationMethod + "();");

			// If this is a container ... then it has children
			// The list of children

			String childAdditionToParentMethod = "add";

			List<? extends TitaniumUIBaseElement> children = null;

			if (element instanceof TitaniumUIContainer) {

				TitaniumUIContainer container = (TitaniumUIContainer) element;

				children = container.getChildren();

			} else {
				if (element instanceof TabGroup) {

					TabGroup tg = (TabGroup) element;

					children = tg.getTabs();

					childAdditionToParentMethod = "addTab";
				}
			}

			if (children != null) {

				for (TitaniumUIBaseElement child : children) {

					// generateCode(child, false, false);

					String childVarName = getVariableNameForElement(child);

					// No tracking required. this makes it tracked twice
					// trackedVariables.add(childVarName);

					String createOrCreateAndAttachMethodName = child instanceof TitaniumUIContainer
							&& !(child instanceof TableView
									|| child instanceof TabbedBar || child instanceof ButtonBar) ? getExposedAttachMethodName(child)
							: getExposedCreationMethod(child);

					generatedCodeLines.add("\t\tthis."
							+ createOrCreateAndAttachMethodName + "();");

					// Push to stack

					elementsStack.push(child);

					// add the variable and methods and all
					generatedCodeLines.add("\t\tthis." + parentVarName + "."
							+ childAdditionToParentMethod + "(this."
							+ childVarName + ");");

				}

				generatedCodeLines.add("\t};");

			}

		}
	}

	/**
	 * 
	 * Even if property is an array of children it is still handled here
	 * 
	 * @param element
	 * @param thisVariableName
	 * @param thisVariableType
	 * @param forPlatform
	 * @param clazz
	 * @param propWriter
	 */
	protected void generateElementProperties(TitaniumUIBaseElement element,
			String forPlatform, JSONWriter propWriter, String fqdn) {

		try {
			propWriter.object();
		} catch (JSONException e) {
		}

		HashMap<String, String> jssProperties = new HashMap<String, String>();

		String jssSelector = null;

		String thisVariableName = element.getName();
		String thisVariableType = element.getType();

		// there are variables
		// which are objects. we need to separate their creation from their
		// assignment in this Object properties.
		// Also if there are advanced objects (Such as other UI elements and
		// containers) which also need to be created before being assigned.
		// We have to delcare them and create them before.
		// Therefore we need to work backwards.

		// Now the question is : do we declare the creation of each one of them
		// as a separate function or what?
		// Or create them Monolothically
		// Or decalre a create method now and implement it later

		List<Field> fields = element.getElementFields();

		for (Field f : fields) {

			// If the property is annotated as Unmapped. Skip this property
			if (f.isAnnotationPresent(Unmapped.class))
				continue;

			// If the property is annotated as Reference. Skip this property and it will be handled later.
			if (f.isAnnotationPresent(Reference.class))
				continue;
			
			try {
				// We need to access field value anyways
				f.setAccessible(true);

				// This field's name
				String fieldName = f.getName();

				// check field type
				Class<?> fieldType = f.getType();

				// By default all properties are supported

				// but if a property has supported platform annotation
				if (f.isAnnotationPresent(SupportedPlatform.class)
						&& !isPlatformSupported(forPlatform, f)) {

					buildLog("["
							+ thisVariableName
							+ "] of type ["
							+ thisVariableType
							+ "] property ["
							+ fieldName
							+ "] should not be mapped. Not supported for this platform");

					continue;

				}

				Object propertyValue = f.get(element);

				// The property value is null. Property should be ignored
				// anyways
				if (propertyValue == null)
					continue;

				// Determine if property is composite and proceed
				boolean composite = f.isAnnotationPresent(Composite.class);

				boolean jssProperty = supportingJSS
						&& element.isJSSProperty(fieldName);

				boolean isEnum = f.getAnnotation(EnumValues.class) != null;

				if (jssProperty) {
					// Assumes JSS Properties are Natives or String-able
					jssProperties
							.put(fieldName,
									String.class.equals(fieldType) && !isEnum ? escapeJSSStringValue(propertyValue
											.toString()) : propertyValue
											.toString());

					continue;
				}

				// Basic properties include Integers (height / width / fonts /
				// borders / etc... )
				if (handleBasicProperty(f, propWriter, fieldName, fieldType,
						propertyValue, isEnum)) {
					continue;
				}

				// If field is another element maybe that element should be
				// created
				// before
				if (TitaniumUIBaseElement.class.isAssignableFrom(fieldType)) {

					// Alright so this is a UI element that we have to create :
					// lets create a function which does that
					// And now lets just call that function

					// This line has to be the line before any other line (The
					// assignment line)

					// This field name

					// this is the field .. we need to set this field's name
					// right?
					// yes so lets do it ...

					TitaniumUIBaseElement childElement = (TitaniumUIBaseElement) propertyValue;

					if (childElement != null) {

						if (composite) {

							String msg = ("[" + fieldName + "] of type ["
									+ fieldType + "] is composite");

							buildLog(msg);

							// Thus it should not be added as an element
							// Its properties need to be navigated in the same
							// way as others
							// add added to the same obj props

							generateElementProperties(childElement,
									forPlatform, propWriter, fqdn);
							continue;

						} else {

							// This is a standard element which has to be
							// created in a standard way
							String localVariableName = getVariableNameForElement(childElement);

							trackedVariables.add(localVariableName);

							String methodName = getExposedCreationMethod(childElement);

							// prepare the line which calls the method and
							// assings
							// the
							// variable
							// local var
							generatedCodeLines.add("\t\tthis." + methodName
									+ "();");

							// add its a property which should be aded

							propWriter.keyUnescaped(fieldName).valueUnescaped(
									"this." + localVariableName);

							this.elementsStack.push(childElement);

						}

						String msg = ("This field is a UIElement " + fieldType
								+ " " + fieldName);

						buildLog(msg);
					}

					continue;
				}

				if (TitaniumUIBaseElement[].class.isAssignableFrom(fieldType)) {

					continue;

				}

				// If there is a property which is a container
				// of other TitaniumUIElements or containers
				// Special handling

				// If its the field of the container which holds children
				// otherwize its a complex property.
				// we have a limited set of complex properties

				String msg = ("Field [" + fieldName + "] of type ["
						+ fieldType.getCanonicalName() + "] in Variable ["
						+ thisVariableName + "] of type [" + thisVariableType + "] has not been parsed");

				buildLog(msg);

			} catch (Exception e) {
				buildLog("Error while handling element property Element:"
						+ element.getName() + " Property:" + f.getName());
			}

		}

		try {
			handleSpecialElementProperties(element, propWriter, fqdn);
		} catch (Exception e) {
			// Simply Ignore the property meanwhile also log it
			buildLog("Error while special handling element properties "
					+ element.getName() + "" + e.toString());
		}

		if (supportingJSS) {

			if (jssSelector == null) {

				String elementJSSID = getJSSIDSelectorName(element);
				jssSelector = "#" + elementJSSID;

				// Also add the extra property

				try {

					propWriter.keyUnescaped("id");
					propWriter.value(elementJSSID);

				} catch (JSONException e) {

				}

			}// Else JSS Selector is not null which means we encountered it as a
				// property and added it to the Element properties not to the
				// JSS properties

			// generatedJSSLines.add(jssSelector + " "+ jssBuilder.toString());

		}

		try {
			propWriter.endObject();
		} catch (JSONException e) {

		}

	}

	/**
	 * @param element
	 * @param propWriter
	 * @param fqdn
	 * @throws JSONException
	 */
	protected void handleBarSpecialProperties(TitaniumUIBaseElement element,
			JSONWriter propWriter, String fqdn) throws JSONException {
		if (element instanceof TabbedBar || element instanceof ButtonBar) {

			String tabBarLabelsArrayName = getTabbedBarLabelsArrayName(element);

			trackedVariables.add(tabBarLabelsArrayName);

			TitaniumUIContainer container = (TitaniumUIContainer) element;

			StringWriter sw = new StringWriter();
			JSONWriter jsonWriter = new JSONWriter(sw);

			jsonWriter.array();

			for (int i = 0; i < container.getChildren().size(); i++) {

				TabbedBarButton buttonChild = (TabbedBarButton) container
						.getChildren().get(i);

				jsonWriter.object();

				if (buttonChild.getEnabled() != null) {
					jsonWriter.keyUnescaped("enabled").value(
							buttonChild.getEnabled());
				}

				if (buttonChild.getSelected() != null) {
					jsonWriter.keyUnescaped("selected").value(
							buttonChild.getSelected());
				}

				if (buttonChild.getWidth() != null) {
					jsonWriter.keyUnescaped("width").value(
							buttonChild.getWidth());
				}

				if (buttonChild.getImage() != null) {
					jsonWriter.keyUnescaped("image").value(
							buttonChild.getImage());
				}

				if (buttonChild.getTitle() != null) {
					jsonWriter.keyUnescaped("title").value(
							buttonChild.getTitle());
				}

				jsonWriter.endObject();

			}

			jsonWriter.endArray();

			String getTabBarMethodName = getTabbedBarLabelsArrayOverrideMethod(element);

			generatedCodeLines
					.add("\t\t//Note: This is an extension point. To override the Buttons you will need to define the function "
							+ fqdn + getTabBarMethodName);
			generatedCodeLines.add("\t\tif(this." + getTabBarMethodName + "){");
			generatedCodeLines.add("\t\t\tthis." + tabBarLabelsArrayName
					+ " = this." + getTabBarMethodName + "();");
			generatedCodeLines.add("\t\t}else{");

			generatedCodeLines.add("\t\t\tthis." + tabBarLabelsArrayName
					+ " = " + sw.toString() + ";");

			generatedCodeLines.add("\t\t}");

			propWriter.keyUnescaped("labels").valueUnescaped(
					"this." + tabBarLabelsArrayName);

		}
	}

	/**
	 * @param element
	 * @param propWriter
	 * @param fqdn
	 * @throws JSONException
	 */
	protected void handleTableSpecialProperties(TitaniumUIBaseElement element,
			JSONWriter propWriter, String fqdn) throws JSONException {
		if (element instanceof TableView) {

			// Note : Data is a propety for Table
			// But now we have a special type (Array) and this array is
			// special
			// is that its not a UI component.. but an array of UI
			// components..

			// Define the array type ... should the array have a separate
			// function? maybe
			// TODO: This code needs to be cleaned up well.

			// So before the array property .. we need array defined :

			// Table view has data attribute .. data attribute is an array.

			TableView tv = (TableView) element;

			String dataArrayName = getTableDataVariableName(tv);

			trackedVariables.add(dataArrayName);

			String extensionMethodName = getTableDataMethodName(tv);
			generatedCodeLines
					.add("\t\t// Note: This is an extension point. To override the static table data define "
							+ fqdn + extensionMethodName + " function.");
			generatedCodeLines.add("\t\tif(this." + extensionMethodName + "){");
			generatedCodeLines.add("\t\t\tthis." + dataArrayName + " = this."
					+ extensionMethodName + "();");
			generatedCodeLines.add("\t\t}else{");

			// var table1Data=[10];
			generatedCodeLines.add("\t\t\tthis." + dataArrayName + " = ["
					+ tv.getChildren().size() + "];");
			// Next we need to iterate through them and define them here
			// before
			// its too late .. but each one of them needs its own functions
			// ..
			// Because it might be complex.

			for (int i = 0; i < tv.getChildren().size(); i++) {

				TitaniumUIBaseElement tableChild = tv.getChildren().get(i);

				// This table child might be a TableViewRow or
				// TableViewSection...
				// Anyways it needs its own creation function but
				// meanwhile...

				String varName = getVariableNameForElement(tableChild);

				// lets prepare and assign ..
				// String localCreationMethodName =
				// getExposedCreationMethod(tableChild);

				String createAttachMethod = getExposedAttachMethodName(tableChild);

				generatedCodeLines.add("\t\t\tthis." + createAttachMethod
						+ "();");

				// data[i]=createRow1 or createSection1 .
				generatedCodeLines.add("\t\t\tthis." + dataArrayName + "[" + i
						+ "] = this." + varName + ";");

				this.elementsStack.push(tableChild);

			}

			generatedCodeLines.add("\t\t}");

			// data=tabl1Data;
			// Finally define this nice variable.
			propWriter.keyUnescaped("data").valueUnescaped(
					"this." + dataArrayName);

		}
	}

}
