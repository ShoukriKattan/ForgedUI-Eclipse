package com.forgedui.model.property;


import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.forgedui.model.Element;
import com.forgedui.model.ElementImpl;

public class DimensionPropertySource implements IPropertySource {
	
	protected Element element;
	
	protected Dimension point = null;

	public static String ID_WIDTH = "Width"; //$NON-NLS-1$
	public static String ID_HEIGHT = "Height"; //$NON-NLS-1$
	protected static IPropertyDescriptor[] descriptors;
	
	static{
		descriptors = new IPropertyDescriptor[] {
			new IntegerPropertyDescriptor(ID_WIDTH,"Width"),
			new IntegerPropertyDescriptor(ID_HEIGHT,"Height")
		};
	}
	
	public DimensionPropertySource(ElementImpl m_element){
		element = m_element;
		this.point = new Dimension(m_element.getDimension());
	}
	
	public Object getEditableValue(){
		return this;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors(){
		return descriptors;
	}
	
	public Object getPropertyValue(Object propName){
		if(ID_WIDTH.equals(propName)){
			return new String(new Integer(point.width).toString());
		}
		if(ID_HEIGHT.equals(propName)){
			return new String(new Integer(point.height).toString());
		}
		return null;
	}
	
	public Dimension getValue(){
		return new Dimension(point.width, point.height);
	}
	
	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(Object)
	 */
	public boolean isPropertySet(Object propName){
		if(ID_WIDTH.equals(propName) || ID_HEIGHT.equals(propName))return true;
		return false;
	}
	
	public void resetPropertyValue(Object propName){}
	
	public void setPropertyValue(Object propName, Object value){
		if(ID_WIDTH.equals(propName)){
			Integer newInt = new Integer((String)value);
			point.width = newInt.intValue();
		}
		if(ID_HEIGHT.equals(propName)){
			Integer newInt = new Integer((String)value);
			point.height = newInt.intValue();
		}
	}
	
	public String toString(){
		return "{" + point.width + ", " + point.height + "}";
	}
	

}
