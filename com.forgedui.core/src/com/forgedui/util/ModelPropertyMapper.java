package com.forgedui.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.CoverFlowView;
import com.forgedui.model.titanium.DashboardItem;
import com.forgedui.model.titanium.EmailDialog;
import com.forgedui.model.titanium.ImageView;
import com.forgedui.model.titanium.Label;
import com.forgedui.model.titanium.MoreTab;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.model.titanium.ProgressBar;
import com.forgedui.model.titanium.ScrollableView;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.Slider;
import com.forgedui.model.titanium.Tab;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TextField;
import com.forgedui.model.titanium.TitaniumUIBaseElement;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.TitaniumUIElement;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Toolbar;
import com.forgedui.model.titanium.Window;

public class ModelPropertyMapper {

	public static Set<String> getFilteredProperties(Class<? extends TitaniumUIBaseElement> clazz) {
		Set<String> props = new HashSet<String>();

		// Add type for all TitaniumUIBaseElement classes
		props.add("type");

		if (MoreTab.class.isAssignableFrom(clazz)) {
			props.add("title");
			props.add("icon");
		}

		if (PickerColumn.class.isAssignableFrom(clazz)
				|| PickerRow.class.isAssignableFrom(clazz) || TabGroup.class.isAssignableFrom(clazz)
				|| TableViewRow.class.isAssignableFrom(clazz) || TableViewSection.class.isAssignableFrom(clazz)
				|| Toolbar.class.isAssignableFrom(clazz)) {
			props.add(TitaniumUIBaseElement.PROP_BOUNDS);
		}

		if (ProgressBar.class.isAssignableFrom(clazz)) {
			props.add("min");
			props.add("max");
			props.add("value");
		}

		if (ScrollableView.class.isAssignableFrom(clazz)) {
			props.add("currentPage");
		}

		if (Tab.class.isAssignableFrom(clazz)) {
			props.add("window");
			props.add(TitaniumUIBaseElement.PROP_BOUNDS);
		}

		if (TabbedBarButton.class.isAssignableFrom(clazz)) {
			props.add("selected");
			props.add("type");
		}

		if (TitaniumUIBoundedElement.class.isAssignableFrom(clazz)) {
			props.add("top");
			props.add("left");
			props.add("bottom");
			props.add("right");
			props.add("height");
			props.add("width");
		}
		
		if (Toolbar.class.isAssignableFrom(clazz)) {
			props.add(Toolbar.ITEMS_PROP);
		} else if (CoverFlowView.class.isAssignableFrom(clazz)) {
			props.add(CoverFlowView.IMAGES_PROP);
		} else if (ImageView.class.isAssignableFrom(clazz)) {
			props.add(ImageView.IMAGES_PROP);
		} else if (Window.class.isAssignableFrom(clazz)) {
			props.add(Window.ORIENTATION_MODES_PROP);
		}

		return props;
	}

