// LICENSE
package com.forgedui.editor.util;

import java.util.List;

import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.forgedui.editor.edit.ElementEditPart;
import com.forgedui.editor.edit.PickerColumnEditPart;
import com.forgedui.editor.edit.ScreenEditPart;
import com.forgedui.editor.edit.TitleBarEditPart;
import com.forgedui.editor.edit.WindowEditPart;
import com.forgedui.editor.figures.PickerColumnFigure;
import com.forgedui.editor.figures.WindowFigure;
import com.forgedui.editor.preference.EditorPreferences;
import com.forgedui.model.Container;
import com.forgedui.model.ElementImpl;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.PickerColumn;
import com.forgedui.model.titanium.PickerRow;
import com.forgedui.model.titanium.Tab;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.TabbedBarButton;
import com.forgedui.model.titanium.TableView;
import com.forgedui.model.titanium.TableViewRow;
import com.forgedui.model.titanium.TableViewSection;
import com.forgedui.model.titanium.TitaniumUIBoundedElement;
import com.forgedui.model.titanium.Window;
import com.forgedui.util.ScreenManager;
import com.forgedui.util.Utils;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public abstract class BoundsHelper {
	
	public static final int PICKER_COLUMN_MARGIN = 4;
	
	private static BoundsHelper iPhoneCalculator = new IPhoneBoundsHelper();
	private static BoundsHelper androidCalculator = new AndroidBoundsHelper();
	
	public static Dimension getSafeParentViewSize(ElementEditPart ep){
		AbstractGraphicalEditPart parent = (AbstractGraphicalEditPart)ep.getParent();
		Dimension parentSize = parent != null ?
				parent.getFigure().getClientArea().getSize()
				: new Dimension(100, 00);
				
		if (parent instanceof WindowEditPart){
			WindowEditPart windowEP = (WindowEditPart)parent;
			WindowFigure windowFigure = (WindowFigure)windowEP.getFigure();
			parentSize.height -= windowFigure.getTitleBarHeight();
		}
				
		if (parent instanceof ScreenEditPart){
			List<?> children = ((ScreenEditPart) parent).getModelChildren_();
			for (Object child : children) {
				if (child instanceof TabGroup) {
					parentSize.height -= ep.getSupport().modelToView(ScreenManager.getTabGroupSize(ep.getModel()).height);
					break;
				}
			}
		}
		return parentSize;
	}
	
	public static Dimension getSafeViewSize(ElementEditPart view){
		return view != null ?
				view.getFigure().getClientArea().getSize()
				: new Dimension(100, 00);
	}
	
	@SuppressWarnings("unchecked")
	public static Rectangle getBounds(ElementEditPart ep){
		AbstractGraphicalEditPart parent = (AbstractGraphicalEditPart)ep.getParent();
		Dimension parentViewSize = getSafeParentViewSize(ep);
		Rectangle result = null;
		
		if (ep.getModel() instanceof Screen){
			Rectangle r = new Rectangle(new Point(0,0),
					((Screen)ep.getModel()).getResolution());
			result = r;
		} else if (ep instanceof TitleBarEditPart){
			Dimension defSize = ep.getDefaults().getSize(ep.getModel());
			defSize.width = parentViewSize.width;
			if (((TitleBarEditPart)ep).getTitlePrompt() != null){
				defSize.height += ScreenManager.TITLE_BAR_PROMPT_HEIGHT;
			}
			result = ep.getSupport().viewToModel(
					new Rectangle(new Point(0, -defSize.height), defSize));
		} else if (ep.getModel() instanceof PickerColumn){
			PickerColumn col = (PickerColumn)ep.getModel();
			List<PickerColumn> children = (List<PickerColumn>)
				((Container)ep.getParent().getModel()).getChildren();
			int setWidth, countWithoutWidth, countWithoutWidthBefore, setWidthBefore;
			setWidth=countWithoutWidth=countWithoutWidthBefore=setWidthBefore=0;
			for (PickerColumn column : children) {
				if (column == col) {
					setWidthBefore = ep.getSupport().modelToView(setWidth);
					countWithoutWidthBefore = countWithoutWidth;
				}
				if (column.getWidth() != null){
					setWidth += Math.max(0, getBound(parentViewSize.width, column.getWidth()));
				} else {
					countWithoutWidth++;
				}
			}
			setWidth = ep.getSupport().modelToView(setWidth);
			int parentWidth = (parentViewSize.width - PICKER_COLUMN_MARGIN*(children.size() - 1));
			int defWidth = (countWithoutWidth > 0)
				? (parentWidth -setWidth)  / countWithoutWidth
						: 0;
			int index = children.indexOf(col);
			int width = col.getWidth() != null ? ep.getSupport().modelToView(
					getBound(parentViewSize.width, col.getWidth())) : defWidth;
			result = ep.getSupport().viewToModel(new Rectangle(
					index*PICKER_COLUMN_MARGIN + setWidthBefore + defWidth*countWithoutWidthBefore,
					0,
					width, parentViewSize.height));
		} else if (ep.getModel() instanceof PickerRow){
			int index = 0;
			if (ep.getParent() != null){
				List<?> children = ((PickerColumnEditPart)ep.getParent()).getModelChildren();
				index = children.indexOf(ep.getModel());
				ScrollPane sp = ((PickerColumnFigure)parent.getFigure()).getScrollPane();
				/*need some place for scrolls*/
				if (sp.getVerticalScrollBar().isVisible()){
				//	parentViewSize.width -= sp.getVerticalScrollBar().getPreferredSize().width;
				}
			}
			int height = ep.getDefaults().getSize(ep.getModel()).height;
			result = ep.getSupport().viewToModel(
					new Rectangle(0, index*height, parentViewSize.width, height));
		} else if (ep.getModel() instanceof TableViewRow
				|| ep.getModel() instanceof TableViewSection){
			TableView table = null;
			if (ep.getModel().getParent() instanceof TableView){
				table = (TableView) ep.getModel().getParent();
			} else {
				table = (TableView) ep.getModel().getParent().getParent();
			}
			int minRowHeight = (int)Utils.getFloat(table.getMinRowHeight(), 0);
			int y = 0;//the bounds are set by top,left,height and width
			int width = getBound(parentViewSize.width, ((TitaniumUIBoundedElement)ep.getModel()).getWidth());
			float height = 0;
			if (ep.getParent() != null){
				List<?> children = ((Container)ep.getParent().getModel()).getChildren();
				for (Object object : children) {
					if (object == ep.getModel()) break;//get view height
					if (object instanceof TableViewSection
							|| object instanceof TableViewRow){
						int h= getBound(parentViewSize.height,
								((TitaniumUIBoundedElement)object).getHeight());//seems can't be null
						if (object instanceof TableViewRow) {
							h = Math.max(minRowHeight, h);
						}
						y += h;
					}
				}
				width = ep.getSupport().viewToModel(parentViewSize.width);	
			}
			
			height = getBound(parentViewSize.width, ((TitaniumUIBoundedElement)ep.getModel()).getHeight());
			if (ep.getModel() instanceof TableViewRow) {
				height = Math.max(minRowHeight, height);
			}
			result = new Rectangle(0, y, width, (int)height);
		} else if (ep.getModel() instanceof TabbedBarButton){
			TabbedBarButton tbb = (TabbedBarButton)ep.getModel();
			List<TabbedBarButton> children = (List<TabbedBarButton>) ((Container)ep.getParent().getModel()).getChildren();
			int setWidth, countWithoutWidth, countWithoutWidthBefore, setWidthBefore;
			setWidth=countWithoutWidth=countWithoutWidthBefore=setWidthBefore=0;
			for (TabbedBarButton tbb2 : children) {
				if (tbb2 == tbb) {
					setWidthBefore = setWidth;
					countWithoutWidthBefore = countWithoutWidth;
				}
				if (tbb2.getWidth() != null){
					setWidth += Math.max(0, tbb2.getWidth());
				} else {
					countWithoutWidth++;
				}
			}
			int defWidth = (countWithoutWidth > 0)
				? (parentViewSize.width -setWidth)  / countWithoutWidth
						: 0;
			int width = tbb.getWidth() != null ? ep.getSupport().modelToView(tbb.getWidth()) : defWidth;
			result = ep.getSupport().viewToModel(new Rectangle(setWidthBefore + defWidth*countWithoutWidthBefore, 0,
					Utils.getInt(width, defWidth), parentViewSize.height));
		} else {
			//necessary for titlebar delta
			/*if (ep.getModel() instanceof Window
					&& Utils.getBoolean(((Window)ep.getModel()).getFullscreen(), false)){
				result = ep.getSupport().viewToModel(new Rectangle(new Point(0,0), parentViewSize));
			} else {*/
				result = getActualHelper(ep).getElementBounds(ep);
			//}
		}
		return result;
	}
	
	public static int getBound(int parentBound, String bound){
		if (Utils.isEmpty(bound) || "auto".equalsIgnoreCase(bound)){
			return parentBound;
		} else if (bound.endsWith("%")){
			float f = Float.parseFloat(bound.substring(0, bound.length() - 1));
			return (int)(f*parentBound/100);
		} else {
			float f = Float.parseFloat(bound);
			return (int)f;
		}
	}
	
	public static String getNewBoundValue(int parentBound, int newValue, String oldValue){
		if (parentBound > 0){
			if ((oldValue == null && EditorPreferences.PreferRelativeBounds)
					|| "auto".equalsIgnoreCase(oldValue)
					|| (oldValue != null && oldValue.endsWith("%"))){
				return Math.round((100.0 * newValue)/parentBound) + "%";
			}
		}
		return "" + newValue;
	}
	
	public static Dimension getDimension(ElementEditPart ep){
		return getBounds(ep).getSize();
	}
	
	public static void setDimension(ElementEditPart parent, TitaniumUIBoundedElement tel, Dimension size){
		//ep.getParent can be null when we drop element from one parent to another
		Dimension parentSize = parent != null
			? ((ElementEditPart) parent).getModelClientAreaSize()
					: new Dimension(100, 100);//how to process here???
		if (tel.getWidth() == null/*&& getParent() != null*/
				&& tel.getLeft() != null && tel.getRight() != null){
			int newValue = parentSize.width - 
				getBound(parentSize.width, tel.getLeft()) - size.width;
			tel.setRight(
					getNewBoundValue(parentSize.width, newValue, tel.getRight()));
		} else {
			tel.setWidth(getNewBoundValue(parentSize.width, size.width, tel.getWidth()));
		}
		
		if (tel.getHeight() == null /*&& getParent() != null*/
				&& tel.getTop() != null && tel.getBottom() != null){
			int newValue = parentSize.height - 
				getBound(parentSize.height, tel.getTop()) - size.height;
			tel.setBottom(
					getNewBoundValue(parentSize.height, newValue, tel.getBottom()));
		} else {
			tel.setHeight(getNewBoundValue(parentSize.height, size.height, tel.getHeight()));
		}
	}
	
	public static void setDimension(ElementEditPart ep, Dimension size){
		if (ep.getModel() instanceof TitaniumUIBoundedElement) {
			setDimension((ElementEditPart)ep.getParent(),
					(TitaniumUIBoundedElement) ep.getModel(), size);
		} else {
			ElementImpl el = (ElementImpl)ep.getModel();
			el.setDimension(size);
		}
	}
		
	public static Point getLocation(ElementEditPart ep){
		return getBounds(ep).getLocation();
	}
	
	public static void setLocation(ElementEditPart parent, TitaniumUIBoundedElement tel, Point location){
		//ep.getParent can be null when we drop element from one parent to another
		Dimension parentSize = parent != null
			? parent.getModelClientAreaSize()
					: new Dimension(100, 100);//how to process here???
		if (tel.getLeft() == null /*&& getParent() != null*/
				&& tel.getWidth() != null && tel.getRight() != null){
			int newValue = parentSize.width - 
				getBound(parentSize.width, tel.getWidth()) - location.x;
			tel.setRight(
					getNewBoundValue(parentSize.width, newValue, tel.getRight()));
		} else {
			tel.setLeft(getNewBoundValue(parentSize.width, location.x, tel.getLeft()));
		}
		
		if (tel.getTop() == null /*&& getParent() != null*/
				&& tel.getHeight() != null && tel.getBottom() != null){
			int newValue = parentSize.height - 
				getBound(parentSize.height, tel.getHeight()) - location.y;
			tel.setBottom(
					getNewBoundValue(parentSize.height, newValue, tel.getBottom()));
		} else {
			tel.setTop(getNewBoundValue(parentSize.height, location.y, tel.getTop()));
		}
	}
	
	public static void setLocation(ElementEditPart ep, Point location){
		if (ep.getModel() instanceof TitaniumUIBoundedElement) {
			setLocation((ElementEditPart) ep.getParent(),
					(TitaniumUIBoundedElement) ep.getModel(), location);
		} else {
			ElementImpl el = (ElementImpl)ep.getModel();
			el.setLocation(location);
		}
	}
	
	public abstract Rectangle getElementBounds(ElementEditPart ep);
	
	static BoundsHelper getActualHelper(ElementEditPart ep){
		switch (ep.getModel().getPlatform()) {
		case Android:
			return androidCalculator;
		default:
			return iPhoneCalculator;
		}
	}
	
}

