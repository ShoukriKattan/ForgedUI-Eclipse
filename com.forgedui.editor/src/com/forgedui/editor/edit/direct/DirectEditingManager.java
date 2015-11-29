package com.forgedui.editor.edit.direct;

import com.forgedui.model.titanium.AlertDialog;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.Label;
import com.forgedui.model.titanium.OptionDialogButton;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.Tab;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TitleBar;

/**
 * A simple manager that is responsible of 
 * getting the values of the string to directly 
 * get edit.
 * All of the components that we are going to directly 
 * editing must go here.
 * @author Tareq Doufish
 *
 */
public class DirectEditingManager {

	/**
	 * Getting the element direct editable 
	 * text.
	 * @param element
	 * @return
	 */
	public static String getElementDirectText(Object element) { 
		String retString = "";
		if (element == null)
			return null;
		
		// We will have to check according to the instance...
		if (element instanceof Label) { 
			retString = ((Label)element).getText();
		} else if (element instanceof Button) { 
			retString = ((Button)element).getTitle();
		} else if(element instanceof TableViewRow) { 
			retString = ((TableViewRow)element).getTitle();
		} else if (element instanceof TabbedBarButton) { 
			retString = ((TabbedBarButton)element).getTitle();
		} else if (element instanceof PickerRow) { 
			retString = ((PickerRow)element).getTitle();
		} else if (element instanceof TitleBar) { 
			retString = ((TitleBar)element).getTitle();
		} else if (element instanceof AlertDialog) { 
			retString = ((AlertDialog) element).getMessage();
		} else if (element instanceof SearchBar) { 
			retString = ((SearchBar)element).getHintText();
		} else if (element instanceof Tab) { 
			retString = ((Tab)element).getTitle();
		} else if (element instanceof OptionDialogButton) { 
			retString = ((OptionDialogButton)element).getTitle();
		}
		
		
		return retString;
	}
	
	public static String getOldValue(Object element) { 
		if (element instanceof Label) { 
			Label managedElement = (Label) element;
			return managedElement.getText();
		} else if (element instanceof Button) { 
			Button managedElement = (Button) element;
			return managedElement.getTitle();
		} else if(element instanceof TableViewRow) { 
			TableViewRow managedElement = (TableViewRow) element;
			return managedElement.getTitle();
		} else if (element instanceof TabbedBarButton) { 
			TabbedBarButton managedElement = (TabbedBarButton) element;
			return managedElement.getTitle();
		} else if (element instanceof PickerRow) { 
			PickerRow managedElement = (PickerRow) element;
			return managedElement.getTitle();
		} else if (element instanceof TitleBar) { 
			TitleBar managedElement = (TitleBar) element;
			return managedElement.getTitle();
		} else if (element instanceof AlertDialog) {
			AlertDialog managedElement = (AlertDialog) element;
			return managedElement.getMessage();
		} else if (element instanceof SearchBar) {
			SearchBar managedElement = (SearchBar) element;
			return managedElement.getHintText();
		} else if (element instanceof Tab) { 
			Tab managedElement = (Tab) element;
			return managedElement.getTitle();
		} else if (element instanceof OptionDialogButton) { 
			OptionDialogButton managedElement = (OptionDialogButton) element;
			return managedElement.getTitle();
		}
		return null;
	}
	
	public static void setElementDirectText(Object element, String newValue) { 
		if (element instanceof Label) { 
			Label managedElement = (Label) element;
			managedElement.setText(newValue);
		} else if (element instanceof Button) { 
			Button managedElement = (Button) element;
			managedElement.setTitle(newValue);
		} else if(element instanceof TableViewRow) {
			TableViewRow managedElement = (TableViewRow) element;
			managedElement.setTitle(newValue);
		} else if (element instanceof TabbedBarButton) { 
			TabbedBarButton managedElement = (TabbedBarButton) element;
			managedElement.setTitle(newValue);
		} else if (element instanceof PickerRow) { 
			PickerRow managedElement = (PickerRow) element;
			managedElement.setTitle(newValue);
		} else if (element instanceof TitleBar) {
			TitleBar managedElement = (TitleBar) element;
			managedElement.setTitle(newValue);
		} else if (element instanceof AlertDialog) {
			AlertDialog managedElement = (AlertDialog) element;
			managedElement.setMessage(newValue);
		} else if (element instanceof SearchBar) {
			SearchBar managedElement = (SearchBar) element;
			managedElement.setHintText(newValue);
		} else if (element instanceof Tab) { 
			Tab managedElement = (Tab) element;
			managedElement.setTitle(newValue);
		} else if (element instanceof OptionDialogButton) {
			OptionDialogButton managedElement = (OptionDialogButton) element;
			managedElement.setTitle(newValue);
		}
		
		// We have to deal with the other elements here when time permits :p.
	}
}
