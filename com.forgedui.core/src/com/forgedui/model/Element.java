// LICENSE
package com.forgedui.model;

import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.geometry.Dimension;

import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.PlatformSupport;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public interface Element {
	
	public final String PROPERTY_NAME = "name";
	public final String PROPERTY_SIZE = "size";
	public final String PROPERTY_LOCATION = "location";
	public final String PROPERTY_PARENT = "parent";

	public String getName();
	public void setName(String name);
	
	//public Dimension getDimension();
	//public void setDimension(Dimension dimension);
	
	//public Point getLocation();
	//public void setLocation(Point location);
	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	public Element getParent();
	public void setParent(Element parent);
	
	public Platform getPlatform();
	public Diagram getDiagram();
	public Dimension getResolution();
	public PlatformSupport getSupport();

}
