package com.forgedui.editor.edit.policy;

import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.CoverFlowView;
import com.forgedui.model.titanium.DashboardItem;
import com.forgedui.model.titanium.DashboardView;
import com.forgedui.model.titanium.ImageView;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.model.titanium.ScrollView;
import com.forgedui.model.titanium.ScrollableView;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.Tab;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.TabbedBar;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Toolbar;
import com.forgedui.model.titanium.View;
import com.forgedui.model.titanium.WebView;
import com.forgedui.model.titanium.Window;
import com.forgedui.model.titanium.ipad.Popover;


/**
 * A class that will validate the items dropping validation
 * at the system. Its too annoying to validate this at the level
 * of the edit policy and the edit part.
 * 
 * @author Tareq Doufish
 *
 */
public class ComponentValidator {
	
	/**
	 * This method takes the two items and will try 
	 * to validate things, the first argument is the 
	 * object that we are going to validate, the second 
	 * argument is the item that is going to hold it.
	 * @param newObject
	 * @param container
	 * @return
	 */
	public boolean validate(Object newObject, Object container) { 
		if(container instanceof TitleBar)
			return newObject instanceof Button;
		
		if(!(container instanceof Container))
			return false;
		
		if((container instanceof Popover))
			return ((Popover) container).getChildren().size() == 0
					&& (newObject instanceof View
							|| newObject instanceof CoverFlowView
							|| newObject instanceof DashboardView
							|| newObject instanceof ScrollableView
							|| newObject instanceof ScrollView
							|| newObject instanceof TableView
							|| newObject instanceof ImageView || newObject instanceof WebView);
		
		if (container instanceof Screen){
			return newObject instanceof Window || newObject instanceof TabGroup;
		}
		
		if (newObject instanceof TabGroup
				|| newObject instanceof Tab){
			return false;
		}
		
		if (newObject instanceof Window && container instanceof Window) {
			return false;
		}
		
		if (newObject instanceof TableViewRow){
			return container instanceof TableView || container instanceof TableViewSection;
		}
		
		if (newObject instanceof TableViewSection ){
			return container instanceof TableView;
		}
		
		//FIXME allow to set footer/header view
		if (container instanceof TableView){
			return newObject instanceof TableView || newObject instanceof TableViewSection;
		}
		
		if (newObject instanceof TitleBar) {
			return container instanceof Window;
		}
		
		if (newObject instanceof Toolbar) {
			if (container instanceof Window){
				for (int i = 0; i < ((Window)container).getChildren().size(); i++) {
					if (((Window)container).getChildren().get(i) instanceof Toolbar) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		
		if (container instanceof Toolbar) {
			return (newObject instanceof Button || newObject instanceof TabbedBar || newObject instanceof ButtonBar);
		}
		
		if (newObject instanceof DashboardItem) {
			return container instanceof DashboardView;
		}
		
		if (container instanceof DashboardView) {
			return newObject instanceof DashboardItem;
		}
		
		// We don't want to drop anything on the search bar unless there is something I don't know about.
		if (container instanceof SearchBar) { 
			return false;
		}
		
		if (container instanceof ButtonBar) { 
			return newObject instanceof TabbedBarButton;
		}
		
		if (container instanceof TabbedBarButton){
			return newObject instanceof TabbedBarButton;
		}
		
		if (container instanceof ScrollableView) { 
			return isFullScreenView(newObject);
		}
		
		if (container instanceof Picker) { 
			return false;
		}
		
		if (newObject instanceof PickerColumn){
			return container instanceof Picker;
		}
		
		if (newObject instanceof PickerRow){
			return container instanceof PickerColumn;
		}
		
		if (container instanceof PickerColumn){
			return newObject instanceof PickerRow;
		}
		
		if (container instanceof PickerRow){
			PickerRow row = (PickerRow)container;
			if (row.getPlatform().isIPhone() && row.canEdit()){
				return newObject instanceof ImageView;
			} else {
				return false;
			}
		}
		
		if (isFullScreenView(newObject)){
			return isFullScreenView(container);
		}
		
		return true;
	}
	
	public static boolean isFullScreenView(Object e){
		return (e instanceof Window//full screen elements
				|| e instanceof ScrollableView
				|| e instanceof ScrollView
				|| e instanceof DashboardView
				|| e instanceof WebView
				|| e instanceof View
				//|| e instanceof ImageView
				|| e instanceof TableView
				|| e instanceof CoverFlowView);
	}
	
	/**
	 * Some elements have setBlaBlaBlaView.
	 * We should validate if the child is view
	 * @param e
	 * @return
	 */
	public static boolean isView(Object e){
		return (e instanceof ScrollableView
				|| e instanceof ScrollView
				|| e instanceof DashboardView
				|| e instanceof WebView
				|| e instanceof View
				|| e instanceof ImageView
				|| e instanceof TableView
				|| e instanceof CoverFlowView);
	}
	
	public boolean isAncestor(Element parent, Element child) {
		if(child == null)
			return false;
		else if(child.getParent() == parent)
			return true;
		else
			return isAncestor(parent,child.getParent());
	}
	
}
