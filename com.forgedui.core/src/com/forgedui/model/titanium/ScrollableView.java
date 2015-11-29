/**
 *
 */
package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.forgedui.model.Element;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @AutoGenerated
 */
public class ScrollableView extends TitaniumUIContainer {

	/**
	 * 
	 */
	@Unmapped
	private static final long serialVersionUID = 1L;

	@Unmapped
	public static final String PROP_CURRENT_PAGE = "currentPage";

	@Unmapped
	public static final String PROP_PAGING_CONTROL_COLOR = "pagingControlColor";
	@Unmapped
	public static final String PROP_PAGING_CONTROL_HEIGHT = "pagingControlHeight";
	@Unmapped
	public static final String PROP_SHOW_PAGING_CONTROL = "showPagingControl";

	private Float maxZoomScale;

	private Float minZoomScale;

	private Boolean showPagingControl;

	private Float pagingControlHeight;

	private Integer currentPage;

	private String pagingControlColor;

	public ScrollableView() {
		type = "Titanium.UI.ScrollableView";
	}

	@Override
	public void addChild(Element child) {
		super.addChild(child);
		setCurrentPage(children.size() - 1);
	}

	@Override
	public void removeChild(Element child) {
		setCurrentPage(children.indexOf(child) - 1);
		super.removeChild(child);
		if (getChildren().size() == 0) {
			setCurrentPage(null);
		} else {
			// restore index
			if (children.size() > getCurrentPage() + 1) {
				setCurrentPage(getCurrentPage() + 1);
			}
		}
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(Integer currentPage) {
		Integer oldcurrentPage = this.currentPage;
		this.currentPage = currentPage;
		listeners.firePropertyChange(PROP_CURRENT_PAGE, oldcurrentPage, currentPage);
	}

	public String getPagingControlColor() {
		return pagingControlColor;
	}

	public void setPagingControlColor(String pagingControlColor) {
		String oldColor = this.pagingControlColor;
		this.pagingControlColor = pagingControlColor;
		listeners.firePropertyChange(PROP_PAGING_CONTROL_COLOR, oldColor, pagingControlColor);
	}

	/**
	 * @return the currentPage
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> fullList = new ArrayList<IPropertyDescriptor>();
		fullList.addAll(Arrays.asList(super.getPropertyDescriptors()));
		List<String> views = getViewNames();
		ComboBoxPropertyDescriptor cur = new ComboBoxPropertyDescriptor(PROP_CURRENT_PAGE, PROP_CURRENT_PAGE,
				views.toArray(new String[views.size()]));
		fullList.add(cur);

		return fullList.toArray(new IPropertyDescriptor[fullList.size()]);
	}

	public List<String> getViewNames() {
		List<String> names = new ArrayList<String>();
		names.add("<None>");
		for (Element e : getChildren()) {
			names.add(e.getName());
		}
		return names;
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (PROP_CURRENT_PAGE.equals(propertyId)) {
			if (currentPage == null) {
				return 0;
			}
			return currentPage + 1;
		}
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (PROP_CURRENT_PAGE.equals(propertyId)) {
			if (value instanceof Integer && (Integer) value > 0) {
				setCurrentPage((Integer) value - 1);
			} else {
				setCurrentPage(null);
				;
			}
		} else
			super.setPropertyValue(propertyId, value);
	}

	@Override
	protected boolean isColorFieldName(String fieldName) {
		boolean res = false;
		if (PROP_PAGING_CONTROL_COLOR.equals(fieldName)) {
			res = true;
		}
		if (!res) {
			res = super.isColorFieldName(fieldName);
		}
		return res;
	}

	/**
	 * @return the maxZoomScale
	 */
	public Float getMaxZoomScale() {
		return maxZoomScale;
	}

	/**
	 * @param maxZoomScale
	 *            the maxZoomScale to set
	 */
	public void setMaxZoomScale(Float maxZoomScale) {
		this.maxZoomScale = maxZoomScale;
	}

	/**
	 * @return the minZoomScale
	 */
	public Float getMinZoomScale() {
		return minZoomScale;
	}

	/**
	 * @param minZoomScale
	 *            the minZoomScale to set
	 */
	public void setMinZoomScale(Float minZoomScale) {
		this.minZoomScale = minZoomScale;
	}

	/**
	 * @return the showPagingControl
	 */
	public Boolean getShowPagingControl() {
		return showPagingControl;
	}

	/**
	 * @param showPagingControl
	 *            the showPagingControl to set
	 */
	public void setShowPagingControl(Boolean showPagingControl) {
		Boolean oldValue = this.showPagingControl;
		this.showPagingControl = showPagingControl;
		listeners.firePropertyChange(PROP_SHOW_PAGING_CONTROL, oldValue, showPagingControl);
	}

	/**
	 * @return the pagingControlHeight
	 */
	public Float getPagingControlHeight() {
		return pagingControlHeight;
	}

	/**
	 * @param pagingControlHeight
	 *            the pagingControlHeight to set
	 */
	public void setPagingControlHeight(Float pagingControlHeight) {
		Float oldValue = this.pagingControlHeight;
		this.pagingControlHeight = pagingControlHeight;
		listeners.firePropertyChange(PROP_PAGING_CONTROL_HEIGHT, oldValue, pagingControlHeight);
	}

}