// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;

import com.forgedui.core.TitaniumProject;
import com.forgedui.editor.edit.policy.ScreenEditPolicy;
import com.forgedui.editor.figures.MobileScreenFigure;
import com.forgedui.editor.figures.ScreenFigure;
import com.forgedui.model.Element;
import com.forgedui.model.Screen;
import com.forgedui.model.titanium.TabGroup;
import com.forgedui.model.titanium.Window;
import com.forgedui.util.Utils;

/**
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class ScreenEditPart extends ElementEditPart {

	private static final long serialVersionUID = 1L;

	@Override
	public List<?> getModelChildren_() {
		List<Element> list = new ArrayList<Element>();
		Screen model = getModel();

		if (model.getDialog() != null) {
			list.add(model.getDialog());
			// addTabGroup(list); Do not show TabGroup
		}

		if (hasTabGroup()) {
			addTabGroup(list);
		}
		Window window = model.getWindow();
		if (window != null) {
			if (Utils.getBoolean(window.getModal(), false)) {
				list.add(window);// will be above tab group
			} else {
				list.add(0, window);
			}
		} else {
			if (model.getTabGroup() != null) {
				list.add(model.getTabGroup());
			}
		}
		list.addAll(getModalElements());

		return list;
	}

	/**
	 * Screen doesn't remove expanded pickers!!!
	 */
	protected List<?> getModelChildren() {
		List<?> children = new ArrayList<Object>(getModelChildren_());
		Collections.sort(children, zIndexComparator);
		return children;
	}

	@Override
	public Screen getModel() {
		return (Screen) super.getModel();
	}

	public boolean hasTabGroup() {
		Window window = getModel().getWindow();
		if (window != null && !Utils.getBoolean(window.getTabBarHidden(), false)){
			Screen model = getModel();
			if (model.getTabGroup() != null
					|| model.getDiagram().getTabGroupDiagram() != null){
				return true;
			}
		}
		return false;
	}

	protected void addTabGroup(List<Element> list) {
		Screen model = getModel();
		if (model.getTabGroup() == null) {
			TabGroup externalTG = model.getDiagram().getTabGroupDiagram();
			if (externalTG != null) {
				ExternalTabGroup etg = new ExternalTabGroup(externalTG);
				etg.setParent(model);
				list.add(etg);
			}
		}
	}

	protected List<Element> getModalElements() {
		List<Element> list = new ArrayList<Element>();
		list.addAll(getExpandedPickers(getModel().getWindow()));
		return list;
	}

	@Override
	protected void createEditPolicies() {
		// super.createEditPolicies();
		installEditPolicy(ScreenEditPolicy.KEY, new ScreenEditPolicy());
	}

	@Override
	protected void refreshVisuals() {
		((ScreenFigure)getFigure()).setResolution(getModel().getResolution());
		super.refreshVisuals();
	}
	
	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		List<?> children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			EditPart editPart = (EditPart) children.get(i);
			editPart.refresh();
		}
	}

	public void propertyChange(PropertyChangeEvent e) {
		if (TitaniumProject.Tab_GROUP_DIAGRAMS.equals(e.getPropertyName())) {
			refresh();
		} else if (Screen.SCREE_WINDOW_PR_TAB_GROUP.equals(e.getPropertyName())
				|| Screen.SCREE_WINDOW_PR_WINDOW.equals(e.getPropertyName())) {
			refreshChildren();
		} else {
			super.propertyChange(e);
		}
	}

	@Override
	public void activate() {
		super.activate();
		TitaniumProject proj = getModel().getDiagram().getProject();
		if (proj != null) {
			proj.addPropertyChangeListener(this);
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();
		TitaniumProject proj = getModel().getDiagram().getProject();
		if (proj != null) {
			proj.removePropertyChangeListener(this);
		}
	}

}
