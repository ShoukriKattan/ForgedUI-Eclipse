/**
 * 
 */
package com.forgedui.model.titanium;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.forgedui.core.ForgedUICorePlugin;
import com.forgedui.model.ElementImpl;
import com.forgedui.model.property.BooleanPropertyDescriptor;
import com.forgedui.model.property.ColorPropertyDescriptor2;
import com.forgedui.model.property.DateTimePropertyDescriptor;
import com.forgedui.model.property.EnumPropertyDescriptor;
import com.forgedui.model.property.FontPropertyDescriptor;
import com.forgedui.model.property.ImagePropertySource;
import com.forgedui.model.property.ImagePropertySourceTwo;
import com.forgedui.model.property.IntegerPropertyDescriptor;
import com.forgedui.model.property.MinmaxValidator;
import com.forgedui.model.property.NumberCellEditor;
import com.forgedui.model.property.NumberPropertyDescriptor;
import com.forgedui.model.property.StringArrayPropertyDescriptor;
import com.forgedui.model.titanium.annotations.FloatValueRange;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;
import com.forgedui.model.titanium.properties.AnchorPoint;
import com.forgedui.model.titanium.properties.AnimatedCenterPoint;
import com.forgedui.model.titanium.properties.BackgroundGradient;
import com.forgedui.model.titanium.properties.Center;
import com.forgedui.model.titanium.properties.Font;
import com.forgedui.model.titanium.properties.Point;
import com.forgedui.model.titanium.properties.ShadowOffset;
import com.forgedui.model.titanium.properties.Size;
import com.forgedui.model.titanium.properties.Transform;
import com.forgedui.util.Converter;
import com.forgedui.util.ModelPropertyMapper;
import com.forgedui.util.ReflectionUtil;

/**
 * @author shoukry
 * 
 */
public abstract class TitaniumUIBaseElement extends ElementImpl {

	/**
	 * 
	 */
	@Unmapped
	private static final long serialVersionUID = 1L;

	@Unmapped
	public static final String PROP_BOUNDS = "Bounds";

	/**
	 * Added by Shoukry: Each element has a type which is a convienience method.
	 * Instead of doing (instanceof) to check for element type the type property
	 * will give the type of the UI element involved this property will be used
	 * 
	 * Note: the type element is internal only should not be exposed in the
	 * properties editor
	 * 
	 * by the code generator
	 */
	@Unmapped
	protected String type;

	public final Set<String> getFilteredProperties() {
		return ModelPropertyMapper.getFilteredProperties(getClass());
	}

	public final Set<String> getHandlerProperties() {
		return ModelPropertyMapper.getHandlerProperties(getClass());
	}

	public final List<String> getJssProperties() {
		return ModelPropertyMapper.getJssProperties(getClass());
	}

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public boolean isJSSProperty(String propertyName) {
		return getJssProperties().contains(propertyName);
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		List<IPropertyDescriptor> fullList = new ArrayList<IPropertyDescriptor>();
		fullList.addAll(Arrays.asList(getPropertyDescriptorsForElement()));
		TextPropertyDescriptor nameDescr = new TextPropertyDescriptor(PROPERTY_NAME, "name");
		nameDescr.setValidator(getDiagram().getUniqueNameValidator(this));
		fullList.add(nameDescr);
		// now get all descriptors for all the properties that we have ...
		// Note : To do : We need some advanced collapsible editor

		return fullList.toArray(new IPropertyDescriptor[fullList.size()]);
	}

	/**
	 * 
	 * @param f
	 * @return whether the field is supported by the platform Ignore this field
	 *         do not generate a descriptior for it as its not supported by this
	 *         platform
	 */
	protected boolean isSupportedInPlatform(Field f) {
		// By default all properties are supported
		// but if a property has supported platform annotation
		if (f.isAnnotationPresent(SupportedPlatform.class)) {

			// By default its not supported

			boolean supported = false;

			// Until the annotation proves its supported
			SupportedPlatform platf = f.getAnnotation(SupportedPlatform.class);

			String forPlatform = getPlatform().name().toLowerCase();
			String[] supportedPlatforms = platf.platforms();

			for (String pla : supportedPlatforms) {

				if (forPlatform.equals(pla)) {
					supported = true;
					break;
				}
			}

			return supported;
		}
		return true;
	}

