package com.forgedui.editor.generator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import org.eclipse.draw2d.geometry.Dimension;
import org.json.JSONException;
import org.json.JSONWriter;

import com.forgedui.editor.analytics.ReportingEventTypes;
import com.forgedui.editor.analytics.ReportingService;
import com.forgedui.model.Diagram;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.AlertDialog;
import com.forgedui.model.titanium.Label;
import com.forgedui.model.titanium.Label.Alignments;
import com.forgedui.model.titanium.OptionDialog;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.TitaniumUIElement.EnumType;
import com.forgedui.model.titanium.Window;
import com.forgedui.model.titanium.TitaniumUIElement.SoftKeyboardType;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.properties.AnchorPoint;
import com.forgedui.model.titanium.properties.AnimatedCenterPoint;
import com.forgedui.model.titanium.properties.BackgroundGradient;
import com.forgedui.model.titanium.properties.Center;
import com.forgedui.model.titanium.properties.Font;
import com.forgedui.model.titanium.properties.Point;
import com.forgedui.model.titanium.properties.ShadowOffset;
import com.forgedui.model.titanium.properties.Size;
import com.forgedui.model.titanium.properties.Transform;
import com.forgedui.util.Utils;

public abstract class AbstractTitaniumCodeGenerator implements CodeGenerator{

	protected List<String> generatedCodeLines;

	protected List<String> buildLog;

	protected List<String> generatedJSSLines;

	protected HashMap<String, Long> stats;

	public static final String LINE_SEP = System.getProperties().getProperty(
			"line.separator");

	protected Stack<TitaniumUIBaseElement> elementsStack;

	protected List<String> trackedVariables;

	protected Logger logger;
	protected ReportingService reportingService;
	protected boolean supportingJSS = false;
	
	
	public AbstractTitaniumCodeGenerator(Logger logger,
			ReportingService reportingSvc, boolean supportJSS) {

		this.generatedCodeLines = new ArrayList<String>();
		this.generatedJSSLines = new ArrayList<String>();
		this.buildLog = new ArrayList<String>();

		this.elementsStack = new Stack<TitaniumUIBaseElement>();
		this.trackedVariables = new ArrayList<String>();
		this.stats = new HashMap<String, Long>();

		this.logger = logger;
		this.reportingService = reportingSvc;

		this.supportingJSS = supportJSS;

	}

	protected boolean handleBasicProperty(Field f, JSONWriter propWriter,
			String fieldName, Class<?> fieldType, Object propertyValue,
			boolean isEnum) throws JSONException {

		boolean handled = false;

		// if field is a simple field
		if (String.class.equals(fieldType)) {

			String value = (String) propertyValue;

			propWriter.keyUnescaped(fieldName);
			
			// If property is enum or is only a numeric .. treat as a number			
			
			if (isEnum || Utils.isNumeric(value)) {

				propWriter.valueUnescaped(value);

			} else {
				
				//otherwise treat is as a String 
				

				propWriter.value(value);
			}

			handled = true;
		}

		if (Integer.class.equals(fieldType)) {

			Integer value = (Integer) propertyValue;

			propWriter.keyUnescaped(fieldName);
			propWriter.value(value.longValue());

			handled = true;
		}

		if (Float.class.equals(fieldType)) {

			// check float value range (FloatValueRange)
//			if (f.isAnnotationPresent(FloatValueRange.class)) {
//				FloatValueRange range = f.getAnnotation(FloatValueRange.class);//
//				float from = range.from();
//				float to = range.to();
//				// Do validation on the value
//			}

			Float value = (Float) propertyValue;

			propWriter.keyUnescaped(fieldName);

			propWriter.value(value.doubleValue());

			handled = true;
		}

		if (Boolean.class.equals(fieldType)) {

			Boolean val = (Boolean) propertyValue;

			propWriter.keyUnescaped(fieldName);
			propWriter.value(val.booleanValue());

			handled = true;
		}

		if (AnchorPoint.class.equals(fieldType)) {

			handled = true;
		}

		if (AnimatedCenterPoint.class.equals(fieldType)) {

			handled = true;
		}

		if (BackgroundGradient.class.equals(fieldType)) {

			handled = true;
		}

		if (Center.class.equals(fieldType)) {

			handled = true;
		}
		// then point
		if (Font.class.equals(fieldType)) {

			Font font = (Font) propertyValue;

			propWriter.keyUnescaped("font");

			propWriter.object();

			if (font.getFamily() != null) {
				propWriter.keyUnescaped("fontFamily");
				propWriter.value(font.getFamily());
			}

			if (font.getWeight() != null) {
				propWriter.keyUnescaped("fontWeight");
				propWriter.value(font.getWeight());
			}

			if (font.getSize() != null) {
				propWriter.keyUnescaped("fontSize");
				propWriter.valueUnescaped(font.getSize());
			}

			if (font.getStyle() != null) {

			}

			propWriter.endObject();

			handled = true;
		}

		if (Point.class.equals(fieldType)) {

			handled = true;
		}

		if (Size.class.equals(fieldType)) {

			handled = true;
		}

		if (Transform.class.equals(fieldType)) {
			handled = true;
		}

		if (ShadowOffset.class.equals(fieldType)) {
			handled = true;
		}

		if (String[].class.equals(fieldType)) {

			String[] value = (String[]) propertyValue;

			propWriter.keyUnescaped(fieldName);

			propWriter.array();

			if (isEnum) {

				for (String v : value) {

					propWriter.valueUnescaped(v);

				}

			} else {

				// Only if the value of this property is not null
				// this property is included

				for (String v : value) {

					propWriter.valueUnescaped(v);

				}

			}

			propWriter.endArray();

			handled = true;
		}

		if (Label.Alignments.class.isAssignableFrom(fieldType)) {

			Label.Alignments al = (Alignments) propertyValue;

			propWriter.keyUnescaped("textAlign").value("" + al.toString());

			handled = true;

		}

		if (EnumType.class.isAssignableFrom(fieldType)) {
			EnumType value = (EnumType) propertyValue;
			propWriter.keyUnescaped(fieldName);
			propWriter.valueUnescaped(value.getQDN());
			handled = true;
		}

		return handled;
	}
	
