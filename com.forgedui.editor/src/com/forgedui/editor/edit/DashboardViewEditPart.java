// LICENSE
package com.forgedui.editor.edit;

import java.util.List;

import org.eclipse.draw2d.IFigure;

import com.forgedui.editor.figures.DashboardViewFigure;
import com.forgedui.model.titanium.DashboardItem;
import com.forgedui.model.titanium.DashboardView;


/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class DashboardViewEditPart extends TitaniumContainerEditPart<DashboardView> {
	
	private int currentPage = 0;
	
	@Override
	protected void refreshChildren() {
		// TODO Auto-generated method stub
		super.refreshChildren();
	}
	
	@Override
	protected void refreshVisuals() {
		DashboardViewFigure figure = (DashboardViewFigure) getFigure();
		figure.setPagesCount((getModel().getChildren().size() - 1) / 9);
		figure.setCurrentPage(currentPage);
		super.refreshVisuals();
	}
	
	/**
	 * Make visible 9 items.
	 * 9 Items represents 1 page.
	 * @param item
	 */
	public void changePageTo(DashboardItem item){
		DashboardView model = getModel();
		int page = model.getChildren().indexOf(item) / 9;
		if (currentPage == page){
			return;
		}
		currentPage = page;
		List<?> childFigures = getFigure().getChildren();
		for (int i = 0; i < childFigures.size(); i++) {
			if (i < currentPage*9 || i >= (currentPage + 1)*9){
				((IFigure)childFigures.get(i)).setVisible(false);
			} else {
				((IFigure)childFigures.get(i)).setVisible(true);
			}
		}
		refreshVisuals();
	}
	
}
