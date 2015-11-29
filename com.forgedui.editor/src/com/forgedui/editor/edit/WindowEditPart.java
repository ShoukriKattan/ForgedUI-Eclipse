// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGrid;

import com.forgedui.editor.edit.policy.ContainerEditPolicy;
import com.forgedui.editor.edit.policy.WindowAddElementEditPolicy;
import com.forgedui.editor.figures.WindowFigure;
import com.forgedui.model.titanium.Window;
import com.forgedui.util.ScreenManager;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class WindowEditPart extends TitaniumContainerEditPart<Window> {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getModelChildren_() {
		List list = new ArrayList(super.getModelChildren_());
		Window model = (Window)getModel();
		if (model.getTitleBar() != null){
			list.add(model.getTitleBar());
		}
		return list;
	}
	
	@Override
	protected void refreshVisuals() {
		WindowFigure figure = (WindowFigure)getFigure();
		Window model = getModel();//FIXME find a better place for this
		if (model.getTitleBar() != null){
			int titleBarHeight = getDefaults().getSize(model.getTitleBar()).height;
			if (model.getTitleBar().getTitlePrompt() != null
					|| model.getTitleBar().getTitlepromptid() != null){
				titleBarHeight += ScreenManager.TITLE_BAR_PROMPT_HEIGHT;
			}
			figure.setTitleBarHeight(titleBarHeight);
		} else {
			figure.setTitleBarHeight(0);
		}
		super.refreshVisuals();
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(ContainerEditPolicy.KEY, new WindowAddElementEditPolicy());
	}
	
	private void updateGridOrigin(){
		Point loc = getFigure().getBounds().getLocation();
		getFigure().translateToAbsolute(loc);
		getViewer().setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN, loc);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (Window.TITLE_BAR_PROP.equals(e.getPropertyName())){
			refresh();
		} else if (Window.MODAL_PROP.equals(e.getPropertyName())
				|| Window.TABS_HIDDEN_PROP.equals(e.getPropertyName())){
			((GraphicalEditPart) getParent()).refresh();
		} else if (Window.FULL_SCREEN_PROP.equals(e.getPropertyName())){
			refreshVisuals();
		} else {
			super.propertyChange(e);
		}
	}
}




