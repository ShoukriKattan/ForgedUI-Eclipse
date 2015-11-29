// LICENSE
package com.forgedui.editor.defaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.forgedui.editor.edit.ExternalMoreTab;
import com.forgedui.editor.edit.ExternalTab;
import com.forgedui.editor.edit.ExternalTabGroup;
import com.forgedui.editor.edit.policy.ComponentValidator;
import com.forgedui.model.Element;
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
import com.forgedui.model.titanium.PlatformSupport;
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
import com.forgedui.util.ScreenManager;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class IphoneDefaultsProvider implements IDefaultsProvider {

	public static final String F_NAME = "Helvetica";
	
	static final String BORDER = "0";
	static final String BACKGROUND = "1";
	static final String TEXT = "2";
	static final String ADDITION = "3";//each view can use it like it want
	
	static final FontData fontData = new FontData(F_NAME, 13, SWT.NORMAL);
	
	static final Font defaultFont = new Font(Display.getCurrent(), fontData);
	
	static final RGB BLACK = new RGB(0, 0, 0);
	static final RGB WHITE = new RGB(255, 255, 255);
	static final RGB GRAY = new RGB(127, 127, 127);
	static final RGB GRAY_LITE = new RGB(200, 200, 200);
	static final RGB GRAY_DARK = new RGB(64, 64, 64);
	
	//TODO use custom registers here which will be
	//disposed on timer. Save last color/font call time
	//and dispose color/font used long time ago.
	protected static FontRegistry fontMap =
		new FontRegistry(Display.getCurrent(), true);
	protected static ColorRegistry colorMap =
		new ColorRegistry(Display.getCurrent(), true);

	protected static Map<String, Dimension> sizeMap =
		new HashMap<String, Dimension>();
	protected static Map<String, Float> radiusMap =
		new HashMap<String, Float>();
	protected static Map<String, Float> widthMap =
		new HashMap<String, Float>();
	protected static List<String> showGradient =
		new ArrayList<String>();
	
	private static final Platform p = Platform.iPhone;
	
	static {
		colorMap.put(getKey(p, BORDER), BLACK);
		colorMap.put(getKey(p, BACKGROUND), WHITE);
		colorMap.put(getKey(p, TEXT), BLACK);
		
		//Button
		///fontMap.put(getKey(p, Button.class), new FontData[]{ new FontData(F_NAME, 14, SWT.BOLD)});
		colorMap.put(getKey(p, Button.class, TEXT), new RGB(0x5d, 0x61, 0x8a));
		colorMap.put(getKey(p, Button.class, BORDER), GRAY);
		widthMap.put(getKey(p, Button.class), 1f);
		radiusMap.put(getKey(p, Button.class), 8f);		
		
		sizeMap.put(getKey(p, Button.class), new Dimension(110, 36));
		
		//TextField
		sizeMap.put(getKey(p, TextField.class), new Dimension(156, 30));
		
		//TextArea
		fontMap.put(getKey(p, TextArea.class), new FontData[]{ new FontData(F_NAME, 17, SWT.NORMAL)});
		sizeMap.put(getKey(p, TextArea.class), new Dimension(156, 97));
		
		//Label
		fontMap.put(getKey(p, Label.class), new FontData[]{ new FontData(F_NAME, 14, SWT.NORMAL)});
		widthMap.put(getKey(p, Label.class), 0f);
		sizeMap.put(getKey(p, Label.class), new Dimension(80, 28));
		
		//CoverFlowView
		colorMap.put(getKey(p, CoverFlowView.class, TEXT), new RGB(0x4c, 0x4c, 0x4c));
		colorMap.put(getKey(p, CoverFlowView.class, BACKGROUND), new RGB(0xcc, 0xcc, 0xcc));
		widthMap.put(getKey(p, CoverFlowView.class), 0f);
		sizeMap.put(getKey(p, CoverFlowView.class), new Dimension(320, 460));
		
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
		colorMap.put(getKey(p, ProgressBar.class, BACKGROUND), new RGB(0xef, 0xef, 0xef));
		colorMap.put(getKey(p, ProgressBar.class, ADDITION), ColorConstants.lightBlue.getRGB());
		widthMap.put(getKey(p, ProgressBar.class), 1f);
		radiusMap.put(getKey(p, ProgressBar.class), 5f);
		sizeMap.put(getKey(p, ProgressBar.class), new Dimension(290, 12));
		showGradient.add(getKey(p, ProgressBar.class));
		
		//Slider
		colorMap.put(getKey(p, Slider.class, BACKGROUND), new RGB(0xef, 0xef, 0xef));
		colorMap.put(getKey(p, Slider.class, ADDITION), ColorConstants.lightBlue.getRGB());
		widthMap.put(getKey(p, Slider.class), 1f);
		radiusMap.put(getKey(p, Slider.class), 5f);
		sizeMap.put(getKey(p, Slider.class), new Dimension(290, 20));
		showGradient.add(getKey(p, Slider.class));
		
		//Switch
		fontMap.put(getKey(p, Switch.class), new FontData[]{ new FontData(F_NAME, 14, SWT.BOLD)});
		colorMap.put(getKey(p, Switch.class, BACKGROUND), new RGB(0xa7, 0xa7, 0xa7));
		colorMap.put(getKey(p, Switch.class, BORDER), new RGB(0x99, 0x99, 0x99));
		colorMap.put(getKey(p, Switch.class, TEXT), new RGB(0x77, 0x77, 0x77));
		widthMap.put(getKey(p, Switch.class), 1f);
		radiusMap.put(getKey(p, Switch.class), 14f);
		sizeMap.put(getKey(p, Switch.class), new Dimension(80, 29));
		showGradient.add(getKey(p, Switch.class));
		
		//Window
		colorMap.put(getKey(p, Window.class, BACKGROUND), BLACK);
		colorMap.put(getKey(p, Window.class, TEXT), new RGB(0xaa, 0xaa, 0xaa));
		widthMap.put(getKey(p, Window.class), 0f);
		sizeMap.put(getKey(p, Window.class), new Dimension(320, 460));
		
		//TabbedBar
		colorMap.put(getKey(p, TabbedBar.class, BACKGROUND), new RGB(0xbd, 0xbd, 0xbd));
		colorMap.put(getKey(p, TabbedBar.class, BORDER), new RGB(0x9f, 0x9f, 0x9f));
		sizeMap.put(getKey(p, TabbedBar.class), new Dimension(320, 38));
		showGradient.add(getKey(p, TabbedBar.class));
		
		//ButtonBar
		colorMap.put(getKey(p, ButtonBar.class, BACKGROUND), WHITE);
		colorMap.put(getKey(p, ButtonBar.class, BORDER), new RGB(0x9f, 0x9f, 0x9f));
		sizeMap.put(getKey(p, ButtonBar.class), new Dimension(320, 38));
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
		widthMap.put(getKey(p, TabbedBarButton.class), 1f);
		radiusMap.put(getKey(p,TabbedBarButton.class), 5f);
		sizeMap.put(getKey(p, TabbedBarButton.class), new Dimension(80, 37));
		showGradient.add(getKey(p, TabbedBarButton.class));
		
		//TableView
		fontMap.put(getKey(p, TableView.class), new FontData[]{ new FontData(F_NAME, 16, SWT.NORMAL)});
		colorMap.put(getKey(p, TableView.class, TEXT), BLACK);
		colorMap.put(getKey(p, TableView.class, ADDITION), WHITE);
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
		colorMap.put(getKey(p, SearchBar.class, ADDITION), new RGB(0xB0, 0xBC, 0xCD));//bar
		colorMap.put(getKey(p, SearchBar.class, TEXT), new RGB(0xaa, 0xaa, 0xaa));
		sizeMap.put(getKey(p, SearchBar.class), new Dimension(320, 34));
		showGradient.add(getKey(p, SearchBar.class));
		
		//TitleBar
		fontMap.put(getKey(p, TitleBar.class), new FontData[]{ new FontData(F_NAME, 11, SWT.BOLD)});
		colorMap.put(getKey(p, TitleBar.class, BACKGROUND), new RGB(0x69, 0x7f, 0x9c)/*new RGB(0xff, 0xa5, 0x0)*/);//prompt
		colorMap.put(getKey(p, TitleBar.class, TEXT), WHITE);
		colorMap.put(getKey(p, TitleBar.class, ADDITION), new RGB(0x69, 0x7f, 0x9c));//bar
		sizeMap.put(getKey(p, TitleBar.class), new Dimension(320, 40));
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
		colorMap.put(getKey(p, AlertDialog.class, BACKGROUND), new RGB(0x20, 0x40, 0x80));
		colorMap.put(getKey(p, AlertDialog.class, BORDER), WHITE);
		radiusMap.put(getKey(p, AlertDialog.class), 10f);
		widthMap.put(getKey(p, AlertDialog.class), 2f);
		sizeMap.put(getKey(p, AlertDialog.class), new Dimension(320, 100));
		showGradient.add(getKey(p, AlertDialog.class));
		
		//AlertDialogButton (button)
		colorMap.put(getKey(p, AlertDialogButton.class, TEXT), WHITE);//destructive
		colorMap.put(getKey(p, AlertDialogButton.class, BACKGROUND), new RGB(0x2a, 0x3b, 0x65));//standard
		colorMap.put(getKey(p, AlertDialogButton.class, ADDITION), new RGB(0xde, 0x22, 0x22));//destructive
		radiusMap.put(getKey(p, AlertDialogButton.class), 5f);
		widthMap.put(getKey(p, AlertDialogButton.class), 3f);
		sizeMap.put(getKey(p, AlertDialogButton.class), new Dimension(320, 100));
		showGradient.add(getKey(p, AlertDialogButton.class));
		
		//OptionDialogButton (button)
		fontMap.put(getKey(p, OptionDialogButton.class), new FontData[]{ new FontData(F_NAME, 18, SWT.NORMAL)});
		colorMap.put(getKey(p, OptionDialogButton.class, TEXT), WHITE);//destructive
		colorMap.put(getKey(p, OptionDialogButton.class, BACKGROUND), new RGB(0x59, 0x59, 0x59));//standard
		colorMap.put(getKey(p, OptionDialogButton.class, ADDITION), new RGB(0xde, 0x22, 0x22));//destructive
		radiusMap.put(getKey(p, OptionDialogButton.class), 5f);
		widthMap.put(getKey(p, OptionDialogButton.class), 3f);
		sizeMap.put(getKey(p, OptionDialogButton.class), new Dimension(320, 100));
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
	
	/*pacakge*/ IphoneDefaultsProvider(){
		
	}
	
	protected Platform getPlatform(){
		return p;
	}

	@Override
	public Font getFont(Element e) {
		 return fontMap.hasValueFor(getKey(e))
				 ? fontMap.get(getKey(e)) : defaultFont;
	}

	@Override
	public Color getForegroundColor(Element e) {
		return colorMap.hasValueFor(getKey(e, TEXT))
				? colorMap.get(getKey(e, TEXT)) : colorMap.get(getKey(getPlatform(), TEXT));
	}
	
	@Override
	public Color getBackgroundColor(Element e) {
		return colorMap.hasValueFor(getKey(e, BACKGROUND))
			? colorMap.get(getKey(e, BACKGROUND)) : colorMap.get(getKey(getPlatform(), BACKGROUND));
	}

	@Override
	public Color getAdditionColor(Element e) {
		return colorMap.hasValueFor(getKey(e, ADDITION))
			? colorMap.get(getKey(e, ADDITION)) : null;
	}
	
	@Override
	public Color getBorderColor(Element e) {
		return colorMap.hasValueFor(getKey(e, BORDER))
			? colorMap.get(getKey(e, BORDER)) : colorMap.get(getKey(getPlatform(), BORDER));
	}

	@Override
	public float getBorderRadius(Element e) {
		Float i = radiusMap.get(getKey(e));
		return i == null ? 0 : i;
	}
	
	@Override
	public float getBorderWidth(Element e) {
		Float i = widthMap.get(getKey(e));
		return i == null ? 1 : i;
	}
	
	@Override
	public Dimension getSize(Element e) {
		return getSize(e, e.getSupport());
	}

	@Override
	public Dimension getSize(Element e, PlatformSupport support) {
		if (ComponentValidator.isFullScreenView(e)){
			return support.modelToView(ScreenManager.getScreenWindowBounds(support));
		}
		Dimension dimension = sizeMap.get(getKey(e));
		if (dimension == null){
			throw new IllegalArgumentException("Please add default size "+
					" for " + e.getClass() + " class '" + getPlatform() +
					"' platform!");
		}
		if (dimension.width == 320){//stretch to actual width
			dimension = dimension.getCopy();
			dimension.width = support.modelToView(ScreenManager.getScreenWindowBounds(support)).width;
		}
		
		return dimension.getCopy();
	}
	

	@Override
	public boolean getShowGradient(Element e) {
		return showGradient.contains(getKey(e));
	}
	
	//***************** keys ****************
	protected String getKey(Element e, String key){
		return getKey(getPlatform(), e.getClass(), key);
	}

	protected static String getKey(Platform p, Class<?> c, String key){
		return new StringBuilder(p.toString())
			.append(c.getSimpleName())
			.append(key).toString();
	}
	
	protected String getKey(Element e){
		return getKey(getPlatform(), e.getClass());
	}

	protected static String getKey(Platform p, Class<?> c){
		return new StringBuilder(p.toString())
			.append(c.getSimpleName()).toString();
	}
	
	protected static String getKey(Platform p, String key){
		return new StringBuilder(p.toString())
			.append(key).toString();
	}

}
