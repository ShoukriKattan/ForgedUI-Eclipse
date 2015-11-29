/**
 *
 */
package com.forgedui.model.titanium;

import java.lang.reflect.Field;

/**
 * @AutoGenerated
 */
public class PickerRow extends TitaniumUIContainer {
	
	public static final String TITLE_PROP = "title";

	public static final String SELECTED_PROP = "selected";

	private Boolean selected;

	private String title;

	private Integer fontSize;

	protected String getObjectName() {
		return "Titanium.UI.PickerRow";
	}

	@Override
	public void setName(String name) {
		String oldName = getName();
		super.setName(name);
		if (oldName == null) {
			setTitle(name);
		}
	}

	@Override
	protected String getBaseName() {
		return "Row";
	}

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

	/**
	 * @return the selected
	 */
	public Boolean getSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(Boolean selected) {
		Boolean old = this.selected;
		this.selected = selected;
		listeners.firePropertyChange(SELECTED_PROP, old, selected);
	}

	/**
	 * @return the fontSize
	 */
	public Integer getFontSize() {
		return fontSize;
	}

	/**
	 * @param fontSize
	 *            the fontSize to set
	 */
	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	@Override
	public PickerColumn getParent() {
		return (PickerColumn) super.getParent();
	}

	public boolean canEdit() {
		return getParent() != null && getParent().canEdit();
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		String prop = null;
		if (propertyId instanceof Field) {
			prop = ((Field) propertyId).getName();
		}
		if (TITLE_PROP.equals(prop)) {// TODO setSelected???
			if (canEdit())
				super.setPropertyValue(propertyId, value);
		} else {
			super.setPropertyValue(propertyId, value);
		}
	}

}