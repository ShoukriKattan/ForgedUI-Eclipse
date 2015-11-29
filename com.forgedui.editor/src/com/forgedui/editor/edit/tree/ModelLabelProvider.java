// LICENSE
package com.forgedui.editor.edit.tree;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.model.Container;
import com.forgedui.model.Element;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.AlertDialog;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.CoverFlowView;
import com.forgedui.model.titanium.DashboardItem;
import com.forgedui.model.titanium.DashboardView;
import com.forgedui.model.titanium.EmailDialog;
import com.forgedui.model.titanium.ImageView;
import com.forgedui.model.titanium.Label;
import com.forgedui.model.titanium.OptionDialog;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.model.titanium.ProgressBar;
import com.forgedui.model.titanium.ScrollView;
import com.forgedui.model.titanium.ScrollableView;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.Slider;
import com.forgedui.model.titanium.Switch;
import com.forgedui.model.titanium.Tab;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.TabbedBar;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TextArea;
import com.forgedui.model.titanium.TextField;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Toolbar;
import com.forgedui.model.titanium.View;
import com.forgedui.model.titanium.WebView;
import com.forgedui.model.titanium.Window;
import com.forgedui.model.titanium.ipad.Popover;
import com.forgedui.model.titanium.ipad.SplitWindow;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ModelLabelProvider {
	
	private static Image CONTAINER = GUIEditorPlugin.getImageDescriptor("icons/container.gif").createImage();
	private static Image ELEMENT = GUIEditorPlugin.getImageDescriptor("icons/flame.png").createImage();
	
	// Doing the rest of the images for the outline view.
	private static Image SCREEN_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/flame.png").createImage();
	private static Image WINDOW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/window.png").createImage();
	private static Image TITLEBAR_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/toolbar.png").createImage();
	private static Image BUTTON_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/buttonview.png").createImage();
	private static Image LABEL_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/labelview.png").createImage();
	private static Image WEBVIEW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/webview.png").createImage();
	private static Image SLIDER_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/sliderview.png").createImage();
	private static Image VIEW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/view.png").createImage();
	private static Image TAB_GROUP_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/tabgroup.png").createImage();
	private static Image TAB_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/tab.png").createImage();
	private static Image PICKER_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/pickerview.png").createImage();
	private static Image PICKER_COL_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/pickercolumn.png").createImage();
	private static Image PICKER_ROW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/pickerrow.png").createImage();
	private static Image SCROLL_VIEW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/scrollview.png").createImage();
	private static Image SCROLLABLE_VIEW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/scrollableview.png").createImage();
	
	private static Image COVER_FLOW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/coverflowview.png").createImage();
	private static Image SWITCH_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/switchview.png").createImage();
	private static Image IMAGEVIEW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/imageview.png").createImage();
	
	private static Image TABLEVIEW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/tableview.png").createImage();
	private static Image TABLEROW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/tableviewrow.png").createImage();
	private static Image TABLESECTION_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/tableviewsection.png").createImage();
	
	private static Image TEXTAREA_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/textview.png").createImage();
	private static Image TEXTFIELD_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/textfieldview.png").createImage();
	
	private static Image DASHBOARD_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/dashboardview.png").createImage();
	private static Image DASHBOARD_ITEM_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/dashboarditem.png").createImage();
	
	private static Image PROGRESS_BAR_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/progressbar.png").createImage();
	
	private static Image BUTTON_BAR_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/buttonbarview.png").createImage();
	private static Image SEARCH_BAR_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/searchbar.png").createImage();
	private static Image TABBED_BAR_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/tabbedbar.png").createImage();
	private static Image TOOL_BAR_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/toolbar.png").createImage();

	private static Image ALERTDIALOG_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/alertdialog.png").createImage();
	private static Image OPTIONDIALOG_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/optiondialog.png").createImage();
	private static Image EMAILDIALOG_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/emaildialog.png").createImage();
	private static Image POPOVER_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/popover.png").createImage();
	private static Image SPLIT_WINDOW_IMAGE = GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/splitwindow.png").createImage();
	
	public static String getText(Object obj){
		if (obj instanceof Tab){
			return Utils.getString(((Tab)obj).getTitle(), "");
		} else if (obj instanceof Button){
			return Utils.getString(((Button)obj).getTitle(), "");
		} else if (obj instanceof Label){
			return Utils.getString(((Label)obj).getText(), "");
		} else if (obj instanceof PickerRow){
			return Utils.getString(((PickerRow)obj).getTitle(), "");
		} else if (obj instanceof Screen){
			Screen screen = (Screen)obj;
			Dimension res = screen.getDiagram().getResForText();
			if(res == null)
				res = screen.getResolution();
			return screen.getPlatform().name()
				+ " " + res.width
				+ "x" + res.height;
		}
		return Utils.getString(((Element)obj).getName(), "");
	}
	
	public static Image getIcon(Object obj){
		if (obj instanceof Screen){
			return SCREEN_IMAGE;
		} else if (obj instanceof Window) {
			return WINDOW_IMAGE;
		} else if (obj instanceof TitleBar) {
			return TITLEBAR_IMAGE;
		} else if (obj instanceof Button) {
			return BUTTON_IMAGE;
		} else if (obj instanceof Label) {
			return LABEL_IMAGE;
		} else if (obj instanceof WebView) {
			return WEBVIEW_IMAGE;
		} else if (obj instanceof Slider) {
			return SLIDER_IMAGE;
		} else if (obj instanceof TabGroup) {
			return TAB_GROUP_IMAGE;
		} else if (obj instanceof View) {
			return VIEW_IMAGE;
		} else if (obj instanceof Tab) {
			return TAB_IMAGE;
		} else if (obj instanceof CoverFlowView) {
			return COVER_FLOW_IMAGE;
		} else if (obj instanceof Switch) {
			return SWITCH_IMAGE;
		} else if (obj instanceof ImageView) {
			return IMAGEVIEW_IMAGE;
		} else if (obj instanceof TableView) {
			return TABLEVIEW_IMAGE;
		} else if (obj instanceof TableViewRow) {
			return TABLEROW_IMAGE;
		} else if (obj instanceof TableViewSection) {
			return TABLESECTION_IMAGE;
		} else if (obj instanceof TextArea) {
			return TEXTAREA_IMAGE;
		} else if (obj instanceof TextField) {
			return TEXTFIELD_IMAGE;
		} else if (obj instanceof Picker) {
			return PICKER_IMAGE;
		} else if (obj instanceof PickerColumn) {
			return PICKER_COL_IMAGE;
		} else if (obj instanceof PickerRow) {
			return PICKER_ROW_IMAGE;
		} else if (obj instanceof ScrollView) {
			return SCROLL_VIEW_IMAGE;
		} else if (obj instanceof ScrollableView) {
			return SCROLLABLE_VIEW_IMAGE;
		} else if (obj instanceof DashboardView) {
			return DASHBOARD_IMAGE;
		} else if (obj instanceof DashboardItem) {
			return DASHBOARD_ITEM_IMAGE;
		} else if (obj instanceof ProgressBar) {
			return PROGRESS_BAR_IMAGE;
		} else if (obj instanceof ButtonBar) {
			return BUTTON_BAR_IMAGE;
		} else if (obj instanceof SearchBar) {
			return SEARCH_BAR_IMAGE;
		} else if (obj instanceof TabbedBar) {
			return TABBED_BAR_IMAGE;
		} else if (obj instanceof Toolbar) {
			return TOOL_BAR_IMAGE;
		} else if (obj instanceof TabbedBarButton) {
			return TAB_IMAGE;
		} else if (obj instanceof AlertDialog) {
			return ALERTDIALOG_IMAGE;
		} else if (obj instanceof OptionDialog) {
			return OPTIONDIALOG_IMAGE;
		} else if (obj instanceof Popover) {
			return POPOVER_IMAGE;
		} else if (obj instanceof SplitWindow) {
			return SPLIT_WINDOW_IMAGE;
		} else if (obj instanceof EmailDialog) {
			return EMAILDIALOG_IMAGE;
		} else if (obj instanceof Container) {
			return CONTAINER;
		} else 
			return ELEMENT;
	}

}
