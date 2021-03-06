/**
 *
 */
package com.forgedui.model.titanium;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.forgedui.model.Element;
import com.forgedui.model.property.EnumPropertyDescriptor;
import com.forgedui.model.titanium.TitaniumUIElement.EnumType;
import com.forgedui.model.titanium.annotations.FloatValueRange;
import com.forgedui.model.titanium.annotations.Review;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @AutoGenerated
 */
public class TableView extends TitaniumUIContainer {

	public static enum Style implements EnumType{
		PLAIN, GROUPED;
		
		public String getQDN() {
			return "Titanium.UI.iPhone.TableViewStyle." + super.toString();
		};
	}

	public static enum SeparatorStyle implements EnumType{
		NONE, SINGLE_LINE;
		
		public String getQDN() {
			return "Titanium.UI.MobileWeb.TableViewSeparatorStyle." + super.toString();
		};
	}

	@Unmapped
	private static final long serialVersionUID = -263147496596380177L;

	@Unmapped
	public static final String PROP_SEPARATOR_COLOR = "separatorColor";
	@Unmapped
	public static final String PROP_SEPARATOR_STYLE = "separatorStyle";
	@Unmapped
	public static final String PROP_STYLE = "style";

	@Unmapped
	public static final String PROP_HEADER_VIEW = "headerView";
	@Unmapped
	public static final String PROP_HEADER_TITLE = "headerTitle";
	@Unmapped
	public static final String PROP_FOOTER_VIEW = "footerView";
	@Unmapped
	public static final String PROP_FOOTER_TITLE = "footerTitle";

	@Unmapped
	public static final String PROP_SEARCH_VIEW = "searchView";
	@Unmapped
	public static final String PROP_SEARCH_VIEW_HIDDEN = "searchViewHidden";
	@Unmapped
	public static final String PROP_MIN_ROW_HEIGHT = "minRowHeight";
	@Unmapped
	public static final String PROP_MAX_ROW_HEIGHT = "maxRowHeight";

	@Review(note = "Highest abstraction??..")
	private TitaniumUIBoundedElement headerView;

	@Review(note = "What the fuck is the index")
	private String index;

	private Boolean moving;

	private Boolean editing;

	@FloatValueRange(from=0, type="minonly")
	private Float minRowHeight;

	private String separatorColor;

	private String footerTitle;

	@Review(note = "God knows what the hell this is ")
	private TitaniumUIBoundedElement[] data;

	private Boolean scrollable;

	@FloatValueRange(from=0, type="minonly")
	private Float maxRowHeight;

	private Boolean allowsSelection;

	@FloatValueRange(from=0, type="minonly")
	private Float rowHeight;

	@Review(note = "Highest abstraction")
	private TitaniumUIBoundedElement footerView;

	private String filterAttribute;

	private Boolean editable;

	@SupportedPlatform(platforms = "iphone")
	private Style style;

	@Review(note = "What")
	private SearchBar search;

	private SeparatorStyle separatorStyle;

	private Boolean searchHidden;

	private String headerTitle;

	private Boolean filterCaseInsensitive;

	public TableView() {

		type = "Titanium.UI.TableView";
	}

	public String getSeparatorColor() {
		return separatorColor;
	}

	public void setSeparatorColor(String separatorColor) {
		String oldColor = this.separatorColor;
		this.separatorColor = separatorColor;
		listeners.firePropertyChange(PROP_SEPARATOR_COLOR, oldColor, separatorColor);
	}