class IPhoneBoundsHelper extends BoundsHelper{
	
	private static final long serialVersionUID = 1L;
	private static int DEFAULT = 0;
	
	/**
	 * returns model size
	 */
	@Override
	public Rectangle getElementBounds(ElementEditPart ep) {
		Dimension parentViewSize = getSafeParentViewSize(ep);
		if (ep.getModel() instanceof Window
				&& Utils.getBoolean(((Window)ep.getModel()).getFullscreen(), false)){
			return ep.getSupport().viewToModel(new Rectangle(new Point(0,0), parentViewSize));
		} else if (ep.getModel() instanceof Tab){
			int visibleTabs = Math.min(((TabGroup)ep.getParent().getModel()).getTabNumber(), 5);
			Rectangle rect = ep.getSupport().viewToModel(
					((AbstractGraphicalEditPart) ep.getParent()).getFigure().getClientArea());
			List<?> tabs = ((TabGroup)ep.getParent().getModel()).getTabs();
			int width = rect.width / visibleTabs;
			if (tabs.indexOf(ep.getModel()) < 6 && tabs.indexOf(ep.getModel()) >= 0){//visible
				Dimension size = new Dimension(width, rect.height);
				return new Rectangle(new Point(width*tabs.indexOf(ep.getModel()), 0), size);
			} else {
				return new Rectangle(rect.right(), 0, 0, rect.height);
			}			
		} else if (ep.getModel() instanceof TabGroup){
			Dimension size = ScreenManager.getTabGroupSize(ep.getModel());
			Point location = new Point(0, ep.getScreenBounds().height - size.height);
			return new Rectangle(location, size);
		} else if (ep.getModel() instanceof TitaniumUIBoundedElement) {
			TitaniumUIBoundedElement e = (TitaniumUIBoundedElement) ep.getModel();
			//This is very important to use ClientArea here!
			Dimension parentViewDimension = getSafeParentViewSize(ep);
			Dimension parentModelDimension = ep.getSupport().viewToModel(parentViewDimension);
				
			int width = calculate1Side(parentModelDimension.width,
					e.getLeft(), e.getWidth(), e.getRight());
			int height = calculate1Side(parentModelDimension.height,
					e.getTop(), e.getHeight(), e.getBottom());
			int x = calculate1Position(parentModelDimension.width,
					e.getLeft(), width, e.getRight());
			int y = calculate1Position(parentModelDimension.height,
					e.getTop(), height, e.getBottom());
			Rectangle rs = new Rectangle(x, y, width, height);
			return rs;
		} else {
			ElementImpl e = (ElementImpl)ep.getElement();
			return new Rectangle(e.getLocation(), e.getDimension());
		}
	}
	
