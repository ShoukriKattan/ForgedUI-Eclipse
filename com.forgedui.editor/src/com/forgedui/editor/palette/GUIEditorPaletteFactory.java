// LICENSE
package com.forgedui.editor.palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.MarqueeSelectionTool;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.model.Diagram;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.CoverFlowView;
import com.forgedui.model.titanium.DashboardItem;
import com.forgedui.model.titanium.DashboardView;
import com.forgedui.model.titanium.ImageView;
import com.forgedui.model.titanium.Label;
import com.forgedui.model.titanium.Picker;
import com.forgedui.model.titanium.ProgressBar;
import com.forgedui.model.titanium.ScrollView;
import com.forgedui.model.titanium.ScrollableView;
import com.forgedui.model.titanium.SearchBar;
import com.forgedui.model.titanium.Slider;
import com.forgedui.model.titanium.Switch;
import com.forgedui.model.titanium.TabbedBar;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TextArea;
import com.forgedui.model.titanium.TextField;
import com.forgedui.model.titanium.TitleBar;
import com.forgedui.model.titanium.Toolbar;
import com.forgedui.model.titanium.View;
import com.forgedui.model.titanium.WebView;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 * palette is create based on the http://docs.appcelerator.com/titanium/2.0/#!/api/Titanium.UI
 * There is Views/Components/Windows sepataion
 * 
 */
public final class GUIEditorPaletteFactory {

