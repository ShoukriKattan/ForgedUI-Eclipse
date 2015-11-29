// LICENSE
package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.forgedui.model.property.NumberCellEditor;
import com.forgedui.model.property.NumberPropertyDescriptor;
import com.forgedui.model.titanium.TitaniumUIElement.EnumType;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class ProgressBar extends TitaniumUIElement {

	@Unmapped
	public static final String PROP_MIN = "min";
	@Unmapped
	public static final String PROP_MAX = "max";
	@Unmapped
	public static final String PROP_VALUE = "value";
	@Unmapped
	public static final String PROP_MESSAGE = "message";
	@Unmapped
	public static final String PROP_COLOR = "color";

	@Unmapped
	private static final long serialVersionUID = 1L;

	/**
	 * the color of the progress bar text
	 */
	private String color;

	/**
	 * the maximum value of the progress bar
	 */
	private Float max = new Float(100);

	/**
	 * the minimum value of the progress bar
	 */
	private Float min = new Float(0);

	/**
	 * the current value of the progress bar
	 */
	private Float value = new Float(0);

	/**
	 * the progress bar message
	 */
	private String message;

	/**
	 * the style of the progress bar
	 */
	@SupportedPlatform(platforms={"iphone", "ipad"})
	private ProgressBarStyle style;

	public ProgressBar() {

		type = "Titanium.UI.ProgressBar";
	}

	private transient ICellEditorValidator minValidator = new ICellEditorValidator() {

		@Override
		public String isValid(Object value) {
			if (value != null && max != null && (Float) value > max) {
				return "Min can't be bigger than max";
			}
			return null;
		}
	};

	private transient ICellEditorValidator maxValidator = new ICellEditorValidator() {
		@Override
		public String isValid(Object value) {
			if (value != null && min != null && (Float) value < min) {
				return "Max can't be smaller than min";
			}
			return null;
		}
	};

	private transient ICellEditorValidator valueValidator = new ICellEditorValidator() {
		@Override
		public String isValid(Object value) {
			if (value != null) {
				if (min != null && (Float) value < min) {
					return "Value can't be smaller than min";
				} else if (max != null && (Float) value > max) {
					return "Value can't be bigger than max";
				}
			}
			return null;
		}
	};

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		String old = this.color;
		this.color = color;
		listeners.firePropertyChange(PROP_COLOR, old, color);
	}

	/**
	 * @return the max
	 */
	public Float getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(Float max) {
		Float old = this.max;
		this.max = max;
		listeners.firePropertyChange(PROP_MAX, old, max);
	}

	/**
	 * @return the min
	 */
	public Float getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(Float min) {
		Float old = this.min;
		this.min = min;
		listeners.firePropertyChange(PROP_MIN, old, min);
	}

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Float value) {
		Float old = this.value;
		this.value = value;
		listeners.firePropertyChange(PROP_VALUE, old, value);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		String old = this.message;
		this.message = message;
		listeners.firePropertyChange(PROP_MESSAGE, old, message);
	}

	/**
	 * @return the style
	 */
	public ProgressBarStyle getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(ProgressBarStyle style) {
		this.style = style;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	protected boolean isColorFieldName(String fieldName) {
		if (PROP_COLOR.equals(fieldName)){
			return true;
		}
		return super.isColorFieldName(fieldName);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> fullList = new ArrayList<IPropertyDescriptor>();
		fullList.addAll(Arrays.asList(super.getPropertyDescriptors()));

		try {
			NumberPropertyDescriptor min = new NumberPropertyDescriptor(getClass().getDeclaredField("min"), "min",
					NumberCellEditor.FLOAT, true);
			min.setValidator(minValidator);
			fullList.add(min);

			NumberPropertyDescriptor max = new NumberPropertyDescriptor(getClass().getDeclaredField("max"), "max",
					NumberCellEditor.FLOAT, true);
			max.setValidator(maxValidator);
			fullList.add(max);

			NumberPropertyDescriptor value = new NumberPropertyDescriptor(getClass().getDeclaredField("value"),
					"value", NumberCellEditor.FLOAT, true);
			value.setValidator(valueValidator);
			fullList.add(value);

			/*NumberPropertyDescriptor width = new NumberPropertyDescriptor(PROP_WIDTH, PROP_WIDTH,
					NumberCellEditor.INTEGER, false);
			width.setValidator(new MinmaxValidator(0, Integer.MAX_VALUE));
			fullList.add(width);*/

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return fullList.toArray(new IPropertyDescriptor[fullList.size()]);
	}

	/*@Override
	public Object getPropertyValue(Object propertyId) {
		if (PROP_WIDTH.equals(propertyId)) {
			return getDimension().width;
		} else {
			return super.getPropertyValue(propertyId);
		}
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (PROP_WIDTH.equals(propertyId)) {
			Dimension size = getDimension().getCopy();
			size.width = Utils.getInt((Integer) value, 200);
			setDimension(size);
		} else {
			super.setPropertyValue(propertyId, value);
		}
	}*/

	public static enum ProgressBarStyle implements EnumType{
		DEFAULT,
		BAR,
		PLAIN;
		
		public String getQDN() {
			return "Titanium.UI.iPhone.ProgressBarStyle." + super.toString();
		};
	}
}
