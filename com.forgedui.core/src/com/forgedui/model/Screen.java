// LICENSE
package com.forgedui.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.forgedui.model.property.ResolutionPropertyDescriptor;
import com.forgedui.model.titanium.Dialog;
import com.forgedui.model.titanium.Platform;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.Window;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class Screen extends ElementImpl {

	public static final String SCREE_WINDOW_PR_DIALOG = "Screen.dialog";
	public static final String SCREE_WINDOW_PR_WINDOW = "Screen.window";
	public static final String SCREE_WINDOW_PR_TAB_GROUP = "Screen.tabGroup";
	
	private static final long serialVersionUID = 1L;

	private Dialog dialog;
	
	private Window window;
	
	private TabGroup tabGroup;
	
	/**
	 * @param imageName
	 */
	public Screen() {
		//setLocation(new Point(10, 10));
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		int size = 1;
		if(Platform.Android == getPlatform())
			size = 2;
		IPropertyDescriptor[] descr = new IPropertyDescriptor[size];
		descr[0] = new PropertyDescriptor(Diagram.PLATFORM, Diagram.PLATFORM);
		if(Platform.Android == getPlatform())
			descr[1] = new ResolutionPropertyDescriptor(getPlatform(), Diagram.RESOLUTION);
		return descr;
	}
	
	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		Window oldWindow = this.window;
		this.window = window;
		fireElementPropertySet(SCREE_WINDOW_PR_WINDOW, oldWindow, window);
		if (window != null){
			//FIXME set this in more appropriate place
			window.setLocation(getSupport().viewToModel(new Point(0, 0)));
		}
	}

	public TabGroup getTabGroup() {
		return tabGroup;
	}

	public void setTabGroup(TabGroup tabGroup) {
		TabGroup oldTabGroup = this.tabGroup;
		this.tabGroup = tabGroup;
		fireElementPropertySet(SCREE_WINDOW_PR_TAB_GROUP, oldTabGroup, tabGroup);
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(Dialog dialog) {
		Dialog oldvalue = this.dialog;
		this.dialog = dialog;
		fireElementPropertySet(SCREE_WINDOW_PR_DIALOG, oldvalue, dialog);
	}

	/**
	 * @return the dialog
	 */
	public Dialog getDialog() {
		return dialog;
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (Diagram.PLATFORM.equals(propertyId)){
			return getDiagram().getPlatform();
		} else if (Diagram.RESOLUTION.equals(propertyId)){
			return getDiagram().getResolution();
		}
		return super.getPropertyValue(propertyId);
	}
	
	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (Diagram.RESOLUTION.equals(propertyId)){
			getDiagram().setResolution((Dimension) value);
		} else
			super.setPropertyValue(propertyId, value);
	}
}
