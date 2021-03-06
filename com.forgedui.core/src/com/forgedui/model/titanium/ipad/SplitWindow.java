/**
 *
 */
package com.forgedui.model.titanium.ipad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.forgedui.model.property.BooleanPropertyDescriptor;
import com.forgedui.model.titanium.Dialog;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIContainer;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.model.titanium.annotations.Reference;
import com.forgedui.model.titanium.annotations.Review;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @AutoGenerated
 */
@SupportedPlatform(platforms="ipad")
public class SplitWindow extends TitaniumUIContainer implements Dialog{
	
	@Unmapped
	public static final String PROPERTY_MASTER_VIEW = "masterView";
	
	@Unmapped
	public static final String PROPERTY_DETAIL_VIEW = "detailView";
	
	@Unmapped
	public static final String PROPERTY_SHOW_MASTER_IN_PORTRAIT = "showMasterInPortrait";
	
	@Reference
	private String masterView;
	@Reference
	private String detailView;
	private Boolean showMasterInPortrait;
	
	public SplitWindow() {
		type = "Titanium.UI.iPad.SplitWindow";
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> props = new ArrayList<IPropertyDescriptor>();
		TextPropertyDescriptor masterPropDes = new TextPropertyDescriptor(PROPERTY_MASTER_VIEW, PROPERTY_MASTER_VIEW);
		masterPropDes.setValidator(getDiagram().getUniqueNameValidator(this));
		props.add(masterPropDes);
		TextPropertyDescriptor detailPropDes = new TextPropertyDescriptor(PROPERTY_DETAIL_VIEW, PROPERTY_DETAIL_VIEW);
		detailPropDes.setValidator(getDiagram().getUniqueNameValidator(this));
		props.add(detailPropDes);
		props.add(new BooleanPropertyDescriptor(PROPERTY_SHOW_MASTER_IN_PORTRAIT, PROPERTY_SHOW_MASTER_IN_PORTRAIT,true));
		return props.toArray(new IPropertyDescriptor[props.size()]);
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (PROPERTY_MASTER_VIEW.equals(propertyId)){
			return getMasterView() != null ? getMasterView() : "";
		} else if (PROPERTY_DETAIL_VIEW.equals(propertyId)){
			return getDetailView() != null ? getDetailView() : "";
		} else if (PROPERTY_SHOW_MASTER_IN_PORTRAIT.equals(propertyId)){
			return getShowMasterInPortrait()== null?0:(getShowMasterInPortrait()==Boolean.TRUE?1:2);
		}else
			return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if ("".equals(value)){
			value = null;
		}
		if (PROPERTY_MASTER_VIEW.equals(propertyId)){
			setMasterView((String)value);
		} else if (PROPERTY_DETAIL_VIEW.equals(propertyId)){
			setDetailView((String)value);
		} else if (PROPERTY_SHOW_MASTER_IN_PORTRAIT.equals(propertyId)){
			Integer value2 = (Integer)value;
			Boolean oldValue = getShowMasterInPortrait();
			setShowMasterInPortrait((value2==null || value2 == 0)? null:(value2 == 1));
			listeners.firePropertyChange(PROPERTY_SHOW_MASTER_IN_PORTRAIT, oldValue , value2);
		}else
			super.setPropertyValue(propertyId, value);
	}
	
	@Override
	public Platform getPlatform() {
		return Platform.iPad;
	}

	public String getMasterView() {
		return masterView;
	}

	public void setMasterView(String masterView) {
		this.masterView = masterView;
	}

	public String getDetailView() {
		return detailView;
	}

	public void setDetailView(String detailView) {
		this.detailView = detailView;
	}

	public Boolean getShowMasterInPortrait() {
		return showMasterInPortrait;
	}

	public void setShowMasterInPortrait(Boolean showMasterInPortrait) {
		this.showMasterInPortrait = showMasterInPortrait;
	}

}