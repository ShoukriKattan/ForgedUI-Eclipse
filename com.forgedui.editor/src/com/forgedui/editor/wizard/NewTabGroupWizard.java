// LICENSE
package com.forgedui.editor.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.analytics.ReportingEventTypes;
import com.forgedui.model.Diagram;
import com.forgedui.model.titanium.TabGroup;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class NewTabGroupWizard extends Wizard implements INewWizard {
	
	private DiagramCreationPage pathPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
		pathPage = new DiagramCreationPage(workbench, selection);
		pathPage.setBaseFileName("TabGroup");
	}
	
	@Override
	public void addPages() {
		addPage(pathPage);
	}
	
	public boolean performFinish() {
		pathPage.setDefaultContent(createFileContent(pathPage.getFileHandle()));
		return pathPage.finish();
	}
	
	public Diagram createFileContent(IFile iFile){
		Diagram diagram = new Diagram(pathPage.getPlatform(), pathPage.getResolution(), pathPage.getWindowResolution());
		diagram.setFile(iFile);
		diagram.setName("TabGroup_Diagram");
		TabGroup tg = new TabGroup();
		diagram.getScreen().setTabGroup(tg);
		tg.setTabNumber(2);
		
		GUIEditorPlugin
			.getDefault()
			.getReportingService()
			.reportEvent(ReportingEventTypes.TABGROUP_CREATED,
					new String[] { diagram.getPlatform().toString(),
					diagram.getResolution().toString()});
		
		return diagram;
	}

}
