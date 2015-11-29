// LICENSE
package com.forgedui.editor.edit;

import org.eclipse.gef.EditPart;

import com.forgedui.model.Diagram;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.AlertDialog;
import com.forgedui.model.titanium.AlertDialogButton;
import com.forgedui.model.titanium.Button;
import com.forgedui.model.titanium.ButtonBar;
import com.forgedui.model.titanium.CoverFlowView;
import com.forgedui.model.titanium.DashboardItem;
import com.forgedui.model.titanium.DashboardView;
import com.forgedui.model.titanium.EmailDialog;
import com.forgedui.model.titanium.ImageView;
import com.forgedui.model.titanium.Label;
import com.forgedui.model.titanium.MoreTab;
import com.forgedui.model.titanium.OptionDialog;
import com.forgedui.model.titanium.OptionDialogButton;
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


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DiagramEditPartFactory implements org.eclipse.gef.EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		if (model instanceof Diagram){
			part = new DiagramEditPart();
		} else if (model instanceof Screen){
			part = new ScreenEditPart();
		} else if (model instanceof Window){
			part = new WindowEditPart();
		}  else if (model instanceof TitleBar){
			part = new TitleBarEditPart();
		} else if (model instanceof Button){
			part = new ButtonEditPart();
		} else if (model instanceof ButtonBar){
			part =  new ButtonBarEditPart();
		} else if (model instanceof DashboardItem){
			part =  new DashboardItemEditPart();
		} else if (model instanceof DashboardView){
			part =  new DashboardViewEditPart();
		} else if (model instanceof ProgressBar){
			part =  new ProgressBarEditPart();
		} else if (model instanceof SearchBar){
			part =  new SearchBarEditPart();
		} else if (model instanceof TabbedBar){
			part = new TabbedBarEditPart();
		} else if (model instanceof TabbedBarButton){
			part = new TabbedBarButtonEditPart();
		} else if (model instanceof Toolbar){
			part = new ToolbarEditPart();
		} else if (model instanceof Label) { 
			part = new LabelEditPart();
		} else if (model instanceof WebView) { 
			part = new WebviewEditPart();
		} else if (model instanceof Slider) { 
			part = new SliderEditPart();
		} else if (model instanceof View)  {
			part = new ViewEditPart();
		} else if (model instanceof TabGroup) { 
			part = new TabGroupEditPart();
		} else if (model instanceof MoreTab) { 
			part = new TabEditPart();
		} else if (model instanceof Tab) { 
			part = new TabEditPart();
		} else if (model instanceof ImageView) {
			part = new ImageViewEditPart();
		} else if (model instanceof TableView) { 
			part = new TableViewEditPart();
		} else if (model instanceof TableViewRow) { 
			part = new TableViewRowEditPart();
		} else if (model instanceof TableViewSection) { 
			part = new TableViewSectionEditPart();
		} else if (model instanceof Switch) {
			part = new SwitchEditPart();
		} else if (model instanceof CoverFlowView) { 
			part = new CoverFlowEditPart();
		} else if (model instanceof TextArea) { 
			part = new TextAreaEditPart();
		} else if (model instanceof TextField) { 
			part = new TextFieldEditPart();
		} else if (model instanceof Picker) { 
			part = new PickerEditPart();
		} else if (model instanceof PickerColumn) { 
			part = new PickerColumnEditPart();
		} else if (model instanceof PickerRow) { 
			part = new PickerRowEditPart();
		} else if (model instanceof ScrollView) { 
			part = new ScrollViewEditPart();
		} else if (model instanceof ScrollableView) { 
			part = new ScrollableViewEditPart();
		} else if (model instanceof AlertDialog) { 
			part = new AlertDialogEditPart();
		} else if (model instanceof OptionDialog) { 
			part = new OptionDialogEditPart();
		} else if (model instanceof Popover) { 
			part = new PopoverEditPart();
		} else if (model instanceof SplitWindow) { 
			part = new SplitWindowEditPart();
		}  else if (model instanceof EmailDialog) { 
			part = new EmailDialogEditPart();
		}  else if (model instanceof AlertDialogButton) { 
			part = new AlertDialogButtonEditPart();
		} else if (model instanceof OptionDialogButton) { 
			part = new OptionDialogButtonEditPart();
		} /* else if (model instanceof PopoverDialog) { 
			part = new PopoverDialogEditPart();
		}  else if (model instanceof PopoverMessage) { 
			part = new PopoverMessageEditPart();
		} */else {
			throw new RuntimeException(
					"Can't create part for model element: "
					+ ((model != null) ? model.getClass().getName() : "null"));
		}
		part.setModel(model);//GEF doesn't do this automatically,
		return part; //<--(part should never be returned as null)

	}

}