	private IPropertyDescriptor[] getPropertyDescriptorsForElement() {

		if (getDiagram() == null) {

			throw new RuntimeException("Cannot generate descriptors for element not attached to a Diagram");
		}

		List<Field> fields = getElementFields();

		ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		Set<String> handlerProperties = getHandlerProperties();

		for (Field f : fields) {

			if (Modifier.isStatic(f.getModifiers())) {
				// do not create property descriptors for static members
				continue;
			}

			try {

				String fieldName = f.getName();
				if (getFilteredProperties().contains(fieldName)) {
					continue;
				}

				if (!isSupportedInPlatform(f)) {
					continue;
				}

				Class<?> fieldType = f.getType();
				if (handlerProperties.contains(fieldName)) {
					handleProperty(descriptors, fieldName, fieldType, f);
					continue;
				}

				if (String.class.equals(fieldType)) {
					descriptors.add(new TextPropertyDescriptor(f, fieldName));
					continue;
				}
				
				ICellEditorValidator validator = getFieldValidator(f);
				
				if (Integer.class.equals(fieldType)) {
					descriptors.add(new IntegerPropertyDescriptor(f, fieldName, true, true));
					continue;
				}

				if (Float.class.equals(fieldType)) {
					NumberPropertyDescriptor descriptory = new NumberPropertyDescriptor(f, fieldName,
							NumberCellEditor.FLOAT, true);
					descriptory.setValidator(validator);
					descriptors.add(descriptory);
					continue;
				}

				if (Double.class.equals(fieldType)) {
					boolean rangeIn = false;
					NumberPropertyDescriptor descriptory = new NumberPropertyDescriptor(f, fieldName,
							NumberCellEditor.DOUBLE, true);
					descriptory.setValidator(validator);
					descriptors.add(descriptory);
					continue;
				}

				if (Boolean.class.equals(fieldType)) {
					descriptors.add(new BooleanPropertyDescriptor(f, fieldName, true));
					continue;
				}

				if (Calendar.class.equals(fieldType)) {
					descriptors.add(new DateTimePropertyDescriptor(f, fieldName, true, true));
					continue;
				}

				if (AnchorPoint.class.equals(fieldType)) {
					continue;
				}

				if (AnimatedCenterPoint.class.equals(fieldType)) {
					continue;
				}

				if (BackgroundGradient.class.equals(fieldType)) {
					continue;
				}

				if (Center.class.equals(fieldType)) {
					continue;
				}
				// then point
				if (Font.class.equals(fieldType)) {
					descriptors.add(new FontPropertyDescriptor(f, fieldName));
					continue;
				}

				if (Point.class.equals(fieldType)) {
					continue;
				}

				if (Size.class.equals(fieldType)) {
					// descriptors.add(new PropertyDescriptor(f, fieldName));
					continue;
				}

				if (Transform.class.equals(fieldType)) {
					continue;
				}

				if (ShadowOffset.class.equals(fieldType)) {
					continue;
				}

				if (String[].class.equals(fieldType)) {
					descriptors.add(new StringArrayPropertyDescriptor(f, fieldName));
					continue;
				}
				
				if (fieldType.isEnum()) {
					descriptors.add(new EnumPropertyDescriptor(f, fieldName, f.getType()));
					continue;
				}
				
				// Container is treated like the element
				// // Is this field of container type ?
				if (TitaniumUIContainer.class.isAssignableFrom(fieldType)) {
					continue;
				}

				if (TitaniumUIBaseElement.class.isAssignableFrom(fieldType)) {
					continue;
				}

				if (TitaniumUIBaseElement[].class.isAssignableFrom(fieldType)) {

					// Lets discuss this
					continue;

				}

			} catch (IllegalArgumentException e) {

			}

		}

		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}
	
