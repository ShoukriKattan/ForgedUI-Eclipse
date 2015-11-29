package com.forgedui.editor.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.forgedui.model.Diagram;
import com.forgedui.model.titanium.ipad.Popover;

public class NewPopoverWizard extends Wizard implements INewWizard {
	
	private DiagramCreationPage pathPage;

	public NewPopoverWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		pathPage = new DiagramCreationPage(workbench, selection){
			protected void postAdvContCreation() {
				getPlatformField().select(1);
				getPlatformField().setEnabled(false);
				updatePlatform();
			}
		};
		pathPage.setBaseFileName("popover");
	}
	
	@Override
	public void addPages() {
		addPage(pathPage);
	}

	@Override
	public boolean performFinish() {
		final Diagram diagram = createFileContent(pathPage.getFileHandle());
		pathPage.setDefaultContent(diagram);
		return pathPage.finish();
	}
	
	protected Diagram createFileContent(IFile iFile) {
		Diagram diagram = new Diagram(pathPage.getPlatform(), pathPage.getResolution(), pathPage.getWindowResolution());
		diagram.setFile(iFile);
		diagram.getScreen().setDialog(new Popover());
		return diagram;
	}


}
