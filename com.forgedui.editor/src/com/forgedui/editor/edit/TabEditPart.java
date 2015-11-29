// LICENSE
package com.forgedui.editor.edit;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.forgedui.editor.figures.TabFigure;
import com.forgedui.model.Diagram;
import com.forgedui.model.titanium.MoreTab;
import com.forgedui.model.titanium.Tab;
import com.forgedui.util.Converter;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class TabEditPart extends TitaniumElementEditPart<Tab> {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void refreshVisuals() {
		Tab model = getModel();
		TabFigure figure = (TabFigure) getFigure();
		if (!((model instanceof MoreTab) || (model instanceof ExternalMoreTab))) {
			figure.setText(model.getTitle());
		}
		figure.setBarImage(Converter.getImageFullPath(model.getDiagram(), model.getIcon()));
		figure.setBadge(model.getBadge());
		boolean active = (getAssociatedDiagram() == null) ? false :
				getAssociatedDiagram().getFile().equals(model.getDiagram().getFile());
		figure.setActive(active);
		
		super.refreshVisuals();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		//can't move this to TypeProperty as we have many classes here
		if (Tab.TITLE_PROP.equals(evt.getPropertyName())
				|| Tab.ICON_PROP.equals(evt.getPropertyName())
				|| Tab.BADGE_PROP.equals(evt.getPropertyName())){
			refreshVisuals();
		} else {
			super.propertyChange(evt);
		}
	}
	
	public void performRequest(Request request) {
		if (directEditingEnabled()) {
			if (request.getType() == RequestConstants.REQ_OPEN) { 
				if (performOpenDiagram()){
					//performDirectEdit();
				}
			}
		}
	}
	
	protected Diagram getAssociatedDiagram(){
		Tab tab = getModel();
		String window = tab.getWindow();
		if (window != null){
			Collection<Diagram> projectDiagrams = tab.getDiagram().getProjectDiagrams();
			for (Diagram diagram : projectDiagrams) {
				if (diagram.getScreen().getWindow() != null) {
					if (window.equals(diagram.getScreen().getWindow().getName())){
						return diagram;
					}
				}
			}
		}
		return null;
	}
	
	protected boolean performOpenDiagram() {
		Diagram diagram = getAssociatedDiagram();
		if (diagram != null){
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
			if (page != null) {
				try {
					IDE.openEditor(page, diagram.getFile(), true);
				} catch (PartInitException e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	protected boolean directEditingEnabled() { 
		return !(getModel() instanceof MoreTab);
	}
}