	protected ICellEditorValidator getFieldValidator(Field f){
		if (f.isAnnotationPresent(FloatValueRange.class)) {
			FloatValueRange valueRange = f.getAnnotation(FloatValueRange.class);
			float minValue = valueRange.from();
			float maxValue = valueRange.to();
			String type = valueRange.type();
			MinmaxValidator minmaxValidator = null;
			if (MinmaxValidator.MIN_ONLY.equals(type)){
				minmaxValidator = new MinmaxValidator(minValue, MinmaxValidator.MIN_ONLY);
			} else if (MinmaxValidator.MAX_ONLY.equals(type)){
				minmaxValidator = new MinmaxValidator(minValue, MinmaxValidator.MAX_ONLY);
			} else {
				minmaxValidator = new MinmaxValidator(minValue, maxValue);
			}
			return minmaxValidator;
		}
		return null;
	}

	protected boolean isColorFieldName(String fieldName) {
		return false;
	}

	protected boolean isImageFieldName(String fieldName) {
		return false;
	}

	/**
	 * Just another method to add the new property descriptor for the fields
	 * that will have the url image as well.
	 * 
	 * @param fieldName
	 * @return
	 */
	protected boolean isCombinedImageFieldName(String fieldName) {
		return false;
	}

	// Those are for the new elements that we want to change there colors stuff
	// Those are mainly for the background colors of the TUIElement.
	protected void handleProperty(ArrayList<IPropertyDescriptor> descriptors, String fieldName, Class<?> fieldType,
			Field f) {
		if (isColorFieldName(fieldName)) {
			descriptors.add(new ColorPropertyDescriptor2(f, fieldName));
		} else if (isImageFieldName(fieldName)) {
			descriptors.add(new ImagePropertySource(f, fieldName));
		} else if (isCombinedImageFieldName(fieldName)) {
			descriptors.add(new ImagePropertySourceTwo(f, fieldName));
		}
	}

	protected Object handlePropertyGetValue(String fieldName, Object value) {
		if (isColorFieldName(fieldName)) {
			if (value != null) {
				String cValue = (String) value;
				if (cValue.length() > 0) {
					return Converter.getColorFromHexa((String) value).getRGB();
				}
			}
		} else if (isImageFieldName(fieldName) || isCombinedImageFieldName(fieldName)) {
			return (String) value;
		}
		return null;
	}

