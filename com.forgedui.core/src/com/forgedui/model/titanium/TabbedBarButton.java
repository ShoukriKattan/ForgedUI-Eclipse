// LICENSE
package com.forgedui.model.titanium;

import com.forgedui.model.titanium.annotations.Unmapped;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class TabbedBarButton extends TitaniumUIBaseElement {

	public static final String TITLE_PROP = "title";

	public static final String SELECTED_PROP = "selected";

	public static final String IMAGE_PROP = "image";

	public static final String WIDTH_PROP = "width";

	private Boolean selected;

	private Boolean enabled;

	private Integer width;

	private String image;

	private String title;
	
	private boolean first;
	
	private boolean last;
	
	@Unmapped
	private String baseName = "X";
	
	public TabbedBarButton(){
		type = "";
	}
	
	public TabbedBarButton(boolean first, boolean last,String baseName) {
		this.first = first;
		this.last = last;
		type = "";
		this.baseName = baseName;
	}

	@Override
	public String getType() {

		return "";
	}

	@Override
	protected String getBaseName() {
		return baseName;
	}
	
	
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	@Override
	public void setName(String name) {
		String oldName = getName();
		super.setName(name);
		if (oldName == null) {
			setTitle(name);
		}
	}

	/**
	 * @param title
	 *            the title to set
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

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(Boolean selected) {
		Boolean oldValue = this.selected;
		boolean oldSelected = Utils.getBoolean(this.selected, false);
		this.selected = selected;
		if (oldSelected != Utils.getBoolean(selected, false)) {
			if (getParent() instanceof TabbedBar) {
				((TabbedBar) getParent()).updateSelection(this);
			} else if (getParent() instanceof ButtonBar) {
				((ButtonBar) getParent()).updateSelection(this);
			}
		}
		listeners.firePropertyChange(SELECTED_PROP, oldValue, selected);
	}

	/**
	 * @return the selected
	 */
	public Boolean getSelected() {
		return selected;
	}

	/**
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(Integer width) {
		Integer oldWidth = this.width;
		this.width = width;
		listeners.firePropertyChange(WIDTH_PROP, oldWidth, width);
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}
	
	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		String oldImage = this.image;
		this.image = image;
		listeners.firePropertyChange(IMAGE_PROP, oldImage, image);
	}

	@Override
	protected boolean isImageFieldName(String fieldName) {
		boolean res = false;
		if (IMAGE_PROP.equals(fieldName)) {
			res = true;
		} else {
			res = super.isImageFieldName(fieldName);
		}
		return res;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}
	
}
