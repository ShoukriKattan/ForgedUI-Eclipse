/**
 *
 */
package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.forgedui.model.titanium.annotations.Review;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @AutoGenerated
 */
public class TabGroup extends TitaniumUIElement {

	/**
	 * 
	 */
	@Unmapped
	private static final long serialVersionUID = 1L;
	@Unmapped
	private static final String PRP_TAB_NUM = "Tabs";
	@Unmapped
	public static final String PROP_BAR_COLOR = "barColor";
	@Unmapped
	public static final String TAB_PROP = "tab";

	private Boolean allowUserCustomization;

	private String barColor;

	@Review(note = "This isnt a property which can be set... daa.. this is runtime not compile time? or isnt it")
	private TitaniumUIBaseElement activeTab;

	private String editButtonTitle;

	@Review(note = "Should we have multiple tabs ... does a tab have a representation and association with window")
	private List<Tab> tabs = new ArrayList<Tab>();

	public TabGroup() {
		type = "Titanium.UI.TabGroup";
	}

	public void setTabNumber(int num) {
		if (num == 5 && tabs.size() > 5) {
			removeTab(tabs.get(4));// remove MoreButton first
		}
		if (num > 5) {
			num++;// reserve MoreTab space
		}
		for (int i = tabs.size() - 1; i >= num; i--) {
			removeTab(tabs.get(i));
		}
		for (int i = tabs.size(); tabs.size() < num; i++) {
			addTab(new Tab());
		}
	}

	public int getTabNumber() {
		return tabs.size();
	}

	public void removeTab(Tab oldTab) {
		tabs.remove(oldTab);
		oldTab.setParent(null);
		listeners.firePropertyChange(TAB_PROP, oldTab, null);
	}

	public void addTab(Tab newTab) {
		if (tabs.size() == 5 && !(tabs.get(4) instanceof MoreTab)) {
			Tab fifthTab = tabs.get(4);
			removeTab(fifthTab);
			addTab(new MoreTab());
			addTab(fifthTab);
		}
		tabs.add(newTab);
		newTab.setParent(this);
		listeners.fireIndexedPropertyChange(TAB_PROP, tabs.size() - 1, null, newTab);
	}

	public List<Tab> getTabs() {
		return Collections.unmodifiableList(tabs);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> fullList = new ArrayList<IPropertyDescriptor>();
		fullList.addAll(Arrays.asList(super.getPropertyDescriptors()));
		TextPropertyDescriptor tpd = new TextPropertyDescriptor(PRP_TAB_NUM, PRP_TAB_NUM);
		tpd.setValidator(new ICellEditorValidator() {

			@Override
			public String isValid(Object value) {
				int intValue = -1;
				try {
					intValue = Integer.parseInt((String) value);
				} catch (NumberFormatException exc) {
					return "Not a number";
				}
				return (intValue >= 2) ? null : "Value must be >=  2";
			}
		});
		fullList.add(tpd);

		return fullList.toArray(new IPropertyDescriptor[fullList.size()]);
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (PRP_TAB_NUM.equals(propertyId)) {
			return "" + (getTabNumber() > 5 ? getTabNumber() - 1 : getTabNumber());// do
																					// not
																					// include
																					// MoreTab
		}
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void resetPropertyValue(Object id) {
		if (PRP_TAB_NUM.equals(id)) {
			setPropertyValue(id, 2);
		} else {
			super.resetPropertyValue(id);
		}
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (PRP_TAB_NUM.equals(propertyId)) {
			setTabNumber(Integer.parseInt((String) value));
		} else {
			super.setPropertyValue(propertyId, value);
		}
	}

	@Override
	protected boolean isColorFieldName(String fieldName) {
		boolean res = false;
		if (PROP_BAR_COLOR.equals(fieldName)) {
			res = true;
		}
		if (!res) {
			res = super.isColorFieldName(fieldName);
		}
		return res;
	}

	/**
	 * @return the allowUserCustomization
	 */
	public Boolean getAllowUserCustomization() {
		return allowUserCustomization;
	}

	/**
	 * @param allowUserCustomization
	 *            the allowUserCustomization to set
	 */
	public void setAllowUserCustomization(Boolean allowUserCustomization) {
		this.allowUserCustomization = allowUserCustomization;
	}

	/**
	 * @return the barColor
	 */
	public String getBarColor() {
		return barColor;
	}

	/**
	 * @param barColor
	 *            the barColor to set
	 */
	public void setBarColor(String barColor) {
		this.barColor = barColor;
	}

	/**
	 * @return the activeTab
	 */
	public TitaniumUIBaseElement getActiveTab() {
		return activeTab;
	}

	/**
	 * @param activeTab
	 *            the activeTab to set
	 */
	public void setActiveTab(TitaniumUIBaseElement activeTab) {
		this.activeTab = activeTab;
	}

	/**
	 * @return the editButtonTitle
	 */
	public String getEditButtonTitle() {
		return editButtonTitle;
	}

	/**
	 * @param editButtonTitle
	 *            the editButtonTitle to set
	 */
	public void setEditButtonTitle(String editButtonTitle) {
		this.editButtonTitle = editButtonTitle;
	}

	/**
	 * @param tabs
	 *            the tabs to set
	 */
	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}

}