// LICENSE
package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.forgedui.model.property.MinmaxValidator;
import com.forgedui.model.property.NumberCellEditor;
import com.forgedui.model.property.NumberPropertyDescriptor;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class OptionDialog extends TitaniumUIContainer implements Dialog {

	@Unmapped
	private static final long serialVersionUID = 1L;
	
	@Unmapped
	public static final String TITLE_PROP = "title";
	
	@Unmapped
	public static final String TITLE_ID_PROP = "titleid";
	
	@Unmapped
	public static final String CANCEL_PROP = "cancel";
	
	@Unmapped
	public static final String DESTRUCTIVE_PROP = "destructive";
	
	@Unmapped
	public static final String SELECTED_PROP = "selectedIndex";
	
	private String title;
	
	private String titleid;
	
	private Integer cancel;
		
	private Integer destructive;
	
	@SupportedPlatform(platforms={"android"})
	private Integer selectedIndex;

	
	public OptionDialog() {
		type="Titanium.UI.OptionDialog";
	}
	
	@Override
	public void setName(String name) {
		String oldName = getName();
		super.setName(name);
		if (oldName == null){
			setTitle("Put your question here");
		}
	}
	
	
	
	@Override
	public List<? extends TitaniumUIBaseElement> getChildren() {
		if (super.getChildren().size() < 2){
			setDialogOptionsNumber(2);
		}
		return super.getChildren();
	}
	
	public void setDialogOptionsNumber(int num){
		for (int i = children.size() - 1; i >= num; i--) {
			removeChild(children.get(i));
		}	
		for (int i = children.size(); children.size() < num; i++) {
			addChild(new OptionDialogButton());
		}
	}
	
	public Integer getCancel() {
		return cancel;
	}

	public void setCancel(Integer cancel) {
		Integer old = this.cancel;
		this.cancel = cancel;
		listeners.firePropertyChange(CANCEL_PROP, old, cancel);
	}
	
	public Integer getDesctructive() {
		return destructive;
	}
	
	public void setDesctructive(Integer destructive) {
		Integer old = this.destructive;
		this.destructive = destructive;
		listeners.firePropertyChange(DESTRUCTIVE_PROP, old, destructive);
	}
	
	public Integer getSelected() {
		return selectedIndex;
	}
	
	public void setSelectedIndex(Integer index) {
		Integer old = this.selectedIndex;
		this.selectedIndex = index;
		listeners.firePropertyChange(SELECTED_PROP, old, index);
	}
	
	public Integer getSelectedIndex() {
		return selectedIndex;
	}
	
	public OptionDialogButton getOptionDialogButton(Integer i){
		if (i != null && getChildren().size() > i){
			return (OptionDialogButton) getChildren().get(i);
		}
		return null;
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
		List<IPropertyDescriptor> props = new ArrayList<IPropertyDescriptor>();
		TextPropertyDescriptor nameDescr = new TextPropertyDescriptor(
				PROPERTY_NAME, PROPERTY_NAME);
		nameDescr.setValidator(getDiagram().getUniqueNameValidator(this));
		props.add(nameDescr);
		props.add(new TextPropertyDescriptor(TITLE_PROP, TITLE_PROP));
		props.add(new TextPropertyDescriptor(TITLE_ID_PROP, TITLE_ID_PROP));
		NumberPropertyDescriptor npd = new NumberPropertyDescriptor(PROPERTY_CHILDREN,
				"buttonsCount", NumberCellEditor.INTEGER, false);
		npd.setValidator(new MinmaxValidator(2, 30));
		props.add(npd);
		npd = new NumberPropertyDescriptor(CANCEL_PROP,
				CANCEL_PROP, NumberCellEditor.INTEGER, true);
		npd.setValidator(new MinmaxValidator(0, MinmaxValidator.MIN_ONLY));
		props.add(npd);
		npd = new NumberPropertyDescriptor(DESTRUCTIVE_PROP,
				DESTRUCTIVE_PROP, NumberCellEditor.INTEGER, true);
		npd.setValidator(new MinmaxValidator(0, MinmaxValidator.MIN_ONLY));
		props.add(npd);
		if (getPlatform().isAndroid()){
			npd = new NumberPropertyDescriptor(SELECTED_PROP,
					SELECTED_PROP, NumberCellEditor.INTEGER, true);
			npd.setValidator(new MinmaxValidator(0, MinmaxValidator.MIN_ONLY));
			props.add(npd);
		}
		return props.toArray(new IPropertyDescriptor[props.size()]);
	}
	
	@Override
	public void resetPropertyValue(Object id) {
		if (PROPERTY_CHILDREN.equals(id)){
			setDialogOptionsNumber(2);
		} else {
			super.resetPropertyValue(id);
		}
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (CANCEL_PROP.equals(propertyId)){
			return getCancel() != null ? getCancel() : "";
		} else if (DESTRUCTIVE_PROP.equals(propertyId)){
			return getDesctructive() != null ? getDesctructive() : "";
		} else if (PROPERTY_CHILDREN.equals(propertyId)){
			return getChildren().size();
		} else if (SELECTED_PROP.equals(propertyId)){
			return getSelected() != null ? getSelected() : "";
		} else if (TITLE_PROP.equals(propertyId)){
			return getTitle() != null ? getTitle() : "";
		} else if (TITLE_ID_PROP.equals(propertyId)){
			return getTitleid() != null ? getTitleid() : "";
		}
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if ("".equals(value)){
			value = null;
		} else if (value instanceof Integer && (Integer)value < 0){
			value = null;
		}
		if (PROPERTY_CHILDREN.equals(propertyId)){
			setDialogOptionsNumber((Integer)value);
		} else if (CANCEL_PROP.equals(propertyId)){
			setCancel((Integer) value);
		} else if (DESTRUCTIVE_PROP.equals(propertyId)){
			setDesctructive((Integer) value);
		} else if (SELECTED_PROP.equals(propertyId)){
			setSelectedIndex((Integer) value);
		} else if (TITLE_PROP.equals(propertyId)){
			setTitle((String) value);
		} else if (TITLE_ID_PROP.equals(propertyId)){
			setTitleid((String) value);
		} else
			super.setPropertyValue(propertyId, value);
	}
	
	protected String[] getOptionButtons(){
		String[] buttonNames = new String[children.size() + 1];
		buttonNames[0] = "";
		for (int i = 0; i < children.size(); i++) {
			buttonNames[i + 1] = children.get(i).getName();
		}
		return buttonNames;
	}

	/**
	 * @param titleid the titleid to set
	 */
	public void setTitleid(String titleId) {
		String old = this.titleid;
		this.titleid = titleId;
		listeners.firePropertyChange(TITLE_ID_PROP, old, titleId);
	}

	/**
	 * @return the titleid
	 */
	public String getTitleid() {
		return titleid;
	}
	
}