	public List<String> getBuildLog() {
		return buildLog;
	}

	public List<String> getGeneratedCodeLines() {
		return generatedCodeLines;
	}

	public List<String> getGeneratedJSSLines() {
		return generatedJSSLines;
	}

	protected void buildLog(String msg) {

		this.buildLog.add(msg);
	}

	/**
	 * 
	 * @param forPlatform
	 * @param f
	 * @return
	 */
	protected boolean isPlatformSupported(String forPlatform, Field f) {
		// By default its not supported

		boolean supported = false;

		// Until the annotation proves its supported
		SupportedPlatform platf = f.getAnnotation(SupportedPlatform.class);

		String[] supportedPlatforms = platf.platforms();

		for (String pla : supportedPlatforms) {

			if (forPlatform.equals(pla)) {
				supported = true;
				break;
			}
		}
		return supported;
	}

	/**
	 * @param element
	 * @param propWriter
	 * @throws JSONException
	 */
	protected void handleDialogsSpecialProperties(TitaniumUIBaseElement element, JSONWriter propWriter)
			throws JSONException {
				boolean isOptionsDialog = element instanceof OptionDialog;
				boolean isAlertDialog = element instanceof AlertDialog;
			
				if (isOptionsDialog || isAlertDialog) {
			
					// Both dialogs are containers
					TitaniumUIContainer container = (TitaniumUIContainer) element;
			
					List<? extends TitaniumUIBaseElement> children = container
							.getChildren();
			
					// Only if we have enough children
					if (children.size() > 0) {
			
						if (isOptionsDialog) {
							propWriter.keyUnescaped("options");
						}
			
						if (isAlertDialog) {
							propWriter.keyUnescaped("buttonNames");
						}
			
						// Add alert options
			
						propWriter.array();
			
						for (TitaniumUIBaseElement child : children) {
			
							String name = child.getName();
			
							propWriter.value(name);
			
						}
			
						propWriter.endArray();
			
					}
			
				}
			}

	/**
	 * @param platform
	 */
	protected void doReporting(Platform platform, String codeGenStyle) {
		// We are done ... lets send the statistics :
	
		String[] data = new String[(stats.size() * 2) + 2];
	
		int i = 0;
	
		for (Entry<String, Long> entry : stats.entrySet()) {
			data[i] = entry.getKey();
			data[i + 1] = entry.getValue() + "";
			i += 2;
		}
	
		data[stats.size()] = platform.toString();
		data[stats.size()+1] = codeGenStyle;
		reportingService.reportEvent(ReportingEventTypes.CODE_GENERATED,
				data);
	}

	/**
	 * 
	 * @param element
	 * @param propWriter
	 * @param fqdn
	 * @throws JSONException
	 */
	final public void handleSpecialElementProperties(
			TitaniumUIBaseElement element, JSONWriter propWriter, String fqdn)
			throws JSONException {
		
		if(element instanceof Window){
			Dimension dim = ((Diagram)((Screen)element.getParent()).getParent()).getResolution();
			if(dim.width > dim.height)
				propWriter.keyUnescaped(Window.ORIENTATION_MODES_PROP).valueUnescaped("["+ Window.ORIENTATION_MODE_LANDSCAPE_LEFT +"]");
		}
		
		handleSpecialElementPropertiesImp(element,propWriter,fqdn);
	}
	
