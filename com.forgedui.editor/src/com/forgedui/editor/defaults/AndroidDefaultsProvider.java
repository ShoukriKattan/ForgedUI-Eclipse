// LICENSE
package com.forgedui.editor.defaults;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import com.forgedui.editor.edit.ExternalMoreTab;
import com.forgedui.editor.edit.ExternalTab;
import com.forgedui.editor.edit.ExternalTabGroup;
import com.forgedui.model.titanium.AlertDialog;
import com.forgedui.model.titanium.AlertDialogButton;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.CoverFlowView;
import com.forgedui.model.titanium.DashboardItem;
import com.forgedui.model.titanium.DashboardView;
import com.forgedui.model.titanium.OptionDialogButton;
import com.forgedui.model.titanium.EmailDialog;
import com.forgedui.model.titanium.ImageView;
import com.forgedui.model.titanium.Label;
import com.forgedui.model.titanium.MoreTab;
import com.forgedui.model.titanium.OptionDialog;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.model.titanium.Platform;
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

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
//FIXME not implemented for now, so just extends iPhone defaults
public class AndroidDefaultsProvider extends IphoneDefaultsProvider {
	
	private static final Platform p = Platform.Android;
	
	static {
		colorMap.put(getKey(p, BORDER), BLACK);
		colorMap.put(getKey(p, BACKGROUND), WHITE);
		colorMap.put(getKey(p, TEXT), BLACK);
		
		//Button
		fontMap.put(getKey(p, Button.class), new FontData[]{ new FontData(F_NAME, 15, SWT.BOLD)});
		colorMap.put(getKey(p, Button.class, TEXT), new RGB(0x51, 0x51, 0x51));
		colorMap.put(getKey(p, Button.class, BACKGROUND), new RGB(0xcf, 0xd1, 0xcf));
		radiusMap.put(getKey(p, Button.class), 3f);		
		sizeMap.put(getKey(p, Button.class), new Dimension(100, 38));
		
		//TextField
		colorMap.put(getKey(p, TextField.class, TEXT), new RGB(0x80, 0x80, 0x80));
		radiusMap.put(getKey(p, TextField.class), 5f);		
		sizeMap.put(getKey(p, TextField.class), new Dimension(156, 30));
		
		//TextArea
		fontMap.put(getKey(p, TextArea.class), new FontData[]{ new FontData(F_NAME, 17, SWT.NORMAL)});
		//colorMap.put(getKey(p, TextArea.class, TEXT), new RGB(0x80, 0x80, 0x80));
		radiusMap.put(getKey(p, TextArea.class), 5f);		
		sizeMap.put(getKey(p, TextArea.class), new Dimension(156, 97));
		
		//Label
		fontMap.put(getKey(p, Label.class), new FontData[]{ new FontData(F_NAME, 15, SWT.NORMAL)});
		colorMap.put(getKey(p, Label.class, TEXT), new RGB(0x80, 0x80, 0x80));
		radiusMap.put(getKey(p, Label.class), 5f);
		widthMap.put(getKey(p, Label.class), 0f);
		sizeMap.put(getKey(p, Label.class), new Dimension(80, 28));
		
		//CoverFlowView
		colorMap.put(getKey(p, CoverFlowView.class, TEXT), new RGB(0x4c, 0x4c, 0x4c));
		colorMap.put(getKey(p, CoverFlowView.class, BACKGROUND), new RGB(0xcc, 0xcc, 0xcc));
		widthMap.put(getKey(p, Label.class), 0f);
		sizeMap.put(getKey(p, CoverFlowView.class), new Dimension(250, 370));
		
		//View
		sizeMap.put(getKey(p, View.class), new Dimension(320, 460));
		
		//ImageView
		widthMap.put(getKey(p, ImageView.class), 0f);
		radiusMap.put(getKey(p, ImageView.class), 0f);
		sizeMap.put(getKey(p, ImageView.class), new Dimension(64, 64));
		
		//WebView
		colorMap.put(getKey(p, WebView.class, TEXT), new RGB(0x4c, 0x4c, 0x4c));
		//colorMap.put(getKey(p, WebView.class, BACKGROUND), new RGB(0xcc, 0xcc, 0xcc));
		widthMap.put(getKey(p, WebView.class), 0f);
		sizeMap.put(getKey(p, WebView.class), new Dimension(320, 460));
		
		//DashboardView
		sizeMap.put(getKey(p, DashboardView.class), new Dimension(320, 460));
		
		//DashboardItem
		//colorMap.put(getKey(p, DashboardItem.class, BACKGROUND), new RGB(0xcc, 0xcc, 0xcc));
		colorMap.put(getKey(p, DashboardItem.class, BORDER), new RGB(0x55, 0x55, 0xff));
		colorMap.put(getKey(p, DashboardItem.class, TEXT), WHITE);
		radiusMap.put(getKey(p, DashboardItem.class), 10f);
		sizeMap.put(getKey(p, DashboardItem.class), new Dimension(80, 80));
		
		//ProgressBar
		colorMap.put(getKey(p, ProgressBar.class, BACKGROUND), new RGB(0x5d, 0x5f, 0x5c));
		colorMap.put(getKey(p, ProgressBar.class, ADDITION), ColorConstants.yellow.getRGB());
		widthMap.put(getKey(p, ProgressBar.class), 1f);
		radiusMap.put(getKey(p, ProgressBar.class), 10f);
		sizeMap.put(getKey(p, ProgressBar.class), new Dimension(80, 28));
		showGradient.add(getKey(p, ProgressBar.class));
		
		//Slider
		colorMap.put(getKey(p, Slider.class, BACKGROUND), new RGB(0x5d, 0x5f, 0x5c));
		colorMap.put(getKey(p, Slider.class, ADDITION), ColorConstants.yellow.getRGB());
		widthMap.put(getKey(p, Slider.class), 1f);
		radiusMap.put(getKey(p, Slider.class), 10f);
		sizeMap.put(getKey(p, Slider.class), new Dimension(290, 20));
		showGradient.add(getKey(p, Slider.class));
		
		//Switch
		//fontMap.put(getKey(p, Switch.class), new FontData[]{ new FontData(F_NAME, 14, SWT.NORMAL)});
		colorMap.put(getKey(p, Switch.class, BACKGROUND), new RGB(0xb0, 0xb0, 0xb0));//toggle
		colorMap.put(getKey(p, Switch.class, ADDITION), new RGB(0x3a, 0x3a, 0x3a));//check
		colorMap.put(getKey(p, Switch.class, BORDER), new RGB(0x99, 0x99, 0x99));
		colorMap.put(getKey(p, Switch.class, TEXT), BLACK);
		widthMap.put(getKey(p, Switch.class), 1f);
		radiusMap.put(getKey(p, Switch.class), 1f);
		sizeMap.put(getKey(p, Switch.class), new Dimension(64, 50));
		//showGradient.add(getKey(p, Switch.class));
		
		//Window
		colorMap.put(getKey(p, Window.class, TEXT), new RGB(0xaa, 0xaa, 0xaa));
		widthMap.put(getKey(p, Window.class), 0f);
		sizeMap.put(getKey(p, Window.class), new Dimension(320, 460));
		
		//TabbedBar
		colorMap.put(getKey(p, TabbedBar.class, BACKGROUND), new RGB(0xbd, 0xbd, 0xbd));
		colorMap.put(getKey(p, TabbedBar.class, BORDER), new RGB(0x9f, 0x9f, 0x9f));
		sizeMap.put(getKey(p, TabbedBar.class), new Dimension(320, 38));
		showGradient.add(getKey(p, TabbedBar.class));
		
		//ButtonBar
//		colorMap.put(getKey(p, ButtonBar.class, BACKGROUND), WHITE);
//		colorMap.put(getKey(p, ButtonBar.class, BORDER), new RGB(0x9f, 0x9f, 0x9f));
		sizeMap.put(getKey(p, ButtonBar.class), new Dimension(320, 49));
		showGradient.add(getKey(p, ButtonBar.class));
		
		//Toolbar
		colorMap.put(getKey(p, Toolbar.class, BACKGROUND), new RGB(0x68, 0x7f, 0x9e));
		colorMap.put(getKey(p, Toolbar.class, BORDER), new RGB(0x0c, 0x0c, 0x0c));
		sizeMap.put(getKey(p, Toolbar.class), new Dimension(320, 43));
		showGradient.add(getKey(p, Toolbar.class));
		
		//TabGroup
		colorMap.put(getKey(p, TabGroup.class, BACKGROUND), new RGB(0x4a, 0x4a, 0x4a));
		sizeMap.put(getKey(p, TabGroup.class), new Dimension(320, 54));
		showGradient.add(getKey(p, TabGroup.class));
		//ExternalTabGroup
		colorMap.put(getKey(p, ExternalTabGroup.class, BACKGROUND), colorMap.getRGB(getKey(p, TabGroup.class, BACKGROUND)));
		sizeMap.put(getKey(p, ExternalTabGroup.class), sizeMap.get(getKey(p, TabGroup.class)));
		showGradient.add(getKey(p, ExternalTabGroup.class));
		
		//Tab
		fontMap.put(getKey(p, Tab.class), new FontData[]{ new FontData(F_NAME, 10, SWT.NORMAL)});
		colorMap.put(getKey(p, Tab.class, BACKGROUND), BLACK);
		colorMap.put(getKey(p, Tab.class, TEXT), WHITE);
		widthMap.put(getKey(p, Tab.class), 0f);
		sizeMap.put(getKey(p, Tab.class), new Dimension(67, 54));
		showGradient.add(getKey(p, Tab.class));
		//MoreTab
		colorMap.put(getKey(p, MoreTab.class, BACKGROUND), BLACK);
		colorMap.put(getKey(p, MoreTab.class, TEXT), WHITE);
		widthMap.put(getKey(p, MoreTab.class), 0f);
		sizeMap.put(getKey(p, MoreTab.class), new Dimension(67, 54));
		showGradient.add(getKey(p, MoreTab.class));
		//ExternalTab
		fontMap.put(getKey(p, ExternalTab.class), new FontData[]{ new FontData(F_NAME, 10, SWT.NORMAL)});
		colorMap.put(getKey(p, ExternalTab.class, BACKGROUND), BLACK);
		colorMap.put(getKey(p, ExternalTab.class, TEXT), WHITE);
		widthMap.put(getKey(p, ExternalTab.class), 0f);
		sizeMap.put(getKey(p, ExternalTab.class), sizeMap.get(getKey(p, Tab.class)));
		showGradient.add(getKey(p, ExternalTab.class));
		//ExternalMoreTab
		colorMap.put(getKey(p, ExternalMoreTab.class, BACKGROUND), BLACK);
		colorMap.put(getKey(p, ExternalMoreTab.class, TEXT), WHITE);
		widthMap.put(getKey(p, ExternalMoreTab.class), 0f);
		sizeMap.put(getKey(p, ExternalMoreTab.class), sizeMap.get(getKey(p, MoreTab.class)));
		showGradient.add(getKey(p, MoreTab.class));
		
		//TabbedBarButton
		colorMap.put(getKey(p, TabbedBarButton.class, TEXT), new RGB(0x99, 0x99, 0x99));
		colorMap.put(getKey(p, TabbedBarButton.class, BACKGROUND), new RGB(0xf0, 0xf0, 0xf0));
		colorMap.put(getKey(p, TabbedBarButton.class, BORDER), new RGB(0x9e, 0x9e, 0x9e));
		widthMap.put(getKey(p, TabbedBarButton.class), 2f);
		sizeMap.put(getKey(p, TabbedBarButton.class), new Dimension(80, 37));
		showGradient.add(getKey(p, TabbedBarButton.class));
		
		//TableView
		fontMap.put(getKey(p, TableView.class), new FontData[]{ new FontData(F_NAME, 16, SWT.NORMAL)});
		colorMap.put(getKey(p, TableView.class, TEXT), GRAY_LITE);
		colorMap.put(getKey(p, TableView.class, ADDITION), GRAY_DARK);
		//radiusMap.put(getKey(p, TableView.class), 5f);
		sizeMap.put(getKey(p, TableView.class), new Dimension(320, 460));
		
		//TableViewSection
		fontMap.put(getKey(p, TableViewSection.class), new FontData[]{ new FontData(F_NAME, 16, SWT.NORMAL)});
		colorMap.put(getKey(p, TableViewSection.class, TEXT), GRAY_LITE);
		colorMap.put(getKey(p, TableViewSection.class, ADDITION), GRAY_DARK);
		sizeMap.put(getKey(p, TableViewSection.class), new Dimension(320, 32));
		
		//TableViewRow
		fontMap.put(getKey(p, TableViewRow.class), new FontData[]{ new FontData(F_NAME, 17, SWT.NORMAL)});
		sizeMap.put(getKey(p, TableViewRow.class), new Dimension(320, 45));
		
		//ScrollableView
		widthMap.put(getKey(p, ScrollableView.class), 0f);
		sizeMap.put(getKey(p, ScrollableView.class), new Dimension(320, 460));
		
		//SearchBar
		colorMap.put(getKey(p, SearchBar.class, BACKGROUND),new RGB(0xB0, 0xBC, 0xCD));//prompt
		colorMap.put(getKey(p, SearchBar.class, ADDITION), BLACK);//bar
		colorMap.put(getKey(p, SearchBar.class, TEXT), new RGB(0xaa, 0xaa, 0xaa));
		sizeMap.put(getKey(p, SearchBar.class), new Dimension(320, 34));
		showGradient.add(getKey(p, SearchBar.class));
		
		//TitleBar
		colorMap.put(getKey(p, TitleBar.class, BACKGROUND), new RGB(0x69, 0x7f, 0x9c)/*new RGB(0xff, 0xa5, 0x0)*/);//prompt
		colorMap.put(getKey(p, TitleBar.class, TEXT), WHITE);
		colorMap.put(getKey(p, TitleBar.class, ADDITION), new RGB(0x69, 0x7f, 0x9c));//bar
		sizeMap.put(getKey(p, TitleBar.class), new Dimension(320, 46));
		showGradient.add(getKey(p, TitleBar.class));
		
		//ScrollView
		sizeMap.put(getKey(p, ScrollView.class), new Dimension(320, 460));
		
		//EmailDialog
		fontMap.put(getKey(p, EmailDialog.class), new FontData[]{ new FontData(F_NAME, 14, SWT.NORMAL)});
		showGradient.add(getKey(p, EmailDialog.class));
		
		//OptionDialog
		colorMap.put(getKey(p, OptionDialog.class, BACKGROUND), new RGB(0x7c, 0x83, 0x8b));
		sizeMap.put(getKey(p, OptionDialog.class), new Dimension(320, 100));
		showGradient.add(getKey(p, OptionDialog.class));
		
		//AlertDialog
		colorMap.put(getKey(p, AlertDialog.class, TEXT), WHITE);
		colorMap.put(getKey(p, AlertDialog.class, BACKGROUND), BLACK);
		colorMap.put(getKey(p, AlertDialog.class, BORDER), GRAY_DARK);
		radiusMap.put(getKey(p, AlertDialog.class), 10f);
		widthMap.put(getKey(p, AlertDialog.class), 2f);
		sizeMap.put(getKey(p, AlertDialog.class), new Dimension(320, 100));
		showGradient.add(getKey(p, AlertDialog.class));
		
		//AlertDialogButton (button)
		colorMap.put(getKey(p, AlertDialogButton.class, TEXT), GRAY_DARK);//destructive
		colorMap.put(getKey(p, AlertDialogButton.class, BACKGROUND), new RGB(0xee, 0xee, 0xee));//standard
		colorMap.put(getKey(p, AlertDialogButton.class, ADDITION), new RGB(0xff, 0x77, 0x00));//destructive
		colorMap.put(getKey(p, AlertDialogButton.class, BORDER), GRAY_DARK);
		sizeMap.put(getKey(p, AlertDialogButton.class), new Dimension(320, 100));
		showGradient.add(getKey(p, AlertDialogButton.class));
		
		//OptionDialogButton (button)
		colorMap.put(getKey(p, OptionDialogButton.class, BACKGROUND), new RGB(233, 233, 240));
		colorMap.put(getKey(p, OptionDialogButton.class, ADDITION), new RGB(233, 233, 240));//destructive
		sizeMap.put(getKey(p, OptionDialogButton.class), new Dimension(0, 35));
		widthMap.put(getKey(p, OptionDialogButton.class), 0f);
		showGradient.add(getKey(p, OptionDialogButton.class));
		
		//Picker
		colorMap.put(getKey(p, Picker.class, BACKGROUND), new RGB(145, 145, 160));
		sizeMap.put(getKey(p, Picker.class), new Dimension(320, 215));
		//there is a problem with column in the case
		//don't show for now
		//showGradient.add(getKey(p, Picker.class)); 
		
		//PickerColumn
		colorMap.put(getKey(p, PickerColumn.class, BACKGROUND), new RGB(250, 250, 255));
		sizeMap.put(getKey(p, PickerColumn.class), new Dimension(150, 196));
		widthMap.put(getKey(p, PickerColumn.class), 1f);
		radiusMap.put(getKey(p, PickerColumn.class), 15f);
		showGradient.add(getKey(p, PickerColumn.class));
		
		//PickerRow
		colorMap.put(getKey(p, PickerRow.class, BACKGROUND), new RGB(233, 233, 240));
		sizeMap.put(getKey(p, PickerRow.class), new Dimension(0, 35));
		widthMap.put(getKey(p, PickerRow.class), 0f);
		showGradient.add(getKey(p, PickerRow.class));
	}
	
	/*pacakge*/ AndroidDefaultsProvider(){
		
	}

	protected Platform getPlatform(){
		return p;
	}

}