	@Override
	protected boolean isColorFieldName(String fieldName) {
		boolean res = false;
		if (PROP_SEPARATOR_COLOR.equals(fieldName)) {
			res = true;
		}
		if (!res) {
			res = super.isColorFieldName(fieldName);
		}
		return res;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the moving
	 */
	public Boolean getMoving() {
		return moving;
	}

	/**
	 * @param moving
	 *            the moving to set
	 */
	public void setMoving(Boolean moving) {
		this.moving = moving;
	}

	/**
	 * @return the editing
	 */
	public Boolean getEditing() {
		return editing;
	}

	/**
	 * @param editing
	 *            the editing to set
	 */
	public void setEditing(Boolean editing) {
		this.editing = editing;
	}

	/**
	 * @return the minRowHeight
	 */
	public Float getMinRowHeight() {
		return minRowHeight;
	}

	/**
	 * @param minRowHeight
	 *            the minRowHeight to set
	 */
	public void setMinRowHeight(Float minRowHeight) {
		Float old = this.minRowHeight;
		this.minRowHeight = minRowHeight;
		listeners.firePropertyChange(PROP_MIN_ROW_HEIGHT, old, minRowHeight);
	}

	/**
	 * @return the headerView
	 */
	public TitaniumUIBoundedElement getHeaderView() {
		return headerView;
	}

	/**
	 * @param headerView
	 *            the headerView to set
	 */
	public void setHeaderView(TitaniumUIBoundedElement headerView) {
		Element oldElement = this.headerView;
		this.headerView = headerView;
		fireElementPropertySet(PROP_HEADER_VIEW, oldElement, headerView);
	}

	/**
	 * @return the footerTitle
	 */
	public String getFooterTitle() {
		return footerTitle;
	}

	/**
	 * @param footerTitle
	 *            the footerTitle to set
	 */
	public void setFooterTitle(String footerTitle) {
		String oldFooter = this.footerTitle;
		this.footerTitle = footerTitle;
		listeners.firePropertyChange(PROP_FOOTER_TITLE, oldFooter, footerTitle);
	}

	/**
	 * @return the headerTitle
	 */
	public String getHeaderTitle() {
		return headerTitle;
	}

	/**
	 * @param headerTitle
	 *            the headerTitle to set
	 */
	public void setHeaderTitle(String headerTitle) {
		String oldHeaderTitle = this.headerTitle;
		this.headerTitle = headerTitle;
		listeners.firePropertyChange(PROP_HEADER_TITLE, oldHeaderTitle, headerTitle);
	}

	/**
	 * @return the footerView
	 */
	public TitaniumUIBoundedElement getFooterView() {
		return footerView;
	}

	/**
	 * @param footerView
	 *            the footerView to set
	 */
	public void setFooterView(TitaniumUIBoundedElement footerView) {
		Element oldElement = this.footerView;
		this.footerView = footerView;
		fireElementPropertySet(PROP_FOOTER_VIEW, oldElement, footerView);
	}

	/**
	 * @return the scrollable
	 */
	public Boolean getScrollable() {
		return scrollable;
	}

	/**
	 * @param scrollable
	 *            the scrollable to set
	 */
	public void setScrollable(Boolean scrollable) {
		this.scrollable = scrollable;
	}

	/**
	 * @return the maxRowHeight
	 */
	public Float getMaxRowHeight() {
		return maxRowHeight;
	}

	/**
	 * @param maxRowHeight
	 *            the maxRowHeight to set
	 */
	public void setMaxRowHeight(Float maxRowHeight) {
		Float old = this.maxRowHeight;
		this.maxRowHeight = maxRowHeight;
		listeners.firePropertyChange(PROP_MAX_ROW_HEIGHT, old, maxRowHeight);
		
	}

	/**
	 * @return the allowsSelection
	 */
	public Boolean getAllowsSelection() {
		return allowsSelection;
	}

	/**
	 * @param allowsSelection
	 *            the allowsSelection to set
	 */
	public void setAllowsSelection(Boolean allowsSelection) {
		this.allowsSelection = allowsSelection;
	}

	/**
	 * @return the rowHeight
	 */
	public Float getRowHeight() {
		return rowHeight;
	}

	/**
	 * @param rowHeight
	 *            the rowHeight to set
	 */
	public void setRowHeight(Float rowHeight) {
		this.rowHeight = rowHeight;
	}

	/**
	 * @return the filterAttribute
	 */
	public String getFilterAttribute() {
		return filterAttribute;
	}

	/**
	 * @param filterAttribute
	 *            the filterAttribute to set
	 */
	public void setFilterAttribute(String filterAttribute) {
		this.filterAttribute = filterAttribute;
	}

	/**
	 * @return the editable
	 */
	public Boolean getEditable() {
		return editable;
	}

	/**
	 * @param editable
	 *            the editable to set
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return the style
	 */
	public Style getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(Style style) {
		this.style = style;
	}

	/**
	 * @return the search
	 */
	public SearchBar getSearch() {
		return search;
	}

	/**
	 * @param search
	 *            the search to set
	 */
	public void setSearch(SearchBar search) {
		Element oldElement = this.search;
		this.search = search;
		fireElementPropertySet(PROP_SEARCH_VIEW, oldElement, search);
	}

	/**
	 * @return the separatorStyle
	 */
	public SeparatorStyle getSeparatorStyle() {
		return separatorStyle;
	}

	/**
	 * @param separatorStyle
	 *            the separatorStyle to set
	 */
	public void setSeparatorStyle(SeparatorStyle separatorStyle) {
		this.separatorStyle = separatorStyle;
	}

	/**
	 * @return the searchHidden
	 */
	public Boolean getSearchHidden() {
		return searchHidden;
	}

	/**
	 * @param searchHidden
	 *            the searchHidden to set
	 */
	public void setSearchHidden(Boolean searchHidden) {
		Boolean oldValue = this.searchHidden;
		this.searchHidden = searchHidden;
		listeners.firePropertyChange(PROP_SEARCH_VIEW_HIDDEN, oldValue, searchHidden);
	}

	/**
	 * @return the filterCaseInsensitive
	 */
	public Boolean getFilterCaseInsensitive() {
		return filterCaseInsensitive;
	}

	/**
	 * @param filterCaseInsensitive
	 *            the filterCaseInsensitive to set
	 */
	public void setFilterCaseInsensitive(Boolean filterCaseInsensitive) {
		this.filterCaseInsensitive = filterCaseInsensitive;
	}

	@Override
	public void removeChild(Element child) {
		if (child == headerView) {
			setHeaderView(null);
		} else if (child == footerView) {
			setFooterView(null);
		} else if (child == search) {
			setSearch(null);
		}
		super.removeChild(child);
	}

}