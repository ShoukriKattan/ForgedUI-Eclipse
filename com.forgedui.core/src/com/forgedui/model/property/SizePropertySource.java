package com.forgedui.model.property;


import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.forgedui.model.titanium.TitaniumUIBoundedElement;

public class SizePropertySource implements IPropertySource {
	
	protected TitaniumUIBoundedElement element;

	public static String ID_WIDTH = "width"; //$NON-NLS-1$
	public static String ID_HEIGHT = "height"; //$NON-NLS-1$
	protected static IPropertyDescriptor[] descriptors;
	
	static{
		descriptors = new IPropertyDescriptor[] {
			new BoundPropertyDescriptor(ID_WIDTH,ID_WIDTH, false),
			new BoundPropertyDescriptor(ID_HEIGHT,ID_HEIGHT, false),
		};
	}
	
	public SizePropertySource(TitaniumUIBoundedElement m_element){
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
	}
	
	@Override
	public String toString() {
		return "";
	}

}