	//FIXME add percent and "auto" processing
	private int calculate1Side(int parentWidth, String left, String width, String right){
		if (width != null){
			return getBound(parentWidth, width);
		} else if(left != null && right != null) {
			int iWidth = parentWidth - getBound(parentWidth, left) - 
				getBound(parentWidth, right);
			return iWidth >= 0 ? iWidth : DEFAULT;
		} else {
			return parentWidth;
		}
	}
	
	//FIXME add percent and "auto" processing
	private int calculate1Position(int parentWidth, String left, int width, String right){
		if (left != null){
			return getBound(parentWidth, left);
		} else {
			if (right != null){
				return parentWidth - width - getBound(parentWidth, right);
			} else {
				return 0;//(parentWidth - iWidth) / 2;//CENTER
			}
		}
		
	}

	
}

class AndroidBoundsHelper extends IPhoneBoundsHelper {//FIXME should extend BoundsHelper

	@Override
	public Rectangle getElementBounds(ElementEditPart ep) {
		if (ep.getModel() instanceof Window){
			Rectangle r = super.getElementBounds(ep);
			if (((ScreenEditPart)ep.getParent()).hasTabGroup()){
				r.y += ScreenManager.getTabGroupSize(ep.getModel()).height;
			}
			return r;
		} else if (ep.getModel() instanceof TabGroup){
			Dimension size = ScreenManager.getTabGroupSize(ep.getModel());
			return new Rectangle(new Point(0,0), size);
		}
		return super.getElementBounds(ep);
	}
	
}