	/**
	 * @param element
	 * @param propWriter
	 * @param fqdn
	 * @throws JSONException
	 */
	protected void handleSpecialElementPropertiesImp(
			TitaniumUIBaseElement element, JSONWriter propWriter, String fqdn)
			throws JSONException {
		
		handleDialogsSpecialProperties(element, propWriter);

		handleTableSpecialProperties(element, propWriter, fqdn);

		handleBarSpecialProperties(element, propWriter, fqdn);
		
		handleTabSpecialProperties(element, propWriter, fqdn);
		
		handleSplitWindowProperties(element, propWriter, fqdn);
		
	}
	
	/**
	 * @param element
	 * @param propWriter
	 * @param fqdn
	 * @throws JSONException
	 */
	protected void handleTableSpecialProperties(TitaniumUIBaseElement element, JSONWriter propWriter, String fqdn) throws JSONException {
	}

	/**
	 * @param element
	 * @param propWriter
	 * @param fqdn
	 * @throws JSONException
	 */
	protected void handleBarSpecialProperties(TitaniumUIBaseElement element, JSONWriter propWriter, String fqdn) throws JSONException {
	}
	
	/**
	 * @param element
	 * @param propWriter
	 * @param fqdn
	 * @throws JSONException
	 */
	protected void handleTabSpecialProperties(TitaniumUIBaseElement element, JSONWriter propWriter, String fqdn) throws JSONException {
	}
	
	/**
	 * @param element
	 * @param propWriter
	 * @param fqdn
	 * @throws JSONException
	 */
	protected void handleSplitWindowProperties(TitaniumUIBaseElement element, JSONWriter propWriter, String fqdn) throws JSONException {
	}
	
	protected static String getModuleName(TitaniumUIBaseElement baseContainer) {

		StringBuilder builder = new StringBuilder(baseContainer.getName());

		builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));

		return builder.toString();

	}

	protected static String getTabbedBarLabelsArrayOverrideMethod(
			TitaniumUIBaseElement element) {

		StringBuilder builder = capitalizeFirstLetter(element.getName());
		builder.insert(0, "get");
		builder.append("_labels");

		return builder.toString();

	}

	protected static String getTabbedBarLabelsArrayName(
			TitaniumUIBaseElement element) {

		String elementName = getVariableNameForElement(element);
		elementName = elementName + "_labels";

		return elementName;
	}

	protected static String getTableDataMethodName(TableView tv) {

		return "get" + tv.getName() + "_data";
	}

	protected static String getTableDataVariableName(TableView tv) {

		String tableElementName = getVariableNameForElement(tv);

		return tableElementName + "_data";

	}

	protected static String getVariableNameForElement(
			TitaniumUIBaseElement element) {
		StringBuilder builder = lowerizeFirstLetter(element.getName());
		return builder.toString();

	}

	protected static String getTitaniumObjectCreationMethod(
			TitaniumUIBaseElement element) {

		String type = element.getType();

		if (type == null) {
			throw new RuntimeException("Element " + element.getName()
					+ " with type " + element.getClass().getCanonicalName()
					+ " has null type");
		}

		// Titanium.UI.Window

		int lastIndexofDot = type.lastIndexOf('.');

		String firstPart = type.substring(0, lastIndexofDot);
		String lastPart = type.substring(lastIndexofDot + 1);

		return firstPart + ".create" + lastPart + "";
	}

	protected static String getExposedAttachMethodName(
			TitaniumUIBaseElement element) {
		StringBuilder builder = capitalizeFirstLetter(element.getName());
		builder.insert(0, "createAttachCh");
		return builder.toString();

	}

	protected static String getExposedCreationMethod(
			TitaniumUIBaseElement element) {
		StringBuilder builder = capitalizeFirstLetter(element.getName());
		builder.insert(0, "create");
		return builder.toString();
	}

	protected static String getJSSIDSelectorName(TitaniumUIBaseElement element) {
		return lowerizeFirstLetter(element.getName()).toString();
	}

	protected static StringBuilder lowerizeFirstLetter(String name) {
		StringBuilder builder = new StringBuilder(name);
		builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
		return builder;

	}

	protected static StringBuilder capitalizeFirstLetter(String name) {
		StringBuilder sb = new StringBuilder(name);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb;
	}

	protected static String escapeJSSStringValue(String val) {
		return "\"" + val + "\"";
	}
}