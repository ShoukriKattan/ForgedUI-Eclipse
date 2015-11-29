package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.forgedui.model.property.BoundsPropertySource;
import com.forgedui.model.titanium.annotations.Unmapped;

public abstract class TitaniumUIBoundedElement extends TitaniumUIBaseElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * String property for the view sBottom position. This position is relative
	 * to the view's parent. Either a float value either a dimension string ie
	 * 'auto' (default), '50%' (iPhone only) are not supported.
	 */
	@Unmapped
	protected String bottom;
	/**
	 * String property for the view height. Either a float value either a
	 * dimension string ie 'auto' (default), '50%' (iPhone only) are not
	 * supported.
	 */
	protected String height;
	/**
	 * property for the view left position. This position is relative to the
	 * view's parent. Either a float value either a dimension string ie 'auto'
	 * (default), '50%' (iPhone only) are not supported.
	 */
	protected String left;
	/**
	 * property for the view right position. This position is relative to the
	 * view's parent. Either a float value either a dimension string ie 'auto'
	 * (default), '50%' (iPhone only) are not supported.
	 */
	protected String right;
	/**
	 * String property for the view top position. This position is relative to
	 * the view's parent. Either a float value either a dimension string ie
	 * 'auto' (default), '50%' (iPhone only) are not supported.
	 */
	protected String top;
	/**
	 * String property for the view width. Either a float value either a
	 * dimension string ie 'auto' (default), '50%' (iPhone only) are not
	 * supported.
	 */
	protected String width;

	public TitaniumUIBoundedElement() {
		super();
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> fullList = new ArrayList<IPropertyDescriptor>();
		fullList.addAll(Arrays.asList(super.getPropertyDescriptors()));
		if (!getFilteredProperties().contains(PROP_BOUNDS)) {
			fullList.add(new PropertyDescriptor(PROP_BOUNDS, PROP_BOUNDS));
		}

		return fullList.toArray(new IPropertyDescriptor[fullList.size()]);
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (PROP_BOUNDS.equals(propertyId)) {
			return new BoundsPropertySource(this);
		}
		return super.getPropertyValue(propertyId);
	}
	
	/**
	 * The method wraps the changes and fire size or location change
	 * notifications
	 * 
	 * @param id
	 * @param oldValue
	 * @param newValue
	 */
	protected void fireBoundsChanged(String id, Object oldValue, Object newValue) {
		if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
			return;
		}
		if ("left".equals(id)) {
			if (getWidth() == null) {
				listeners.firePropertyChange(PROPERTY_SIZE, null, getDimension());
			}
			listeners.firePropertyChange(PROPERTY_LOCATION, null, getLocation());
		} else if ("right".equals(id)) {
			if (getWidth() == null) {
				listeners.firePropertyChange(PROPERTY_SIZE, null, getDimension());
			}
			if (getLeft() == null) {
				listeners.firePropertyChange(PROPERTY_LOCATION, null, getLocation());
			}
		} else if ("sBottom".equals(id)) {
			if (getHeight() == null) {
				listeners.firePropertyChange(PROPERTY_SIZE, null, getDimension());
			}
			if (getTop() == null) {
				listeners.firePropertyChange(PROPERTY_LOCATION, null, getLocation());
			}
		} else if ("top".equals(id)) {
			if (getHeight() == null) {
				listeners.firePropertyChange(PROPERTY_SIZE, null, getDimension());
			}
			listeners.firePropertyChange(PROPERTY_LOCATION, null, getLocation());
		} else if ("height".equals(id)) {
			listeners.firePropertyChange(PROPERTY_SIZE, null, getDimension());
			if (getTop() == null) {
				listeners.firePropertyChange(PROPERTY_LOCATION, null, getLocation());
			}
		} else if ("width".equals(id)) {
			listeners.firePropertyChange(PROPERTY_SIZE, null, getDimension());
			if (getLeft() == null) {
				listeners.firePropertyChange(PROPERTY_LOCATION, null, getLocation());
			}
		}
	}

	public String getBottom() {
		return bottom;
	}

	public void setBottom(String bottom) {
		String oldBottom = getBottom();
		this.bottom = bottom;
		fireBoundsChanged("sBottom", oldBottom, bottom);
	}

	public void setBottom(int bottom) {
		setBottom("" + bottom);
	}

	public String getHeight() {
		return height;
	}

	/**
	 * @param height
	 */
	public void setHeight(String height) {
		String oldHeight = getHeight();
		this.height = height;
		fireBoundsChanged("height", oldHeight, height);
	}

	public void setHeight(int height) {
		setHeight("" + height);
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		String oldLeft = getLeft();
		this.left = left;
		fireBoundsChanged("left", oldLeft, left);
	}

	public void setLeft(int left) {
		setLeft("" + left);
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		String oldRight = getRight();
		this.right = right;
		fireBoundsChanged("right", oldRight, right);
	}

	public void setRight(int right) {
		setRight("" + right);
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		String oldTop = getTop();
		this.top = top;
		fireBoundsChanged("top", oldTop, top);
	}

	public void setTop(int top) {
		setTop("" + top);
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		String oldWidth = getWidth();
		this.width = width;
		fireBoundsChanged("width", oldWidth, width);
	}

	public void setWidth(int width) {
		setWidth("" + width);
	}

}