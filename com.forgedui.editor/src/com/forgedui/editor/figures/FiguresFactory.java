// LICENSE
package com.forgedui.editor.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;

import com.forgedui.editor.defaults.DefaultsProvider;
import com.forgedui.editor.defaults.IDefaultsProvider;
import com.forgedui.editor.figures.TitaniumTextFigure.Alignments;
import com.forgedui.editor.images.TitaniumImages;
import com.forgedui.model.Element;
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
import com.forgedui.model.titanium.ipad.Popover;
import com.forgedui.model.titanium.ipad.SplitWindow;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class FiguresFactory {

	public static IFigure createFigure(Element element){

		IFigure figure = createTitaniumFigure(element);
		if (figure != null){
			//figure.setSize(element.getSupport().modelToView(element.getDimension()));
			return figure;
		}
		IDefaultsProvider defaults = getDefaults(element);
		if (element instanceof Screen){
			ScreenFigure screen = null;

			switch (element.getPlatform()) {
			case iPhone:
				screen = new IPhoneMobileScreenFigure();
			break;
			case Android:
				screen = new AndroidMobileScreenFigure();
			break;
			case iPad:
				screen = new IPhoneMobileScreenFigure();
			break;
				default:
				throw new IllegalArgumentException("Unknown platform " + element.getPlatform());
			}
			screen.setResolution(element.getResolution().getCopy());
			screen.setCarrierFigure(getCarrierFigure(element));
			screen.setSignalImage(TitaniumImages.getImage(element.getPlatform(), TitaniumImages.SIGNAL));
			screen.setBatteryImage(TitaniumImages.getImage(element.getPlatform(), TitaniumImages.BATTERY));
			if (element.getPlatform().isAndroid()){
				screen.setSignal3GImage(TitaniumImages.getImage(element.getPlatform(), TitaniumImages.SIGNAL_3G));
			}
			return screen;
		} else if (element instanceof EmailDialog) {
			EmailDialogFigure eDialog = new EmailDialogFigure(element.getResolution());
			eDialog.setDefaultFont(defaults.getFont(element));
			return eDialog;
		}
		throw new IllegalArgumentException("Can't create figure for " + element);
	}
	
	public static TitaniumFigure createTitaniumFigure(Element element){
		TitaniumFigure figure = null;
		IDefaultsProvider defaults = getDefaults(element);
		if (element instanceof TitleBar) { 
			figure = new TitleBarFigure(/*defaults.getAdditionColor(element)*/);
		} else if (element instanceof Button){
			figure = new ButtonFigure();
		} else if (element instanceof TextField) {
			figure = new TextFieldFigure();
		} else if (element instanceof TextArea) {
			figure = new TextAreaFigure();
		} else if (element instanceof Label) {
			figure = new LabelFigure();
			((LabelFigure)figure).setDefaultTextHorisontalAlignment(Alignments.left);
		} else if (element instanceof CoverFlowView) {
			figure = new CoverFlowFigure();
		} else if (element instanceof WebView) {
			figure = new WebViewFigure();
		} else if (element instanceof DashboardView){
			figure = new DashboardViewFigure();
		} else if (element instanceof DashboardItem){
			figure = new DashboardItemFigure();
		} else if (element instanceof ProgressBar){
			figure = new ProgressBarFigure(
					defaults.getAdditionColor(element));
		} else if (element instanceof Slider) {
			figure = element.getPlatform().isAndroid()
				? new SliderFigureAndroid(defaults.getAdditionColor(element))
				: new SliderFigure(
					defaults.getAdditionColor(element));
		} else if (element instanceof Window){
			figure = new WindowFigure();
		} else if (element instanceof TabbedBarButton){
			figure = new TabbedBarButtonFigure();
		} else if (element instanceof TabbedBar
				|| element instanceof ButtonBar
				|| element instanceof Toolbar
				|| element instanceof TabGroup){
			figure = new TitaniumFigure();
			figure.setInsets(new Insets(4, 0, 0, 0));
		} else if (element instanceof TableView) {
			figure = new TableViewFigure(defaults.getAdditionColor(element));
		} else if (element instanceof TableViewRow) {
			figure = new TableViewRowFigure();
		} else if (element instanceof TableViewSection) {
			figure = new TableViewSectionFigure(defaults.getAdditionColor(element));
		} else if (element instanceof ScrollableView) {
			if (element.getPlatform().isAndroid()) {
				figure = new ScrollableViewFigureAndroid();
			} else {
				figure = new ScrollableViewFigure();
			}
		} else if (element instanceof Switch) {
			if (element.getPlatform().isAndroid()){
				figure = new SwitchFigureAndroid(
						TitaniumImages.getImage(element.getPlatform(), TitaniumImages.HAS_CHECK));
			} else {
				figure = new SwitchFigure();
			}
		} else if (element instanceof MoreTab){
			figure = new MoreTabFigure();
		} else if (element instanceof Tab){
			figure = new TabFigure();
		} else if (element instanceof SearchBar){
			if (element.getPlatform().isAndroid()){
				figure = new SearchBarFigureAndroid(defaults.getAdditionColor(element),
						TitaniumImages.getImage(element.getPlatform(), TitaniumImages.CANCEL));
			} else {
				figure = new SearchBarFigure(defaults.getAdditionColor(element),
						TitaniumImages.getImage(element.getPlatform(), TitaniumImages.ZOOM));
			}
		} else if (element instanceof ScrollView) {
			figure = new ScrollViewFigure();
		} else if (element instanceof AlertDialogButton) {
			figure = new DialogOptionFigure();
		} else if (element instanceof OptionDialogButton) {
			if (element.getPlatform().isAndroid()){
				figure = new PickerRowFigure();
				((PickerRowFigure)figure).setTextHorisontalAlign(Alignments.left);
			} else {
				figure =new DialogOptionFigure();
			}
		} else if (element instanceof OptionDialog) {
			figure = new OptionDialogFigure();
		} else if (element instanceof Popover) {
			figure = new PopoverFigure();
		} else if (element instanceof SplitWindow) {
			figure = new SplitWindowFigure();
		} else if (element instanceof AlertDialog) {
			AlertDialogButton button = new AlertDialogButton();
			button.setParent(element);
			DialogOptionFigure okButton = new DialogOptionFigure();
			initFigureDefaults(button, okButton);
			figure = element.getPlatform().isAndroid() ? new AndroidAlertDialogFigure(okButton)
				: new AlertDialogFigure(okButton);
		} else if (element instanceof Picker) {
			Picker p = (Picker) element;
			figure = new PickerFigure();
			((PickerFigure)figure).setExpanded(p.isExpanded());
		} else if (element instanceof PickerColumn) {
			figure = new PickerColumnFigure();
		} else if (element instanceof PickerRow) {
			figure = new PickerRowFigure();
		} else if (element instanceof View) {
			figure = new ViewFigure();
			figure.setOpaque(true);
		} else if (element instanceof ImageView) {
			figure = new ImageViewFigure();
		}
		
		if (figure != null){
			initFigureDefaults(element, figure);
		}
		if (element instanceof Picker) {
			Picker p = (Picker)element;
			if (!p.isExpanded()){
				figure.setSize(new PlatformSupport(p.getPlatform(), p.getResolution()).modelToView(new Dimension(Integer.parseInt(p.getWidth()),Integer.parseInt(p.getHeight()))));
			}
		}
		
		return figure;
	}

	/**
	 * @param element
	 * @param figure
	 * @param defaults
	 */
	private static void initFigureDefaults(Element element,
			TitaniumFigure figure) {
		IDefaultsProvider defaults = getDefaults(element);
		//necessary for create element feedback, we don't know resolution here
		figure.setSize(defaults.getSize(element));
		figure.setBorder(createTitaniumBorder(element));
		figure.setDefaultFont(defaults.getFont(element));
		figure.setDefaultFontColor(defaults.getForegroundColor(element));
	}


	private static TitaniumBackgroundBorder createTitaniumBorder(Element model) {
		IDefaultsProvider defaults = getDefaults(model);
		if (model instanceof PickerColumn) {
			return new PickerColumnBackgroundBorder(
					defaults.getBackgroundColor(model),
					defaults.getBorderColor(model),
					defaults.getBorderWidth(model),
					defaults.getBorderRadius(model),
					defaults.getShowGradient(model));
		}
		return new TitaniumBackgroundBorder(
				defaults.getBackgroundColor(model),
				defaults.getBorderColor(model),
				defaults.getBorderWidth(model),
				defaults.getBorderRadius(model),
				defaults.getShowGradient(model),
				model);
	}

	/**
	 * @return
	 */
	private static IDefaultsProvider getDefaults(Element base) {
		return DefaultsProvider.getDefaultsProvider(base.getPlatform());
	}

	public static CarrierFigure getCarrierFigure(Element e){
		if (e.getPlatform().isIPhone()){
			return new IPhoneCarrierFigure();
		} else if (e.getPlatform().isAndroid()){
			return new AndroidCarrierFigure();
		} else if (e.getPlatform().isIPad()){
			return new IPadCarrierFigure();
		}else {
			throw new IllegalArgumentException("Please add Carrier " +
					 "figure for the platform " + e.getPlatform());
		}
	}

}
