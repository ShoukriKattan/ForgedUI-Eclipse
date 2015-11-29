/**
 *
 */
package com.forgedui.model.titanium;

import com.forgedui.model.titanium.annotations.Review;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @AutoGenerated
 */
public class CoverFlowView extends TitaniumUIContainer {

	@Unmapped
	private static final long serialVersionUID = 1L;
	
	public static final String IMAGES_PROP = "images";

	private Integer selected;

	@Review
	private String[] images;

	public CoverFlowView() {

		type = "Titanium.UI.CoverFlowView";
	}

	/**
	 * @return the selected
	 */
	public Integer getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(Integer selected) {
		this.selected = selected;
	}

}