	public static Set<String> getHandlerProperties(Class<? extends TitaniumUIBaseElement> clazz) {
		Set<String> props = new HashSet<String>();

		if (Button.class.isAssignableFrom(clazz)) {
			props.add(Button.COLOR);
			props.add(Button.IMAGE);
			props.add(Button.SELECTED_COLOR);
			props.add(Button.STYLE);
		}

		if (DashboardItem.class.isAssignableFrom(clazz)) {
			props.add("image");
			props.add("selectedImage");
		}

		if (EmailDialog.class.isAssignableFrom(clazz)) {
			props.add(EmailDialog.BAR_COLOR);
		}

		if (ImageView.class.isAssignableFrom(clazz)) {
			props.add(ImageView.PROP_IMAGE);
			props.add(ImageView.PROP_DEFAULT_IMAGE);
		}

		if (Label.class.isAssignableFrom(clazz)) {
			props.add("color");
			props.add("shadowColor");
			props.add("highlightedColor");
		}

		if (Picker.class.isAssignableFrom(clazz)) {
			props.add(Picker.PICKER_TYPE);
		}

		if (ProgressBar.class.isAssignableFrom(clazz)) {
			props.add(ProgressBar.PROP_COLOR);
		}
		
		if (TextField.class.isAssignableFrom(clazz)) {
			props.add(TextField.PRO_KEYBOARD_TOOLBAR_COLOR);
		}

		if (ScrollableView.class.isAssignableFrom(clazz)) {
			props.add("pagingControlColor");
		}

		if (SearchBar.class.isAssignableFrom(clazz)) {
			props.add(SearchBar.PROP_BAR_COLOR);
		}

		if (Slider.class.isAssignableFrom(clazz)) {
			props.add(Slider.PROP_THUMB_IMAGE);
			props.add(Slider.PROP_LEFT_TRACK_IMAGE);
			props.add(Slider.PROP_RIGHT_TRACK_IMAGE);
			props.add(Slider.PROP_SELECTED_THUMB_IMAGE);
			props.add(Slider.PROP_SELECTED_LEFT_TRACK_IMAGE);
			props.add(Slider.PROP_SELECTED_RIGHT_TRACK_IMAGE);
			props.add(Slider.PROP_DISABLED_THUMB_IMAGE);
			props.add(Slider.PROP_DISABLED_LEFT_TRACK_IMAGE);
			props.add(Slider.PROP_DISABLED_RIGHT_TRACK_IMAGE);
			props.add(Slider.PROP_HIGHLIGHTED_THUMB_IMAGE);
			props.add(Slider.PROP_HIGHLIGHTED_LEFT_TRACK_IMAGE);
			props.add(Slider.PROP_HIGHLIGHTED_RIGHT_TRACK_IMAGE);
		}

		if (Tab.class.isAssignableFrom(clazz)) {
			props.add(Tab.ICON_PROP);
		}

		if (TabbedBarButton.class.isAssignableFrom(clazz)) {
			props.add(TabbedBarButton.IMAGE_PROP);
		}

		if (TabGroup.class.isAssignableFrom(clazz)) {
			props.add(TabGroup.PROP_BAR_COLOR);
		}

		if (TableView.class.isAssignableFrom(clazz)) {
			props.add(TableView.PROP_SEPARATOR_COLOR);
		}

		if (TableViewRow.class.isAssignableFrom(clazz)) {
			props.add(TableViewRow.PROP_SELECTED_BACKGROUND_COLOR);
			props.add(TableViewRow.PROP_COLOR);
			props.add(TableViewRow.PROP_SELECTED_COLOR);
			props.add(TableViewRow.PROP_SELECTED_BACKGROUND_IMAGE);
			props.add(TableViewRow.PROP_RIGHT_IMAGE);
			props.add(TableViewRow.PROP_LEFT_IMAGE);
		}

		if (TitaniumUIElement.class.isAssignableFrom(clazz)) {
			props.add(TitaniumUIElement.PROPERTY_BORDER_COLOR);
			props.add(TitaniumUIElement.PROPERTY_BACKGROUND_SELECTED_COLOR);
			props.add(TitaniumUIElement.PROPERTY_BACKGROUND_FOCUSED_COLOR);
			props.add(TitaniumUIElement.PROPERTY_BACKGROUND_DISABLED_COLOR);
			props.add(TitaniumUIElement.PROPERTY_BACKGROUND_COLOR);
			props.add(TitaniumUIElement.PROPERTY_BACKGROUND_IMAGE);
			props.add(TitaniumUIElement.PROPERTY_BACKGROUND_SELECTED_IMAGE);
			props.add(TitaniumUIElement.PROPERTY_BACKGROUND_DISABLED_IMAGE);
			props.add(TitaniumUIElement.PROPERTY_BACKGROUND_FOCUSED_IMAGE);
		}

		if (TitleBar.class.isAssignableFrom(clazz)) {
			props.add(TitleBar.PROPERTY_BAR_IMAGE);
			props.add(TitleBar.PROPERTY_TITLE_IMAGE);
			props.add(TitleBar.PROPERTY_BAR_COLOR);
			props.add(TitleBar.PROP_BACK_B_IMAGE);
		}

		return props;
	}

	public static List<String> getJssProperties(Class<? extends TitaniumUIBaseElement> class1) {
		List<String> props = new ArrayList<String>();

		props.add("height");
		props.add("width");
		props.add("top");
		props.add("left");
		props.add("right");
		props.add("bottom");
		props.add("text");

		return props;
	}

}
