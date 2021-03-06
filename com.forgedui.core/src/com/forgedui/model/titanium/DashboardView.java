/**
 *
 */
package com.forgedui.model.titanium;

import com.forgedui.model.titanium.annotations.Review;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @AutoGenerated TODO: Dashboard view can accept Dashboard Item. Dashboard item
 */
public class DashboardView extends TitaniumUIContainer {

	/**
	 * 
	 */
	@Unmapped
	private static final long serialVersionUID = 1L;

	private Boolean wobble;

	@Review(note = "Should we have represenation for all items. such as dashboard item as it has different properties")
	private DashboardItem[] data;

	public DashboardView() {
		type = "Titanium.UI.DashboardView";
	}

	/**
	 * @return the wobble
	 */
	public Boolean getWobble() {
		return wobble;
	}

	/**
	 * @param wobble the wobble to set
	 */
	public void setWobble(Boolean wobble) {
		this.wobble = wobble;
	}

	/**
	 * @return the data
	 */
	public DashboardItem[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(DashboardItem[] data) {
		this.data = data;
	}

}