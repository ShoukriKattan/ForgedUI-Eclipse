package com.forgedui.editor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.forgedui.model.titanium.AlertDialogButton;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.EmailDialog;
import com.forgedui.model.titanium.ImageView;
import com.forgedui.model.titanium.Label;
import com.forgedui.model.titanium.OptionDialogButton;
import com.forgedui.model.titanium.ProgressBar;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.Slider;
import com.forgedui.model.titanium.Switch;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.model.titanium.TitleBar;

/**
 * This class will help us weather this property is a visual one 
 * or not.
 * 
 * @author Tareq Doufish
 *
 */
public class PropertyType {

	// Register a class with a visual attribute.
	private static Map<String, List<String>> modelPropertyMap = new HashMap<String, List<String>>();

	// This would map all the basic element visual properties.
	private static List<String> basicElementVisualProperties = new ArrayList<String>();

	// I will register only one property here which is the button, we can do other elements 
	// later.
	static {
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BORDER_RADIUS);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BORDER_WIDTH);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BORDER_COLOR);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_OPACITY);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BACKGROUND_SELECTED_COLOR);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BACKGROUND_FOCUSED_COLOR);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BACKGROUND_DISABLED_COLOR);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BACKGROUND_COLOR);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BACKGROUND_IMAGE);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_BACKGROUND_DISABLED_IMAGE);
		basicElementVisualProperties.add(TitaniumUIElement.PROPERTY_FONT);
		// Adding the font property as the basic property name here.
		basicElementVisualProperties.add("Font");
		
		// Doing the button element.
		List<String> properties = new ArrayList<String>();
		properties.add(Button.TITLE_PROP);
		properties.add(Button.TITLE_ID_PROP);
		properties.add(Button.PROP_ENABLED);
		properties.add(Button.PROP_COLOR);
		properties.add("Font");
		properties.add(Button.PROP_IMAGE);
		modelPropertyMap.put(Button.class.getName(),properties);
		
		// Adding the other attributes for the title bar stuff.
		properties = new ArrayList<String>();
		properties.add(TitleBar.PROPERTY_BAR_COLOR);
		properties.add(TitleBar.PROP_TITLE);
		properties.add(TitleBar.PROP_TITLE_ID);
		properties.add(TitleBar.PROPERTY_BAR_IMAGE);
		properties.add(TitleBar.PROPERTY_TITLE_IMAGE);
		properties.add(TitleBar.PROPERTY_TITLEPROMPT);
		properties.add(TitleBar.PROP_BACK_B_TITLE);
		properties.add(TitleBar.PROP_BACK_B_IMAGE);
		modelPropertyMap.put(TitleBar.class.getName(),properties);
		
		// Adding the label attributes.
		properties = new ArrayList<String>();
		properties.add(Label.PROP_COLOR);
		properties.add(Label.PROP_HIGHLIGHTED_COLOR);
		properties.add(Label.TEXT_PROP);
		properties.add(Label.TEXT_ID_PROP);
		properties.add(Label.PROP_BACKGROUNDPADDING_RIGHT);
		properties.add(Label.PROP_BACKGROUNDPADDING_LEFT);
		properties.add(Label.PROP_BACKGROUNDPADDING_BOTTOM);
		properties.add(Label.PROP_BACKGROUNDPADDING_TOP);
		properties.add(Label.PROP_ELLIPSIZE);
		properties.add(Label.PROP_TEXT_ALIGN);
		properties.add(Label.PROP_WORD_WRAP);
		modelPropertyMap.put(Label.class.getName(),properties);
		
		// Adding the switch element.
		properties = new ArrayList<String>();
		properties.add(Switch.PROPERTY_SWITCH_VALUE);
		properties.add(Switch.PROPERTY_TITLE_OFF);
		properties.add(Switch.PROPERTY_TITLE_ON);
		properties.add(Switch.PROPERTY_TITLE);
		properties.add(Switch.PROPERTY_SWITCH_STYLE);
		modelPropertyMap.put(Switch.class.getName(),properties);
		
		// Adding the table view row at the system.
		properties = new ArrayList<String>();
		properties.add(TableViewRow.PROP_SELECTED_BACKGROUND_COLOR);
		properties.add(TableViewRow.PROP_COLOR);
		properties.add(TableViewRow.PROP_SELECTED_COLOR);
		properties.add(TableViewRow.PROP_SELECTED_BACKGROUND_IMAGE);
		properties.add(TableViewRow.PROP_RIGHT_IMAGE);
		properties.add(TableViewRow.PROP_LEFT_IMAGE);
		properties.add(TableViewRow.TITLE_PROP);
		properties.add(TableViewRow.PROP_HAS_CHILD);
		properties.add(TableViewRow.PROP_HAS_CHECK);
		properties.add(TableViewRow.PROP_HAS_DETAIL);
		properties.add(TableViewRow.PROP_INDENTATION_LEVEL);
		modelPropertyMap.put(TableViewRow.class.getName(),properties);
		
		// The table view section.
		properties = new ArrayList<String>();
		properties.add(TableViewSection.PROP_FOOTER_TITLE);
		properties.add(TableViewSection.PROP_HEADER_TITLE);
		modelPropertyMap.put(TableViewSection.class.getName(),properties);
		
		
		// Registering the new items and components at the system.
		// TODO : Please check weather we do have more elements here or not.
		// Adding the search bar.
		properties = new ArrayList<String>();
		properties.add(SearchBar.PROP_BAR_COLOR);
		properties.add(SearchBar.PROP_HINT_TEXT);
		properties.add(SearchBar.PROP_HINT_TEXT_ID);
		properties.add(SearchBar.PROP_SHOW_CANCEL);
		properties.add(SearchBar.PROP_PROMT);
		properties.add(SearchBar.PROP_PROMT_ID);
		modelPropertyMap.put(SearchBar.class.getName(),properties);
		
		// Adding the slider.
		properties = new ArrayList<String>();
		properties.add(Slider.PROP_MIN);
		properties.add(Slider.PROP_MAX);
		properties.add(Slider.PROP_MIN_RANGE);
		properties.add(Slider.PROP_MAX_RANGE);
		properties.add(Slider.PROP_VALUE);
		properties.add(Slider.PROP_THUMB_IMAGE);
		properties.add(Slider.PROP_LEFT_TRACK_IMAGE);
		properties.add(Slider.PROP_RIGHT_TRACK_IMAGE);
		properties.add(Slider.PROP_SELECTED_THUMB_IMAGE);
		properties.add(Slider.PROP_SELECTED_LEFT_TRACK_IMAGE);
		properties.add(Slider.PROP_SELECTED_RIGHT_TRACK_IMAGE);
		properties.add(Slider.PROP_DISABLED_THUMB_IMAGE);
		properties.add(Slider.PROP_DISABLED_LEFT_TRACK_IMAGE);
		properties.add(Slider.PROP_DISABLED_RIGHT_TRACK_IMAGE);
		properties.add(Slider.PROP_HIGHLIGHTED_THUMB_IMAGE);
		properties.add(Slider.PROP_HIGHLIGHTED_LEFT_TRACK_IMAGE);
		properties.add(Slider.PROP_HIGHLIGHTED_RIGHT_TRACK_IMAGE);
		properties.add(Slider.PROPERTY_NAME);
		modelPropertyMap.put(Slider.class.getName(),properties);
		
		// Adding the progressbar.
		properties = new ArrayList<String>();
		properties.add(ProgressBar.PROP_MIN);
		properties.add(ProgressBar.PROP_MAX);
		properties.add(ProgressBar.PROP_VALUE);
		properties.add(ProgressBar.PROP_MESSAGE);
		properties.add(ProgressBar.PROP_COLOR);
		modelPropertyMap.put(ProgressBar.class.getName(),properties);
		
		// Adding the table view.
		properties = new ArrayList<String>();
		properties.add(com.forgedui.model.titanium.TableView.PROP_SEPARATOR_COLOR);
		properties.add(com.forgedui.model.titanium.TableView.PROP_FOOTER_TITLE);
		properties.add(com.forgedui.model.titanium.TableView.PROP_HEADER_TITLE);
		modelPropertyMap.put(com.forgedui.model.titanium.TableView.class.getName(),properties);
		
		// Adding the scroll bar view.
		properties = new ArrayList<String>();
		properties.add(com.forgedui.model.titanium.ScrollableView.PROP_PAGING_CONTROL_COLOR);
		properties.add(com.forgedui.model.titanium.ScrollableView.PROP_PAGING_CONTROL_HEIGHT);
		properties.add(com.forgedui.model.titanium.ScrollableView.PROP_SHOW_PAGING_CONTROL);
		modelPropertyMap.put(com.forgedui.model.titanium.ScrollableView.class.getName(),properties);
		
		
		// Register the scroll view element.
		properties = new ArrayList<String>();
		properties.add(com.forgedui.model.titanium.ScrollView.PROP_ZOOM_SCALE);
		properties.add(com.forgedui.model.titanium.ScrollView.PROP_H_SCROLL);
		properties.add(com.forgedui.model.titanium.ScrollView.PROP_V_SCROLL);
		properties.add(com.forgedui.model.titanium.ScrollView.PROP_SCROLL_TYPE);
		properties.add(com.forgedui.model.titanium.ScrollView.PROP_CONTENT_WIDTH);
		properties.add(com.forgedui.model.titanium.ScrollView.PROP_CONTENT_HEIGHT);
		modelPropertyMap.put(com.forgedui.model.titanium.ScrollView.class.getName(),properties);
		
		
		// Adding the image view.
		properties = new ArrayList<String>();
		properties.add(ImageView.PROP_IMAGE);
		modelPropertyMap.put(ImageView.class.getName(), properties);
		
		// Register the text field and the text area here.
		properties = new ArrayList<String>();
		properties.add(com.forgedui.model.titanium.TextArea.VALUE);
		modelPropertyMap.put(com.forgedui.model.titanium.TextArea.class.getName(),properties);
		
		properties = new ArrayList<String>();
		properties.add(com.forgedui.model.titanium.TextField.HINT_TEXT);
		modelPropertyMap.put(com.forgedui.model.titanium.TextField.class.getName(),properties);
		
		
		// Doing the dashboard items.
		properties = new ArrayList<String>();
		properties.add(com.forgedui.model.titanium.DashboardItem.PROP_BADGE);
		properties.add(com.forgedui.model.titanium.DashboardItem.PROP_IMAGE);
		modelPropertyMap.put(com.forgedui.model.titanium.DashboardItem.class.getName(),properties);
		
		// Adding the option button stuff.
		properties = new ArrayList<String>();
		properties.add(OptionDialogButton.TITLE_PROP);
		modelPropertyMap.put(OptionDialogButton.class.getName(),properties);
		modelPropertyMap.put(AlertDialogButton.class.getName(),properties);
		
		// Adding the alert dialog stuff.
		properties = new ArrayList<String>();
		properties.add(com.forgedui.model.titanium.AlertDialog.TITLE_PROP);
		properties.add(com.forgedui.model.titanium.AlertDialog.MESSAGE_PROP);
		properties.add(com.forgedui.model.titanium.AlertDialog.TITLE_ID_PROP);
		properties.add(com.forgedui.model.titanium.AlertDialog.MESSAGE_ID_PROP);
		properties.add(com.forgedui.model.titanium.AlertDialog.OK_PROP);
		properties.add(com.forgedui.model.titanium.AlertDialog.OK_ID_PROP);
		properties.add(com.forgedui.model.titanium.AlertDialog.CANCEL_PROP);
		modelPropertyMap.put(com.forgedui.model.titanium.AlertDialog.class.getName(),properties);
		
		// Adding the alert dialog stuff.
		properties = new ArrayList<String>();
		properties.add(EmailDialog.BAR_COLOR);
		properties.add(EmailDialog.BCC_RECIPIENTS);
		properties.add(EmailDialog.CC_RECIPIENTS);
		properties.add(EmailDialog.TO_RECIPIENTS);
		properties.add(EmailDialog.SUBJECT);
		properties.add(EmailDialog.MESSAGE_BODY);
		modelPropertyMap.put(EmailDialog.class.getName(),properties);
		
		// Adding the picker row here.
		properties = new ArrayList<String>();
		properties.add(com.forgedui.model.titanium.PickerRow.TITLE_PROP);
		properties.add(com.forgedui.model.titanium.PickerRow.SELECTED_PROP);
		modelPropertyMap.put(com.forgedui.model.titanium.PickerRow.class.getName(),properties);
		
	}
	
	public static boolean isVisualProperty(String className, String property) { 
		// We first check for the basic element properties, then we will search 
		// at the other element properties.
		for (String value :basicElementVisualProperties) { 
			if (value.equals(property))
				return true;
		}
		
		if (className == null)
			return false;
		
		// Then proceed with the other element.
		List<String> properties = modelPropertyMap.get(className);
		if (properties != null) {
			for (String value :properties) { 
				if (value.equals(property))
					return true;
			}
		}
		return false;
	}
}
