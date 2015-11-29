/**
 * 
 */
package com.forgedui.model.titanium;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.forgedui.model.Container;
import com.forgedui.model.Diagram;
import com.forgedui.model.Element;
import com.forgedui.model.property.EnumPropertyDescriptor;
import com.forgedui.model.property.MinmaxValidator;
import com.forgedui.model.property.NumberCellEditor;
import com.forgedui.model.property.NumberPropertyDescriptor;
import com.forgedui.model.titanium.TitaniumUIElement.EnumType;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;
import com.forgedui.util.Utils;

/**
 * @author shoukry This doesnt have background stuff
 */
public class Picker extends TitaniumUIBoundedElement implements Container {

	@Unmapped
	public static final String EXPAND_COLLAPSE = "expand_collapse";

	@Unmapped
	public static final String PICKER_TYPE = "pickerType";

	@Unmapped
	public static final String VALUE = "value";

	@Unmapped
	public static final String FORMAT24 = "format24";

	@Unmapped
	public static final String USE_SPINNER = "useSpinner";

	@Unmapped
	public static final String VISIBLE_ITEMS = "visibleItems";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * list of column values
	 */
	List<PickerColumn> columns = new ArrayList<PickerColumn>();

	/**
	 * the duration value in milliseconds for count down timer pickers. (Note
	 * that Titanium's Android implementation does not support the countdown
	 * timer.)
	 */
	Double countDownDuration;

	/**
	 * the locale used for displaying Date/Time pickers values
	 */
	String locale;

	/**
	 * I am not sure if this should become a java.util.date; and i am not sure
	 * its critital to have this. lets check with brandon the minimum Date/Time
	 * for value for date pickers
	 */
	Calendar minDate;

	/**
	 * I am not sure if this should become a java.util.date; and i am not sure
	 * its critital to have this. lets check with brandon the minimum Date/Time
	 * for value for date pickers
	 */
	Calendar maxDate;

	/**
	 * property to set the interval displayed by the minutes wheel (for example,
	 * 15 minutes). The interval value must be evenly divided into 60; if it is
	 * not, the default value is used. The default and minimum values are 1; the
	 * maximum value is 30. (Not currently supported on Android.)
	 */
	Integer minuteInterval;

	/**
	 * for basic picker, boolean value to indicate whether the visual selection
	 * style is shown. On the iPhone, this is a blue selected bar.
	 */
	Boolean selectionIndicator;

	/**
	 * the type constant for the picker. One of Titanium.UI.PICKER_TYPE_PLAIN
	 * (default), Titanium.UI.PICKER_TYPE_DATE_AND_TIME,
	 * Titanium.UI.PICKER_TYPE_DATE, Titanium.UI.PICKER_TYPE_TIME or
	 * Titanium.UI.PICKER_TYPE_COUNT_DOWN_TIMER. (Note that Titanium's Android
	 * implementation does not support the countdown timer or date+time
	 * varieties.)
	 * 
	 * 
	 * NOTE: Internally maps to INTEGER ENUM
	 */
	protected PickerType pickerType;

	/**
	 * (Android only, default false.) An indicator that you wish to use a
	 * non-native Android control that looks and behaves more like the iOS
	 * picker in the sense that the user selects values by spinning a wheel.
	 * (The native Android spinner is more like a conventional "dropdown".) Note
	 * that this option works both plain pickers, date pickers and time pickers.
	 * Set it preferably immediately when creating the picker, i.e.,
	 * Titanium.UI.createPicker({useSpinner:true});, but definitely before
	 * .add()ing the picker to its parent.
	 */
	@SupportedPlatform(platforms = { "android" })
	protected Boolean useSpinner;

	/**
	 * (Android only, default false.) If true, a 24-hour cloc will be used, with
	 * hours 0 through 23
	 */
	@SupportedPlatform(platforms = { "android" })
	protected Boolean format24;

	/**
	 * the Date/Time value for date pickers TODOD: Again check if this should
	 * become a date object
	 */
	protected Calendar value = Calendar.getInstance();

	/**
	 * (Android only.) This is relevant only if you set useSpinner to true, and
	 * it is relevant only for the plain picker (not date/time). By default, the
	 * spinner-style Android picker will show 5 rows: the one in the middle
	 * which is selected, and then 2 above and below. You can set this to allow
	 * more (use an odd number to be sure the selected row is in the middle.)
	 */
	@SupportedPlatform(platforms = { "android" })
	protected Integer visibleItems;

	@Unmapped
	protected boolean expanded;

	public Picker() {
		type = "Titanium.UI.Picker";
	}

