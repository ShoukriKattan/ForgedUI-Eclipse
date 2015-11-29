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
import com.forgedui.model.titanium.Window;

/**
 * Create new new gui builder-file. Those files can be used with the GUIEditor
 * (see plugin.xml).
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 */
public class NewWindowCreationWizard extends Wizard implements INewWizard {

	private DiagramCreationPage pathPage;

	public void addPages() {
		addPage(pathPage);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		pathPage = new DiagramCreationPage(workbench, selection);
	}

	public boolean performFinish() {
		pathPage.setDefaultContent(createFileContent(pathPage.getFileHandle()));
		return pathPage.finish();
	}

	protected Diagram createFileContent(IFile file) {
		Diagram diagram = new Diagram(pathPage.getPlatform(), pathPage.getResolution(), pathPage.getWindowResolution());
		diagram.setFile(file);
		diagram.getScreen().setWindow(new Window());

		// TODO: From Shoukry : How do we get the Platform here... Is this the
		// best place to log or is there better?

		GUIEditorPlugin
				.getDefault()
				.getReportingService()
				.reportEvent(ReportingEventTypes.WINDOW_CREATED,
						new String[] { diagram.getPlatform().toString(),
						diagram.getResolution().toString()});

		return diagram;
	}

}
