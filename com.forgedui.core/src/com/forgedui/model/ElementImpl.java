// LICENSE
package com.forgedui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.forgedui.model.property.DimensionPropertySource;
import com.forgedui.model.property.LocationPropertySource;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.PlatformSupport;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class ElementImpl implements Element, IPropertySource, Serializable {

	private static final long serialVersionUID = 2;

	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
			new PropertyDescriptor(PROPERTY_LOCATION, "Location"),
			new PropertyDescriptor(PROPERTY_SIZE, "Dimension") };

	/**
	 * Each UI Element has a name . the name of the particular instance of this
	 * UI Element
	 * 
	 * Note: names should be unique within the entire Diagram. TODO: Ensure name
	 * is unique within the entire diagram
	 * 
	 */
	protected String name;

	/** Location of this shape. */
	private Point location = new Point(0, 0);
	/** Size of this shape. */
	private Dimension dimension = new Dimension(0, 0);

	/**
	 * Parent Container if any.
	 */
	protected Element parent;

	protected PropertyChangeSupport listeners;

	public ElementImpl() {
		if(listeners==null)
		listeners = new PropertyChangeSupport(this);
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension size) {
		Dimension oldSize = this.dimension;
		this.dimension = size;
		listeners.firePropertyChange(PROPERTY_SIZE, oldSize, size);
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		Point oldLocation = this.location;
		this.location = location;
		listeners.firePropertyChange(PROPERTY_LOCATION, oldLocation, location);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	/**
	 * Children should override this. The default implementation returns an
	 * empty array.
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descr = new IPropertyDescriptor[descriptors.length + 1];
		System.arraycopy(descriptors, 0, descr, 0, descriptors.length);
		TextPropertyDescriptor nameDescr = new TextPropertyDescriptor(
				PROPERTY_NAME, "Name");
		nameDescr.setValidator(getDiagram().getUniqueNameValidator(this));
		descr[descriptors.length] = nameDescr;
		return descr;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (PROPERTY_LOCATION.equals(propertyId)) {
			return new LocationPropertySource(this);
		}
		if (PROPERTY_SIZE.equals(propertyId)) {
			return new DimensionPropertySource(this);
		}
		if (PROPERTY_NAME.equals(propertyId)) {
			return name != null ? name : "";
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (PROPERTY_NAME.equals(id)) {
			return false;// can't reset
		}
		Object value = getPropertyValue(id);
		if (value instanceof String) {
			return Utils.isNotEmpty((String) value);
		}
		return value != null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		listeners.firePropertyChange(PROPERTY_NAME, oldName, name);
	}

	@Override
	public void resetPropertyValue(Object id) {
		setPropertyValue(id, null);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (PROPERTY_LOCATION.equals(propertyId)) {
			LocationPropertySource locationProp = (LocationPropertySource) value;
			setLocation(locationProp != null ? locationProp.getValue() : null);
		} else if (PROPERTY_SIZE.equals(propertyId)) {
			DimensionPropertySource dimensionProp = (DimensionPropertySource) value;
			setDimension(dimensionProp != null ? dimensionProp.getValue()
					: null);
		} else if (PROPERTY_NAME.equals(propertyId)) {
			setName((String) value);
		}
	}

	@Override
	public Element getParent() {
		return parent;
	}

	@Override
	public void setParent(Element parent) {
		// Container oldParent = this.parent;
		this.parent = parent;
		if (parent != null && name == null) {
			setName(getDiagram().generateUniqueName(getBaseName()));
		}
		// TODO not sure we need this as we already have children change
		// notification
		// listeners.firePropertyChange(PROPERTY_PARENT, oldParent, parent);
	}

	protected String getBaseName() {
		return getClass().getSimpleName();
	}

	@Override
	public Diagram getDiagram() {
		// <null> here means that something goes wrong.
		Assert.isNotNull(getParent(), "Parent is null!");
		return getParent().getDiagram();
	}

	@Override
	public Platform getPlatform() {
		return getDiagram().getPlatform();
	}

	@Override
	public Dimension getResolution() {
		return getDiagram().getResolution();
	}

	@Override
	public PlatformSupport getSupport() {
		return getDiagram().getSupport();
	}

	public void fireElementPropertySet(String propertyName, Element oldValue,
			Element newValue) {
		if (oldValue != null) {
			oldValue.setParent(null);
		}
		if (newValue != null) {
			newValue.setParent(this);
		}
		listeners.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void initDefaults() {
		
	}

}