	public void setColumnCount(int num) {
		for (int i = columns.size() - 1; i >= num; i--) {
			removeChild(columns.get(i));
		}
		for (int i = columns.size(); columns.size() < num; i++) {
			PickerColumn child = new PickerColumn();
			addChild(child);
			child.initDefaults();
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> fullList = new ArrayList<IPropertyDescriptor>();
		fullList.addAll(Arrays.asList(super.getPropertyDescriptors()));
		NumberPropertyDescriptor npd = new NumberPropertyDescriptor(PROPERTY_CHILDREN, "columnCount",
				NumberCellEditor.INTEGER, false);
		npd.setValidator(new MinmaxValidator(0, 10));
		fullList.add(npd);
		
		return fullList.toArray(new IPropertyDescriptor[fullList.size()]);
	}
	
	@Override
	protected ICellEditorValidator getFieldValidator(Field f) {
		if (VISIBLE_ITEMS.equals(f.getName())){
			return new MinmaxValidator(1, MinmaxValidator.MIN_ONLY);
		}
		return super.getFieldValidator(f);
	}

	@Override
	public void resetPropertyValue(Object id) {
		if (PROPERTY_CHILDREN.equals(id)) {
			setColumnCount(1);
		} else {
			super.resetPropertyValue(id);
		}
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (PROPERTY_CHILDREN.equals(propertyId)) {
			return "" + columns.size();
		}
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (PROPERTY_CHILDREN.equals(propertyId)) {
			if (canEdit()) {
				// On Android, the useSpinner property must be enabled to
				// support multiple columns.
				// http://docs.appcelerator.com/titanium/2.0/#!/api/Titanium.UI.PickerColumn
				if (!getPlatform().isAndroid()
						|| (getPlatform().isAndroid() && Utils.getObject(getUseSpinner(), false))) {
					setColumnCount((Integer) value);
				} else {
					setColumnCount(1);
				}
			}
		} else {
			super.setPropertyValue(propertyId, value);
		}
	}

	@Override
	public void addChild(Element child) {
		this.addChild(child, -1);
	}

	@Override
	public void addChild(Element child, int index) {
		child.setParent(this);
		if (index >= 0)
			columns.add(index, (PickerColumn) child);
		else
			columns.add((PickerColumn) child);
		listeners.fireIndexedPropertyChange(PROPERTY_CHILDREN, index, null, child);
	}

	public void moveChild(Element child, int toIndex) {
		int currentIndex = getChildren().indexOf(child);
		if (currentIndex >= 0 && toIndex < getChildren().size() && toIndex >= 0 && toIndex != currentIndex) {
			columns.remove(currentIndex);
			if (currentIndex < toIndex) {
				toIndex--;
			}
			columns.add(toIndex, (PickerColumn) child);
			listeners.fireIndexedPropertyChange(PROPERTY_CHILDREN, toIndex, null, child);
		}
	}

	@Override
	public void removeChild(Element oldColumn) {
		if (this.columns.remove(oldColumn)) {
			oldColumn.setParent(null);
			listeners.firePropertyChange(PROPERTY_CHILDREN, oldColumn, null);
		}
	}
	
	@Override
	public void initDefaults() {
		super.initDefaults();
		setColumnCount(1);
		Diagram d = getDiagram();
		if(d.getPlatform().isAndroid()){
			Dimension dim = d.getSupport().viewToModel(new Dimension(200, 40));
			setWidth(dim.width);
			setHeight(dim.height);
			setDimension(dim);
			setExpanded(false);
		}else
			setExpanded(true);
	}
	
	@Override
	public List<? extends PickerColumn> getChildren() {
		return Collections.unmodifiableList(columns);
	}

	/**
	 * @return the countDownDuration
	 */
	public Double getCountDownDuration() {
		return countDownDuration;
	}

	/**
	 * @param countDownDuration
	 *            the countDownDuration to set
	 */
	public void setCountDownDuration(Double countDownDuration) {
		this.countDownDuration = countDownDuration;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return the minDate
	 */
	public Calendar getMinDate() {
		return minDate;
	}

	/**
	 * @param minDate
	 *            the minDate to set
	 */
	public void setMinDate(Calendar minDate) {
		this.minDate = minDate;
	}

	/**
	 * @return the maxDate
	 */
	public Calendar getMaxDate() {
		return maxDate;
	}

	/**
	 * @param maxDate
	 *            the maxDate to set
	 */
	public void setMaxDate(Calendar maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * @return the minuteInterval
	 */
	public Integer getMinuteInterval() {
		return minuteInterval;
	}

	/**
	 * @param minuteInterval
	 *            the minuteInterval to set
	 */
	public void setMinuteInterval(Integer minuteInterval) {
		this.minuteInterval = minuteInterval;
	}

	/**
	 * @return the selectionIndicator
	 */
	public Boolean getSelectionIndicator() {
		return selectionIndicator;
	}

	/**
	 * @param selectionIndicator
	 *            the selectionIndicator to set
	 */
	public void setSelectionIndicator(Boolean selectionIndicator) {
		this.selectionIndicator = selectionIndicator;
	}

	/**
	 * @return the pickerType
	 */
	public PickerType getPickerType() {
		return pickerType;
	}

	/**
	 * @param pickerType
	 *            the pickerType to set
	 */
	public void setPickerType(PickerType pickerType) {
		PickerType old = this.pickerType;
		this.pickerType = pickerType;
		listeners.firePropertyChange(PICKER_TYPE, old, pickerType);
	}

	/**
	 * @return the useSpinner
	 */
	public Boolean getUseSpinner() {
		return useSpinner;
	}

	/**
	 * @param useSpinner
	 *            the useSpinner to set
	 */
	public void setUseSpinner(Boolean useSpinner) {
		Boolean old = this.useSpinner;
		this.useSpinner = useSpinner;
		listeners.firePropertyChange(USE_SPINNER, old, useSpinner);
	}

	/**
	 * @return the value
	 */
	public Calendar getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Calendar value) {
		Calendar old = this.value;
		this.value = value;
		listeners.firePropertyChange(VALUE, old, value);
	}

	/**
	 * @return the format24
	 */
	public Boolean getFormat24() {
		return format24;
	}

	/**
	 * @param format24
	 *            the format24 to set
	 */
	public void setFormat24(Boolean format24) {
		Boolean old = this.format24;
		this.format24 = format24;
		listeners.firePropertyChange(FORMAT24, old, format24);
	}

	/**
	 * @return the visibleItems
	 */
	public Integer getVisibleItems() {
		return visibleItems;
	}

	/**
	 * @param visibleItems
	 *            the visibleItems to set
	 */
	public void setVisibleItems(Integer visibleItems) {
		Integer old = this.visibleItems;
		this.visibleItems = visibleItems;
		listeners.firePropertyChange(VISIBLE_ITEMS, old, format24);
	}

	/**
	 * @return the expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * @param expanded
	 *            the expanded to set
	 */
	public void setExpanded(boolean expanded) {
		boolean old = this.expanded;
		this.expanded = expanded;
		listeners.firePropertyChange(EXPAND_COLLAPSE, old, expanded);
	}

	protected void handleProperty(ArrayList<IPropertyDescriptor> descriptors, String fieldName, Class<?> fieldType,
			Field f) {
		// For the alignment values.
		if (fieldName.equals(PICKER_TYPE)) {
			descriptors.add(new EnumPropertyDescriptor(f, fieldName, f.getType()) {

				@Override
				protected Object[] getEnumConstants(Class enumm) {
					return getFilteredPickerTypeValues().toArray();
				}
			});
		}
		super.handleProperty(descriptors, fieldName, fieldType, f);
	}

	protected List<PickerType> getFilteredPickerTypeValues() {
		List<PickerType> result = new ArrayList<PickerType>(Arrays.asList((PickerType.values())));
		if (getPlatform().isAndroid()) {
			result.remove(PickerType.PICKER_TYPE_COUNT_DOWN_TIMER);
			result.remove(PickerType.PICKER_TYPE_DATE_AND_TIME);
		}
		return result;
	}

	protected Object handlePropertyGetValue(String fieldName, Object value) {
		Object retValue = super.handlePropertyGetValue(fieldName, value);
		if (retValue != null)
			return retValue;
		if (fieldName.equals(PICKER_TYPE)) {
			return (pickerType == null) ? 0 : getFilteredPickerTypeValues().indexOf(pickerType) + 1;
		}

		return null;
	}

	protected Object handlePropertySetValue(String fieldName, Object value) {
		Object retValue = super.handlePropertySetValue(fieldName, value);
		if (retValue != null)
			return retValue;
		if (fieldName.equals(PICKER_TYPE)) {
			if (value instanceof Integer && (Integer) value > 0) {
				return getFilteredPickerTypeValues().get((Integer) value - 1);
			}
		}

		return null;
	}

	public boolean canEdit() {
		return PickerType.PICKER_TYPE_PLAIN == Utils.getObject(getPickerType(), PickerType.PICKER_TYPE_PLAIN);
	}
	
	public static enum PickerType implements EnumType{
		PICKER_TYPE_PLAIN,
		PICKER_TYPE_DATE_AND_TIME,
		PICKER_TYPE_DATE,
		PICKER_TYPE_TIME,
		PICKER_TYPE_COUNT_DOWN_TIMER;
		
		public String getQDN() {
			return "Titanium.UI." + super.toString();
		};
	}

}
