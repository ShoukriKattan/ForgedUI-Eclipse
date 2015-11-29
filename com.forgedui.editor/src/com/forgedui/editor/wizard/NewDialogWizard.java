// LICENSE
package com.forgedui.editor.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.forgedui.editor.GUIEditorPlugin;
import com.forgedui.editor.analytics.ReportingEventTypes;
import com.forgedui.model.Diagram;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class NewDialogWizard extends Wizard implements INewWizard {
	
	private DiagramCreationPage pathPage;
	
	private DialogTypeWizardPage selectPage;
	
	private boolean pathPageShown;

	public NewDialogWizard() {

	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
		selectPage = new DialogTypeWizardPage("Select Dialog");
		pathPage = new DiagramCreationPage(workbench, selection);
	}
	
	@Override
	public void addPages() {
		addPage(selectPage);
		addPage(pathPage);
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage page2 = super.getNextPage(page);
		if (page2 == pathPage){
			pathPage.setBaseFileName(selectPage.getDialogType() + "Dialog");
			pathPageShown = true;
		}
		return page2;
	}

	public boolean performFinish() {
		if (!pathPageShown){
			pathPage.setBaseFileName(selectPage.getDialogType() + "Dialog");
		}
		pathPage.setDefaultContent(createFileContent(pathPage.getFileHandle()));
		return pathPage.finish();
	}
	
	protected Diagram createFileContent(IFile iFile) {
		Diagram diagram = new Diagram(pathPage.getPlatform(), pathPage.getResolution(), pathPage.getWindowResolution());
		diagram.setFile(iFile);
		diagram.getScreen().setDialog(selectPage.getDialog());
		
		// TODO: From Shoukry : How do we get the Platform here... Is this the
		// best place to log or is there better?

		GUIEditorPlugin
				.getDefault()
				.getReportingService()
				.reportEvent(ReportingEventTypes.DIALOG_CREATED,
						new String[] { diagram.getPlatform().toString(),
						diagram.getResolution().toString(),
						selectPage.getDialog().getClass().getSimpleName()});
		return diagram;
	}

}