	/** Create the "Components" drawer. 
	 * @param diagram */
	private static PaletteContainer createComponentsDrawer(Diagram diagram) {
		PaletteDrawer componentsDrawer = new PaletteDrawer("Controls");

		CombinedTemplateCreationEntry component = null;
		
		component = new CombinedTemplateCreationEntry(
				"Button", "Create a button view", Button.class,
				new TitaniumUIElementFactory(Button.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/buttonview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/buttonview.png"));

		componentsDrawer.add(component);
		
		// newlly added for the lable and the web view.
		component = new CombinedTemplateCreationEntry(
				"Label", "Create a label view", Label.class,
				new TitaniumUIElementFactory(Label.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/labelview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/labelview.png"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Slider", "Create a slider view", Slider.class,
				new TitaniumUIElementFactory(Slider.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/sliderview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/sliderview.png"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"ProgressBar", "Create a progress bar view", ProgressBar.class,
				new TitaniumUIElementFactory(ProgressBar.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/progressbar.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/progressbar.png"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Switch", "Create a switch view", Switch.class,
				new TitaniumUIElementFactory(Switch.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/switchview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/switchview.png"));
		componentsDrawer.add(component);
		
		
		// Newly added for the text view and the text field items.
		component = new CombinedTemplateCreationEntry(
				"Text field", "Create an textfield view", TextField.class,
				new TitaniumUIElementFactory(TextField.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/textfieldview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/textfieldview.png"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Text area", "Create a text view", TextArea.class,
				new TitaniumUIElementFactory(TextArea.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/textview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/textview.png"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Picker", "Create a picker", Picker.class,
				new TitaniumUIElementFactory(Picker.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/pickercolumn.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/pickercolumn.png"));
		componentsDrawer.add(component);
	
		component = new CombinedTemplateCreationEntry(
				"SearchBar", "Create a search bar", SearchBar.class,
				new TitaniumUIElementFactory(SearchBar.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/searchbar.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/searchbar.png"));
		componentsDrawer.add(component);
		
		if (!diagram.getPlatform().isAndroid()){
			component = new CombinedTemplateCreationEntry(
					"TitleBar", "Create a titlebar", TitleBar.class,
					new TitaniumUIElementFactory(TitleBar.class, diagram),
					GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/toolbar.png"),
					GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/toolbar.png"));
			componentsDrawer.add(component);
			
			component = new CombinedTemplateCreationEntry(
					"Button bar", "Create a button bar", ButtonBar.class,
			 		new TitaniumUIElementFactory(ButtonBar.class, diagram),
					GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/buttonbarview.png"),
					GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/tbuttonbarview.png"));
			componentsDrawer.add(component);
			
			component = new CombinedTemplateCreationEntry(
					"TabbedBar", "Create a tabbed bar", TabbedBar.class,
					new TitaniumUIElementFactory(TabbedBar.class, diagram),
					GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/tabbedbar.png"),
					GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/tabbedbar.png"));
			componentsDrawer.add(component);
			
			component = new CombinedTemplateCreationEntry(
					"Toolbar", "Create a toolbar", Toolbar.class,
					new TitaniumUIElementFactory(Toolbar.class, diagram),
					GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/toolbar.png"),
					GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/toolbar.png"));
			componentsDrawer.add(component);
		}

		return componentsDrawer;
	}
	
	static private PaletteContainer createControlGroup(PaletteRoot root){
		PaletteGroup controlGroup = new PaletteGroup("Control Group");

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();

		ToolEntry tool = new PanningSelectionToolEntry();
		entries.add(tool);
		root.setDefaultEntry(tool);

		PaletteStack marqueeStack = new PaletteStack("Marquee Tools", "", null); //$NON-NLS-1$ $NON-NLS-2$
		marqueeStack.add(new MarqueeToolEntry());
		MarqueeToolEntry marquee = new MarqueeToolEntry();
		marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
				new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
		marqueeStack.add(marquee);
		marquee = new MarqueeToolEntry();
		marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
				new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED 
				| MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED));
		marqueeStack.add(marquee);
		marqueeStack.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		entries.add(marqueeStack);
		
		controlGroup.addAll(entries);
		return controlGroup;
	}
	
	/**
	 * Create palette for the base.
	 * @return
	 */
	private static PaletteContainer createViewsPalette(Diagram diagram) {
		PaletteDrawer componentsDrawer = new PaletteDrawer("Views");

		CombinedTemplateCreationEntry component = null;
		if (!diagram.getPlatform().isAndroid()){
			PaletteStack dashBoardStack = new PaletteStack("Dashboard", "Dashboard components",GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/dashboardview.png"));
			
			component = new CombinedTemplateCreationEntry(
					"Dashboard view", "Create an dashboard view", DashboardView.class,
					new TitaniumUIElementFactory(DashboardView.class, diagram),
					GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/dashboardview.png"),
					GUIEditorPlugin.getImageDescriptor("icons/palette/24/Base/dashboardview.png"));
			dashBoardStack.add(component);
			
			component = new CombinedTemplateCreationEntry(
					"Dashboard item", "Create an dashboard item", DashboardItem.class,
					new TitaniumUIElementFactory(DashboardItem.class, diagram),
					GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/dashboarditem.png"),
					GUIEditorPlugin.getImageDescriptor("icons/palette/24/Base/dashboarditem.png"));
			
			dashBoardStack.add(component);
			componentsDrawer.add(dashBoardStack);
			
			component = new CombinedTemplateCreationEntry(
					"Cover flow view", "Create a cover flow view", CoverFlowView.class,
					new TitaniumUIElementFactory(CoverFlowView.class, diagram),
					GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/coverflowview.png"),
					GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/coverflowview.png"));
			componentsDrawer.add(component);
		}
		
		// Creating a stack for the table items.
		PaletteStack tableStack = new PaletteStack("Table", "Table components", GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/tableview.png"));
		component = new CombinedTemplateCreationEntry(
				"Table view", "Create a table view", TableView.class,
				new TitaniumUIElementFactory(TableView.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/tableview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Base/tableview.png"));
		tableStack.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Table view row", "Create an table view row", TableViewRow.class,
				new TitaniumUIElementFactory(TableViewRow.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/tableviewrow.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Base/tableviewrow.png"));
		tableStack.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Table view section", "Create a table view section", TableViewSection.class,
				new TitaniumUIElementFactory(TableViewSection.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/tableviewsection.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Base/tableviewsection.png"));
		tableStack.add(component);
		// Adding the table stack to the palette.
		componentsDrawer.add(tableStack);

		
		component = new CombinedTemplateCreationEntry(
				"Image view", "Create an image view", ImageView.class,
				new TitaniumUIElementFactory(ImageView.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/imageview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/imageview.png"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Scroll view", "Create a scroll view", ScrollView.class,
				new TitaniumUIElementFactory(ScrollView.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/scrollview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/scrollview.png"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Scrollable view", "Create a scrollable view", ScrollableView.class,
				new TitaniumUIElementFactory(ScrollableView.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Controls/scrollableview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Controls/scrollableview.png"));
		componentsDrawer.add(component);
		
		// For creating a container view
		component = new CombinedTemplateCreationEntry(
				"View", "Create a view", View.class,
				new TitaniumUIElementFactory(View.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/webview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Base/webview.png"));
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry(
				"Web view", "Create a web view", WebView.class,
				new TitaniumUIElementFactory(WebView.class, diagram),
				GUIEditorPlugin.getImageDescriptor("icons/palette/16/Base/webview.png"),
				GUIEditorPlugin.getImageDescriptor("icons/palette/24/Base/webview.png"));
		componentsDrawer.add(component);
		
		return componentsDrawer;
	}
	
	/**
	 * Creates the PaletteRoot and adds all palette elements. Use this factory
	 * method to create a new palette for your graphical editor.
	 * 
	 * @return a new PaletteRoot
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static PaletteRoot createPalette(Diagram diagram) {
		PaletteRoot palette = new PaletteRoot();
		
		palette.add(createControlGroup(palette));
		
		PaletteDrawer drawer = (PaletteDrawer) createComponentsDrawer(diagram);
		List<PaletteEntry> children = drawer.getChildren();
		Collections.sort(children, new PaletteComparator());
		drawer.setChildren(children);
		palette.add(drawer);
		
		drawer = (PaletteDrawer) createViewsPalette(diagram);
		children = drawer.getChildren();
		Collections.sort(children, new PaletteComparator());
		drawer.setChildren(children);
		palette.add(drawer);
		
		return palette;
	}

	/** Utility class. */
	private GUIEditorPaletteFactory() {
		// Utility class
	}

	public static class PaletteComparator implements Comparator<PaletteEntry> {
		@Override
		public int compare(PaletteEntry itemOne, PaletteEntry itemTwo) {
			if(itemOne.getLabel() != null && itemTwo.getLabel() != null)
				return itemOne.getLabel().compareTo(itemTwo.getLabel());
			return 0;
		} 
	}
	
}