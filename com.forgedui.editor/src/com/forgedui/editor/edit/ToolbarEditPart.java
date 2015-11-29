// LICENSE
package com.forgedui.editor.edit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.forgedui.model.titanium.Toolbar;
import com.forgedui.util.ScreenManager;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class ToolbarEditPart extends TitaniumContainerEditPart<Toolbar> {

	@Override
	protected IFigure createFigure() {
		Toolbar model = getModel();
		Dimension d = model.getDimension();
		IFigure figure = super.createFigure();
		if (d.width <= 0 || d.height <= 0) {
			model.setDimension(ScreenManager.getToolBarSize(model));
			Point location = new Point(0, getScreenBounds().height
					- model.getDimension().height);
			if (model.getDiagram().getTabGroupDiagram() != null) {
				location.translate(0,
						-getSupport().modelToView(
								ScreenManager.getTabGroupSize(model).height));
			}
			model.setLocation(location);
		}
		return figure;
	}

}
