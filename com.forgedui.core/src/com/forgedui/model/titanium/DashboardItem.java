/**
 * 
 */
package com.forgedui.model.titanium;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.forgedui.model.property.ImagePropertySource;
import com.forgedui.model.titanium.annotations.Unmapped;
import com.forgedui.util.Converter;

/**
 * @author shoukry
 * 
 *         What should i do with this
 */
public class DashboardItem extends TitaniumUIBaseElement {

	@Unmapped
	private static final long serialVersionUID = 1L;

	@Unmapped
	public static final String PROP_BADGE = "badge";

	@Unmapped
	public static final String PROP_IMAGE = "image";

	@Unmapped
	public static final String PROP_SELECTED_IMAGE = "selectedImage";

	private Boolean canDelete;

	private String image;

	private String selectedImage;

	private Integer badge;

	public DashboardItem() {

		type = "Titanium.UI.DashboardItem";
	}

	/**
	 * @param badge
	 *            the badge to set
	 */
	public void setBadge(Integer badge) {
		Integer oldValue = this.badge;
		this.badge = badge;
		listeners.firePropertyChange(PROP_BADGE, oldValue, badge);
	}

	/**
	 * @return the badge
	 */
	public Integer getBadge() {
		return badge;
	}

	public void setImage(String image) {
		String oldImage = this.image;
		this.image = image;
		listeners.firePropertyChange(PROP_IMAGE, oldImage, image);
	}

	public void setSelectedImage(String selectedImage) {
		String oldImage = this.selectedImage;
		this.selectedImage = selectedImage;
		listeners.firePropertyChange(PROP_SELECTED_IMAGE, oldImage, selectedImage);
	}

	public String getImage() {
		return image;
	}

	public String getSelectedImage() {
		return this.selectedImage;
	}

	/**
	 * Do the color property stuff here.
	 */
	protected void handleProperty(ArrayList<IPropertyDescriptor> descriptors, String fieldName, Class<?> fieldType,
			Field f) {
		// Adding the color property descriptor here for the button.
		if (fieldName.equals(PROP_IMAGE) || fieldName.equals(PROP_SELECTED_IMAGE)) {
			descriptors.add(new ImagePropertySource(f, fieldName));
		}
		super.handleProperty(descriptors, fieldName, fieldType, f);
	}

	protected Object handlePropertyGetValue(String fieldName, Object value) {
		Object retValue = super.handlePropertyGetValue(fieldName, value);
		if (retValue != null)
			return retValue;
		// For now lets return null here.
		if (fieldName.equals(PROP_IMAGE) || fieldName.equals(PROP_SELECTED_IMAGE)) {
			// Getting the image path.
			if (value != null)
				return value;////Zrieq removed this:Converter.getImageFullPath(getDiagram(), value.toString());
		}
		return null;
	}

	protected Object handlePropertySetValue(String fieldName, Object value) {
		Object retValue = super.handlePropertySetValue(fieldName, value);
		if (retValue != null)
			return retValue;
		if (fieldName.equals(PROP_IMAGE) || fieldName.equals(PROP_SELECTED_IMAGE)) {
			// We have to strip the value of the path here.
			return value;//Zrieq removed this:Converter.getImageFullPath(getDiagram(), value.toString());
		}
		return null;
	}

	/**
	 * @return the canDelete
	 */
	public Boolean getCanDelete() {
		return canDelete;
	}

	/**
	 * @param canDelete
	 *            the canDelete to set
	 */
	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}

}
