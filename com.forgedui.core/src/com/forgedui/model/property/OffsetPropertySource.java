package com.forgedui.model.property;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.forgedui.model.titanium.ScrollView;

/**
 * @author VY
 */
public class OffsetPropertySource implements IPropertySource {
	
	protected ScrollView sv;

	public static String ID_X = "x"; //$NON-NLS-1$
	public static String ID_Y = "y"; //$NON-NLS-1$
	protected static IPropertyDescriptor[] descriptors;
	
	static{
		descriptors = new IPropertyDescriptor[] {
			new NumberPropertyDescriptor(ID_X, ID_X, NumberCellEditor.INTEGER, false, new MinmaxValidator()),
			new NumberPropertyDescriptor(ID_Y, ID_Y, NumberCellEditor.INTEGER, false, new MinmaxValidator()),
		};
	}
	
	public OffsetPropertySource(ScrollView sv) {
		this.sv = sv;
	}
	
	public Object getEditableValue() {
		return this;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}
	
	public Object getPropertyValue(Object propName) {
		if (ID_X.equals(propName)) {
			return sv.getContentOffset() == null ? "" : Integer.toString(sv.getContentOffset().x);
		}
		if (ID_Y.equals(propName)) {
			return sv.getContentOffset() == null ? "" : Integer.toString(sv.getContentOffset().y);
		}
		return null;
	}
	
	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(Object)
	 */
	public boolean isPropertySet(Object propName) {
		return true;
	}
	
	public void resetPropertyValue(Object propName) {}
	
	public void setPropertyValue(Object propName, Object value) {
		if (value != null && value.toString().trim().length() == 0) {
			value = null;
		}
		Point pt = sv.getContentOffset();
		if (pt == null) {
			pt = new Point(0, 0);
		}
		if (ID_X.equals(propName)) {
			pt.x = getIntegerValue(value);
			sv.setContentOffset(pt);
		}
		if (ID_Y.equals(propName)) {
			pt.y = getIntegerValue(value);
			sv.setContentOffset(pt);
		}
	}
	
	protected Integer getIntegerValue(Object value) {
		if (value instanceof Integer) {
			return (Integer)value;
		}
		return	(value != null && !value.toString().isEmpty()) ? new Integer((String)value) : null;
	}
	
	protected String getStringValue(Float value) {
		return	(value == null) ? "" : value.toString();
	}
	
	public String toString() {
		return "";
	}

}
