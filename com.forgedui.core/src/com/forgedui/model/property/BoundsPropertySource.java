package com.forgedui.model.property;


import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.forgedui.model.titanium.TitaniumUIBoundedElement;

public class BoundsPropertySource implements IPropertySource {
	
	protected TitaniumUIBoundedElement element;

	public static String ID_WIDTH = "width"; //$NON-NLS-1$
	public static String ID_HEIGHT = "height"; //$NON-NLS-1$
	public static String ID_TOP = "top"; //$NON-NLS-1$
	public static String ID_BOTTOM = "bottom"; //$NON-NLS-1$
	public static String ID_LEFT = "left"; //$NON-NLS-1$
	public static String ID_RIGHT = "right"; //$NON-NLS-1$
	protected static IPropertyDescriptor[] descriptors;
	
	static{
		descriptors = new IPropertyDescriptor[] {
			new BoundPropertyDescriptor(ID_WIDTH,ID_WIDTH, false),
			new BoundPropertyDescriptor(ID_HEIGHT,ID_HEIGHT, false),
			new BoundPropertyDescriptor(ID_TOP,ID_TOP),
			new BoundPropertyDescriptor(ID_BOTTOM,ID_BOTTOM),
			new BoundPropertyDescriptor(ID_LEFT,ID_LEFT),
			new BoundPropertyDescriptor(ID_RIGHT,ID_RIGHT),
		};
	}
	
	public BoundsPropertySource(TitaniumUIBoundedElement m_element){
		element = m_element;
	}
	
	public Object getEditableValue(){
		return this;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors(){
		return descriptors;
	}
	
	public Object getPropertyValue(Object propName){
		if(ID_WIDTH.equals(propName)){
			return element.getWidth() == null ? "" : element.getWidth();
		}
		if(ID_HEIGHT.equals(propName)){
			return element.getHeight() == null ? "" : element.getHeight();
		}
		if(ID_TOP.equals(propName)){
			return element.getTop() == null ? "" : element.getTop();
		}
		if(ID_BOTTOM.equals(propName)){
			return element.getBottom() == null ? "" : element.getBottom();
		}
		if(ID_LEFT.equals(propName)){
			return element.getLeft() == null ? "" : element.getLeft();
		}
		if(ID_RIGHT.equals(propName)){
			return element.getRight() == null ? "" : element.getRight();
		}
		return null;
	}
	
	public boolean isPropertySet(Object propName){
		return !"".equals(getPropertyValue(propName));
	}
	
	public void resetPropertyValue(Object propName){
		setPropertyValue(propName, null);
	}
	
	public void setPropertyValue(Object propName, Object value){
		if (value != null && value.toString().trim().length() == 0){
			value = null;
		}
		String strValue = value != null ? value.toString() : null;
		if(ID_WIDTH.equals(propName)){
			element.setWidth(strValue);
		}
		if(ID_HEIGHT.equals(propName)){
			element.setHeight(strValue);
		}
		if(ID_TOP.equals(propName)){
			element.setTop(strValue);
		}
		if(ID_BOTTOM.equals(propName)){
			element.setBottom(strValue);
		}
		if(ID_LEFT.equals(propName)){
			element.setLeft(strValue);
		}
		if(ID_RIGHT.equals(propName)){
			element.setRight(strValue);
		}
	}
	
	@Override
	public String toString() {
		return "";
	}

}
