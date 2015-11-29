// LICENSE
package com.forgedui.model.property;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.forgedui.model.Element;
import com.forgedui.model.ElementImpl;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class LocationPropertySource implements IPropertySource {
	
	protected Element element;
	
	protected Point point = null;

	public static String ID_X = "X"; //$NON-NLS-1$
	public static String ID_Y = "Y"; //$NON-NLS-1$
	protected static IPropertyDescriptor[] descriptors;
	
	static{
		descriptors = new IPropertyDescriptor[] {
			new IntegerPropertyDescriptor(ID_X,"X", true, true),
			new IntegerPropertyDescriptor(ID_Y,"Y", true, true)
		};
	}
		
	public LocationPropertySource(ElementImpl m_element){
		element = m_element;
		this.point = new Point(m_element.getLocation());
	}
	
	public Object getEditableValue(){
		return this;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors(){
		return descriptors;
	}
	
	public Object getPropertyValue(Object propName){
		if(ID_X.equals(propName)){
			return new String(new Integer(point.x).toString());
		}
		if(ID_Y.equals(propName)){
			return new String(new Integer(point.y).toString());
		}
		return null;
	}
	
	public Point getValue(){
		return new Point(point.x, point.y);
	}
	
	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(Object)
	 */
	public boolean isPropertySet(Object propName){
		if(ID_X.equals(propName) || ID_Y.equals(propName))return true;
		return false;
	}
	
	public void resetPropertyValue(Object propName){}
	
	public void setPropertyValue(Object propName, Object value){
		if(ID_X.equals(propName)){
			Integer newInt = new Integer((String)value);
			point.x = newInt.intValue();
		}
		if(ID_Y.equals(propName)){
			Integer newInt = new Integer((String)value);
			point.y = newInt.intValue();
		}
	}
	
	public String toString(){
		return "{" + point.x + ", " + point.y + "}";
	}
	

}