	protected Object handlePropertySetValue(String fieldName, Object value) {
		if (isColorFieldName(fieldName)) {
			if (value != null && value instanceof RGB) {
				return Converter.getHexColorValue((RGB) value);
			} else if (value != null && value instanceof String) {
				return Converter.getHexColorValue((RGB) value);
			}
		} else if (isImageFieldName(fieldName) || isCombinedImageFieldName(fieldName)) {
			return (String) value;
		}
		return null;
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (propertyId instanceof Field) {
			Field f = (Field) propertyId;
			Class<?> fieldType = f.getType();
			Object value;
			try {
				f.setAccessible(true);
				value = f.get(this);
				// FIXME process fields which don't look pretty
				// We have to make sure not to pass only a string !.
				if (value instanceof Font)
					return value;

				if (value instanceof Calendar)
					return value;

				if (getHandlerProperties().contains(f.getName())) {
					return handlePropertyGetValue(f.getName(), value);
				}
				
				if (fieldType.isEnum()){
					return (value == null) ? 0 : Arrays.asList(fieldType.getEnumConstants()).indexOf(value) + 1;
				}

				// There is a special handling of the boolean property here.
				if (Boolean.class.equals(fieldType)) {
					return Converter.getComboValue(value);
				} else if (String[].class.equals(fieldType)) {
					return value;
				}
				return value != null ? value.toString() : "";
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return "";
		}
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		try {
			if (propertyId instanceof Field) {
				Object oldValue = getPropertyValue(propertyId);
				Field f = (Field) propertyId;
				// Do the conversion of from font data to font.
				if (value instanceof FontData) {
					value = Converter.getFontFromFontData((FontData) value);
				}

				if (getHandlerProperties().contains(f.getName())) {
					value = handlePropertySetValue(f.getName(), value);
				}
				
				Method setter = ReflectionUtil.getSetter(this.getClass(), f);
				if (setter != null) {
					if (value == null || value.toString().isEmpty()){
						ReflectionUtil.executeMethod(this, setter, new Object[]{null});
					} else {
						if (f.getType().isEnum()){
							//default value is present here!!!
							if (value instanceof Integer && (Integer) value > 0) {
								value = f.getType().getEnumConstants()[(Integer)value - 1];
							}
						} else if (Font.class.equals(f.getType())) {
							if (value == null || value.toString().isEmpty()) {
								ReflectionUtil.executeMethod(this, setter, value);
								// Just to notify, we must thing in a different way for the
								// properties here.
								listeners.firePropertyChange("Font", oldValue, value);
								return;
							}
						} else if (Float.class.equals(f.getType())) {
							value = Float.parseFloat(value.toString());
						} else if (Double.class.equals(f.getType())) {
							value = Double.parseDouble(value.toString());
						} else if (Boolean.class.equals(f.getType())) {
							value = Converter.getBooleanValue(value);
						} else if (Integer.class.equals(f.getType())) {
							value = Integer.parseInt(value.toString());
						}
						ReflectionUtil.executeMethod(this, setter, value);
					}
				} else {
					System.err.println("No setter found for " + f.getName() + " property");
				}
			} else {
				super.setPropertyValue(propertyId, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorDialog.openError(null, "Error", e.getMessage(), new Status(IStatus.ERROR,
					ForgedUICorePlugin.PLUGIN_ID, "No setter implemented"));
		}
	}
	
	public TitaniumUIBaseElement getCopy() throws InstantiationException, IllegalAccessException {
		TitaniumUIBaseElement copy = getClass().newInstance();
		
		List<Field> fields = getElementFields();

		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers())) {
				// do not copy static members
				continue;
			}

			if ("name".equals(f.getName())) {
				continue;
			}

			f.setAccessible(true);

			Object value = f.get(this);
			if (value == null) {
				continue;
			}

			Class<?> fieldType = f.getType();
			Object newValue = null;

			if (value instanceof Point) {
				newValue = fieldType.newInstance();
				((Point) newValue).init((Point) value);
			} else if (String.class.equals(fieldType)) {
				newValue = new String((String) value);
			} else if (Integer.class.equals(fieldType)) {
				newValue = new Integer((Integer) value);
			} else if (Float.class.equals(fieldType)) {
				newValue = new Float((Float) value);
			} else if (Double.class.equals(fieldType)) {
				newValue = new Double((Double) value);
			} else if (Boolean.class.equals(fieldType)) {
				newValue = new Boolean((Boolean) value);
			} else if (BackgroundGradient.class.equals(fieldType)) {

			} else if (Font.class.equals(fieldType)) {
				newValue = new Font();
				((Font) newValue).init((Font) value);
			} else if (Size.class.equals(fieldType)) {
				newValue = new Size((Size) value);
			} else if (Transform.class.equals(fieldType)) {

			} else if (ShadowOffset.class.equals(fieldType)) {

			} else if (String[].class.equals(fieldType)) {
				newValue = new String[((String[]) value).length];
			} else if (TitaniumUIContainer.class.isAssignableFrom(fieldType)) {
				// FIXME
			} else if (TitaniumUIBaseElement.class.isAssignableFrom(fieldType)) {
				// FIXME
			} else if (TitaniumUIBaseElement[].class.isAssignableFrom(fieldType)) {
				// FIXME
			}

			if (newValue != null) {
				Method setter = ReflectionUtil.getSetter(this.getClass(), f);
				ReflectionUtil.executeMethod(copy, setter, newValue);
			}
		}
		return copy;
	}

	/**
	 * Quick reflection method which goes through all element properties and get
	 * the reflection fields
	 * 
	 * Note: Fields in the Inheritance hierarchy above This element are IGNORED
	 * !!!
	 * 
	 * @return
	 */
	public List<Field> getElementFields() {

		return ReflectionUtil.getObjectFields(this, TitaniumUIBaseElement.class);

	}

}
