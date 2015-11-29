package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.forgedui.model.ContainerImpl;

public class PopoverDialog extends ContainerImpl implements Dialog {

	private static final long serialVersionUID = 1L;
	
	public static final String TITLE_PROP = "title";
	public static final String TITLE_ID_PROP = "titleId";
	public static final String POPOVER_PROP = "popover";
	
	private String title;
	
	private String titleId;
	
	//private PopoverDialogOption popover;
	
	@Override
	public void setName(String name) {
		String oldName = getName();
		super.setName(name);
		if (oldName == null){
			setTitle("Put your question here");
		}
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		String oldTitle = this.title;
		this.title = title;
		listeners.firePropertyChange(TITLE_PROP, oldTitle, title);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> props = new ArrayList<IPropertyDescriptor>(
				Arrays.asList(super.getPropertyDescriptors()));
		props.add(new TextPropertyDescriptor(TITLE_ID_PROP, TITLE_ID_PROP));
		props.add(new TextPropertyDescriptor(TITLE_PROP, TITLE_PROP));
		//props.add(new ComboBoxPropertyDescriptor(POPOVER_PROP, POPOVER_PROP,
		//		getOptionButtons()));
		return props.toArray(new IPropertyDescriptor[props.size()]);
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (TITLE_ID_PROP.equals(propertyId)){
			return titleId == null ? "" : titleId;
		} else if (TITLE_PROP.equals(propertyId)){
			return title == null ? "" : title;
		//} else if (POPOVER_PROP.equals(propertyId)){
		//	return popover == null ? 0 : buttons.indexOf(cancel) + 1;
		}
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if ("".equals(value)){
			value = null;
		}
		if (TITLE_ID_PROP.equals(propertyId)){
			setTitleId(value == null ? null : value.toString());
		} else if (TITLE_PROP.equals(propertyId)){
			setTitle(value == null ? null : value.toString());
		//} else if (POPOVER_PROP.equals(propertyId)){
		//	setCancel((Integer)value == 0  ? null : buttons.get(((Integer)value) - 1));
		} else {
			super.setPropertyValue(propertyId, value);
		}
	}

	/**
	 * @param titleId the titleId to set
	 */
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	/**
	 * @return the titleId
	 */
	public String getTitleId() {
		return titleId;
	}
	
	@Override
	public void setDimension(Dimension size) {
		super.setDimension(size);
	}
	
	@Override
	public void setLocation(Point location) {
		super.setLocation(location);
	}

	@Override
	public Platform getPlatform() {
		return Platform.iPad;
	}

}
